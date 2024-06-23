package controllers;

import Models.Books;
import Models.Transactions;
import config.BooksModel;
import config.TransactionsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import session.Session;

import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedList;

public class PatronController {

    @FXML
    private TableView<Books> booksTable;
    @FXML
    private TableColumn<Books, Integer> bookIdColumn;
    @FXML
    private TableColumn<Books, String> bookTitleColumn;
    @FXML
    public TableColumn<Books, Date> bookPublishedDateColumn;
    @FXML
    private TableColumn<Books, String> bookIsbnColumn;
    @FXML
    private TableColumn<Books, Boolean> bookAvailabilityColumn;
    @FXML
    private TextField searchField;
    @FXML
    private TextField patronIdField;  // Assuming patrons will input their ID to borrow

    // For transactions view
    @FXML
    private TableView<Transactions> transactionsTable;
    @FXML
    private TableColumn<Transactions, Integer> transactionIdColumn;
    @FXML
    private TableColumn<Transactions, Integer> bookIdTransactionColumn;
    @FXML
    private TableColumn<Transactions, Integer> patronIdTransactionColumn;
    @FXML
    private TableColumn<Transactions, String> transactionDateColumn;
    @FXML
    private TableColumn<Transactions, String> dueDateColumn;

    private BooksModel booksModel;
    private TransactionsModel transactionsModel;
    private int patronId;

    public PatronController() throws SQLException {
        this.patronId = Session.getPatronId(); // Retrieve patron ID from session
        booksModel = new BooksModel();
        transactionsModel = new TransactionsModel();
    }
    @FXML
    public void initialize() {
        // Initialize books table
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookIsbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bookPublishedDateColumn.setCellValueFactory(new PropertyValueFactory<>("published_date"));
        bookAvailabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));

        // Initialize transactions table
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookIdTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        patronIdTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("patronId"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        // Load all books and patron's transactions when the view is initialized
        try {
            loadAllBooks();
            loadPatronTransactions(patronId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAllBooks() throws SQLException {
        LinkedList<Books> allBooksList = booksModel.getAllBooks();
        ObservableList<Books> allBooks = FXCollections.observableArrayList(allBooksList);
        booksTable.setItems(allBooks);
    }

    @FXML
    private void handleSearchBooks() throws SQLException {
        String keyword = searchField.getText();
        LinkedList<Books> bookList = booksModel.searchBooks(keyword);
        ObservableList<Books> books = FXCollections.observableArrayList(bookList);
        booksTable.setItems(books);
    }

    @FXML
    private void handleBorrowBook() {
        try {
            Books selectedBook = booksTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null && selectedBook.getAvailability()) {
                transactionsModel.borrowBook(selectedBook.getId(), patronId);
                // Update the availability status of the book
                selectedBook.setAvailability(false);
                booksModel.updateBook(selectedBook);
                handleSearchBooks(); // Refresh the table view
                loadPatronTransactions(patronId); // Load patron's transactions after borrowing
            } else {
                showAlert(Alert.AlertType.WARNING, "Book Unavailable", "The selected book is not available for borrowing.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    private void loadPatronTransactions(int patronId) {
        try {
            LinkedList<Transactions> transactionsList = transactionsModel.getTransactionsByPatron(patronId);
            ObservableList<Transactions> transactions = FXCollections.observableArrayList(transactionsList);
            transactionsTable.setItems(transactions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
