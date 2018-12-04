package app.model;

import java.util.List;

public class DataPointList {

    private List<DataPoint> dataPoints;

    public DataPointList(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public DataPoint get(int i){
        return this.dataPoints.get(i);
    }

    public int size(){
        return this.dataPoints.size();
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

}
