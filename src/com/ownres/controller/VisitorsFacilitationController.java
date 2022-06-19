package com.ownres.controller;

import com.ownres.model.DatabaseConnection;
import com.ownres.model.Visitor;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static java.time.LocalTime.now;

public class VisitorsFacilitationController implements Initializable{


        @FXML
        private GridPane visitorInformationGridPane;
        @FXML
        private AnchorPane visitorAnchorPane;

        @FXML
        private Button buttonRefresh;
        @FXML
        private Button deleteButton;
        @FXML
        private Button buttonDeafault;
        @FXML
        private Button exportButton;
        @FXML
        private Button buttonBack;
        @FXML
        private Button buttonPopulate;
        @FXML
        private Button retrieveButton;
        @FXML
        private Button buttonTimedIn;
        @FXML
        private Button buttonTimedOut;
        @FXML
        private Button timeOutVisitorButton;
        @FXML
        private Button timeInVisitorButton;


        @FXML
        private HBox topLeftVisitorHbox;
        @FXML
        private HBox topVisitorHbox;
        @FXML
        private VBox leftVisitorVBox;

        @FXML
        public DatePicker datePicker;
        @FXML
        public DatePicker toDatePicker;
        public static DatePicker tooDatePicker = new DatePicker();


        @FXML
        private Label visitorFirstNameLabel;
        @FXML
        private Label visitorGenderLabel;
        @FXML
        private Label visitorIdLabel;
        @FXML
        private Label visitorLastNameLabel;
        @FXML
        private Label visitorSecondNameLabel;
        @FXML
        private Label roomVisitLabel;
        @FXML
        private Label warningLabel;

        @FXML
        private TextField visitorLastNameText;
        @FXML
        private TextField visitorIdText;
        @FXML
        private TextField visitorFirstNameText;
        @FXML
        private TextField visitorSecondNameText;
        @FXML
        private TextField visitorPhoneNumberText;
        @FXML
        private TextField searchFNameText;
        @FXML
        private TextField searchSNameText;
        @FXML
        private TextField roomVisitText;
        @FXML
        private TextField searchIDText;
        @FXML
        private TextField filterTextField;

        private TextField deleteText;

        @FXML
        private TableView<Visitor> visitorTableView;
        @FXML
        private TableColumn<Visitor, String> visitorRoomVisitCol;
        @FXML
        private TableColumn<Visitor, String> visitorSecondNameCol;
        @FXML
        private TableColumn<Visitor, Integer> visitorPhoneNumberCol;
        @FXML
        private TableColumn<Visitor, String> visitorTimeInCol;
        @FXML
        private TableColumn<Visitor, String> visitorTimeOutCol;
        @FXML
        private TableColumn<Visitor, String> visitorThirdNameCol;
        @FXML
        private TableColumn<Visitor, String> visitorDateCol;
        @FXML
        private TableColumn<Visitor, Integer> visitorIdCol;
        @FXML
        private TableColumn<Visitor, String> visitorGenderCol;
        @FXML
        private TableColumn<Visitor, String> visitorFirstNameCol;

        @FXML
        private ComboBox<String> visitorGenderCombo = new ComboBox<String>();

        Stage stage;
        Parent root;
        Scene scene;
        Connection conn;
        Statement stm;
        ResultSet rs;


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter ttf = DateTimeFormatter.ofPattern("HH:mm:ss");

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

    /**
     * Method for implementing initializable
     * It is the main method of the controller.class
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showVisitors();
        ObservableList<String> gender = FXCollections.observableArrayList("Male", "Female");
        visitorGenderCombo.getItems().addAll(gender);
        visitorGenderCombo.setValue("Select Gender");
        //this.stage.addEventHandler(KeyEvent.KEY_PRESSED, scene.getKeyHandler() );

        //datePicker = new DatePicker();
        //datePicker.setValue(java.time.LocalDate.now());
        final EventType<KeyEvent> KEY_PRESSED;
        //if (KEY_PRESSED == KeyCode.V)
        Callback<DatePicker,DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell();
            }
        };
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

    public boolean checkVisitor(String id){
        String querySelect="select count(1) from visitor_exist where visitor_id= ? ";
        executeQuery(querySelect);
        try {
            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    return true;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public EventHandler<KeyEvent> getKeyHandler(){
        return new EventHandler<KeyEvent>(){
                    @Override
                    public void handle(KeyEvent event){
                        if(event.getCode() == KeyCode.V){
                            toDatePicker.setVisible(true);
                        }
                        if(event.getCode() == KeyCode.I){
                            toDatePicker.setVisible(false);
                        }


                    }

                };
    }

    public boolean isVisitorExist(String id) {

        boolean exist = false;
        String querySelect = "select count(1) from visitor_exist where visitor_id= ? ";

        try {
            Connection conn = DatabaseConnection.ConnectDatabase();
            assert conn != null;
            PreparedStatement statement = conn.prepareStatement(querySelect);
            if (statement != null) {
                statement.setString(1, id);

                try {
                    ResultSet results = statement.executeQuery();
                    if (results != null) {
                        try {
                            if (results.next()) {
                                exist = true;

                            }
                        } catch (Exception resultSetException) {
                            resultSetException.printStackTrace();
                        }
                        results.close();
                    }
                } catch (Exception statmentExcption) {
                    statmentExcption.printStackTrace();
                }
                statement.close();
            }
        } catch (Exception generalException) {
            generalException.printStackTrace();
        }
        return exist;
    }


    /**
         * Onclick Method for Insert
         * @param event
         */
        @FXML
        void timeInVisitor(ActionEvent event) {
            if(visitorIdText.getText().isEmpty() || visitorFirstNameText.getText().isEmpty() || visitorSecondNameText.getText().isEmpty() || roomVisitText.getText().isEmpty()){
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        warningLabel.setText("             ");
                                        warningLabel.setStyle("-fx-background-color: transparent");
                                    }
                                });

                            }
                        },
                        3000
                );

                warningLabel.setStyle("-fx-background-color: #eb4848; -fx-background-radius: 5px; -fx-border-radius: 5px;");
                warningLabel.setText("  Empty Taxpayer Information. Please input details!  ");
                visitorIdText.requestFocus();
            }

            else {
                String query = "insert into Visitor values(' 0 ','" + java.time.LocalDate.now() + "','" + visitorFirstNameText.getText().toUpperCase() + "','" + visitorSecondNameText.getText().toUpperCase() + "','" + visitorLastNameText.getText().toUpperCase() + "','" + visitorIdText.getText() + "','" + visitorGenderCombo.getValue().toUpperCase() + "','" + visitorPhoneNumberText.getText() + "','" + roomVisitText.getText() + "','" + now() + "','')";
                executeQuery(query);
                showVisitors();
                conn = DatabaseConnection.ConnectDatabase();
                if (isVisitorExist(visitorIdText.getText())){
                    try {
                        String queryInsert = "insert ignore into visitor_exist values('" + visitorIdText.getText().toUpperCase() + "','"
                                + visitorFirstNameText.getText().toUpperCase() + "','" + visitorSecondNameText.getText().toUpperCase() + "','"
                                + visitorLastNameText.getText().toUpperCase() + "','" + visitorGenderCombo.getValue().toUpperCase() + "','"
                                + visitorPhoneNumberText.getText().toUpperCase() + "')";
                        executeQuery(queryInsert);
                    }
                    catch (NumberFormatException exception) {
                        exception.printStackTrace();
                    }

                }
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        warningLabel.setText("             ");
                                        warningLabel.setStyle("-fx-background-color: transparent");
                                    }
                                });

                            }
                        },
                        3000
                );

                warningLabel.setStyle("-fx-background-color: #6ff26f; -fx-background-radius: 5px; -fx-border-radius: 5px;");
                warningLabel.setText("  " + visitorFirstNameText.getText().toUpperCase() + "'S visitation captured successfully.  ");
                refreshFields();

                System.out.println(" new visitor Added");

                /**
                 Alert userAddedAlert = new Alert(Alert.AlertType.INFORMATION);
                 userAddedAlert.setContentText("VISITOR ADDED");
                 userAddedAlert.setContentText("" + visitorFirstNameText.getText().toUpperCase() + " has been Added successfully.");
                 userAddedAlert.showAndWait();
                 **/
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
        void actionBackToMainWindow(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ownres/view/MainWindow.fxml"));
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


        public  void refreshFields() {
            visitorIdText.clear();
            visitorFirstNameText.clear();
            visitorSecondNameText.clear();
            visitorLastNameText.clear();
            visitorPhoneNumberText.clear();
            visitorGenderCombo.setValue("Select Gender");
            roomVisitText.clear();
            showVisitors();
        }

    /***
     * Method for setting Textfields empty
     * @param event
     */
        @FXML
        void refreshFields(ActionEvent event){
                visitorIdText.clear();
                visitorFirstNameText.clear();
                visitorSecondNameText.clear();
                visitorLastNameText.clear();
                visitorPhoneNumberText.clear();
                visitorGenderCombo.setValue("Select Gender");
                roomVisitText.clear();
                showVisitors();
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    warningLabel.setText("             ");
                                    warningLabel.setStyle("-fx-background-color: transparent;");
                                }
                            });

                        }
                    },
                    3000
            );

            warningLabel.setText("  Fields Refreshed Successfully!  ");
            warningLabel.setStyle("-fx-background-color: #6ff26f; -fx-background-radius: 5px; -fx-border-radius: 5px;");

            System.out.println(" Fields Refreshed successfully");


    }

        /**
         * Method to get visitors from visitors table
         * It returns a list of available visitors on the current day
         * @return
         */

    private ObservableList<Visitor> getVisitors() {
        ObservableList<Visitor> visitorsList = FXCollections.observableArrayList();
        String query = "select * from visitor where date='" + java.time.LocalDate.now() + "'";
        try {
            Connection conn = DatabaseConnection.ConnectDatabase();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Visitor visitor;
            while (resultSet.next()) {
                visitor = new Visitor(resultSet.getString("date"), resultSet.getString("firstname"),
                        resultSet.getString("secondname"), resultSet.getString("thirdname"),
                        resultSet.getInt("idno"), resultSet.getString("gender"),
                        resultSet.getInt("phoneno"), resultSet.getInt("roomvisit"),
                        resultSet.getString("timein"), resultSet.getString("timeout"));
                visitorsList.add(visitor);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return visitorsList;
    }

    /**
     * Method to get Timedout visitors.LocalDate.now() automatically
     * Else datepicker value is used
     * @return
     */
    private ObservableList<Visitor> getTimedOutVisitors() {
        ObservableList<Visitor> visitorsList = FXCollections.observableArrayList();
        String dateCheck = null;
        if (datePicker.getValue() != null) {
            dateCheck = String.valueOf(datePicker.getValue());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String query = "select * from visitor where date='" + dateCheck + "' and timeout!='00:00:00'";
            try {
                Connection conn = DatabaseConnection.ConnectDatabase();
                assert conn != null;
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                Visitor visitor;
                while (resultSet.next()) {
                    visitor = new Visitor(resultSet.getString("date"), resultSet.getString("firstname"),
                            resultSet.getString("secondname"), resultSet.getString("thirdname"),
                            resultSet.getInt("idno"), resultSet.getString("gender"),
                            resultSet.getInt("phoneno"), resultSet.getInt("roomvisit"),
                            resultSet.getString("timein"), resultSet.getString("timeout"));
                    visitorsList.add(visitor);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return visitorsList;
        }
        else{
            String query = "select * from visitor where date='" + java.time.LocalDate.now() + "' and timeout!='00:00:00'";
            try {
                Connection conn = DatabaseConnection.ConnectDatabase();
                assert conn != null;
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                Visitor visitor;
                while (resultSet.next()) {
                    visitor = new Visitor(resultSet.getString("date"), resultSet.getString("firstname"),
                            resultSet.getString("secondname"), resultSet.getString("thirdname"),
                            resultSet.getInt("idno"), resultSet.getString("gender"),
                            resultSet.getInt("phoneno"), resultSet.getInt("roomvisit"),
                            resultSet.getString("timein"), resultSet.getString("timeout"));
                    visitorsList.add(visitor);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return visitorsList;
        }
    }
    /**
     * Method to get untimedout visitors
     * It considers date parameters
     * If date picker is empty, java.time.LocalDate.now() is used automatically
     * else date picker value is used
     * @return visitorslist
     */
    private ObservableList<Visitor> getUntimedOutVisitors() {
        ObservableList<Visitor> visitorsList = FXCollections.observableArrayList();
        //datePicker.setValue(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        if (datePicker.getValue() == null)
        {
            LocalDate dates = java.time.LocalDate.now();
            String query = "select * from visitor where date='" + dates + "' and timeout='00:00:00'";
            try {
                Connection conn = DatabaseConnection.ConnectDatabase();
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                Visitor visitor;
                while (resultSet.next()) {
                    visitor = new Visitor(resultSet.getString("date"), resultSet.getString("firstname"),
                            resultSet.getString("secondname"), resultSet.getString("thirdname"),
                            resultSet.getInt("idno"), resultSet.getString("gender"),
                            resultSet.getInt("phoneno"), resultSet.getInt("roomvisit"),
                            resultSet.getString("timein"), resultSet.getString("timeout"));
                    visitorsList.add(visitor);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return visitorsList;
        }

        else {
            LocalDate dateunt = datePicker.getValue();
            String query = "select * from visitor where date='" + dateunt + "' and timeout='00:00:00'";
            try {
                Connection conn = DatabaseConnection.ConnectDatabase();
                assert conn != null;
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                Visitor visitor;
                while (resultSet.next()) {
                    visitor = new Visitor(resultSet.getString("date"), resultSet.getString("firstname"),
                            resultSet.getString("secondname"), resultSet.getString("thirdname"),
                            resultSet.getInt("idno"), resultSet.getString("gender"),
                            resultSet.getInt("phoneno"), resultSet.getInt("roomvisit"),
                            resultSet.getString("timein"), resultSet.getString("timeout"));
                    visitorsList.add(visitor);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return visitorsList;
        }
    }

    /**
     * Method for retrieving visitors with respect to search fields
     * Returns a visitors list based on the filled search fields
     * @return
     */
    private ObservableList<Visitor> getRetrieveVisitors() {
        ObservableList<Visitor> visitorsList = FXCollections.observableArrayList();
        if (datePicker.getValue() == null)
        {
            if (searchIDText.getText().isEmpty() && searchFNameText.getText().isEmpty() && searchSNameText.getText().isEmpty()){
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        warningLabel.setText("             ");
                                        warningLabel.setStyle("-fx-background-color: transparent");
                                    }
                                });

                            }
                        },
                        3000
                );

                warningLabel.setStyle("-fx-background-color: #eb4848; -fx-background-radius: 5px; -fx-border-radius: 5px;");
                warningLabel.setText("  Empty search fields. Input at least one search field to Retrieve!  ");
                searchIDText.requestFocus();
            }
            else if (searchFNameText.getText().isEmpty() && searchSNameText.getText().isEmpty()){
                LocalDate dates = java.time.LocalDate.now();
                String query = "select * from visitor where date='" + dates + "' and idno='" + searchIDText.getText().toUpperCase() + "'";
                try {
                    Connection conn = DatabaseConnection.ConnectDatabase();
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    Visitor visitor;
                    while (resultSet.next()) {
                        visitor = new Visitor(resultSet.getString("date"), resultSet.getString("firstname"),
                                resultSet.getString("secondname"), resultSet.getString("thirdname"),
                                resultSet.getInt("idno"), resultSet.getString("gender"),
                                resultSet.getInt("phoneno"), resultSet.getInt("roomvisit"),
                                resultSet.getString("timein"), resultSet.getString("timeout"));
                        visitorsList.add(visitor);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (searchSNameText.getText().isEmpty()){
                LocalDate dates = java.time.LocalDate.now();
                String query = "select * from visitor where date='" + dates + "' and idno='"+ searchIDText.getText().toUpperCase()+ "' and firstname='" + searchFNameText.getText().toUpperCase() + "'";
                try {
                    Connection conn = DatabaseConnection.ConnectDatabase();
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    Visitor visitor;
                    while (resultSet.next()) {
                        visitor = new Visitor(resultSet.getString("date"), resultSet.getString("firstname"),
                                resultSet.getString("secondname"), resultSet.getString("thirdname"),
                                resultSet.getInt("idno"), resultSet.getString("gender"),
                                resultSet.getInt("phoneno"), resultSet.getInt("roomvisit"),
                                resultSet.getString("timein"), resultSet.getString("timeout"));
                        visitorsList.add(visitor);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (searchFNameText.getText().isEmpty()){
                LocalDate dates = java.time.LocalDate.now();
                String query = "select * from visitor where date='" + dates + "' and idno='"+ searchIDText.getText().toUpperCase() +"' and secondname='"+ searchSNameText.getText().toUpperCase() +"'";
                try {
                    Connection conn = DatabaseConnection.ConnectDatabase();
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    Visitor visitor;
                    while (resultSet.next()) {
                        visitor = new Visitor(resultSet.getString("date"), resultSet.getString("firstname"),
                                resultSet.getString("secondname"), resultSet.getString("thirdname"),
                                resultSet.getInt("idno"), resultSet.getString("gender"),
                                resultSet.getInt("phoneno"), resultSet.getInt("roomvisit"),
                                resultSet.getString("timein"), resultSet.getString("timeout"));
                        visitorsList.add(visitor);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (searchIDText.getText().isEmpty()){
                LocalDate dates = java.time.LocalDate.now();
                String query = "select * from visitor where date='" + dates + "' and firstname='"+ searchFNameText.getText().toUpperCase() +"' and secondname='"+ searchSNameText.getText().toUpperCase() +"'";
                try {
                    Connection conn = DatabaseConnection.ConnectDatabase();
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    Visitor visitor;
                    while (resultSet.next()) {
                        visitor = new Visitor(resultSet.getString("date"), resultSet.getString("firstname"),
                                resultSet.getString("secondname"), resultSet.getString("thirdname"),
                                resultSet.getInt("idno"), resultSet.getString("gender"),
                                resultSet.getInt("phoneno"), resultSet.getInt("roomvisit"),
                                resultSet.getString("timein"), resultSet.getString("timeout"));
                        visitorsList.add(visitor);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return visitorsList;
        }

        else {
            if (searchIDText.getText().isEmpty() && searchFNameText.getText().isEmpty() && searchSNameText.getText().isEmpty() && toDatePicker.getValue() != null) {
                //AtomicReference<String> froLockDate = new AtomicReference<>(new String());
                String toDate = String.valueOf(datePicker.getValue());
                String froLockDate = String.valueOf(toDatePicker.getValue());
                //Display a label
                String query = "select * from visitor where DATE(date) BETWEEN'" + toDate + "' AND '" + froLockDate + "'";
                try {
                    Connection conn = DatabaseConnection.ConnectDatabase();
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    Visitor visitor;
                    while (resultSet.next()) {
                        visitor = new Visitor(resultSet.getString("date"), resultSet.getString("firstname"),
                                resultSet.getString("secondname"), resultSet.getString("thirdname"),
                                resultSet.getInt("idno"), resultSet.getString("gender"),
                                resultSet.getInt("phoneno"), resultSet.getInt("roomvisit"),
                                resultSet.getString("timein"), resultSet.getString("timeout"));
                        visitorsList.add(visitor);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Long data processed");
                froLockDate = null;
                toDatePicker.setValue(null);
                toDatePicker.getEditor().setText(null);
            }
            else if (searchIDText.getText().isEmpty() && searchFNameText.getText().isEmpty() && searchSNameText.getText().isEmpty() && toDatePicker.getValue() == null) {
                LocalDate dates = datePicker.getValue();
                String query = "select * from visitor where date='" + dates + "'";
                try {
                    Connection conn = DatabaseConnection.ConnectDatabase();
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    Visitor visitor;
                    while (resultSet.next()) {
                        visitor = new Visitor(resultSet.getString("date"), resultSet.getString("firstname"),
                                resultSet.getString("secondname"), resultSet.getString("thirdname"),
                                resultSet.getInt("idno"), resultSet.getString("gender"),
                                resultSet.getInt("phoneno"), resultSet.getInt("roomvisit"),
                                resultSet.getString("timein"), resultSet.getString("timeout"));
                        visitorsList.add(visitor);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (searchFNameText.getText().isEmpty() && searchSNameText.getText().isEmpty()) {
                LocalDate dates = datePicker.getValue();
                String query = "select * from visitor where date='" + dates + "' and idno='" + searchIDText.getText().toUpperCase() + "'";
                try {
                    Connection conn = DatabaseConnection.ConnectDatabase();
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    Visitor visitor;
                    while (resultSet.next()) {
                        visitor = new Visitor(resultSet.getString("date"), resultSet.getString("firstname"),
                                resultSet.getString("secondname"), resultSet.getString("thirdname"),
                                resultSet.getInt("idno"), resultSet.getString("gender"),
                                resultSet.getInt("phoneno"), resultSet.getInt("roomvisit"),
                                resultSet.getString("timein"), resultSet.getString("timeout"));
                        visitorsList.add(visitor);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (searchSNameText.getText().isEmpty()) {
                LocalDate dates = datePicker.getValue();
                String query = "select * from visitor where date='" + dates + "' and idno='" + searchIDText.getText().toUpperCase() + "' and firstname='" + searchFNameText.getText().toUpperCase() + "'";
                try {
                    Connection conn = DatabaseConnection.ConnectDatabase();
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    Visitor visitor;
                    while (resultSet.next()) {
                        visitor = new Visitor(resultSet.getString("date"), resultSet.getString("firstname"),
                                resultSet.getString("secondname"), resultSet.getString("thirdname"),
                                resultSet.getInt("idno"), resultSet.getString("gender"),
                                resultSet.getInt("phoneno"), resultSet.getInt("roomvisit"),
                                resultSet.getString("timein"), resultSet.getString("timeout"));
                        visitorsList.add(visitor);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (searchFNameText.getText().isEmpty()) {
                LocalDate dates = datePicker.getValue();
                String query = "select * from visitor where date='" + dates + "' and idno='" + searchIDText.getText().toUpperCase() + "' and secondname='" + searchSNameText.getText().toUpperCase() + "'";
                try {
                    Connection conn = DatabaseConnection.ConnectDatabase();
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    Visitor visitor;
                    while (resultSet.next()) {
                        visitor = new Visitor(resultSet.getString("date"), resultSet.getString("firstname"),
                                resultSet.getString("secondname"), resultSet.getString("thirdname"),
                                resultSet.getInt("idno"), resultSet.getString("gender"),
                                resultSet.getInt("phoneno"), resultSet.getInt("roomvisit"),
                                resultSet.getString("timein"), resultSet.getString("timeout"));
                        visitorsList.add(visitor);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (searchIDText.getText().isEmpty()) {
                LocalDate dates = datePicker.getValue();
                String query = "select * from visitor where date='" + dates + "' and firstname='" + searchFNameText.getText().toUpperCase() + "' and secondname='" + searchSNameText.getText().toUpperCase() + "'";
                try {
                    Connection conn = DatabaseConnection.ConnectDatabase();
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    Visitor visitor;
                    while (resultSet.next()) {
                        visitor = new Visitor(resultSet.getString("date"), resultSet.getString("firstname"),
                                resultSet.getString("secondname"), resultSet.getString("thirdname"),
                                resultSet.getInt("idno"), resultSet.getString("gender"),
                                resultSet.getInt("phoneno"), resultSet.getInt("roomvisit"),
                                resultSet.getString("timein"), resultSet.getString("timeout"));
                        visitorsList.add(visitor);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return visitorsList;
        }
    }


    /**
         * Method for updating the column values in TableView
         */
    ObservableList<Visitor> showVisitors;
        private void showVisitors() {
        //ObservableList<Visitor>
            showVisitors = getVisitors();
        visitorIdCol.setCellValueFactory(new PropertyValueFactory<Visitor, Integer>("idNumber"));
        visitorFirstNameCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("firstName"));
        visitorThirdNameCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("thirdName"));
        visitorGenderCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("gender"));
        visitorPhoneNumberCol.setCellValueFactory(new PropertyValueFactory<Visitor, Integer>("phoneNumber"));
        visitorSecondNameCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("secondName"));
        visitorDateCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("date"));
        visitorRoomVisitCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("roomVisit"));
        visitorTimeInCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("timeIn"));
        visitorTimeOutCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("timeOut"));
        visitorTableView.setItems(showVisitors);
    }


    /**
     * Method to handle actionRetrieve from database
     * Displays visitors list based on search criteria
     * If Id search field is empty, First name and Second name criteria is used
     * If Id and First name search fields are empty, Second name criteria is used
     * If Id and Second name search fields are empty, First name criteria is used
     * If First and Second name search fields are empty, Id search criteria is used
     * Date is set by default to today if Datepicker value is empty
     * @param event
     */
    @FXML
    private void actionRetrieve(ActionEvent event){
        //ObservableList<Visitor>
        showVisitors = getRetrieveVisitors();
        visitorIdCol.setCellValueFactory(new PropertyValueFactory<Visitor, Integer>("idNumber"));
        visitorFirstNameCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("firstName"));
        visitorThirdNameCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("thirdName"));
        visitorGenderCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("gender"));
        visitorPhoneNumberCol.setCellValueFactory(new PropertyValueFactory<Visitor, Integer>("phoneNumber"));
        visitorSecondNameCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("secondName"));
        visitorDateCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("date"));
        visitorRoomVisitCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("roomVisit"));
        visitorTimeInCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("timeIn"));
        visitorTimeOutCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("timeOut"));
        visitorTableView.setItems(showVisitors);
    }
    @FXML
    private void populateVisitor(ActionEvent event) {
        if (visitorIdText.getText().isEmpty()) {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    warningLabel.setText("             ");
                                    warningLabel.setStyle("-fx-background-color: transparent;");
                                }
                            });

                        }
                    },
                    3000
            );

            warningLabel.setText("  ID field empty. Please input ID number to populate other fields!  ");
            warningLabel.setStyle("-fx-background-color: #eb4848; -fx-background-radius: 5px; -fx-border-radius: 5px;");
            visitorIdText.requestFocus();
        }
        else if (!isVisitorExist(visitorIdText.getText())) {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    warningLabel.setText("             ");
                                    warningLabel.setStyle("-fx-background-color: transparent;");
                                }
                            });

                        }
                    },
                    3000
            );

            warningLabel.setText("  The taxpayer " + visitorIdText.getText().toUpperCase() + " does not Exist. Please capture their Details!  ");
            warningLabel.setStyle("-fx-background-color: #eb4848; -fx-background-radius: 5px; -fx-border-radius: 5px;");
            visitorIdText.requestFocus();
        }
        else {
            String querySel = "select visitor_id,first_name,second_name,third_name,gender,phone_no from visitor_exist where visitor_id='" + visitorIdText.getText() + "'";
            try {
                Connection conn = DatabaseConnection.ConnectDatabase();
                assert conn != null;
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(querySel);
                while (resultSet.next()) {

                    visitorIdText.setText(resultSet.getString("visitor_id"));
                    visitorFirstNameText.setText(resultSet.getString("first_name"));
                    visitorSecondNameText.setText(resultSet.getString("second_name"));
                    visitorLastNameText.setText(resultSet.getString("third_name"));
                    visitorGenderCombo.setValue(resultSet.getString("gender"));
                    visitorPhoneNumberText.setText(resultSet.getString("phone_no"));
                }

            } catch (Exception pride) {
                pride.printStackTrace();
            }
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    warningLabel.setText("             ");
                                    warningLabel.setStyle("-fx-background-color: transparent;");
                                }
                            });

                        }
                    },
                    3000
            );

            warningLabel.setText("  Taxpayer " + visitorFirstNameText.getText().toUpperCase() + " populated successfully.  ");
            warningLabel.setStyle("-fx-background-color: #6ff26f; -fx-background-radius: 5px; -fx-border-radius: 5px;");

            System.out.println(" Taxpayer populated successfully");
        }
    }

    /**
     * Method for handling action event defaultview
     * Displays the current/today facilitated visitors
     * @param event
     */
    @FXML
    private void deafaultView(ActionEvent event){
        //ObservableList<Visitor>
        showVisitors = getVisitors();
        visitorIdCol.setCellValueFactory(new PropertyValueFactory<Visitor, Integer>("idNumber"));
        visitorFirstNameCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("firstName"));
        visitorThirdNameCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("thirdName"));
        visitorGenderCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("gender"));
        visitorPhoneNumberCol.setCellValueFactory(new PropertyValueFactory<Visitor, Integer>("phoneNumber"));
        visitorSecondNameCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("secondName"));
        visitorDateCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("date"));
        visitorRoomVisitCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("roomVisit"));
        visitorTimeInCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("timeIn"));
        visitorTimeOutCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("timeOut"));
        visitorTableView.setItems(showVisitors);
    }

    /**
     * Method to handle timedOutView action event.
     * Displays all list of visitors that have been timed out as per the date selected
     * on the datepicker.
     * @param event
     */
    @FXML
    private void timedOutView(ActionEvent event){
            //ObservableList<Visitor>
            showVisitors = getUntimedOutVisitors();
            visitorIdCol.setCellValueFactory(new PropertyValueFactory<Visitor, Integer>("idNumber"));
            visitorFirstNameCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("firstName"));
            visitorThirdNameCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("thirdName"));
            visitorGenderCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("gender"));
            visitorPhoneNumberCol.setCellValueFactory(new PropertyValueFactory<Visitor, Integer>("phoneNumber"));
            visitorSecondNameCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("secondName"));
            visitorDateCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("date"));
            visitorRoomVisitCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("roomVisit"));
            visitorTimeInCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("timeIn"));
            visitorTimeOutCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("timeOut"));
            visitorTableView.setItems(showVisitors);
    }

    /**
     * Method to handle Export action event
     * Exports the data on the table view as an excel document format
     * @param event
     */
    @FXML
    public void actionExport(ActionEvent event) throws Exception {
        String userType = getUserType();
        if (!userType.equals("Admin")){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("UNAUTHORISED OPERATION!");
            alert.setHeaderText("You are not an Admin!");
            alert.setContentText("Contact your Administrator for Assistance.");
            alert.setGraphic(new ImageView(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png").toString()));
            alert.showAndWait();
        }
        else {
            //HSSFWorkBook hssfWorkBook = new HSSFWorkBook();
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
            XSSFSheet xssfSheet = xssfWorkbook.createSheet("sheet 1");
            XSSFRow firstRow = xssfSheet.createRow(0);
            //set title of columns
            for (int i = 0; i < visitorTableView.getColumns().size(); i++) {
                firstRow.createCell(i).setCellValue(visitorTableView.getColumns().get(i).getText());
            }
            for (int row = 0; row < visitorTableView.getItems().size(); row++) {
                XSSFRow xssfRow = xssfSheet.createRow(row + 1);
                for (int col = 0; col < visitorTableView.getColumns().size(); col++) {
                    Object object = visitorTableView.getColumns().get(col).getCellObservableValue(row).getValue();
                    try {
                        if (object != null && Double.parseDouble(object.toString()) != 0.0) {
                            xssfRow.createCell(col).setCellValue(Double.parseDouble(object.toString()));
                        }
                    } catch (NumberFormatException e) {
                        xssfRow.createCell(col).setCellValue(object.toString());
                    }
                }
            }
            //save excel file
            try {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HHmmss ddMMyyyy");
                String fileName = (dtf.format(LocalDateTime.now()) + " Data.xlsx");
                File excel = new File("C:" + File.separator + "Users" + File.separator + "panta" + File.separator + "Downloads" + File.separator + fileName);
                //xssfWorkbook.write(new FileOutputStream("C:\\\\Users\\\\panta\\\\Downloads\\\\" + fileName));
                xssfWorkbook.write(new FileOutputStream(excel));
                xssfWorkbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    warningLabel.setText("             ");
                                    warningLabel.setStyle("-fx-background-color: transparent;");
                                }
                            });

                        }
                    },
                    3000
            );

            warningLabel.setText("  Data Exported to Downloads folder Successfully.  ");
            warningLabel.setStyle("-fx-background-color: #6ff26f; -fx-background-radius: 5px; -fx-border-radius: 5px;");

            System.out.println(" Data exported successfully. ");
        }

    }

    /**
     * Method to handle timedIn action event
     * Displays on the table view all visitors that have been timed out of the building
     * @param event
     */
    @FXML
    private void timedInView(ActionEvent event){
        //ObservableList<Visitor>
                showVisitors = getTimedOutVisitors();
        visitorIdCol.setCellValueFactory(new PropertyValueFactory<Visitor, Integer>("idNumber"));
        visitorFirstNameCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("firstName"));
        visitorThirdNameCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("thirdName"));
        visitorGenderCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("gender"));
        visitorPhoneNumberCol.setCellValueFactory(new PropertyValueFactory<Visitor, Integer>("phoneNumber"));
        visitorSecondNameCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("secondName"));
        visitorDateCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("date"));
        visitorRoomVisitCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("roomVisit"));
        visitorTimeInCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("timeIn"));
        visitorTimeOutCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("timeOut"));
        visitorTableView.setItems(showVisitors);
    }

    /**
     * Method to delete data from database
     * @param event
     */
    //String my = LoginController.logType;
    @FXML
    private void deleteVisitor(ActionEvent event) throws Exception {
        String userType = getUserType();
        if (!userType.equals("Admin")){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("UNAUTHORISED OPERATION!");
            alert.setHeaderText("You are not an Admin!");
            alert.setContentText("Contact your Administrator for Assistance.");
            alert.setGraphic(new ImageView(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png").toString()));
            alert.showAndWait();
        }
        else {
            if (datePicker.getValue() == null) {
                if (searchIDText.getText().isEmpty()) {
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            warningLabel.setText("             ");
                                            warningLabel.setStyle("-fx-background-color: transparent;");
                                        }
                                    });

                                }
                            },
                            3000
                    );

                    warningLabel.setText("   ID search field is empty!   ");
                    warningLabel.setStyle("-fx-background-color: #eb4848; -fx-background-radius: 5px; -fx-border-radius: 5px;");
                    searchIDText.requestFocus();
                } else {
                    String query = "select count(1) from visitor where idno='" + searchIDText.getText() + "'";
                    try {
                        conn = DatabaseConnection.ConnectDatabase();
                        assert conn != null;
                        stm = conn.createStatement();
                        rs = stm.executeQuery(query);
                        while (rs.next()) {
                            if (rs.getInt(1) >= 1) {
                                String deleteQuery = "delete from visitor where idno='" + searchIDText.getText() + "'";
                                executeQuery(deleteQuery);
                                showVisitors();
                                new java.util.Timer().schedule(
                                        new java.util.TimerTask() {
                                            @Override
                                            public void run() {
                                                Platform.runLater(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        warningLabel.setText("             ");
                                                        warningLabel.setStyle("-fx-background-color: transparent;");
                                                    }
                                                });

                                            }
                                        },
                                        3000
                                );

                                warningLabel.setText("  Taxpayer " + searchIDText.getText() + " deleted.  ");
                                warningLabel.setStyle("-fx-background-color: #6ff26f; -fx-background-radius: 5px; -fx-border-radius: 5px;");


                            } else {
                                new java.util.Timer().schedule(
                                        new java.util.TimerTask() {
                                            @Override
                                            public void run() {
                                                Platform.runLater(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        warningLabel.setText("             ");
                                                        warningLabel.setStyle("-fx-background-color: transparent;");
                                                    }
                                                });

                                            }
                                        },
                                        3000
                                );

                                warningLabel.setText("  Taxpayer " + searchIDText.getText() + " does not exist!  ");
                                warningLabel.setStyle("-fx-background-color: #eb4848; -fx-background-radius: 5px; -fx-border-radius: 5px;");
                                visitorIdText.clear();
                                visitorIdText.requestFocus();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (toDatePicker.getValue() != null) {
                    LocalDate date = datePicker.getValue();
                    String datee = String.valueOf(toDatePicker.getValue());
                    String delete = "delete * from visitor where DATE(date) BETWEEN'" + date + "' AND '" + datee + "'";
                    try {
                        conn = DatabaseConnection.ConnectDatabase();
                        assert conn != null;
                        stm = conn.createStatement();
                        rs = stm.executeQuery(delete);
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                warningLabel.setText("             ");
                                                warningLabel.setStyle("-fx-background-color: transparent;");
                                            }
                                        });

                                    }
                                },
                                3000
                        );

                        warningLabel.setText("  Data from date " + date + " to " + datee + " has been deleted successfully.  ");
                        warningLabel.setStyle("-fx-background-color: #6ff26f; -fx-background-radius: 5px; -fx-border-radius: 5px;");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    datee = null;
                    toDatePicker.getEditor().setText(null);
                    toDatePicker.setValue(null);
                }
            }
        }

    }

        /**
         * Method for performing itemSelect from TableView
         * It displays the selected item on VBox view
         * @param mouseEvent
         */
        Label label = new Label();
        Label labelOut = new Label();
        Label date = new Label();
        public void showVisitor(MouseEvent mouseEvent) {
        Visitor visitor = visitorTableView.getSelectionModel().getSelectedItem();
        visitorIdText.setText("" + visitor.getIdNumber());
        visitorFirstNameText.setText(visitor.getFirstName());
        visitorSecondNameText.setText(visitor.getSecondName());
        visitorLastNameText.setText(visitor.getThirdName());
        date.setText(String.valueOf(visitor.getDate()));
        label.setText(String.valueOf(visitor.getTimeIn()));
        labelOut.setText(String.valueOf(visitor.getTimeOut()));
        visitorGenderCombo.setValue(visitor.getGender());
        visitorPhoneNumberText.setText(String.valueOf(visitor.getPhoneNumber()));
        roomVisitText.setText(String.valueOf(visitor.getRoomVisit()));

    }

        /**
         * Method to update visitor timeout time from VBox view
         * @param event
         */
        public void timeOutVisitor(ActionEvent event) {
            if(visitorIdText.getText().isEmpty() || visitorFirstNameText.getText().isEmpty() || visitorSecondNameText.getText().isEmpty() || roomVisitText.getText().isEmpty()){
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        warningLabel.setText("             ");
                                        warningLabel.setStyle("-fx-background-color: transparent;");
                                    }
                                });

                            }
                        },
                        3000
                );

                warningLabel.setText("  Empty Taxpayer Information. Please input details!  ");
                warningLabel.setStyle("-fx-background-color: #eb4848; -fx-background-radius: 5px; -fx-border-radius: 5px;");
                visitorIdText.requestFocus();
            }
           else if(!labelOut.getText().equals("00:00:00")){
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        warningLabel.setText("             ");
                                        warningLabel.setStyle("-fx-background-color: transparent;");
                                    }
                                });

                            }
                        },
                        3000
                );

                warningLabel.setText("  Taxpayer " + visitorFirstNameText.getText().toUpperCase() + " "
                        + visitorSecondNameText.getText().toUpperCase() +" was timed out on " + date.getText() +
                                " at " + labelOut.getText() + "  ");
                warningLabel.setStyle("-fx-background-color: #eb4848; -fx-background-radius: 5px; -fx-border-radius: 5px;");
            }
            else {
                String query = "update visitor set timeout='" + now() + "' where idno='" + visitorIdText.getText() + "' and timein = '" + label.getText() + "'";
                executeQuery(query);
                System.out.println("User updated Successfully");
                showVisitors();

                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        warningLabel.setText("             ");
                                        warningLabel.setStyle("-fx-background-color: transparent;");
                                    }
                                });

                            }
                        },
                        3000
                );

                warningLabel.setText("  Taxpayer " + visitorFirstNameText.getText().toUpperCase() + " "
                        + visitorSecondNameText.getText().toUpperCase() + " timed out Successfully!  ");
                warningLabel.setStyle("-fx-background-color: #6ff26f; -fx-background-radius: 5px; -fx-border-radius: 5px;");
            }

    }

        /**
         * Method to delete visitor based on Algorithm Search from existing users
         * @param event
         */

        public void DeleteUser1(ActionEvent event) {
        if (deleteText.getText().isEmpty()) {
            Alert emptyID = new Alert(Alert.AlertType.INFORMATION);
            emptyID.setTitle("VISITOR ID IS EMPTY");
            emptyID.setContentText("Enter Visitor's National ID to Delete");
            emptyID.showAndWait();
            return;
        }

        String query = "select count(1) from visitor where idno='" + deleteText.getText() + "'";
        try {
            conn = DatabaseConnection.ConnectDatabase();
            assert conn != null;
            stm = conn.createStatement();
            rs = stm.executeQuery(query);
            while (rs.next()) {
                if (rs.getInt(1) == 1) {
                    Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    deleteAlert.setTitle("DELETE VISITOR");
                    deleteAlert.setContentText("Do you want to delete visitor " + deleteText.getText());
                    deleteAlert.setGraphic(new ImageView(this.getClass().getResource("com/ownres/icons/Launcher_Icon.png").toString()));

                    if (deleteAlert.showAndWait().get() == ButtonType.OK) {
                        String deleteQuery = "delete from visitor where idno='" + deleteText.getText() + "'";
                        executeQuery(deleteQuery);
                        deleteText.clear();
                        System.out.println("Visitor deleted Successfully");
                        Alert successDelete = new Alert(Alert.AlertType.INFORMATION);
                        successDelete.setTitle("DELETE SUCCESS");
                        successDelete.setContentText("Visitor Has Been Deleted Successfully!");
                        successDelete.showAndWait();
                        showVisitors();
                    }

                } else {
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            warningLabel.setText("             ");
                                            warningLabel.setStyle("-fx-background-color: transparent;");
                                        }
                                    });

                                }
                            },
                            3000
                    );

                    warningLabel.setText("Invalid visitor ID. Enter a valid National ID to delete!");
                    warningLabel.setStyle("-fx-background-color: #eb4848; -fx-background-radius: 5px; -fx-border-radius: 5px;");
                    visitorIdText.clear();
                    visitorIdText.requestFocus();
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
        String query = "delete from visitor where idno='" + visitorIdText.getText() + "'";
        executeQuery(query);
        showVisitors();
            visitorIdText.clear();
            visitorFirstNameText.clear();
            visitorSecondNameText.clear();
            visitorLastNameText.clear();
            visitorPhoneNumberText.clear();
            visitorGenderCombo.setValue("");
            roomVisitText.clear();
        System.out.println("Visitor deleted Successfully");

    }


    public void filterAction(KeyEvent keyEvent) {

            //initiolize filtered list
            FilteredList<Visitor> filtereData= new FilteredList<>(showVisitors, e->true);
            filterTextField.setOnKeyReleased(e->{


                filterTextField.textProperty().addListener((observable,oldValue,newValue) -> {
                    filtereData.setPredicate((Predicate<? super Visitor>) visitor->{

                        if(newValue==null||newValue.isEmpty()||newValue.isBlank()){
                            return true;
                        }
                        String toLowerCase= newValue.toLowerCase();


                        if(visitor.getFirstName().toLowerCase().contains(toLowerCase)){
                            return true;
                        }
                        if(Integer.toString(visitor.getIdNumber()).toLowerCase().contains(toLowerCase)) {
                            return true;
                        }
                            else if(visitor.getSecondName().toLowerCase().contains(toLowerCase)){
                            return true;
                        }else return visitor.getThirdName().toLowerCase().contains(toLowerCase);
                    });
                });
                final SortedList<Visitor> visitors =new SortedList<>(filtereData);
                visitors.comparatorProperty().bind(visitorTableView.comparatorProperty());
                visitorTableView.setItems(visitors);
            });

    }
}
