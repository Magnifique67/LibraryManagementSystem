package controllers;

import Models.Patrons;
import config.PatronsModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class PatronRegistrationController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleRegister() throws SQLException {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Debugging statements
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Email: " + email);
        System.out.println("Address: " + address);
        System.out.println("Phone: " + phone);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        if (username == null || username.isEmpty()) {
            showAlert("Error", "Username cannot be null or empty");
            return;
        }

        Patrons patron = new Patrons(0,firstName, lastName, email, address, phone, username, password);
        PatronsModel model = new PatronsModel();
        boolean isAdded = model.addPatron(patron);

        if (isAdded) {
            showAlert("Success", "Patron registered successfully");
        } else {
            showAlert("Error", "Failed to register patron");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
