package app.readers;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import app.model.DataPoint;
import app.model.DataPointList;

public class YahooFinanceCsvReader extends FormatReader {

    public YahooFinanceCsvReader(String path) {
        super(path);
    }

    public DataPointList getDataPointList() {
        List<DataPoint> pointList = new ArrayList<DataPoint>();

        String line;
        String cvsSplitBy = ",";
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {

                String[] data = line.split(cvsSplitBy);
                DataPoint dataPoint = new DataPoint();
                if(data[0].equals("Date")) continue; //bez pierwszej linii
                dataPoint.setDate(ft.parse(data[0]));
                dataPoint.setPrice(new BigDecimal(data[1]));
                pointList.add(dataPoint);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new DataPointList(pointList);
    }
}