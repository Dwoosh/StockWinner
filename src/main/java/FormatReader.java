import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public abstract class FormatReader {
    protected String path;

    public String getPath(){
        return this.path;
    }

    public void setPath(String path){
        this.path = path;
    }

    public FormatReader(String path){
        this.path = path;
    }

    public abstract List<DataPoint> getDataPointList();
}
