package config;

import Models.Transactions;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class TransactionsModel {
    private Connection connection;

    public TransactionsModel() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }

    public static void createTable() {
        String createTableTransactions = "CREATE TABLE IF NOT EXISTS Transactions ("
                + "id SERIAL PRIMARY KEY,"
                + "book_id INTEGER NOT NULL,"
                + "patron_id INTEGER NOT NULL,"
                + "transaction_date TIMESTAMP NOT NULL,"
                + "due_date DATE NOT NULL,"
                + "FOREIGN KEY (book_id) REFERENCES Books(id),"
                + "FOREIGN KEY (patron_id) REFERENCES Patrons(id)"
                + ");";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableTransactions);
            System.out.println("Transactions table created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTransaction(Transactions transaction) throws SQLException {
        String insertTransactionSQL = "INSERT INTO Transactions(book_id, patron_id, transaction_date, due_date) VALUES(?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertTransactionSQL)) {
            pstmt.setInt(1, transaction.getBook_id());
            pstmt.setInt(2, transaction.getPatron_id());
            pstmt.setTimestamp(3, transaction.getTransaction_date());
            pstmt.setDate(4, transaction.getDue_date());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public LinkedList<Transactions> getAllTransactions() throws SQLException {
        LinkedList<Transactions> transactions = new LinkedList<>();
        String selectTransactionsSQL = "SELECT * FROM Transactions";
        try (PreparedStatement pstmt = connection.prepareStatement(selectTransactionsSQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Transactions transaction = new Transactions(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getInt("patron_id"),
                        rs.getTimestamp("transaction_date"),
                        rs.getDate("due_date")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return transactions;
    }

    public LinkedList<Transactions> getTransactionsByPatron(int patronId) throws SQLException {
        LinkedList<Transactions> transactionsList = new LinkedList<>();
        String query = "SELECT * FROM Transactions WHERE patron_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patronId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Transactions transaction = new Transactions(
                            resultSet.getInt("id"),
                            resultSet.getInt("book_id"),
                            resultSet.getInt("patron_id"),
                            resultSet.getTimestamp("transaction_date"),
                            resultSet.getDate("due_date")
                    );
                    transactionsList.add(transaction);
                }
            }
        }
        return transactionsList;
    }

    public void deleteTransaction(int transactionId) throws SQLException {
        String deleteTransactionSQL = "DELETE FROM Transactions WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteTransactionSQL)) {
            pstmt.setInt(1, transactionId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void borrowBook(int bookId, int loggedInUserId) throws SQLException {
        String checkAvailabilitySQL = "SELECT availability FROM Books WHERE id = ? AND availability = true";
        String borrowBookSQL = "INSERT INTO Transactions (book_id, patron_id, transaction_date, due_date) VALUES (?, ?, ?, ?)";
        String updateBookAvailabilitySQL = "UPDATE Books SET availability = false WHERE id = ?";

        try (PreparedStatement pstmtCheck = connection.prepareStatement(checkAvailabilitySQL);
             PreparedStatement pstmtBorrow = connection.prepareStatement(borrowBookSQL);
             PreparedStatement pstmtUpdate = connection.prepareStatement(updateBookAvailabilitySQL)) {

            // Check if the book is available
            pstmtCheck.setInt(1, bookId);
            try (ResultSet rs = pstmtCheck.executeQuery()) {
                if (rs.next()) {
                    boolean available = rs.getBoolean("availability");
                    if (!available) {
                        System.out.println("Book is not available for borrowing.");
                        return;
                    }
                } else {
                    System.out.println("Book not found or not available.");
                    return;
                }
            }

            // Start transaction
            connection.setAutoCommit(false);

            // Insert into transactions table
            pstmtBorrow.setInt(1, bookId);
            pstmtBorrow.setInt(2, loggedInUserId); // Use the logged-in user's ID
            pstmtBorrow.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pstmtBorrow.setDate(4, Date.valueOf(LocalDate.now().plusWeeks(2))); // assuming 2 weeks borrowing period
            pstmtBorrow.executeUpdate();

            // Update books table to set availability to false
            pstmtUpdate.setInt(1, bookId);
            pstmtUpdate.executeUpdate();

            // Commit transaction
            connection.commit();

            System.out.println("Book borrowed successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    // Rollback transaction if any error occurs
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void returnBook(int bookId) throws SQLException {
        String checkAvailabilitySQL = "SELECT availability FROM Books WHERE id = ?";
        String updateBookAvailabilitySQL = "UPDATE Books SET availability = true WHERE id = ?";

        // Check if the book is available
        boolean isBookAvailable = false;
        try (PreparedStatement checkStmt = connection.prepareStatement(checkAvailabilitySQL)) {
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                isBookAvailable = rs.getBoolean("availability");
            } else {
                throw new SQLException("Book with ID " + bookId + " not found.");
            }
        }

        // If the book is currently borrowed, update its availability to true (available)
        if (!isBookAvailable) {
            try (PreparedStatement updateStmt = connection.prepareStatement(updateBookAvailabilitySQL)) {
                updateStmt.setInt(1, bookId);
                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated != 1) {
                    throw new SQLException("Failed to update availability for book with ID " + bookId);
                }
            }
        } else {
            throw new SQLException("Book with ID " + bookId + " is already available.");
        }
    }
}
