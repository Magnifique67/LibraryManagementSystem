package controllers;

import Models.Patrons;
import config.PatronsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.LinkedList;

public class PatronsInfo {

    @FXML
    private TableView<Patrons> patronsTable;
    @FXML
    private TableColumn<Patrons, Integer> patronIDColumn;
    @FXML
    private TableColumn<Patrons, String> firstNameColumn;
    @FXML
    private TableColumn<Patrons, String> lastNameColumn;
    @FXML
    private TableColumn<Patrons, String> emailColumn;
    @FXML
    private TableColumn<Patrons, String> addressColumn;
    @FXML
    private TableColumn<Patrons, String> phoneColumn;

    @FXML
    private TextField patronId;
    @FXML
    private TextField patronFirstName;
    @FXML
    private TextField patronLastName;
    @FXML
    private TextField patronEmail;
    @FXML
    private TextField patronAddress;
    @FXML
    private TextField patronPhone;
    @FXML
    private TextField patronUsername;
    @FXML
    private TextField patronPassword;

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField searchField;

    private PatronsModel patronsModel;

    @FXML
    public void initialize() {
        try {
            patronsModel = new PatronsModel();

            // Initialize table columns with corresponding property values from Patrons class
            patronIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
//            addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
//            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

            loadPatrons();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Handle table row selection
        patronsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleTableClick());
    }

    // Load patrons data into the TableView
    private void loadPatrons() throws SQLException {
        LinkedList<Patrons> patronsList = patronsModel.getAllPatrons();
        ObservableList<Patrons> patronsObservableList = FXCollections.observableArrayList(patronsList);
        patronsTable.setItems(patronsObservableList);
    }

    // Handle selection of a row in the TableView
    @FXML
    private void handleTableClick() {
        Patrons selectedPatron = patronsTable.getSelectionModel().getSelectedItem();
        if (selectedPatron != null) {
            patronId.setText(String.valueOf(selectedPatron.getId()));
            patronFirstName.setText(selectedPatron.getFirstName());
            patronLastName.setText(selectedPatron.getLastName());
            patronEmail.setText(selectedPatron.getEmail());
            patronAddress.setText(selectedPatron.getAddress());
            patronPhone.setText(selectedPatron.getPhone());
        }
    }

    // Handle adding a new patron
    @FXML
    private void handleAddPatron() {
        try {
            String firstName = patronFirstName.getText();
            String lastName = patronLastName.getText();
            String email = patronEmail.getText();
            String address = patronAddress.getText();
            String phone = patronPhone.getText();
            String username = patronUsername.getText();
            String password = patronPassword.getText();

            // Use the constructor without ID for adding new patrons
            Patrons newPatron = new Patrons(firstName, lastName, email, address, phone, username, password);
            patronsModel.addPatron(newPatron);
            loadPatrons();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Handle updating an existing patron
    @FXML
    private void handleUpdatePatron() {
        try {
            int patronIdValue = Integer.parseInt(patronId.getText());
            String firstName = patronFirstName.getText();
            String lastName = patronLastName.getText();
            String email = patronEmail.getText();
            String address = patronAddress.getText();
            String phone = patronPhone.getText();
            String username = patronUsername.getText();
            String password = patronPassword.getText();

            Patrons patron = new Patrons(patronIdValue, firstName, lastName, email, address, phone, username, password);
            patronsModel.updatePatron(patron);
            loadPatrons();
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Handle deleting a patron
    @FXML
    private void handleDeletePatron() {
        try {
            int patronIdValue = Integer.parseInt(patronId.getText());
            patronsModel.deletePatron(patronIdValue);
            loadPatrons();
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }


    // Handle searching for a patron
    @FXML
    private void handleSearch() {
//        try {
//            String searchText = searchField.getText();
//            LinkedList<Patrons> patronsList = patronsModel.searchPatron(searchText);
//            ObservableList<Patrons> patronsObservableList = FXCollections.observableArrayList(patronsList);
//            patronsTable.setItems(patronsObservableList);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
   // Clear all input fields
    private void clearFields() {
        patronId.clear();
        patronFirstName.clear();
        patronLastName.clear();
        patronEmail.clear();
        patronAddress.clear();
        patronPhone.clear();
        patronUsername.clear();
        patronPassword.clear();
    }
}
