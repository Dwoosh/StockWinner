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

    private Scene helloScene;
    private Scene graphScene;

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
            helloScene = new Scene(layout);
            primaryStage.setScene(helloScene);
            primaryStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void initGraphScene(String view){
        try {
            primaryStage.setTitle("Line Chart");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource(view));
            AnchorPane layout = loader.load();

            GraphDialogController controller = loader.getController();
            controller.initialize(this, fileLocation, chosenWebsite, layout);
            graphScene = new Scene(layout);
            primaryStage.setScene(graphScene);
            primaryStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void goBackToFirstWindow() {
        primaryStage.setScene(helloScene);
        primaryStage.show();
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
