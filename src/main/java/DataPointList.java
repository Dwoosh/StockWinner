import java.util.List;

public class DataPointList {

    private List<DataPoint> dataPoints;

    public void addDataPointListFromReader(FormatReader reader){
        this.dataPoints = reader.getDataPointList();
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

}
