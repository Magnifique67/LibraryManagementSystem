package controllers;

import Models.Transactions;
import config.TransactionsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class TransactionsController {

    @FXML
    private TableView<Transactions> transactionsTable;
    @FXML
    private TableColumn<Transactions, Integer> transactionIDColumn;
    @FXML
    private TableColumn<Transactions, Integer> bookIDColumn;
    @FXML
    private TableColumn<Transactions, Integer> patronIDColumn;
    @FXML
    private TableColumn<Transactions, String> bookTitleColumn;
    @FXML
    private TableColumn<Transactions, String> patronFirstNameColumn;
    @FXML
    private TableColumn<Transactions, String> patronLastNameColumn;
    @FXML
    private TableColumn<Transactions, Timestamp> transactionDateColumn;
    @FXML
    private TableColumn<Transactions, Date> dueDateColumn;
    @FXML
    private TableColumn<Transactions, Boolean> returnedColumn;

    @FXML
    private TextField transactionId;
    @FXML
    private TextField transactionBookId;
    @FXML
    private TextField transactionPatronId;
    @FXML
    private TextField transactionDate;
    @FXML
    private TextField transactionDueDate;
    @FXML
    private CheckBox returnedCheckBox;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField searchField;

    private TransactionsModel transactionsModel;

    @FXML
    public void initialize() {
        try {
            transactionsModel = new TransactionsModel();
            transactionIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("book_id"));
            patronIDColumn.setCellValueFactory(new PropertyValueFactory<>("patron_id"));
            bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("book_title"));
            patronFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("patron_firstName"));
            patronLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("patron_lastName"));
            transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
            dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("due_date"));
            returnedColumn.setCellValueFactory(new PropertyValueFactory<>("returned"));

            loadTransactions();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        transactionsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleTableClick());
    }

    private void loadTransactions() {
        try {
            List<Transactions> transactionsList = transactionsModel.getAllTransactions();
            ObservableList<Transactions> transactions = FXCollections.observableArrayList(transactionsList);
            transactionsTable.setItems(transactions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTableClick() {
        Transactions selectedTransaction = transactionsTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            transactionId.setText(String.valueOf(selectedTransaction.getId()));
            transactionBookId.setText(String.valueOf(selectedTransaction.getBook_id()));
            transactionPatronId.setText(String.valueOf(selectedTransaction.getPatron_id()));
            transactionDate.setText(String.valueOf(selectedTransaction.getTransaction_date()));
            transactionDueDate.setText(String.valueOf(selectedTransaction.getDue_date()));
            returnedCheckBox.setSelected(selectedTransaction.isReturned());
        }
    }

    @FXML
    private void handleUpdateTransaction() {
        try {
            int transactionIdValue = Integer.parseInt(transactionId.getText());
            int transactionBookIdValue = Integer.parseInt(transactionBookId.getText());
            int transactionPatronIdValue = Integer.parseInt(transactionPatronId.getText());
            Timestamp transactionDateValue = Timestamp.valueOf(transactionDate.getText());
            Date dueDateValue = Date.valueOf(transactionDueDate.getText());
            boolean returnedValue = returnedCheckBox.isSelected();

            Transactions transaction = new Transactions(transactionIdValue, transactionBookIdValue, transactionPatronIdValue,
                    null, null, null, transactionDateValue, dueDateValue, returnedValue);

            transactionsModel.updateTransaction(transaction);
            loadTransactions();
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteTransaction() {
        try {
            int transactionIdValue = Integer.parseInt(transactionId.getText());
            transactionsModel.deleteTransaction(transactionIdValue);
            loadTransactions();
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        // Implement search functionality here
        // You can use the searchField
    }

    private void clearFields() {
        transactionId.clear();
        transactionBookId.clear();
        transactionPatronId.clear();
        transactionDate.clear();
        transactionDueDate.clear();
        returnedCheckBox.setSelected(false);
    }
}
