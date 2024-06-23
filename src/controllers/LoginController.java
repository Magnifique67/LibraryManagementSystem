package controllers;

import config.LibrarianModel;
import config.PatronsModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorMessage;

    // Method to handle login action
    @FXML
    private void handleLogin() throws SQLException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Authenticate librarian
        LibrarianModel librarianModel = new LibrarianModel();
        if (librarianModel.authenticateLibrarian(username, password)) {
            openDashboard("/LibrarianSection.fxml");
        }
        // Authenticate patron
        else {
            PatronsModel patronsModel = new PatronsModel();
            if (patronsModel.authenticatePatron(username, password)) {
                openDashboard("/Patrons.fxml");
            } else {
                errorMessage.setVisible(true); // Show error message
            }
        }
    }

    // Method to open dashboard
    private void openDashboard(String dashboardFXML) {
        try {
            System.out.println("Loading FXML: " + dashboardFXML);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(dashboardFXML));
            Parent root = loader.load();
            System.out.println("FXML Loaded Successfully");

            // Set up the stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Close the login window (optional)
            VBox loginVBox = (VBox) usernameField.getScene().getRoot();
            loginVBox.getScene().getWindow().hide();  // Hide login window

        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + dashboardFXML);
            e.printStackTrace();
        }
    }
    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PatronsView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Register");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
