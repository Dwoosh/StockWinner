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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphDialogController {

    private AppController appController;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private TextField dateFrom;

    @FXML
    private TextField dateTo;

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
            dateFrom.getText().isEmpty() ||
            dateTo.getText().isEmpty() ||
            percent.getText().isEmpty() ||
            change.getSelectionModel().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Value not set");
            alert.setContentText("Please set all needed values");
            alert.showAndWait();
            return;
        }
        Pattern pattern = Pattern.compile("^[0-9]+$");
        Matcher matcher1 = pattern.matcher(dateFrom.getText());
        Matcher matcher2 = pattern.matcher(dateTo.getText());
        //if both fields contain only numbers and from is greater than to alert won't show
        if(!(matcher1.find() && matcher2.find() && dateFrom.getText().compareTo(dateTo.getText()) > 0)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incorrect date value(s)");
            alert.setContentText("Field values should be integers and upper field should be greater than lower");
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
        if(change.getValue().equals(StrategyEnums.Change.INCREASE))
            percentValue += 100.0;
        BigDecimal val = new BigDecimal(percentValue);

        int fromDateInteger = Integer.parseInt(dateFrom.getText());
        int toDateInteger = Integer.parseInt(dateTo.getText());
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR,-fromDateInteger);
        Date from = now.getTime();
        now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR,-toDateInteger);
        Date to = now.getTime();
        if(condition.getSelectionModel().isEmpty()){

            condition.setDisable(false);
            IStrategyComponent strategy = new Strategy(from, to,
                    val, change.getValue(), pointList);
            strategyComposite = new StrategyComposite(strategy);
            secondStrategy = true;

        }
        else {
            condition.setDisable(true);
            IStrategyComponent strategy = new Strategy(from, to,
                    val, change.getValue(), pointList);
            strategyComposite.addStrategy(strategy);
            strategyComposite.setCondition(condition.getValue());
        }


        condList.getItems().add(" From " + from
                + " to " + to + " percent change " + percent.getText()
                + " action " + change.getValue().toString());


    }

    @FXML
    private void handleDoneButton(ActionEvent event){

        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Evaluation");
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
