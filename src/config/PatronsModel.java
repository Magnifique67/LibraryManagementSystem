package config;

import Models.Patrons;
import java.sql.*;
import java.util.LinkedList;

public class PatronsModel {

    private Connection conn;

    public PatronsModel() throws SQLException {
        conn = DatabaseConnection.getConnection();
        createPatronsTable();
    }

    private void createPatronsTable() {
        String createPatronsTable = "CREATE TABLE IF NOT EXISTS Patrons ("
                + "id SERIAL PRIMARY KEY,"
                + "firstName VARCHAR(255) NOT NULL,"
                + "lastName VARCHAR(255) NOT NULL,"
                + "email VARCHAR(255) NOT NULL,"
                + "address VARCHAR(255) NOT NULL,"
                + "phone VARCHAR(255) NOT NULL,"
                + "username VARCHAR(255) NOT NULL,"
                + "password VARCHAR(255) NOT NULL"
                + ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createPatronsTable);
            System.out.println("Patrons table created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<Patrons> getAllPatrons() throws SQLException {
        LinkedList<Patrons> patrons = new LinkedList<>();
        String selectPatronsSQL = "SELECT * FROM Patrons";
        try (PreparedStatement pstmt = conn.prepareStatement(selectPatronsSQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Patrons patron = new Patrons(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                patrons.add(patron);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return patrons;
    }

    public boolean addPatron(Patrons patron) throws SQLException {
        String insertPatronSQL = "INSERT INTO Patrons(firstName, lastName, email, address, phone, username, password) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertPatronSQL)) {
            if (patron.getUsername() != null && !patron.getUsername().isEmpty()) {
                pstmt.setString(1, patron.getFirstName());
                pstmt.setString(2, patron.getLastName());
                pstmt.setString(3, patron.getEmail());
                pstmt.setString(4, patron.getAddress());
                pstmt.setString(5, patron.getPhone());
                pstmt.setString(6, patron.getUsername());
                pstmt.setString(7, patron.getPassword());

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0; // Return true if insertion was successful
            } else {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw SQLException to propagate the error
        }
    }

    public void deletePatron(int patronId) throws SQLException {
        String deletePatronSQL = "DELETE FROM Patrons WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(deletePatronSQL)) {
            pstmt.setInt(1, patronId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean authenticatePatron(String username, String password) {
        String sql = "SELECT * FROM Patrons WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean checkPatronExists(int patronId) throws SQLException {
        String query = "SELECT COUNT(*) FROM patrons WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, patronId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // Returns true if patron with patronId exists, false otherwise
                }
            }
        }
        return false; // Return false if no records were found (shouldn't happen with proper setup)
    }
    public int getPatronId(String username) {
        int patronId = 0; // Initialize to default value or handle exceptions

        try (PreparedStatement stmt = conn.prepareStatement("SELECT id FROM patrons WHERE username = ?")) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                patronId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }

        return patronId;
    }
    public void updatePatron(Patrons patron) throws SQLException {

    }
    public void searchPatron(){



    }

}
