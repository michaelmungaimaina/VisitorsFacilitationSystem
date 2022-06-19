package com.ownres.controller;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;
import com.ownres.model.User;
import com.ownres.model.DatabaseConnection;
import javafx.stage.Stage;

/*********************************************************************************
 * *******************************OWNRESTECH**************************************
 * *******************************************************************************
 * *******************Created and distributed by OWNRESTECH***********************
 * **************************Supremacy is to Subdue*******************************
 */

public class UserController implements Initializable {

    @FXML
    private ImageView addImageView;
    @FXML
    private GridPane userInformationGridPane;
    @FXML
    private AnchorPane userAnchorPane;
    @FXML
    private Separator userSeparator;

    @FXML
    private ImageView serchImageView;
    @FXML
    private ImageView deleteImageView;
    @FXML
    private ImageView updateImageView;
    @FXML
    private ImageView backImageView;

    @FXML
    private Button backButton;
    @FXML
    private Button buttonRefresh;
    @FXML
    private Button deleteButton;
    @FXML
    private Button serchButton;

    @FXML
    private HBox topLeftUserHbox;
    @FXML
    private HBox topUserHbox;
    @FXML
    private HBox usertableViewHbox;
    @FXML
    private VBox leftUserVBox;


    @FXML
    private Label userFirstNameLabel;
    @FXML
    private Label userGenderLabel;
    @FXML
    private Label userIdLabel;
    @FXML
    private Label userLastNameLabel;
    @FXML
    private Label passwordLabel;

    @FXML
    private TextField userLastNameText;
    @FXML
    private TextField userIdText;
    @FXML
    private TextField userFirstNameText;
    @FXML
    private TextField userSecondNameText;
    @FXML
    private TextField phoneNumberText;
    @FXML
    private TextField serchText;
    @FXML
    private TextField deleteText;
    @FXML
    private PasswordField passwordText;


    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> userPasswordCol;
    @FXML
    private TableColumn<User, String> userSecondNameCol;
    @FXML
    private TableColumn<User, String> userPhoneNumberCol;
    @FXML
    private TableColumn<User, String> userDesignitionCol;
    @FXML
    private TableColumn<User, String> userLastNameCol;
    @FXML
    private TableColumn<User, Integer> userIdCol;
    @FXML
    private TableColumn<User, String> userGenderCol;
    @FXML
    private TableColumn<User, String> userFirstNameCol;

    @FXML
    private ComboBox<String> userDesignitionCombo = new ComboBox<String>();
    @FXML
    private ComboBox<String> userGenderCombo = new ComboBox<String>();

    Stage stage;
    Parent root;
    Scene scene;
    Connection conn;
    Statement stm;
    ResultSet rs;

    /**
     * Onclick Method for Insert
     * @param event
     */
    @FXML
    void addNewUser(ActionEvent event) {
        String query = "insert into Table_Users values('" + userIdText.getText() + "','" + userFirstNameText.getText() + "','" + userSecondNameText.getText() + "','" + userLastNameText.getText() + "','" + userDesignitionCombo.getValue() + "','" + userGenderCombo.getValue() + "','" + passwordText.getText() + "','" + phoneNumberText.getText() + "')";
        executeQuery(query);
        userIdText.clear();
        userFirstNameText.clear();
        userSecondNameText.clear();
        userLastNameText.clear();
        passwordText.clear();
        phoneNumberText.clear();
        userGenderCombo.setValue("");
        userDesignitionCombo.setValue("");
        showUsers();
        Alert userAddedAlert = new Alert(Alert.AlertType.INFORMATION);
        userAddedAlert.setContentText("NEW USER ADDED");
        userAddedAlert.setContentText("" + userFirstNameText.getText() + "Has been Added successfully as" + userDesignitionCombo.getValue());
        userAddedAlert.showAndWait();

        System.out.println(" new user Added");

    }

    /**
     * Method for connecting to the database
     * Executes the query when called
     * @param query
     */
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
     * Method for changing the current scene to the next scene
     * It changes the context
     * Throws exception if the intended context is cannot fit the current window or is not available
     * @param event
     * @throws Exception
     */
    @FXML
    void backToMainWindow(ActionEvent event) throws Exception {
        Parent root= FXMLLoader.load(getClass().getResource("/com/ownres/view/MainWindow.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.resizableProperty().setValue(true);
        scene=new Scene(root,1350,750);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        //Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        //stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        //stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

    }

    /***
     * Method for setting Textfields empty
     * @param event
     */
    @FXML
    void refreshFields(ActionEvent event) {
        userIdText.clear();
        userFirstNameText.clear();
        userLastNameText.clear();
        passwordText.clear();
        userGenderCombo.setValue("");
        userDesignitionCombo.setValue("");
        userSecondNameText.clear();
        phoneNumberText.clear();
        showUsers();
        ;

    }

    /**
     * Method to get users fro users table
     * It returns a list of available users
     * @return
     */
    private ObservableList<User> getUsers() {
        ObservableList<User> usersList = FXCollections.observableArrayList();
        String query = "select * from Table_Users";
        try {
            Connection conn = DatabaseConnection.ConnectDatabase();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            User user;
            while (resultSet.next()) {
                user = new User(resultSet.getInt("National_ID"), resultSet.getString("First_Name"),
                        resultSet.getString("Second_Name"), resultSet.getString("Third_Name"),
                        resultSet.getString("Gender"), resultSet.getString("Designition"),
                        resultSet.getInt("phone_number"), resultSet.getString("Password"));
                usersList.add(user);
            }

        } catch (Exception e) {

        }
        return usersList;
    }

    /**
     * Method for updating the column values in TableView
     */
    private void showUsers() {
        ObservableList<User> showUsers = getUsers();
        userIdCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("userNationalID"));
        userFirstNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("userFirstName"));
        userLastNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("userLastName"));
        userGenderCol.setCellValueFactory(new PropertyValueFactory<User, String>("userGender"));
        userPhoneNumberCol.setCellValueFactory(new PropertyValueFactory<User, String>("userPhoneNumber"));
        userSecondNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("userSecondName"));
        userDesignitionCol.setCellValueFactory(new PropertyValueFactory<User, String>("userDesignation"));
        userPasswordCol.setCellValueFactory(new PropertyValueFactory<User, String>("userPassword"));
        userTableView.setItems(showUsers);
    }

    /**
     * Method for populating the ComboBox
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showUsers();
        ObservableList<String> gender = FXCollections.observableArrayList("Male", "Female");
        userGenderCombo.getItems().addAll(gender);
        userGenderCombo.setValue("Select Gender");
        ObservableList<String> userType = FXCollections.observableArrayList("Admin", "Attache", "Intern", "Guard", "KRA Officer");
        userDesignitionCombo.getItems().addAll(userType);
        userDesignitionCombo.setValue("Select Designation");

    }

    /**
     * Method for performing itemSelect from TableView
     * It displays the selected item on VBox view
     * @param mouseEvent
     */
    public void showUser(MouseEvent mouseEvent) {
        User user = userTableView.getSelectionModel().getSelectedItem();
        userIdText.setText (String.valueOf(user.getUserNationalID()));
        userFirstNameText.setText(user.getUserFirstName());
        userSecondNameText.setText(user.getUserSecondName());
        userLastNameText.setText(user.getUserLastName());
        userGenderCombo.setValue(user.getUserGender());
        userDesignitionCombo.setValue(user.getUserDesignation());
        phoneNumberText.setText(String.valueOf(user.getUserPhoneNumber()));
        passwordText.setText(user.getUserPassword());
    }

    /**
     * Method to udate user from VBox view
     * @param event
     */
    public void updateUser(ActionEvent event) {
        String query = "update Table_Users set National_ID='" + userIdText.getText() + "',First_Name='" + userFirstNameText.getText() + "',Second_Name=" + userSecondNameText.getText() + "',Third_Name='" + userLastNameText.getText() + "',Gender='" + userGenderCombo.getValue() + "',Phone_Number='" + phoneNumberText.getText() + "',Designation='" + userDesignitionCombo.getValue() + "',userPassword='" + passwordText.getText() + "' where National_ID='" + userIdText.getText() + "'";
        executeQuery(query);
        System.out.println("User updated Successfully");
        userIdText.clear();
        userFirstNameText.clear();
        userLastNameText.clear();
        passwordText.clear();
        userGenderCombo.setValue("");
        phoneNumberText.clear();
        userDesignitionCombo.setValue("");
        userSecondNameText.clear();

        showUsers();
        Alert updateAlert = new Alert(Alert.AlertType.INFORMATION);
        updateAlert.setTitle("USER UPDATE");
        updateAlert.setContentText(" The User Has Been Updated Successfully");
        updateAlert.showAndWait();

    }

    /**
     * Method to delete user based on Algorithm Search from existing users
     * @param event
     */

    public void DeleteUser1(ActionEvent event) {
        if (deleteText.getText().isEmpty()) {
            Alert emptyID = new Alert(Alert.AlertType.INFORMATION);
            emptyID.setTitle("USER ID IS EMPTY");
            emptyID.setContentText("Enter User National ID to Delete");
            emptyID.showAndWait();
            return;
        }

        String query = "select count(1) from Table_Users where National_ID='" + deleteText.getText() + "'";
        try {
            conn = DatabaseConnection.ConnectDatabase();
            assert conn != null;
            stm = conn.createStatement();
            rs = stm.executeQuery(query);
            while (rs.next()) {
                if (rs.getInt(1) == 1) {
                    Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    deleteAlert.setTitle("DELETE USER");
                    deleteAlert.setContentText("Do you Really want to delete user " + deleteText.getText());
                    if (deleteAlert.showAndWait().get() == ButtonType.OK) {
                        String deleteQuery = "delete from Table_Users where National_ID='" + deleteText.getText() + "'";
                        executeQuery(deleteQuery);
                        deleteText.clear();
                        System.out.println("User deleted Successfully");
                        Alert successDelete = new Alert(Alert.AlertType.INFORMATION);
                        successDelete.setTitle("DELETE SUCCESS");
                        successDelete.setContentText("User Has Been Deleted Successfully");
                        successDelete.showAndWait();
                        showUsers();
                    }

                } else {
                    Alert invalidUserID = new Alert(Alert.AlertType.INFORMATION);
                    invalidUserID.setTitle("INVALID USER ID");
                    invalidUserID.setHeaderText("User ID invalid or not Found");
                    invalidUserID.setContentText("Enter a valid National ID to delete User");
                    invalidUserID.showAndWait();
                    userIdText.clear();
                    userIdText.requestFocus();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Method to delete user from input VBox
     * @param event
     */

    public void deleteUser(ActionEvent event) {
        String query = "delete from Table_Users where National_ID='" + userIdText.getText() + "'";
        executeQuery(query);
        showUsers();
        userIdText.clear();
        userFirstNameText.clear();
        userLastNameText.clear();
        passwordText.clear();
        userGenderCombo.setValue("");
        userDesignitionCombo.setValue("");
        userSecondNameText.clear();
        phoneNumberText.clear();
        System.out.println("User deleted Successfully");

    }
}