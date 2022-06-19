package com.ownres.controller;

import com.ownres.model.DatabaseConnection;
import com.ownres.model.Signup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * *******************************************************************************
 * *******************************OWNRESTECH**************************************
 * *******************************************************************************
 * *******************Created and distributed by OWNRESTECH***********************
 * **************************Supremacy is to Subdue*******************************
 */

public class SignupWindowController  implements Initializable {

    @FXML
    private ComboBox<String> designationComboBox=new ComboBox<>();
    @FXML
    private ComboBox<String > genderComboBox=new ComboBox<>();

    @FXML
    private AnchorPane newUserAnchorPane;

    @FXML
    private Button newUserBackButton;
    @FXML
    private Button newUserCancelButton;
    @FXML
    private Button newUserRegisterButton;

    @FXML
    private Label newUserEmailLabel;
    @FXML
    private Label newUserConfirmPasswordLabel;
    @FXML
    private Label newUserFirstNameLabel;
    @FXML
    private Label newUserGenderLabel;
    @FXML
    private Label newUserGuideLabel;
    @FXML
    private Label newUserLabel;
    @FXML
    private Label newUserPasswordLabel;
    @FXML
    private Label newUserRegistrationLabel;
    @FXML
    private Label newuserUserNameLabel;
    @FXML
    private Label passwordMismatchLabel;
    @FXML
    private Label warningLabel;

    @FXML
    private PasswordField newUserConfirmPasswordTextField;
    @FXML
    private TextField newUserSecondNameTextField;
    @FXML
    private TextField newUserFirstNameTextField;
    @FXML
    private TextField newUserLastNameTextField;
    @FXML
    private PasswordField newUserPasswordTextField;
    @FXML
    private TextField newUserNationalIdTextField;
    @FXML
    private TextField newUserPhoneNumberTextField;

    @FXML
    private ImageView newUserImageView;

    Parent parent;
    Scene scene;
    Stage stage;
    Connection connection;
    ResultSet resultSet;
    PreparedStatement preparedStatement;

    /**
     * Action to swicth from the current context to login context
     * @param event
     * @throws Exception
     */
    @FXML
    void backToLogin(ActionEvent event)throws Exception {
        parent= FXMLLoader.load(getClass().getResource("/com/ownres/view/LoginWindow.fxml"));
        stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(parent);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        //Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        //stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        //stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

    }

    /**
     * Action to cancel SignUp window
     * Returns to login window
     * @param event
     */
    @FXML
    void cancelSignup(ActionEvent event) {
       Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
       alert.setTitle("CANCEL");
       alert.setHeaderText("New user Registration cancellation");
       alert.setContentText("This session will be terminated. " +
               "Do you really want to cancel??");
        alert.setGraphic(new ImageView(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png").toString()));
        if(alert.showAndWait().get()==ButtonType.OK){
           stage.close();
       }
    }

    /**
     * Method for Action event register
     * Checks if Passwords match
     * Checks whether every input fields are empty
     * If input field is empty, requestFocus() is called and an error text is displayed.
     * If input field is not empty and values are authentic, a row instance is created in the database
     * @param event
     */
    @FXML
    void register(ActionEvent event) {
        String query="insert into Table_Users(National_ID,First_Name,Second_Name,Third_Name,Gender,Phone_Number,Designition,Password) " +
                "values('"+newUserNationalIdTextField.getText()+"','"+newUserFirstNameTextField.getText()+"','"+newUserSecondNameTextField.getText()+"','"+newUserLastNameTextField.getText()+"','"+genderComboBox.getValue()+"','"+newUserPhoneNumberTextField.getText()+"','"+designationComboBox.getValue()+"','"+newUserPasswordTextField.getText()+"')";
try {
    if(newUserFirstNameTextField.getText().isEmpty()){
        System.out.println("Firstname empty!");
        warningLabel.setText("Enter your first name");
        newUserFirstNameTextField.requestFocus();

        return;
    }
    if(newUserSecondNameTextField.getText().isEmpty()){
        System.out.println("Second Name is empty!");
        warningLabel.setText("Enter your Second Name!");
        newUserSecondNameTextField.requestFocus();
    }
    if(newUserLastNameTextField.getText().isEmpty()){
        System.out.println("Lastname empty!");
        warningLabel.setText("Enter your Last Name!");
        newUserLastNameTextField.requestFocus();
        return;
    }
    if(genderComboBox.getValue().isEmpty()){
        System.out.println("Gender empty!");
        warningLabel.setText("Select your Gender!");
        return;
    }
    if(newUserPhoneNumberTextField.getText().isEmpty()){
        System.out.println("Phone Number is empty!");
        warningLabel.setText("Enter your Phone Number!");
        newUserPhoneNumberTextField.requestFocus();
        return;
    }
    if(newUserNationalIdTextField.getText().isEmpty()){
        warningLabel.setText("ID cannot be Empty!");
        newUserNationalIdTextField.requestFocus();
    }
    if(designationComboBox.getValue().isEmpty()){
        System.out.println("Designation empty!");
        warningLabel.setText("Select your Designation!");
        return;
    }
    if(newUserPasswordTextField.getText().isEmpty()){
        System.out.println("Password empty!");
        warningLabel.setText("Enter your password!");
        newUserPasswordTextField.requestFocus();
        return;
    }
    if(!newUserPasswordTextField.getText().equals(newUserConfirmPasswordTextField.getText())){
        System.out.println("Password do not match");
        warningLabel.setText("Your passwords do not match!");
        newUserConfirmPasswordTextField.requestFocus();
        passwordMismatchLabel.setText("Passwords do not match!");
        return;
    }
    if(isDublicateUserID()){
        System.out.println("Duplicate ID");
        warningLabel.setText("This ID number belongs to another person!");
        Alert alert=new Alert(Alert.AlertType.WARNING);
        alert.setTitle("IMPERSONIFICATION");
        alert.setHeaderText(" The ID number "+newUserNationalIdTextField.getText()+"  belongs to another person!");
        alert.setContentText("Kindly use your National ID number.");
        alert.setGraphic(new ImageView(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png").toString()));
        alert.showAndWait();
        newUserNationalIdTextField.requestFocus();
        return;
    }
    else {
        connection=DatabaseConnection.ConnectDatabase();
        Statement statement=connection.createStatement();
        statement.executeUpdate(query);
       System.out.println("Registration successful.");
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SUCCESS");
        alert.setContentText("You Have Successfully created your account "+newUserFirstNameTextField.getText()+".");
        alert.setGraphic(new ImageView(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png").toString()));
        newUserFirstNameTextField.clear();
        newUserLastNameTextField.clear();
        newUserNationalIdTextField.clear();
        newUserPhoneNumberTextField.clear();
        newUserPasswordTextField.clear();
        newUserConfirmPasswordTextField.clear();
        newUserSecondNameTextField.clear();

    }
} catch (Exception e) {
    e.printStackTrace();
}
    }

    /**
     * Method to populate ChoiceBox
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<String> gender= FXCollections.observableArrayList("Male","Female");
        genderComboBox.getItems().addAll(gender);
        genderComboBox.setVisible(true);
        genderComboBox.setPromptText("Select your gender");
        //genderComboBox.setValue("Male");

        ObservableList<String> designation=FXCollections.observableArrayList("Admin","Attache","Intern","Guard","KRA Officer");
        designationComboBox.getItems().addAll(designation);
        designationComboBox.setVisible(true);
        //designationComboBox.setValue("Admin");
        designationComboBox.setPromptText("Select your designation");

    }

    /**
     * Method to check if there's another instance of the input id --- checks for uniqueness
     * returns true if there is duplication.
     * @return
     */
    public boolean isDublicateUserID(){
        String username="select count(1) from Table_Users where National_ID='"+newUserNationalIdTextField.getText()+"'";
        try {
            connection=DatabaseConnection.ConnectDatabase();
            Statement st=connection.createStatement();
            resultSet=st.executeQuery(username);
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
}

