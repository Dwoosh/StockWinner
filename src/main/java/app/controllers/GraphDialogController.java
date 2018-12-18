package app.controllers;

import app.exceptions.InvalidContentException;
import app.exceptions.InvalidConditionException;
import app.exceptions.NoValidDateFoundException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

public class GraphDialogController {

    private AppController appController;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private ComboBox<Date> dateFrom;

    @FXML
    private ComboBox<Date> dateTo;

    @FXML
    private ComboBox<StrategyEnums.Change> change;

    @FXML
    private ComboBox<StrategyEnums.Conditions> condition;

    @FXML
    private TextField percent;

    @FXML
    private ListView<String> condList;

    private DataPointList pointList;

    private StrategyComposite strategyComposite;

    private boolean secondStrategy = false;

    public void initialize(AppController controller, String fileLocation, WebSites.SupportedWebSites chosenWebsite, Pane chartParent) throws IOException {

        this.appController = controller;
        this.change.setItems(FXCollections.observableArrayList(StrategyEnums.Change.values()));
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
        try {
            pointList = reader.getDataPointList(fileLocation);

        } catch (InvalidContentException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid content of file");
            alert.setContentText("Choose another file");
            alert.showAndWait();
        }
        
        for(DataPoint dp: pointList.getDataPoints()) {
            series.getData().add(new XYChart.Data(dp.getDate().toString(), dp.getPrice()));
            this.dateFrom.getItems().add(dp.getDate());
            this.dateTo.getItems().add(dp.getDate());
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

        if((secondStrategy && condition.getSelectionModel().isEmpty()) ||
            dateFrom.getSelectionModel().isEmpty() ||
            dateTo.getSelectionModel().isEmpty() ||
            percent.getText().isEmpty() ||
            change.getSelectionModel().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Value not set");
            alert.setContentText("Please set all needed values");
            alert.showAndWait();
            return;
        }

        Double percentValue = new Double(percent.getText());
        if(percentValue <0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incorrect percent");
            alert.setContentText("Percent change should not be < 0");
            alert.showAndWait();
            return;
        }
        percentValue = percentValue /100;
        if(change.getValue().equals(StrategyEnums.Change.INCREASE))
            percentValue +=1;
        BigDecimal val = new BigDecimal(percentValue);

        String cond;
        if(condition.getSelectionModel().isEmpty()){

            cond = "";
            condition.setDisable(false);
            IStrategyComponent strategy = new Strategy(dateFrom.getValue(), dateTo.getValue(),
                    val, change.getValue(), StrategyEnums.Conditions.NONE, pointList);
            strategyComposite = new StrategyComposite(strategy);
            secondStrategy = true;

        }
        else {
            cond = condition.getValue().toString();
            condition.setDisable(true);
            IStrategyComponent strategy = new Strategy(dateFrom.getValue(), dateTo.getValue(),
                    val, change.getValue(), condition.getValue(), pointList);
            strategyComposite.addStrategy(strategy);
        }


        condList.getItems().add(cond +" From " + dateFrom.getValue()
                + " to " + dateTo.getValue() + " percent change " + percent.getText()
                + " action " + change.getValue().toString());


    }

    @FXML
    private void handleDoneButton(ActionEvent event){

        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Eveluation");
            alert.setHeaderText("Strategy evaluation:");
            String text = String.valueOf(strategyComposite.evaluate());
            alert.setContentText(text);
            alert.showAndWait();
        }
        catch (NoValidDateFoundException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incorrect date period:");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            return;
        }
        catch (InvalidConditionException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid condition");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            return;
        }


    }

}
