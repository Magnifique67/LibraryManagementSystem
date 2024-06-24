package controllers;

import Models.Books;
import Models.Transactions;
import config.BooksModel;
import config.TransactionsModel;
import config.PatronsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import session.Session;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.LinkedList;

public class PatronController {

    @FXML
    private TableView<Books> booksTable;
    @FXML
    private TableColumn<Books, Integer> bookIdColumn;
    @FXML
    private TableColumn<Books, String> bookTitleColumn;
    @FXML
    private TableColumn<Books, String> bookIsbnColumn;
    @FXML
    private TableColumn<Books, Boolean> bookAvailabilityColumn;
    @FXML
    private TextField searchField;

    @FXML
    private TableView<Transactions> transactionsTable;
    @FXML
    private TableColumn<Transactions, Integer> transactionIdColumn;
    @FXML
    private TableColumn<Transactions, Integer> bookIdTransactionColumn;
    @FXML
    private TableColumn<Transactions, Integer> patronIdTransactionColumn;
    @FXML
    private TableColumn<Transactions, Timestamp> transactionDateColumn;
    @FXML
    private TableColumn<Transactions, LocalDate> dueDateColumn;

    private BooksModel booksModel;
    private TransactionsModel transactionsModel;
    private  PatronsModel patronsModel;

    public PatronController() {
        try {
            booksModel = new BooksModel();
            transactionsModel = new TransactionsModel();
            patronsModel=new PatronsModel();
        } catch (SQLException e) {
            // Handle exception appropriately, e.g., log it or show an error message
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Initialize books table columns
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookIsbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bookAvailabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));

        // Initialize transactions table columns
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookIdTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("book_id"));
        patronIdTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("patron_id"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("due_date"));

        // Load all books and patron transactions
        try {
            loadAllBooks();
            loadPatronTransactions();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load data. Please try again.");
        }
    }

    private void loadAllBooks() throws SQLException {
        LinkedList<Books> allBooksList = booksModel.getAllBooks();
        ObservableList<Books> allBooks = FXCollections.observableArrayList(allBooksList);
        booksTable.setItems(allBooks);
    }

    @FXML
    private void handleSearchBooks() {
        try {
            String keyword = searchField.getText();
            LinkedList<Books> bookList = booksModel.searchBooks(keyword);
            ObservableList<Books> books = FXCollections.observableArrayList(bookList);
            booksTable.setItems(books);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search books. Please try again.");
        }
    }

    @FXML
    private void handleBorrowBook() {
        try {
            int bookId = getSelectedBookId();
            int patronId = Session.getPatronId();

            // Check if a patron is logged in
            if (patronId == -1) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please log in as a patron.");
                return;
            }

            // Log patron ID for debugging purposes
            System.out.println("Patron ID: " + patronId);

            // Check if the patron exists in the database
            if (!patronsModel.checkPatronExists(patronId)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid patron ID. Please log in again.");
                return;
            }

            // Proceed with borrowing the book
            transactionsModel.borrowBook(bookId, patronId);

            // Update book availability
            Books selectedBook = booksTable.getSelectionModel().getSelectedItem();
            selectedBook.setAvailability(false);
            booksModel.updateBook(selectedBook);

            // Refresh tables
            loadAllBooks();
            loadPatronTransactions();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Book borrowed successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to borrow book. Please try again.");
        }
    }


    @FXML
    private void loadPatronTransactions() {
        try {
            int patronId = Session.getPatronId();
            LinkedList<Transactions> transactionsList = transactionsModel.getTransactionsByPatron(patronId);
            ObservableList<Transactions> transactions = FXCollections.observableArrayList(transactionsList);
            transactionsTable.setItems(transactions);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load patron transactions. Please try again.");
        }
    }

    private int getSelectedBookId() {
        Books selectedBook = booksTable.getSelectionModel().getSelectedItem();
        return selectedBook != null ? selectedBook.getId() : -1;
    }

    private void showAlert(Alert.AlertType alertType, String title, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
