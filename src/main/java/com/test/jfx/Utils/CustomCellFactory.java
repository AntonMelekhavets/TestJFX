package com.test.jfx.Utils;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class CustomCellFactory implements Callback<TableColumn<String, String>, TableCell<String, String>> {

    @Override
    public TableCell<String, String> call(final TableColumn<String, String> col) {
        return new CustomTableCell();
    }

}
