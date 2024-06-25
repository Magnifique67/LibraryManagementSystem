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
    private AnchorPane transactionViewContainer;
    @FXML
    private  AnchorPane patronViewContainer;

    @FXML
    public void initialize() {
        loadBookView();
        loadAuthorView();
        loadTransactionView();
        loadPatronView();
    }

    private void loadBookView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BookView.fxml"));
            VBox bookView = loader.load();
            bookViewContainer.getChildren().add(bookView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAuthorView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Authors.fxml"));
            VBox authorView = loader.load();
            authorViewContainer.getChildren().add(authorView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTransactionView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TransactionView.fxml"));
            VBox transactionView = loader.load();
            transactionViewContainer.getChildren().add(transactionView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadPatronView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PatronsInfo.fxml"));
            VBox patronView = loader.load();
            patronViewContainer.getChildren().add(patronView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
