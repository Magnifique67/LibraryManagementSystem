package controllers;

import Models.Librarians;
import config.LibrarianModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.LinkedList;

public class LibrariansController {

    @FXML
    private TableView<Librarians> librarianTable;
    @FXML
    private TableColumn<Librarians, Integer> librarianIdColumn;
    @FXML
    private TableColumn<Librarians, String> librarianFirstNameColumn;
    @FXML
    private TableColumn<Librarians, String> librarianLastNameColumn;
    @FXML
    private TableColumn<Librarians, String> librarianEmailColumn;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    private LibrarianModel librarianModel;

    public LibrariansController() throws SQLException {
        librarianModel = new LibrarianModel();
    }

    @FXML
    public void initialize() {
        librarianIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        librarianFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        librarianLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        librarianEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));


        try {
            loadLibrarians();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadLibrarians() throws SQLException {
        LinkedList<Librarians> librarianList = librarianModel.getAllLibrarians();
        ObservableList<Librarians> librarians = FXCollections.observableArrayList(librarianList);
        librarianTable.setItems(librarians);
    }

    @FXML
    private void handleAddLibrarian() throws SQLException {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        Librarians newLibrarian = new Librarians(0, firstName, lastName, email, username, password);

        librarianModel.addLibrarian(newLibrarian);
        loadLibrarians();
        clearFields();
    }

    @FXML
    private void handleDeleteLibrarian() throws SQLException {
        Librarians selectedLibrarian = librarianTable.getSelectionModel().getSelectedItem();
        if (selectedLibrarian != null) {
            librarianModel.deleteLibrarian(selectedLibrarian.getId());
            loadLibrarians();
            clearFields();
        }
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        usernameField.clear();
        passwordField.clear();
    }
}
