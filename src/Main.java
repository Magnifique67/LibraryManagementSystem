import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Main instance;

    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        loadDashboardView(primaryStage);
    }

    private void loadDashboardView(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
            Parent root = loader.load();

            stage.setTitle("Library Management System - Dashboard");
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadLibrarianView() {
        Stage librarianStage = new Stage();
        loadView(librarianStage, "/LibrarianView.fxml", "Library Management System - Librarians");
    }

    public static void loadPatronsView() {
        Stage patronStage = new Stage();
        loadView(patronStage, "/Patrons.fxml", "Library Management System - Patrons");
    }

    private static void loadView(Stage stage, String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile));
            Parent root = loader.load();

            stage.setTitle(title);
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Main getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
