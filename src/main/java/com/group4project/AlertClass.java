package com.group4project;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Optional;

public class AlertClass {

    public static void errorAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
    public static void confirmAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public static Optional<ButtonType> askConfirmAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText(text);
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    public static Alert progressAlert() {
        Alert alert = new Alert(Alert.AlertType.NONE);  // Initially no buttons
        alert.setTitle("Data Insertion");
        alert.setHeaderText(null);
        alert.getDialogPane().setPrefSize(400, 150);
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        Label label = new Label("Updating data...");
        label.setFont(Font.font(18));
        HBox hBox = new HBox(progressIndicator, label);
        hBox.setSpacing(15);
        hBox.setAlignment(Pos.CENTER_LEFT);
        alert.getDialogPane().setContent(hBox);

        return alert;
    }
}
