package app.readers;

import app.model.DataPointList;

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

    public abstract DataPointList getDataPointList();
}
