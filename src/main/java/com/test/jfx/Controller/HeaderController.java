package com.test.jfx.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.*;

import java.net.URL;
import java.util.ResourceBundle;

public class HeaderController implements Initializable {
    @FXML
    private HBox controlBar;
    @FXML
    private Label dialogTitle;

    private double xOffset;
    private double yOffset;

    public void onMouseDragged(MouseEvent mouseEvent) {
        Window window = controlBar.getScene().getWindow();
        window.setX(mouseEvent.getScreenX() - xOffset);
        window.setY(mouseEvent.getScreenY() - yOffset);
    }

    public void onMousePressed(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    public void onMinimizeClick() {
        Stage stage = (Stage) controlBar.getScene().getWindow();
        stage.setIconified(true);
    }

    public void onMaximizeClick() {
        Stage stage = (Stage) controlBar.getScene().getWindow();
        if (stage.isMaximized()) {
            stage.setMaximized(false);
        } else {
            stage.setMaximized(true);
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
        }
    }

    public void onCloseClick() {
        try {
            Stage stage = (Stage) controlBar.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        } catch (ClassCastException e) {
            controlBar.getScene().getWindow();
            Popup popup = (Popup) controlBar.getScene().getWindow();
            popup.hide();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (resourceBundle != null)
            dialogTitle.setText(resourceBundle.getString("appTitle"));
    }
}
