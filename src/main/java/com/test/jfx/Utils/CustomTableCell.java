package com.test.jfx.Utils;

import javafx.scene.control.TableCell;
import javafx.scene.input.MouseButton;

public class CustomTableCell extends TableCell<String, String> {

    public CustomTableCell() {
        setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (getIndex() >= getTableView().getItems().size()) {
                    getTableView().getSelectionModel().clearSelection();
                } else {
                    getTableView().getSelectionModel().select(getIndex(), getTableColumn());
                }
            }
        });

        setOnDragDetected(event -> {
            startFullDrag();
            getTableView().getSelectionModel().select(getIndex(), getTableColumn());
        });

        setOnMouseDragEntered(event -> {
            getTableView().getSelectionModel().select(getIndex(), getTableColumn());
        });
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
        } else {
            setText(item);
        }
    }
}
