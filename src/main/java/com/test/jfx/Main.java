package com.test.jfx;

import com.test.jfx.Controller.SimpleController;
import com.test.jfx.Model.User;
import com.test.jfx.Utils.ResizeHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ResourceBundle;

public class Main extends Application {
    public static final String BUNDLE_NAME = "i18n.Localization";
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage() {
        return stage;
    }

    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Simple.fxml"), ResourceBundle.getBundle(BUNDLE_NAME));
        Parent node = fxmlLoader.load();
        SimpleController simpleController = fxmlLoader.getController();
        simpleController.initTable(new User());
        Scene scene = new Scene(node);
        scene.setOnKeyPressed(keyEvent -> {
            if (new KeyCodeCombination(KeyCode.A).match(keyEvent))
                System.out.println("A");
            else if (new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN).match(keyEvent))
                System.out.println("ALT + S");
            else if (new KeyCodeCombination(KeyCode.A, KeyCombination.SHIFT_DOWN).match(keyEvent))
                System.out.println("SHIFT + A");
            else if (new KeyCodeCombination(KeyCode.S).match(keyEvent))
                System.out.println("S");
            else if (keyEvent.isShortcutDown() && keyEvent.getCode().equals(KeyCode.B))
                System.out.println("CTRL + B");
            else
                System.out.println("Other");
        });
        primaryStage.setX(30);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        ResizeHelper.addResizeListener(primaryStage);

        primaryStage.setOnCloseRequest(windowEvent -> {
            System.out.println("Closing...");
        });

        System.out.println("Starting...");
        primaryStage.show();
    }
}
