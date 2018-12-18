package app.controllers;

import app.model.*;
import app.readers.*;
import app.support.ZoomManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class GraphDialogController {

    private AppController appController;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private LineChart<String, Number> lineChart;

    public void initialize(AppController controller, String fileLocation, WebSites.SupportedWebSites chosenWebsite, Pane chartParent) throws IOException {

        this.appController = controller;

        xAxis.setLabel("Date");
        yAxis.setLabel("Price");

        XYChart.Series series = new XYChart.Series();
        series.setName("Price fluctuations");

        ReaderFactory readerFactory = new ReaderFactory();
        FormatReader reader = readerFactory.concreteReader(fileLocation, chosenWebsite);
        DataPointList pointList =  reader.getDataPointList();

        for(DataPoint dp: pointList.getDataPoints()) {
            series.getData().add(new XYChart.Data(dp.getDate().toString(), dp.getPrice()));
        }
        new ZoomManager(chartParent, lineChart,series);
    }


    @FXML
    private void handleGoingBack(ActionEvent event){

        appController.goBackToFirstWindow();
    }

    @FXML
    private void handleReset(ActionEvent event){

        appController.initGraphScene("views/GraphView.fxml");
    }

}
