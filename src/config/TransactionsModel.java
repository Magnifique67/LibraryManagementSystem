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
                + "returned BOOLEAN DEFAULT FALSE,"
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
    public LinkedList<Transactions> getAllTransactions() throws SQLException {
        LinkedList<Transactions> transactions = new LinkedList<>();
        String selectTransactionsSQL = "SELECT t.id, t.book_id, b.title as book_title, t.patron_id, p.firstName as patron_firstName, p.lastName as patron_lastName, t.transaction_date, t.due_date, t.returned " +
                "FROM Transactions t " +
                "INNER JOIN Books b ON t.book_id = b.id " +
                "INNER JOIN Patrons p ON t.patron_id = p.id";
        try (PreparedStatement pstmt = connection.prepareStatement(selectTransactionsSQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Transactions transaction = new Transactions(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getInt("patron_id"),
                        rs.getString("book_title"),
                        rs.getString("patron_firstName"),
                        rs.getString("patron_lastName"),
                        rs.getTimestamp("transaction_date"),
                        rs.getDate("due_date"),
                        rs.getBoolean("returned") // Include the returned status
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
        String query = "SELECT t.id, t.book_id, t.patron_id, b.title AS book_title, p.firstName as patron_firstName, p.lastName as patron_lastName, t.transaction_date, t.due_date, t.returned " +
                "FROM Transactions t " +
                "INNER JOIN Books b ON t.book_id = b.id " +
                "INNER JOIN Patrons p ON t.patron_id = p.id " +
                "WHERE t.patron_id = ? AND t.returned = false";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patronId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Transactions transaction = new Transactions(
                            resultSet.getInt("id"),
                            resultSet.getInt("book_id"),
                            resultSet.getInt("patron_id"),
                            resultSet.getString("book_title"),
                            resultSet.getString("patron_firstName"),
                            resultSet.getString("patron_lastName"),
                            resultSet.getTimestamp("transaction_date"),
                            resultSet.getDate("due_date"), // Changed to getDate for due_date
                            resultSet.getBoolean("returned") // Fetch the returned status
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

    public void updateTransaction(Transactions transaction) throws SQLException {
        String updateTransactionSQL = "UPDATE Transactions SET book_id=?, patron_id=?, transaction_date=?, due_date=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateTransactionSQL)) {
            pstmt.setInt(1, transaction.getBook_id());
            pstmt.setInt(2, transaction.getPatron_id());
            pstmt.setTimestamp(3, transaction.getTransaction_date());
            pstmt.setDate(4, transaction.getDue_date());
            pstmt.setInt(5, transaction.getId());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Transaction updated successfully.");
            } else {
                System.out.println("No transaction found with ID: " + transaction.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void borrowBook(int bookId, int loggedInUserId) throws SQLException {
        String checkAvailabilitySQL = "SELECT availability FROM Books WHERE id = ? AND availability = true";
        String borrowBookSQL = "INSERT INTO Transactions (book_id, patron_id, transaction_date, due_date, returned) VALUES (?, ?, ?, ?, false)";
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
            pstmtBorrow.setInt(2, loggedInUserId);
            pstmtBorrow.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pstmtBorrow.setDate(4, Date.valueOf(LocalDate.now().plusWeeks(2)));
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

    public void returnBook(int transactionId) throws SQLException {
        String getBookIdSQL = "SELECT book_id FROM Transactions WHERE id = ?";
        String updateBookAvailabilitySQL = "UPDATE Books SET availability = true WHERE id = ?";
        String updateTransactionReturnedSQL = "UPDATE Transactions SET returned = true WHERE id = ?";

        int bookId = 0;

        try (PreparedStatement getBookIdStmt = connection.prepareStatement(getBookIdSQL)) {
            getBookIdStmt.setInt(1, transactionId);
            ResultSet rs = getBookIdStmt.executeQuery();

            if (rs.next()) {
                bookId = rs.getInt("book_id");
            } else {
                throw new SQLException("Transaction with ID " + transactionId + " not found.");
            }
        }

        if (bookId != 0) {
            try (PreparedStatement updateStmt = connection.prepareStatement(updateBookAvailabilitySQL);
                 PreparedStatement updateTransactionStmt = connection.prepareStatement(updateTransactionReturnedSQL)) {
                connection.setAutoCommit(false); // Start transaction

                // Update books table to set availability to true
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();

                // Update transactions table to set returned to true
                updateTransactionStmt.setInt(1, transactionId);
                updateTransactionStmt.executeUpdate();

                connection.commit(); // Commit transaction

                System.out.println("Book returned successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
                if (connection != null) {
                    try {
                        connection.rollback(); // Rollback transaction if any error occurs
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
    }


}
