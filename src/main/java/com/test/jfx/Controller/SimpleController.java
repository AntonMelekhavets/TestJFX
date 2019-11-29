package com.test.jfx.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.jfx.Control.FrameTimer;
import com.test.jfx.Main;
import com.test.jfx.Model.User;
import com.test.jfx.Utils.CustomCellFactory;
import com.test.jfx.Utils.ProgressChanger;
import com.test.jfx.Utils.ResizeHelper;
import com.test.jfx.Utils.impl.WindowsScreenCapturer;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SimpleController implements Initializable {
    @FXML
    private Pane pane;
    @FXML
    private Button dialogButton;
    @FXML
    private Button choiceButton;
    @FXML
    private Button popupButton;
    @FXML
    private Button printScreenButton;
    @FXML
    private TableView tableView;
    @FXML
    private VBox vBox;
    private ObjectMapper objectMapper = new ObjectMapper();
    private FileChooser fileChooser;
    private String selected = "desktop";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pane.getChildren().add(loadFxml("/fxml/Header.fxml"));
        dialogButton.setText(resources.getString("dialogButton"));
        choiceButton.setText(resources.getString("choiceButton"));
        popupButton.setText(resources.getString("popupButton"));
        printScreenButton.setText(resources.getString("printScreenButton"));

        createContextMenu();
        createTooltip(dialogButton);
        createTooltip(choiceButton);
        createTooltip(popupButton);
    }

    private void createTooltip(Button button) {
        Tooltip tooltip = new Tooltip();
        tooltip.setOpacity(0.5);
        tooltip.setShowDelay(Duration.seconds(0.5));
        tooltip.setText(button.getText());
        Tooltip.install(button, tooltip);
    }

    public void initTable(User user) {
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn<Map.Entry<String, String>, String> fieldColumn = new TableColumn("Field");
        TableColumn<Map.Entry<String, String>, String> valueColumn = new TableColumn("Value");
        fieldColumn.setCellValueFactory(map -> new ReadOnlyObjectWrapper<>(map.getValue().getKey()));
        valueColumn.setCellValueFactory(map -> new ReadOnlyObjectWrapper<>(map.getValue().getValue()));
        tableView.getColumns().addAll(fieldColumn, valueColumn);
        tableView.getSelectionModel().setCellSelectionEnabled(true);

        Callback cellFactory = new CustomCellFactory();
        fieldColumn.setCellFactory(cellFactory);
        valueColumn.setCellFactory(cellFactory);

        Map<String, String> userMap = objectMapper.convertValue(user, Map.class);
        ObservableList<Map.Entry<String, String>> tableItems = FXCollections.observableArrayList(userMap.entrySet());
        tableView.setItems(tableItems);
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            Node source = (Node) keyEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            Platform.runLater(Main.getStage()::requestFocus);
        }
    }

    public void onDialogClick() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Dialog.fxml"), ResourceBundle.getBundle(Main.BUNDLE_NAME));
        try {
            AnchorPane parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage dialog = new Stage();
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(Main.getStage());
            dialog.setScene(scene);
            dialog.setMaxWidth(Double.MAX_VALUE);
            dialog.setMaxHeight(Double.MAX_VALUE);
            centerDialogOnMain(dialog, parent.prefWidthProperty());
            ResizeHelper.addResizeListener(dialog);
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void centerDialogOnMain(Stage dialog, DoubleProperty prefWidthProperty) {
        Stage main = Main.getStage();
        DoubleBinding mainCenter = main.xProperty().add(main.widthProperty().divide(2));
        dialog.setX(mainCenter.subtract(prefWidthProperty.divide(2)).doubleValue());
    }

    private List<String> getOptions() {
        List<String> options = WindowsScreenCapturer.getWindows();
        return options;
    }

    public void onPopupClick() {
        Popup popup = new Popup();
        popup.getScene().getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        VBox mainBox = new VBox();
        mainBox.setId("popupBox");
        VBox contentBox = new VBox();
        contentBox.setSpacing(10);
        contentBox.setId("contentBox");
        Label popupLabel = new Label("PopupContent");
        popupLabel.setId("popupLabel");

        Spinner<Integer> spinner = new Spinner();
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 24, 4);
        valueFactory.setAmountToStepBy(2);
        spinner.setValueFactory(valueFactory);
        spinner.setPromptText("Prompt text");

        ProgressChanger changer = new ProgressChanger();
        DoubleProperty doubleProperty = changer.getDoubleProperty();

        DatePicker datePicker = new DatePicker();
        ColorPicker colorPicker = new ColorPicker();
        ProgressBar progressBar = new ProgressBar();
        progressBar.progressProperty().bind(doubleProperty);
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.progressProperty().bind(doubleProperty);

        contentBox.getChildren().addAll(popupLabel, spinner, datePicker, colorPicker, progressBar, progressIndicator);
        mainBox.getChildren().addAll(loadHeader(), contentBox);
        popup.setAutoHide(true);
        popup.getContent().addAll(mainBox);
        popup.show(pane.getScene().getWindow());

        changer.start();
    }

    public void onChoiceClick() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>();
        dialog.getItems().addAll(getOptions());
        ComboBox<String> comboBox = (ComboBox<String>) dialog.getDialogPane().lookup(".combo-box");
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String object) {
                if (object != null)
                    return object.split("\\W")[0];
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });
        dialog.setHeaderText("Choose captured window.");
        dialog.setTitle("Capturing window?");
        dialog.setContentText("Choose the window to be captured:");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(pane.getScene().getWindow());
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.getDialogPane().setHeader(loadHeader());
        ResizeHelper.addResizeListener((Stage) dialog.getDialogPane().getScene().getWindow());

        Optional<String> choice = dialog.showAndWait();
        selected = choice.orElse("desktop");
        System.out.println(selected);
    }

    public void onPrintScreenClick() throws Exception {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        FrameTimer frameTimer = new FrameTimer(imageView, selected);

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setResizable(true);
        alert.getButtonTypes().add(ButtonType.CANCEL);
        alert.getDialogPane().setContent(imageView);
        alert.getDialogPane().autosize();
        alert.setOnCloseRequest(event -> frameTimer.stop());
        alert.show();
        frameTimer.start();
    }

    private Parent loadFxml(String url) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(url), ResourceBundle.getBundle(Main.BUNDLE_NAME));
            System.out.println(fxmlLoader.getResources().keySet().iterator().next());
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return new HBox();
        }
    }

    private Parent loadHeader() {
        return loadFxml("/fxml/Header.fxml");
    }

    private void createContextMenu() {
        Menu subMenu = new Menu("Other");
        MenuItem selectAllItem = new MenuItem("Select all");
        MenuItem searchItem = new MenuItem("Search");
        MenuItem replaceMenu = new MenuItem("Replace");
        subMenu.getItems().addAll(searchItem, replaceMenu, selectAllItem);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem copyItem = new MenuItem("Copy");
        copyItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));
        MenuItem addItem = new MenuItem("Add");
        addItem.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
        contextMenu.getItems().addAll(addItem, copyItem, deleteItem, subMenu);
        tableView.setContextMenu(contextMenu);

        copyItem.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
        deleteItem.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());

        selectAllItem.setOnAction(actionEvent -> {
            tableView.getSelectionModel().selectAll();
        });

        copyItem.setOnAction(onCopy());
        deleteItem.setOnAction(onDelete());
        addItem.setOnAction(onAdd());
    }

    private EventHandler<ActionEvent> onCopy() {
        return event -> {
            ClipboardContent clipboardContent = new ClipboardContent();
            ObservableList cells = tableView.getSelectionModel().getSelectedCells();
            StringBuilder copyStrings = new StringBuilder();
            for (TablePosition cell : (ObservableList<TablePosition>) cells) {
                copyStrings.append(cell.getTableColumn().getCellData(cell.getRow())).append(System.lineSeparator());
            }
            clipboardContent.putString(copyStrings.toString());
            Clipboard.getSystemClipboard().setContent(clipboardContent);
        };
    }

    private EventHandler<ActionEvent> onDelete() {
        return event -> {
            ObservableList selected = tableView.getSelectionModel().getSelectedItems();
            tableView.getItems().removeAll(selected);
        };
    }

    private EventHandler<ActionEvent> onAdd() {
        return event -> {
            Map.Entry<String, String> newElement = createAddDialog();
            if (newElement != null && !newElement.getKey().isEmpty() && !newElement.getValue().isEmpty())
                tableView.getItems().add(newElement);
        };
    }

    private Map.Entry<String, String> createAddDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddItemDialog.fxml"), ResourceBundle.getBundle(Main.BUNDLE_NAME));
            GridPane gridPane = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setAlwaysOnTop(true);
            stage.setTitle("Add Dialog");
            stage.initOwner(Main.getStage());
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(gridPane);
            stage.setScene(scene);
            stage.showAndWait();
            AddItemDialogController addItemDialogController = fxmlLoader.getController();
            return addItemDialogController.getReturn();
        } catch (IOException e) {
            e.printStackTrace();
            return new AbstractMap.SimpleEntry("", "");
        }
    }
}
