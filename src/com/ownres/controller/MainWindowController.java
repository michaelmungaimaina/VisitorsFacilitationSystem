
package com.ownres.controller;

import com.ownres.model.DatabaseConnection;
import com.ownres.model.UserValidity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    private ImageView logOutImageView;

    @FXML
    private AnchorPane mainWindowAnchorPane;
    @FXML
    private AnchorPane HboxAnchorPane;


    @FXML
    private HBox mainWindowHBox;
    @FXML
    private HBox buttonsHBox;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Button reportsButton;
    @FXML
    private Button infoButton;
    @FXML
    private Button forgottenIdButton;
    @FXML
    private Button complainsButton;
    @FXML
    private Button graphsButton;
    @FXML
    private Button logOutButton;
    @FXML
    private Button facilitateButton;
    @FXML
    private Button visitorsButton;
    @FXML
    private Button complimentsButton;
    @FXML
    private Button usersButton;
    @FXML
    private Button analysisButton;
    @FXML
    private Button detailButton;
    @FXML
    private Button menuButton;

    @FXML
    private Label myLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label greetingLabel;
    @FXML
    private Label facilitationButton;



    Stage stage;
    Parent root;
    Scene scene;
    Connection conn;
    Statement stm;
    ResultSet rs;

    @FXML
    void editSchoolInformation(ActionEvent event) {

    }

    @FXML
    void showVisitorsWindow(ActionEvent event) {

    }

    @FXML
    void showForgotIdWindow(ActionEvent event) {

    }

    @FXML
    void showAvailableCompliments(ActionEvent event) {

    }

    @FXML
    void showReportedComplains(ActionEvent event) {

    }

    @FXML
    void showReports(ActionEvent event) {

    }

    @FXML
    void showFacilitationGraphs(ActionEvent event) {

    }

    @FXML
    void performAnalysis(ActionEvent event) {

    }

    @FXML
    void logoutMainWindow(ActionEvent event) throws IOException{
        Alert logoutAlert=new Alert(Alert.AlertType.CONFIRMATION);
        logoutAlert.setTitle("MAIN WINDOW LOGOUT");
        logoutAlert.setHeaderText("This Session Will be Terminated!");
        logoutAlert.setContentText("Do you Really want to logout?");
        if(logoutAlert.showAndWait().get()==ButtonType.OK){
            Parent root = FXMLLoader.load(getClass().getResource("/com/ownres/view/LoginWindow.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 571, 532);
            stage.setScene(scene);
            stage.resizableProperty().setValue(false);
            stage.show();
            stage.centerOnScreen();
            //Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            //stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            //stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
            clearRecords();
        }

    }


    @FXML
    void showUsersWindow(ActionEvent event) throws Exception {
        String userType = getUserType();
        if (!userType.equals("Admin")){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("UNAUTHORISED ACCESS!");
            alert.setHeaderText("You are not an Admin!");
            alert.setContentText("Contact your Administrator for Assistance.");
            alert.showAndWait();
        }
        else {
            Parent root = FXMLLoader.load(getClass().getResource("/com/ownres/view/Users.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1390, 700);
            stage.setScene(scene);
            stage.resizableProperty().setValue(true);
            stage.show();
            stage.centerOnScreen();
            //Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            //stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            //stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        }
    }

    @FXML
    void launchFacilitationWindow(ActionEvent event)throws Exception {
        root= FXMLLoader.load(getClass().getResource("/com/ownres/view/VisitorsWindow.fxml"));
        stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        //Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        //stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        //stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

    }


    public String getFirstName() throws Exception{
        String query = "SELECT * FROM session";
        Connection conn = DatabaseConnection.ConnectDatabase();
        assert conn != null;
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        String firstName = null;
        while (resultSet.next()){
            firstName = resultSet.getString("firstname");
        }
        return firstName;
    }
    public String getUserType() throws Exception{
        String query = "SELECT * FROM session";
        Connection conn = DatabaseConnection.ConnectDatabase();
        assert conn != null;
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        String firstName = null;
        while (resultSet.next()){
            firstName = resultSet.getString("usertype");
        }
        return firstName;
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

    //public static String logType = String.valueOf(logInChoiceBox.getValue());
    private void executeQuery(String query) {
        try {
            Connection connection = DatabaseConnection.ConnectDatabase();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (LocalTime.now().isBefore(LocalTime.NOON)){
            greetingLabel.setText("Good Morning,");
        }
        else if (LocalTime.now().isAfter(LocalTime.NOON)){
            greetingLabel.setText("Good Afternoon,");
        }

        String userDesgn = LoginController.userDesignation.toUpperCase();
        try {
            String userType = getUserType();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            nameLabel.setText(getFirstName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        int time = 0;
        try {
            time = Integer.parseInt(String.valueOf(getTime()).substring(String.valueOf(getTime()).length() - 2));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (time >= 1)
        System.out.println(String.valueOf(LoginController.userId.toUpperCase()));
    }

}




