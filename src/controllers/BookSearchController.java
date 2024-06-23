package controllers;

import Models.Books;
import config.BooksModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.LinkedList;

public class BookSearchController {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<Books> bookTable;
    @FXML
    private TableColumn<Books, Integer> bookIdColumn;
    @FXML
    private TableColumn<Books, String> bookTitleColumn;
    @FXML
    private TableColumn<Books, String> bookIsbnColumn;
    @FXML
    private TableColumn<Books, Boolean> bookAvailabilityColumn;

    private BooksModel booksModel;

    public BookSearchController() throws SQLException {
        booksModel = new BooksModel();
    }

    @FXML
    public void initialize() {
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookIsbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bookAvailabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
    }

    @FXML
    private void handleSearch() throws SQLException {
        String keyword = searchField.getText();
        LinkedList<Books> bookList = booksModel.searchBooks(keyword);
        ObservableList<Books> books = FXCollections.observableArrayList(bookList);
        bookTable.setItems(books);
    }
}
