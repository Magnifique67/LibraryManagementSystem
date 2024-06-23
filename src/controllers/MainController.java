package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    public void openLibrarianPanel() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/LibrarianView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Librarian Panel");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openPatronPanel() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/PatronsView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Patron Panel");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
