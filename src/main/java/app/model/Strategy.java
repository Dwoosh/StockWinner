package app.model;

import app.exceptions.NoValidDateFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Strategy implements IStrategyComponent{

    private Date fromDate, toDate;
    private BigDecimal percent;
    private StrategyEnums.Change action;
    private StrategyEnums.Conditions condition;
    private DataPointList dataPointList;

    public Strategy(Date fromDate, Date toDate, BigDecimal percent, StrategyEnums.Change action,
                    StrategyEnums.Conditions condition, DataPointList dataPointList){
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.percent = percent;
        this.action = action;
        this.condition = condition;
        this.dataPointList = dataPointList;
    }

    public StrategyEnums.Conditions getCondition() {
        return condition;
    }

    public boolean evaluate() throws NoValidDateFoundException{
        List<DataPoint> tmpList = new ArrayList<DataPoint>();
        for(DataPoint point : dataPointList.getDataPoints()){
            Date pointDate = point.getDate();
            if(pointDate.compareTo(fromDate) >= 0 && pointDate.compareTo(toDate) <= 0){
                tmpList.add(point);
            }
        }
        if(tmpList.isEmpty()) throw new NoValidDateFoundException();

        DataPoint min = null, max = null;
        for(DataPoint point : tmpList){
            if(min == null || max == null){
                min = point;
                max = point;
            }
            //check for new min
            if(point.getPrice().compareTo(min.getPrice()) < 0){
                min = point;
            }
            //check for new max
            if(point.getPrice().compareTo(max.getPrice()) > 0){
                max = point;
            }
        }
        BigDecimal threshold;
        boolean result = false;
        percent = percent.divide(new BigDecimal(100.0));
        switch (action){
            //check if maximum is equal or greater than minimum with applied percent change
            case INCREASE:
                threshold = min.getPrice().multiply(percent);
                result = max.getPrice().compareTo(threshold) >= 0;
                break;
            //check if minimum is equal or less than maximum with applied percent change
            case DECREASE:
                threshold = max.getPrice().multiply(percent);
                threshold = max.getPrice().subtract(threshold);
                result = min.getPrice().compareTo(threshold) <= 0;
                break;
        }
        return result;
    }
}
