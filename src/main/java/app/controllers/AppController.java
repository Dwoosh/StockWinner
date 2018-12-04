package app.controllers;

import app.model.DataPoint;
import app.model.DataPointList;
import app.readers.FormatReader;
import app.readers.ReaderFactory;
import app.readers.WebSites;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sun.misc.FormattedFloatingDecimal;

import java.util.Date;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;



public class AppController {

    private Stage primaryStage;

    private String fileLocation;

    private WebSites.SupportedWebSites chosenWebsite;

    public AppController(Stage stage){
        this.primaryStage = stage;
    }

    public void initialize(){
        try {
            primaryStage.setTitle("Stock Winner");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("views/FileOpenerView.fxml"));
            AnchorPane layout = loader.load();

            FileOpenerController controller = loader.getController();
            controller.initialize(this);
            Scene scene = new Scene(layout);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void switchScene(String view){
        try {
            primaryStage.setTitle("Line Chart");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource(view));
            AnchorPane layout = loader.load();

            GraphDialogController controller = loader.getController();
            controller.initialize(this, fileLocation, chosenWebsite);
            Scene scene = new Scene(layout);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage(){
        return this.primaryStage;
    }

    public void setFileLocation(String location){
        this.fileLocation = location;
    }

    public void setChosenWebsite(WebSites.SupportedWebSites chosenWebsite) {
        this.chosenWebsite = chosenWebsite;
    }
}
