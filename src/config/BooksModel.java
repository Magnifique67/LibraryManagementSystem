package config;

import Models.Authors;
import Models.Books;

import java.sql.*;
import java.util.LinkedList;

public class BooksModel {

    private Connection connection;

    public BooksModel() throws SQLException {
        connection = DatabaseConnection.getConnection();
        createBooksTable();
        createAuthorsBooksTable();
    }

    private void createBooksTable() {
        String createBooksTable = "CREATE TABLE IF NOT EXISTS Books ("
                + "id SERIAL PRIMARY KEY,"
                + "title VARCHAR(255) NOT NULL,"
                + "published_date DATE NOT NULL,"
                + "isbn VARCHAR(255) NOT NULL,"
                + "availability BOOLEAN NOT NULL"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createBooksTable);
            System.out.println("Books table created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createAuthorsBooksTable() {
        String createAuthorsBooksTable = "CREATE TABLE IF NOT EXISTS AuthorsBooks ("
                + "book_id INT NOT NULL,"
                + "author_id INT NOT NULL,"
                + "PRIMARY KEY (book_id, author_id),"
                + "FOREIGN KEY (book_id) REFERENCES Books(id),"
                + "FOREIGN KEY (author_id) REFERENCES Authors(id)"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createAuthorsBooksTable);
            System.out.println("AuthorsBooks table created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBook(Books book, Authors author) throws SQLException {
        String insertBookSQL = "INSERT INTO Books(title, published_date, isbn, availability) VALUES(?,?,?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertBookSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setDate(2, book.getPublished_date());
            pstmt.setString(3, book.getIsbn());
            pstmt.setBoolean(4, book.getAvailability());
            pstmt.executeUpdate();

            // Retrieve the generated keys
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            int bookId = 0;
            if (generatedKeys.next()) {
                bookId = generatedKeys.getInt(1);
                book.setId(bookId);
            }

            // Insert into AuthorsBooks
            insertIntoAuthorsBooks(bookId, author.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void insertIntoAuthorsBooks(int bookId, int authorId) throws SQLException {
        String insertAuthorBookSQL = "INSERT INTO AuthorsBooks(book_id, author_id) VALUES(?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertAuthorBookSQL)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, authorId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public LinkedList<Books> getAllBooks() {
        LinkedList<Books> books = new LinkedList<>();
        String selectBooksSql = "SELECT * FROM Books";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectBooksSql)) {
            while (rs.next()) {
                Books book = new Books(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDate("published_date"),
                        rs.getString("isbn"),
                        rs.getBoolean("availability")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Log or handle the exception appropriately
        }
        return books;
    }

    public void updateBook(Books book) {
        String updateBookSql = "UPDATE Books SET title=?, published_date=?, isbn=?, availability=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateBookSql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setDate(2, book.getPublished_date());
            pstmt.setString(3, book.getIsbn());
            pstmt.setBoolean(4, book.getAvailability());
            pstmt.setInt(5, book.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Log or handle the exception appropriately
        }
    }

    public void deleteBook(int bookId) {
        String deleteBookSql = "DELETE FROM Books WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteBookSql)) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Log or handle the exception appropriately
        }
    }

    public LinkedList<Books> searchBooks(String searchTerm) throws SQLException {
        LinkedList<Books> books = new LinkedList<>();
        String searchSQL = "SELECT DISTINCT b.* FROM Books b "
                + "LEFT JOIN AuthorsBooks ab ON b.id = ab.book_id "
                + "LEFT JOIN Authors a ON ab.author_id = a.id "
                + "WHERE b.title ILIKE ? OR b.isbn ILIKE ? OR a.First_Name ILIKE ? OR a.Last_Name ILIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(searchSQL)) {
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Books book = new Books(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getDate("published_date"),
                            rs.getString("isbn"),
                            rs.getBoolean("availability")
                    );
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return books;
    }
    public Books getBookById(int bookId) throws SQLException {
        String query = "SELECT * FROM Books WHERE id = ?";
        Books book = null;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) { // Use if instead of while, since there should be only one result
                book = new Books(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDate("published_date"),
                        rs.getString("isbn"),
                        rs.getBoolean("availability")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return book; // Return the book variable
    }


}

