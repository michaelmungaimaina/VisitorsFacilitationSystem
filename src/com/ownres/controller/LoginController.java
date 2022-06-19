package com.ownres.controller;
import com.ownres.model.DatabaseConnection;
import com.ownres.model.Login;

import com.ownres.model.UserValidity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import static java.time.LocalTime.now;


/**
 *
 * *******************************************************************************
 * *******************************OWNRESTECH**************************************
 * *******************************************************************************
 * *******************Created and distributed by OWNRESTECH***********************
 * **************************Supremacy is to Subdue*******************************
 */

public class LoginController implements Initializable {

    @FXML
    private AnchorPane loginAnchorpane;

    @FXML
    private ChoiceBox<String> logInChoiceBox=new ChoiceBox<>();
    @FXML
    private PasswordField loginPasswordField;
    @FXML
    private TextField userNationalIdTextField;

    @FXML
    private Button passwordResetButton;
    @FXML
    private Button signupButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button cancelButton;

    @FXML
    private Label errorLabel;

    public static String userDesignation;
    public static String userId;

    Connection conn=null;
    Statement stm=null;
    ResultSet rs=null;
    PreparedStatement ps=null;
    Stage stage;
    Parent parent;
    Scene scene;

    /**
     * Method for Signup button pressed
     * It shifts the current context to a signup window
     * If path to the signup fxml is wrong or does not exist, an exception is thrown
     * @param event
     * @throws Exception
     */
    @FXML
    void signup(ActionEvent event)throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ownres/view/SignupWindow.fxml"));
        stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.sizeToScene();
        //Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        //stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        //stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

    }

    /**
     * Method for Action event button cancel is pressed
     * if pressed, the current stage is closed and the application exits
     * @param event
     */
    @FXML
    void cancel(ActionEvent event) {
        Alert cancelAlert=new Alert(Alert.AlertType.CONFIRMATION);
        cancelAlert.setTitle("CANCEL");
        cancelAlert.setHeaderText("You are about to cancel login");
        cancelAlert.setContentText("Do you really want to Cancel Login!!?");
        if(cancelAlert.showAndWait().get()==ButtonType.OK){
            stage=(Stage) loginAnchorpane.getScene().getWindow();
            stage.close();
        }

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

    /**
     * Method for Action event login button pressed
     * It checks if the input fields are empty or they got value
     * If empty, the user is prompted with an interruption
     * If filled but with wrong credentials, the user is prompted to enter valid inputs
     * If inputs are authentic the current context is changed to main context
     * @param event
     */
    @FXML
    void login(ActionEvent event) {
        Login login=new Login(userNationalIdTextField.getText(),loginPasswordField.getText(),logInChoiceBox.getValue());
        System.out.println(login.getUserNationalId());
        System.out.println(login.getUserNationalId());
        login.getUserNationalId();
        System.out.println(login.getDesignation());
        try {
            System.out.println(getFirstName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String query1 = null;
        try {
            query1 = "insert into session values('"+ login.getUserNationalId()+"','" + java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("mm")) +
                    "','" + getFirstName() + "','" + getSecondName() + "','" + getUsertype() + "')";
        } catch (Exception e) {
            e.printStackTrace();
        }
       executeQuery(query1);
        String query="select count(1) from Table_Users where National_ID='"+userNationalIdTextField.getText()+"'and Password='"+loginPasswordField.getText()+"'and Designition='"+logInChoiceBox.getValue()+"'";
        try {
            conn=DatabaseConnection.ConnectDatabase();
            stm=conn.createStatement();
            rs=stm.executeQuery(query);
            while (rs.next()){
                if(userNationalIdTextField.getText().equals("")){
                    userNationalIdTextField.setStyle("-fx-border-color:red;-fx-background-radius:30px;-fx-border-radius:30px");
                    userNationalIdTextField.requestFocus();
                    errorLabel.setText("ID number cannot be null!");
                    errorLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 14));
                    errorLabel.setTextFill(Color.RED);
                    Alert alert=new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("NULL ID NUMBER!");
                    alert.setHeaderText("ID number cannot be null");
                    alert.setContentText("Enter your ID number to login");
                    alert.setGraphic(new ImageView(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png").toString()));
                    alert.showAndWait();

                    return;
                }
                if(loginPasswordField.getText().equals("")){
                    loginPasswordField.setStyle("-fx-border-color:red;-fx-background-radius:30px;-fx-border-radius:30px");
                    loginPasswordField.requestFocus();
                    errorLabel.setText("Password field cannot be null");
                    errorLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 14));
                    errorLabel.setTextFill(Color.RED);
                    //JOptionPane.showMessageDialog(null,"Enter password to login");
                    Alert alert=new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("NULL PASSWORD!");
                    alert.setHeaderText("Password field cannot be null");
                    alert.setContentText("Enter your password to login");
                    alert.setGraphic(new ImageView(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png").toString()));
                    alert.showAndWait();

                    return;
                }

                if(logInChoiceBox.getValue().equals("")){
                    logInChoiceBox.setStyle("-fx-border-color:red;-fx-background-radius:30px;-fx-border-radius:30px");
                    return;
                }

                if(rs.getInt(1)==1){
                    errorLabel.setTextFill(Color.GREEN);
                    System.out.println("You have logged in successfully");
                    errorLabel.setText("You have successfully login");
                    errorLabel.setTextFill(Color.GREEN);
                    userNationalIdTextField.setText("");
                    loginPasswordField.setText("");
                    logInChoiceBox.setValue("");
                    errorLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 14));
                    Parent root= FXMLLoader.load(getClass().getResource("/com/ownres/view/MainWindow.fxml"));
                    stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
                    stage.resizableProperty().setValue(true);
                    scene=new Scene(root,1350,750);
                    stage.setScene(scene);
                    stage.setY(0);
                    stage.setX(0);
                    stage.show();
                    stage.centerOnScreen();
                }
                else {
                    errorLabel.setText("Kindly, enter valid login credentials!");
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 14));
                    userNationalIdTextField.setText("");
                    loginPasswordField.setText("");
                    Alert loginAlert = new Alert(Alert.AlertType.WARNING);
                    loginAlert.setTitle("INVALID");
                    loginAlert.setHeaderText("Invalid login credentials!!");
                    loginAlert.setContentText("Your trial to login access denied");
                    loginAlert.setGraphic(new ImageView(String.valueOf(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png"))));
                    loginAlert.showAndWait();
                    userNationalIdTextField.requestFocus();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    //int id = 36783358;
    public String getFirstName() throws Exception{
        String query = "SELECT First_Name FROM Table_Users WHERE National_ID ='"+ userNationalIdTextField.getText() +"'";
        Connection conn = DatabaseConnection.ConnectDatabase();
        assert conn != null;
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        String firstName = null;
        while (resultSet.next()){
            firstName = resultSet.getString("First_Name");
        }
        return firstName;
    }
    public String getSecondName() throws Exception{
        String query = "SELECT Second_Name FROM Table_Users WHERE National_ID ='"+ userNationalIdTextField.getText() +"'";
        Connection conn = DatabaseConnection.ConnectDatabase();
        assert conn != null;
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        String firstName = null;
        while (resultSet.next()){
            firstName = resultSet.getString("Second_Name");
        }
        return firstName;
    }
    public String getUsertype() throws Exception{
        String query = "SELECT Designition FROM Table_Users WHERE National_ID ='"+ userNationalIdTextField.getText() +"'";
        Connection conn = DatabaseConnection.ConnectDatabase();
        assert conn != null;
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        String firstName = null;
        while (resultSet.next()){
            firstName = resultSet.getString("Designition");
        }
        return firstName;
    }



    /**
     * Method to populate the ChoiceBox
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String > designation= FXCollections.observableArrayList("Admin","Attache","Intern","Guard","KRA Officer");
        logInChoiceBox.getItems().addAll(designation);
        logInChoiceBox.setValue("Select Designation");

        userDesignation = logInChoiceBox.getValue();
        userId = userNationalIdTextField.getText().trim();

        UserValidity.setUserId(userId);

    }

    /**
     * Method for Action event reset password
     * It changes the context from Login window to Passwordreset window
     * Throws an exception if the path to the fxml file is wrong or does not exist
     * @param event
     * @throws Exception
     */
    public void resetPassword(ActionEvent event) throws Exception{
        Parent root= FXMLLoader.load(getClass().getResource("/com/ownres/view/PasswordReset.fxml"));
            stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
            scene=new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.sizeToScene();
        }

    public void getKeyEvent(KeyEvent keyEvent) throws Exception{
                if(keyEvent.getCode() == KeyCode.ENTER){
                    {
                        Login login=new Login(userNationalIdTextField.getText(),loginPasswordField.getText(),logInChoiceBox.getValue());
                        System.out.println(login.getUserNationalId());
                        System.out.println(login.getUserNationalId());
                        login.getUserNationalId();
                        System.out.println(login.getDesignation());
                        String query="select count(1) from Table_Users where National_ID='"+userNationalIdTextField.getText()+"'and Password='"+loginPasswordField.getText()+"'and Designition='"+logInChoiceBox.getValue()+"'";
                        try {
                            conn=DatabaseConnection.ConnectDatabase();
                            stm=conn.createStatement();
                            rs=stm.executeQuery(query);
                            while (rs.next()){

                                if(userNationalIdTextField.getText().equals("")){
                                    userNationalIdTextField.setStyle("-fx-border-color:red;-fx-background-radius:30px;-fx-border-radius:30px");
                                    userNationalIdTextField.requestFocus();
                                    errorLabel.setText("ID number cannot be null!");
                                    errorLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 14));
                                    errorLabel.setTextFill(Color.RED);
                                    Alert alert=new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("NULL ID NUMBER!");
                                    alert.setHeaderText("ID number cannot be null");
                                    alert.setContentText("Enter your ID number to login");
                                    alert.setGraphic(new ImageView(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png").toString()));
                                    alert.showAndWait();

                                    return;
                                }
                                if(loginPasswordField.getText().equals("")){
                                    loginPasswordField.setStyle("-fx-border-color:red;-fx-background-radius:30px;-fx-border-radius:30px");
                                    loginPasswordField.requestFocus();
                                    errorLabel.setText("Password field cannot be null");
                                    errorLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 14));
                                    errorLabel.setTextFill(Color.RED);
                                    //JOptionPane.showMessageDialog(null,"Enter password to login");
                                    Alert alert=new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("NULL PASSWORD!");
                                    alert.setHeaderText("Password field cannot be null");
                                    alert.setContentText("Enter your password to login");
                                    alert.setGraphic(new ImageView(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png").toString()));
                                    alert.showAndWait();

                                    return;
                                }
                                if(logInChoiceBox.getValue().equals("")){
                                    logInChoiceBox.setStyle("-fx-border-color:red;-fx-background-radius:30px;-fx-border-radius:30px");
                                    return;
                                }

                                if(rs.getInt(1)==1){
                                    errorLabel.setTextFill(Color.GREEN);
                                    System.out.println("You have logged in successfully");
                                    errorLabel.setText("You have successfully login");
                                    errorLabel.setTextFill(Color.GREEN);
                                    userNationalIdTextField.setText("");
                                    loginPasswordField.setText("");
                                    logInChoiceBox.setValue("");
                                    errorLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 14));
                                    Parent root= FXMLLoader.load(getClass().getResource("/com/ownres/view/MainWindow.fxml"));
                                    stage =(Stage) ((Node)keyEvent.getSource()).getScene().getWindow();
                                    stage.resizableProperty().setValue(true);
                                    scene=new Scene(root,1350,750);
                                    stage.setScene(scene);
                                    stage.setY(0);
                                    stage.setX(0);
                                    stage.show();
                                    stage.centerOnScreen();

                                }
                                else {
                                    errorLabel.setText("Kindly, enter valid login credentials!");
                                    errorLabel.setTextFill(Color.RED);
                                    errorLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 14));
                                    userNationalIdTextField.setText("");
                                    loginPasswordField.setText("");
                                    Alert loginAlert = new Alert(Alert.AlertType.WARNING);
                                    loginAlert.setTitle("INVALID");
                                    loginAlert.setHeaderText("Invalid login credentials!!");
                                    loginAlert.setContentText("Your trial to login access denied");
                                    loginAlert.setGraphic(new ImageView(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png").toString()));
                                    loginAlert.showAndWait();
                                    userNationalIdTextField.requestFocus();

                                }

                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


            }


    public void keyEnter(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER){
            loginPasswordField.requestFocus();
        }
    }
}