package controllers;

import Models.Books;
import Models.Transactions;
import config.BooksModel;
import config.TransactionsModel;
import config.PatronsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import session.Session;

import java.sql.Date;
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
    private TableColumn<Books, Date> bookPublishedDateColumn;
    @FXML
    private TextField searchField;

    @FXML
    private TableView<Transactions> transactionsTable;
    @FXML
    private TableColumn<Transactions, Integer> transactionIdColumn;
    @FXML
    private TableColumn<Transactions, String> bookTitleTransactionColumn;
    @FXML
    private TableColumn<Transactions, String> patronFirstNameColumn;
    @FXML
    private TableColumn<Transactions, String> patronLastNameColumn;
    @FXML
    private TableColumn<Transactions, Timestamp> transactionDateColumn;
    @FXML
    private TableColumn<Transactions, LocalDate> dueDateColumn;
    @FXML
    private TableColumn<Transactions, Boolean> returnedColumn;
    @FXML
    private TableColumn<Transactions, Void> actionsColumn; // Added actions column

    private BooksModel booksModel;
    private TransactionsModel transactionsModel;
    private PatronsModel patronsModel;

    public PatronController() {
        try {
            booksModel = new BooksModel();
            transactionsModel = new TransactionsModel();
            patronsModel = new PatronsModel();
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
        bookPublishedDateColumn.setCellValueFactory(new PropertyValueFactory<>("published_date"));
        bookIsbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bookAvailabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));

        // Initialize transactions table columns
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookTitleTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("book_title"));
        patronFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("patron_firstName"));
        patronLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("patron_lastName"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("due_date"));
        returnedColumn.setCellValueFactory(new PropertyValueFactory<>("returned"));

        // Set the cell factory for the actions column
        actionsColumn.setCellFactory(new Callback<TableColumn<Transactions, Void>, TableCell<Transactions, Void>>() {
            @Override
            public TableCell<Transactions, Void> call(final TableColumn<Transactions, Void> param) {
                return new ActionCell(PatronController.this);
            }
        });

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

            // Debug logging to verify patron ID retrieval
            System.out.println("Patron ID Retrieved: " + patronId);

            // Check if patron ID is 0 (or any other unexpected value)
            if (patronId == 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Patron ID is invalid or not set correctly.");
                return;
            }

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

    @FXML
    public void handleUpdateTransaction(Transactions selectedTransaction) {
        if (selectedTransaction == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a transaction to update.");
            return;
        }

        // Show a DatePicker for the user to select a new due date
        DatePicker newDueDatePicker = new DatePicker();
        newDueDatePicker.setPromptText("Select new due date");

        // Create a dialog for updating the due date
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Update Due Date");
        dialog.setHeaderText("Update Due Date for Transaction ID: " + selectedTransaction.getId());
        dialog.getDialogPane().setContent(newDueDatePicker);

        // Add OK and Cancel buttons
        ButtonType buttonTypeUpdate = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getButtonTypes().setAll(buttonTypeUpdate, buttonTypeCancel);

        // Handle button actions
        dialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeUpdate) {
                try {
                    // Convert LocalDate from DatePicker to Timestamp for SQL
                    LocalDate newDueDate = newDueDatePicker.getValue();
                    Date newDueTimestamp = Date.valueOf(newDueDate);

                    // Update the transaction in the database
                    selectedTransaction.setDue_date(newDueTimestamp);
                    transactionsModel.updateTransaction(selectedTransaction);

                    // Refresh transactions table
                    loadPatronTransactions();

                    showAlert(Alert.AlertType.INFORMATION, "Success", "Due Date updated successfully!");
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update Due Date. Please try again.");
                }
            }
        });
    }

    @FXML
    public void handleReturnTransaction(Transactions selectedTransaction) {
        if (selectedTransaction == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a transaction to return.");
            return;
        }

        try {
            // Mark the transaction as returned
            transactionsModel.returnBook(selectedTransaction.getId());

            // Update book availability
            Books returnedBook = booksModel.getBookById(selectedTransaction.getBook_id());
            returnedBook.setAvailability(true);
            booksModel.updateBook(returnedBook);

            // Refresh tables
            loadAllBooks();
            loadPatronTransactions();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Book returned successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to return book. Please try again.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
