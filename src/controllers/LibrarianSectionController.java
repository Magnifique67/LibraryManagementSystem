package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class LibrarianSectionController {

    @FXML
    private AnchorPane bookViewContainer;

    @FXML
    private AnchorPane authorViewContainer;

    @FXML
    public void initialize() {
        loadBookView();
        loadAuthorView();
    }

    private void loadBookView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BookView.fxml"));
            VBox bookView = loader.load(); // Changed to VBox
            bookViewContainer.getChildren().add(bookView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAuthorView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Authors.fxml"));
            VBox authorView = loader.load(); // Changed to VBox
            authorViewContainer.getChildren().add(authorView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
