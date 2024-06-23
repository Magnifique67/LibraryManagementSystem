package controllers;

import Models.Authors;
import config.AuthorsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.LinkedList;

public class AuthorController {

    @FXML
    private TableView<Authors> authorTable;
    @FXML
    private TableColumn<Authors, Integer> authorIdColumn;
    @FXML
    private TableColumn<Authors, String> firstNameColumn;
    @FXML
    private TableColumn<Authors, String> lastNameColumn;
    @FXML
    private TableColumn<Authors, String> emailColumn;

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;

    private AuthorsModel authorsModel;

    public AuthorController() {
        authorsModel = new AuthorsModel();
    }

    @FXML
    public void initialize() {
        // Initialize TableView and columns
        authorIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Load authors into TableView
        try {
            loadAuthors();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error loading authors: " + e.getMessage());
        }

        // Set selection listener for TableView
        authorTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showAuthorDetails(newValue));
    }

    private void loadAuthors() throws SQLException {
        LinkedList<Authors> authorList = authorsModel.getAuthors();
        ObservableList<Authors> authors = FXCollections.observableArrayList(authorList);
        authorTable.setItems(authors);
    }

    private void showAuthorDetails(Authors author) {
        if (author != null) {
            firstNameField.setText(author.getFirst_name());
            lastNameField.setText(author.getLast_name());
            emailField.setText(author.getEmail());
        } else {
            clearFields();
        }
    }

    @FXML
    private void handleAddAuthor() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();

        // Validate input fields
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        Authors newAuthor = new Authors(0, firstName, lastName, email);

        try {
            authorsModel.addAuthor(newAuthor);
            loadAuthors();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error adding author: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateAuthor() {
        Authors selectedAuthor = authorTable.getSelectionModel().getSelectedItem();
        if (selectedAuthor != null) {
            selectedAuthor.setFirst_name(firstNameField.getText());
            selectedAuthor.setLast_name(lastNameField.getText());
            selectedAuthor.setEmail(emailField.getText());

            try {
                authorsModel.updateAuthor(selectedAuthor);
                loadAuthors();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error updating author: " + e.getMessage());
            }
        } else {
            showAlert("Please select an author to update.");
        }
    }

    @FXML
    private void handleDeleteAuthor() {
        Authors selectedAuthor = authorTable.getSelectionModel().getSelectedItem();
        if (selectedAuthor != null) {
            try {
                authorsModel.deleteAuthor(selectedAuthor.getId());
                loadAuthors();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error deleting author: " + e.getMessage());
            }
        } else {
            showAlert("Please select an author to delete.");
        }
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
