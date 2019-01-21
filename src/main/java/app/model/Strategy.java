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
    private DataPointList dataPointList;

    public Strategy(Date fromDate, Date toDate, BigDecimal percent, StrategyEnums.Change action, DataPointList dataPointList){
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.percent = percent;
        this.action = action;
        this.dataPointList = dataPointList;
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
        int length = tmpList.size();
        DataPoint first = tmpList.get(0), last = tmpList.get(length-1);
        BigDecimal delta = last.getPrice().divide(first.getPrice());
        boolean result = false;
        percent = percent.divide(new BigDecimal(100.0));
        switch (action){
            case INCREASE:
                result = delta.compareTo(percent) >= 0;
                break;
            case DECREASE:
                result = delta.compareTo(percent) <= 0;
                break;
        }
        return result;
    }

}
