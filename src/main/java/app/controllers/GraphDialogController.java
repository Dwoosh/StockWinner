package app.controllers;

import app.model.*;
import app.readers.*;
import app.support.ZoomManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

    @FXML
    private ComboBox dateFrom;

    @FXML
    private ComboBox dateTo;

    @FXML
    private ComboBox<StrategyEnums.Actions> action;

    @FXML
    private ComboBox<StrategyEnums.Conditions> condition;

    @FXML
    private TextField percent;

    @FXML
    private ListView<String> condList;

    public void initialize(AppController controller, String fileLocation, WebSites.SupportedWebSites chosenWebsite, Pane chartParent) throws IOException {

        this.appController = controller;
        this.action.setItems(FXCollections.observableArrayList(StrategyEnums.Actions.values()));
        //this.condition.setItems(FXCollections.observableArrayList(StrategyEnums.Conditions.values()));
        this.condition.getItems().addAll(
            StrategyEnums.Conditions.AND,
                StrategyEnums.Conditions.OR
        );

        xAxis.setLabel("Date");
        yAxis.setLabel("Price");

        XYChart.Series series = new XYChart.Series();
        series.setName("Price fluctuations");

        ReaderFactory readerFactory = new ReaderFactory();
        FormatReader reader = readerFactory.concreteReader(fileLocation, chosenWebsite);
        DataPointList pointList =  reader.getDataPointList();

        for(DataPoint dp: pointList.getDataPoints()) {
            series.getData().add(new XYChart.Data(dp.getDate().toString(), dp.getPrice()));
            this.dateFrom.getItems().add(dp.getDate().toString());
            this.dateTo.getItems().add(dp.getDate().toString());
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
    @FXML
    private void handleAddCondButton(ActionEvent event){

        String cond;
        if(condition.getSelectionModel().isEmpty()){
            cond = "";
            condition.setDisable(false);

        }
        else {
            cond = condition.getValue().toString();
            condition.setDisable(true);
        }


        condList.getItems().add(cond +" From " + dateFrom.getValue()
                + " to " + dateTo.getValue() + " percent change " + percent.getText()
                + " action " + action.getValue().toString());
    }

    @FXML
    private void handleDoneButton(ActionEvent event){


    }

}
