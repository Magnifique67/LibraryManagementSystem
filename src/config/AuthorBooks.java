package config;

import java.sql.*;
import java.util.LinkedList;
import Models.Authors;
import Models.Books;

public class AuthorBooks {

    public static void createTable() {
        String createAuthorsBooksTable = "CREATE TABLE IF NOT EXISTS AuthorsBooks ("
                + "book_id INT NOT NULL, "
                + "author_id INT NOT NULL, "
                + "PRIMARY KEY (book_id, author_id), "
                + "FOREIGN KEY (book_id) REFERENCES Books(id), "
                + "FOREIGN KEY (author_id) REFERENCES Authors(id)"
                + ");";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createAuthorsBooksTable);
            System.out.println("AuthorsBooks table created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addAuthorToBook(int bookId, int authorId) throws SQLException {
        String query = "INSERT INTO AuthorsBooks (book_id, author_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, authorId);
            stmt.executeUpdate();
        }
    }

    public void removeAuthorFromBook(int bookId, int authorId) throws SQLException {
        String query = "DELETE FROM AuthorsBooks WHERE book_id = ? AND author_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, authorId);
            stmt.executeUpdate();
        }
    }

    public LinkedList<Authors> getAuthorsForBook(int bookId) throws SQLException {
        LinkedList<Authors> authors = new LinkedList<>();
        String query = "SELECT a.id, a.First_Name, a.Last_Name FROM Authors a INNER JOIN AuthorsBooks ba ON a.id = ba.author_id WHERE ba.book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Authors author = new Authors(
                        rs.getInt("id"),
                        rs.getString("First_Name"),
                        rs.getString("Last_Name"),
                        rs.getString("Email")
                );
                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    public static void main(String[] args) {
        createTable();
    }
}
