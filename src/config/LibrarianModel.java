package config;

import java.sql.*;
import java.util.LinkedList;
import Models.Librarians;

public class LibrarianModel {

    private Connection connection;

    public LibrarianModel() throws SQLException {
        connection = DatabaseConnection.getConnection();
        createLibrariansTable();
    }

    private void createLibrariansTable() {
        String createLibrariansTable = "CREATE TABLE IF NOT EXISTS Librarians ("
                + "id SERIAL PRIMARY KEY,"
                + "First_Name VARCHAR(255) NOT NULL,"
                + "Last_Name VARCHAR(255) NOT NULL,"
                + "Email VARCHAR(255) NOT NULL,"
                + "Username VARCHAR(255) NOT NULL,"
                + "Password VARCHAR(255) NOT NULL"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createLibrariansTable);
            System.out.println("Librarians table created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<Librarians> getAllLibrarians() throws SQLException {
        LinkedList<Librarians> librarians = new LinkedList<>();
        String selectLibrariansSQL = "SELECT * FROM Librarians";
        try (PreparedStatement pstmt = connection.prepareStatement(selectLibrariansSQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Librarians librarian = new Librarians(
                        rs.getInt("id"),
                        rs.getString("First_Name"),
                        rs.getString("Last_Name"),
                        rs.getString("Email"),
                        rs.getString("Username"),
                        rs.getString("Password")
                );
                librarians.add(librarian);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return librarians;
    }

    public void addLibrarian(Librarians librarian) throws SQLException {
        String insertLibrarianSQL = "INSERT INTO Librarians(First_Name, Last_Name, Email, Username, Password) VALUES(?,?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertLibrarianSQL)) {
            pstmt.setString(1, librarian.getFirstName());
            pstmt.setString(2, librarian.getLastName());
            pstmt.setString(3, librarian.getEmail());
            pstmt.setString(4, librarian.getUsername());
            pstmt.setString(5, librarian.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteLibrarian(int librarianId) throws SQLException {
        String deleteLibrarianSQL = "DELETE FROM Librarians WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteLibrarianSQL)) {
            pstmt.setInt(1, librarianId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public  boolean authenticateLibrarian(String username, String password) {
        String sql = "SELECT * FROM Librarians WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // returns true if the query finds a matching patron
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // return false in case of exception or no matching patron
        }
    }
}
