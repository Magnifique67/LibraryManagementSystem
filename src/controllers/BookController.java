package controllers;

import Models.Authors;
import Models.Books;
import config.AuthorsModel;
import config.BooksModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class BookController {

    @FXML
    private TableView<Books> bookTable;
    @FXML
    private TableColumn<Books, Integer> bookIdColumn;
    @FXML
    private TableColumn<Books, String> titleColumn;
    @FXML
    private TableColumn<Books, Date> publishedDateColumn;
    @FXML
    private TableColumn<Books, String> isbnColumn;
    @FXML
    private TableColumn<Books, Boolean> availabilityColumn;

    @FXML
    private TextField titleField;
    @FXML
    private DatePicker publishedDateField;
    @FXML
    private TextField isbnField;

    @FXML
    private ComboBox<Authors> authorComboBox;
    @FXML
    private Button searchBooksButton;

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField searchField;
    @FXML
    private BooksModel booksModel;
    private AuthorsModel authorsModel;

    public BookController() throws SQLException {
        booksModel = new BooksModel();
        authorsModel = new AuthorsModel();
    }

    @FXML
    public void initialize() {
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        publishedDateColumn.setCellValueFactory(new PropertyValueFactory<>("published_date"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));

        // Initialize authorComboBox
        initAuthorComboBox();

        try {
            loadBooks();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initAuthorComboBox() {
        List<Authors> authorList = authorsModel.getAuthors();
        ObservableList<Authors> authors = FXCollections.observableArrayList(authorList);
        authorComboBox.setItems(authors);
    }

    private void loadBooks() throws SQLException {
        LinkedList<Books> bookList = booksModel.getAllBooks();
        ObservableList<Books> books = FXCollections.observableArrayList(bookList);
        bookTable.setItems(books);
    }

    @FXML
    private void handleAddBook() throws SQLException {
        String title = titleField.getText();
        Date published_date = null;
        if (publishedDateField.getValue() != null) {
            published_date = Date.valueOf(publishedDateField.getValue());
        }
        String isbn = isbnField.getText();
        boolean availability = true; // Default availability

        Authors selectedAuthor = authorComboBox.getValue();
        if (selectedAuthor != null) {
            Books newBook = new Books(0, title, published_date, isbn, availability);
            booksModel.addBook(newBook, selectedAuthor);
            loadBooks();
            clearFields();
        } else {
            // Handle case where no author is selected
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an author!");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleUpdateBook() throws SQLException {
        Books selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            selectedBook.setTitle(titleField.getText());
            if (publishedDateField.getValue() != null) {
                selectedBook.setPublished_date(Date.valueOf(publishedDateField.getValue()));
            }
            selectedBook.setIsbn(isbnField.getText());

            booksModel.updateBook(selectedBook);
            loadBooks();
            clearFields();
        }
    }

    @FXML
    private void handleDeleteBook() throws SQLException {
        Books selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            booksModel.deleteBook(selectedBook.getId());
            loadBooks();
            clearFields();
        }
    }

    @FXML
    private void handleSearchBooks() throws SQLException {
        String keyword = searchField.getText();
        LinkedList<Books> bookList = booksModel.searchBooks(keyword);
        ObservableList<Books> books = FXCollections.observableArrayList(bookList);
        bookTable.setItems(books);
    }
    @FXML
    private void handleTableClick() {
        Books selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            titleField.setText(selectedBook.getTitle());
            if (selectedBook.getPublished_date() != null) {
                publishedDateField.setValue(selectedBook.getPublished_date().toLocalDate());
            }
            isbnField.setText(selectedBook.getIsbn());
        }
    }



    private void clearFields() {
        titleField.clear();
        publishedDateField.setValue(null);
        isbnField.clear();
    }
}
