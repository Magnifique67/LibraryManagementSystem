package controllers;

import Models.Transactions;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;

public class ActionCell extends TableCell<Transactions, Void> {
    private final Button updateButton = new Button("Update");
    private final Button returnButton = new Button("Return");

    public ActionCell(PatronController controller) {
        HBox hbox = new HBox(updateButton, returnButton);
        updateButton.setOnAction(event -> {
            Transactions transaction = getTableView().getItems().get(getIndex());
            controller.handleUpdateTransaction(transaction);
        });
        returnButton.setOnAction(event -> {
            Transactions transaction = getTableView().getItems().get(getIndex());
            controller.handleReturnTransaction(transaction);
        });
        setGraphic(hbox);
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(new HBox(updateButton, returnButton));
        }
    }
}
