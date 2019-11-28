package com.test.jfx.Controller;

import com.test.jfx.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.AbstractMap;
import java.util.Map;


public class AddItemDialogController {

    @FXML
    private GridPane gridPane;
    @FXML
    private TextField fieldText;
    @FXML
    private TextField valueText;
    @FXML
    private Button addButton;
    private boolean isAddClick;

    @FXML
    private void initialize() {
        addButton.disableProperty().bind(fieldText.textProperty().isEmpty().or(valueText.textProperty().isEmpty()));
        Platform.runLater(fieldText::requestFocus);
        isAddClick = false;
    }

    public void onAddClick() {
        isAddClick = true;
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            onAddClick();
        else if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            Node source = (Node) keyEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            Platform.runLater(Main.getStage()::requestFocus);
        }
    }


    public Map.Entry<String, String> getReturn() {
        if (isAddClick)
            return new AbstractMap.SimpleEntry<>(fieldText.getText(), valueText.getText());
        else
            return null;
    }
}
