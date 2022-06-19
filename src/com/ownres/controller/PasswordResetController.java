package com.ownres.controller;

import com.ownres.model.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * *******************************************************************************
 * *******************************OWNRESTECH**************************************
 * *******************************************************************************
 * *******************Created and distributed by OWNRESTECH***********************
 * **************************Supremacy is to Subdue*******************************
 */
public class PasswordResetController  implements Initializable {

    @FXML
    private AnchorPane resetPasswordAnchorPane;
    @FXML
    private HBox resetPasswordHbox;

    @FXML
    private Label appLabel;
    @FXML
    private Label guideLabel;
    @FXML
    private Label passwordResetLabel;

    @FXML
    private PasswordField confirmPasswordText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private TextField userIdText;

    @FXML
    private Button loginButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button resetButton;

    Stage stage;
    Parent parent;
    Scene scene;
    Connection connection;
    ResultSet resultSet;
    Statement statement;

    /**
     * Event to switch context back to login window
     * @param event
     * @throws Exception
     */
    @FXML
    void backtoLogin(ActionEvent event) throws Exception {
        Parent root= FXMLLoader.load(getClass().getResource("/com/ownres/view/LoginWindow.fxml"));
        stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        //Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        //stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        //stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

    }

    /**
     * Closes the resetPassword window
     * @param event
     */
    @FXML
    void cancelPasswordReset(ActionEvent event) {
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("PASSWORD RESET CANCELLATION");
        alert.setHeaderText("This process will be Terminated...!");
        alert.setContentText("Do You Want To Cancel This Process?");
        alert.setGraphic(new ImageView(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png").toString()));
        if(alert.showAndWait().get()==ButtonType.OK){
            stage= (Stage) resetPasswordAnchorPane.getScene().getWindow();
            stage.close();
        }

    }

    /**
     * Actual event of password change
     * This method queries the database to update password at the column of the provided id number
     * @param event
     */
    @FXML
    void resetPassword(ActionEvent event) {
        String query="update  Table_Users set Password='"+passwordText.getText()+"' where National_ID='"+userIdText.getText()+"'";
        try{
            if(validateFields()){

                Alert alert=new Alert(Alert.AlertType.WARNING);
                alert.setTitle("EMPTY FIELD(s)");
                alert.setHeaderText("Fields cannot be empty...");
                alert.setContentText("Enter your username and new password to reset your password.");
                alert.setGraphic(new ImageView(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png").toString()));
                alert.showAndWait();
                return;
            }
            if(!isValidUserID()){
                Alert alert=new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ID NOT FOUND");
                alert.setHeaderText(" "+userIdText.getText()+"  is invalid ID Number!");
                alert.setContentText("Kindly enter a valid ID to reset your password.");
                alert.setGraphic(new ImageView(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png").toString()));
                alert.showAndWait();
                userIdText.clear();
                confirmPasswordText.clear();
                passwordText.clear();
                userIdText.requestFocus();
                return;
            }
            if(!isValidPassword()){
                Alert alert=new Alert(Alert.AlertType.WARNING);
                alert.setTitle("PASSWORD");
                alert.setHeaderText("PASSWORD  TOO WEAK");
                alert.setContentText("Make sure you entered a strong password containing upper case letters," +
                        "lower case letters, digits and special characters");
                alert.setGraphic(new ImageView(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png").toString()));
                alert.showAndWait();
                passwordText.requestFocus();

                return;

            }
            if(!passwordText.getText().equals(confirmPasswordText.getText())){
                Alert alert=new Alert(Alert.AlertType.WARNING);
                alert.setTitle("PASSWORDS MISMATCH");
                alert.setContentText("Make sure your two passwords matches to complete this process");
                alert.setGraphic(new ImageView(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png").toString()));
                confirmPasswordText.requestFocus();
                alert.showAndWait();
            }

            else {

                connection = DatabaseConnection.ConnectDatabase();
                statement = connection.createStatement();
                statement.executeUpdate(query);
              Alert alert=new Alert(Alert.AlertType.INFORMATION);
              alert.setTitle("SUCCESS");
              alert.setHeaderText("Password updated successfully");
              alert.setContentText("Dear "+userIdText.getText()+", You have successfully reset your password.");
                alert.setGraphic(new ImageView(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png").toString()));
                alert.showAndWait();
              userIdText.clear();
              confirmPasswordText.clear();
              passwordText.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to check if ID provided exists in our database
     * @return
     */
    public boolean isValidUserID(){
        String query="Select count(1) from Table_Users where National_ID='"+userIdText.getText()+"'";
        try{
            connection= DatabaseConnection.ConnectDatabase();
            statement=connection.createStatement();
            resultSet=statement.executeQuery(query);
            while (resultSet.next()){
                if(resultSet.getInt(1)==1){
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
return false;
    }

    /**
     * Method to check the strength of the password
     * Ensures that the password includes at least 1 uppercase letter and 1 lowercase with at least 1 special character
     * @return
     */
    @FXML
    private boolean isValidPassword(){
        String password="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,15})";
        Pattern pattern=Pattern.compile(password);
        Matcher matcher=pattern.matcher(passwordText.getText());
        // System.out.println("Strong password");
        return matcher.matches();
    }

    /**
     * Method to check if input fields are empty
     * returns true if are empty
     * @return
     */
    private  boolean validateFields(){
        if(passwordText.getText().isEmpty()||userIdText.getText().isEmpty()||confirmPasswordText.getText().isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}