package com.test.jfx.Controller;

import com.test.jfx.Main;
import com.test.jfx.Web.SocketClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DialogController {
    @FXML
    private AnchorPane pane;
    @FXML
    private VBox vBox;
    @FXML
    private TextArea text;
    @FXML
    private Button sendButton;
    @FXML
    private Button switchButton;
    @FXML
    private Text contentText;
    @FXML
    private TextFlow textOut;
    @FXML
    private HBox ipBox;
    @FXML
    private Text serverIp;
    @FXML
    private TextField firstIpNum;
    @FXML
    private TextField secondIpNum;
    @FXML
    private TextField thirdIpNum;
    @FXML
    private TextField fourthIpNum;

    private List<Parent> optionalElements;

    @FXML
    private void initialize() {
        ipBox.getChildren()
                .filtered(node -> node instanceof TextField)
                .forEach(node -> setMaxTextLength((TextField) node));

        serverIp.textProperty().bind(firstIpNum.textProperty()
                .concat(".").concat(secondIpNum.textProperty())
                .concat(".").concat(thirdIpNum.textProperty())
                .concat(".").concat(fourthIpNum.textProperty()));

        Slider widthSlider = new Slider();
        widthSlider.setMin(pane.prefWidthProperty().subtract(20).doubleValue());
        widthSlider.setMax(900.0);
        setMaxSize(widthSlider);
        configureSlider(widthSlider);

        Slider heightSlider = new Slider();
        heightSlider.setMin(200.0);
        heightSlider.setMax(500.0);
        setMaxSize(heightSlider);
        configureSlider(heightSlider);

        PieChart.Data argon = new PieChart.Data("Argon", 93);
        ObservableList<PieChart.Data> valueList = FXCollections.observableArrayList(
                new PieChart.Data("Nitrogen", 7809),
                new PieChart.Data("Oxygen", 1125),
                argon,
                new PieChart.Data("Other", 973));

        PieChart pieChart = new PieChart(valueList);
        pieChart.setClockwise(false);
        pieChart.setStartAngle(90);
        pieChart.setTitle("Air composition");
        pieChart.setMinHeight(heightSlider.getMin());
        pieChart.setPrefHeight(heightSlider.getMin());
        pieChart.setPrefWidth(widthSlider.getMin());
        pieChart.setMinWidth(widthSlider.getMin());

        heightSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            pieChart.setMinSize(pieChart.getMinWidth(), newValue.doubleValue());
            pane.getScene().getWindow().sizeToScene();
        });

        widthSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
            pieChart.setMinSize(newValue.doubleValue(), pieChart.getMinHeight());
            pane.getScene().getWindow().sizeToScene();
        }));

        RadioButton radioButton = new RadioButton();
        radioButton.setLayoutY(110);

        pieChart.getData().forEach(data -> {
            String percentage = String.format("%.2f%%", (data.getPieValue() / 100));
            Tooltip toolTip = new Tooltip(percentage);
            toolTip.setShowDelay(Duration.seconds(0.3));
            Tooltip.install(data.getNode(), toolTip);
        });

        vBox.getChildren().addAll(widthSlider, heightSlider, pieChart);

        Group group = new Group();
        vBox.getChildren().add(group);
        group.getChildren().addAll(createButtons(10));
        group.getChildren().add(radioButton);
        Platform.runLater(heightSlider::requestFocus);
        optionalElements = new ArrayList<>(Arrays.asList(widthSlider, heightSlider, pieChart, group));
        onSwitchClick();
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            Node source = (Node) keyEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            Platform.runLater(Main.getStage()::requestFocus);
        }
    }

    public void onSendClick() {
        try {
            SocketClient socketClient;
            if (!text.getText().trim().isEmpty())
                socketClient = new SocketClient(serverIp.getText());
            else
                socketClient = new SocketClient();
            contentText.textProperty().bind(socketClient.getServerAnswerProperty());
            socketClient.sendMessage(text.getText());
        } catch (IOException e) {
            System.out.println("[ERROR] Error when connection with server: " + e.getMessage());
            contentText.setText("Bad connection with server. Please, try again later");
        }
    }

    public void onSwitchClick() {
        optionalElements.forEach(o -> o.setVisible(!o.isVisible()));
        if (switchButton.getText().equals("Show")) {
            switchButton.setText("Hide");
        } else {
            switchButton.setText("Show");
        }
    }

    private void setMaxSize(Control node) {
        Insets vBoxInsets = vBox.getInsets();
        double width = pane.prefWidthProperty().subtract(vBoxInsets.getLeft() + vBoxInsets.getRight()).doubleValue();
        node.setMaxWidth(width);
    }

    private void configureSlider(Slider slider) {
        slider.setShowTickLabels(true);
        slider.setSnapToTicks(true);
        slider.setMinorTickCount(0);
        slider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return String.format("%.0f", object);
            }

            @Override
            public Double fromString(String string) {
                return Double.parseDouble(string);
            }
        });
        slider.setMajorTickUnit(slider.maxProperty().subtract(slider.minProperty()).divide(15).doubleValue());
    }

    private List<Button> createButtons(int count) {
        List<Button> buttons = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Button button = new Button(String.format("Button%d", i));
            button.setLayoutX(button.prefWidthProperty().add(68).multiply(i % 4).doubleValue());
            button.setLayoutY(button.prefHeightProperty().add(35).multiply(i / 4).doubleValue());
            buttons.add(button);
        }
        return buttons;
    }

    private void setMaxTextLength(TextField textField) {
        textField.textProperty().addListener(((observable, oldValue, newValue) -> {
            Pattern pattern = Pattern.compile("\\d{0,3}");
            Matcher matcher = pattern.matcher(newValue);
            if (!matcher.matches() || (!newValue.isEmpty() && Integer.parseInt(newValue) > 255)) {
                textField.setText(oldValue);
            }
        }));
    }
}
