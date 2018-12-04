package app.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;



public class AppController {

    private Stage primaryStage;

    private String fileLocation;

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
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource(view));
            GraphDialogController controller = loader.getController();
            //zmiana pane'a jeśli trzeba
            AnchorPane layout = loader.load();
            controller.initialize(this);

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

}
