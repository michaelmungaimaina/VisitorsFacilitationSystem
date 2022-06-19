package com.ownres.controller;


import com.ownres.model.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

/**
 * *******************************************************************************
 * *******************************OWNRESTECH**************************************
 * *******************************************************************************
 * *******************Created and distributed by OWNRESTECH***********************
 * **************************Supremacy is to Subdue*******************************
 * ******************************Hard to Achieve**********************************
 */
public class Launcher extends Application {

    /**
     * Method that is launched when the application is executed
     * It starts the stage and loads login window as the main view
     * The stage is the primary stage
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root =FXMLLoader.load(getClass().getResource("/com/ownres/view/LoginWindow.fxml"));
        primaryStage.setTitle("VISITORS FACILITATION SYSTEM");
        primaryStage.resizableProperty().setValue(false);
        primaryStage.setScene(new Scene(root, 516, 538));
        primaryStage.show();
        primaryStage.centerOnScreen();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
        Image image =new Image("com/ownres/icons/Launcher_Icon.png");
        primaryStage.getIcons().add(image);
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            cancel(primaryStage);

        });
    }

    private void executeQuery(String query) {
        try {
            Connection connection = DatabaseConnection.ConnectDatabase();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTime() throws Exception{
        String query = "SELECT * FROM session";
        Connection conn = DatabaseConnection.ConnectDatabase();
        assert conn != null;
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        String firstName = null;
        while (resultSet.next()){
            firstName = resultSet.getString("time");
        }
        return firstName;
    }

    public void clearRecords(){
        int currentMin = Integer.parseInt(String.valueOf(java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("mm"))).substring(String.valueOf(java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("mm"))).length() - 2));
        int initTime = 0;
        try {
            initTime = Integer.parseInt(String.valueOf(getTime()).substring(String.valueOf(getTime()).length() - 2));
        } catch (Exception e) {
            e.printStackTrace();
        }

        int timeChange1 = currentMin - initTime;
        int timeChange2 = initTime - currentMin;

        if (timeChange2 >= 1 || timeChange1 >= 1){
            String deleteQuery = "delete from session";
            executeQuery(deleteQuery);
        }
    }


    /**
     * Cancel method
     * it closes the stage and exits the program when invoked
     * @param stage
     */
    void cancel(Stage stage) {
        Image image =new Image("com/ownres/icons/Launcher_Icon.png");
        Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
        cancelAlert.setTitle("QUIT");
        cancelAlert.setHeaderText("You are about to quit application.");
        cancelAlert.setContentText("Do you want to Quit?");
        if (cancelAlert.showAndWait().get() == ButtonType.OK) {
            stage.close();
            clearRecords();
        }
    }

    /**
     * This is the main method
     * This is the launcher program where the activity of the whole program lies
     * It is loaded into the main memory by the loader in OS when invoked
     * It is the launcher program that contains the macros
     * @param args
     */
        public static void main (String[]args){
            launch(args);


        }


    }
