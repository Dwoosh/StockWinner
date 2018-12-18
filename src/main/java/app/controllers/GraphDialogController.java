package app.controllers;

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
    private ComboBox<StrategyEnums.Actions> action;

    @FXML
    private ComboBox<StrategyEnums.Conditions> condition;

    @FXML
    private TextField percent;

    @FXML
    private ListView<String> condList;

    private DataPointList pointList;

    private StrategyComposite strategyComposite;

    public void initialize(AppController controller, String fileLocation, WebSites.SupportedWebSites chosenWebsite, Pane chartParent) throws IOException {

        this.appController = controller;
        this.action.setItems(FXCollections.observableArrayList(StrategyEnums.Actions.values()));
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
        pointList =  reader.getDataPointList();

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

        String cond;
        if(condition.getSelectionModel().isEmpty()){
            cond = "";
            condition.setDisable(false);
            IStrategyComponent strategy = new Strategy(dateFrom.getValue(), dateTo.getValue(),
                    new BigDecimal(percent.getText()), action.getValue(), condition.getValue(), pointList);
            strategyComposite = new StrategyComposite(strategy);
            strategyComposite.setCondition(StrategyEnums.Conditions.NONE);

        }
        else {
            cond = condition.getValue().toString();
            condition.setDisable(true);
            IStrategyComponent strategy = new Strategy(dateFrom.getValue(), dateTo.getValue(),
                    new BigDecimal(percent.getText()), action.getValue(), condition.getValue(), pointList);
            strategyComposite.addStrategy(strategy);
            strategyComposite.setCondition(condition.getValue());
        }


        condList.getItems().add(cond +" From " + dateFrom.getValue()
                + " to " + dateTo.getValue() + " percent change " + percent.getText()
                + " action " + action.getValue().toString());


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
            alert.setContentText("No valid date was found");
            alert.showAndWait();
            return;
        }
        catch (InvalidConditionException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid condition");
            alert.setContentText("Invalid condition exception");
            alert.showAndWait();
            return;
        }


    }

}
