package gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by donovandesmedt on 23/04/16.
 */
public class PasswordDialog {
    private String password = "";
    private String passwordRepeat = "";
    private String uitvoer;
    private boolean flag;
    public String password(String title){
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Password");
        dialog.setHeaderText(title);

// Set the icon (must be included in the project).
        dialog.setGraphic(new ImageView(new Image("images/lock.png")));

// Set the button types.
        ButtonType nieuwButtonType = new ButtonType("Enter", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(nieuwButtonType, ButtonType.CANCEL);

        String css = this.getClass().getResource("/styleSheet/dialog.css").toExternalForm();
        dialog.getDialogPane().getStylesheets().add(css);

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 100, 10, 10));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        PasswordField passwordRepeatField = new PasswordField();
        passwordRepeatField.setPromptText("Repeat Password");

        grid.add(new Label("Password:"), 0, 0);
        grid.add(passwordField, 1, 0);
        grid.add(new Label("Reenter password:"), 0, 1);
        grid.add(passwordRepeatField, 1, 1);

        Label lblErrorPasswordRepeat = new Label("Passwords don't match");
        lblErrorPasswordRepeat.getStyleClass().add("label-error");
        lblErrorPasswordRepeat.setVisible(false);
        grid.add(lblErrorPasswordRepeat, 1, 3);

// Enable/Disable login button depending on whether a username was entered.
        Node nieuwButton = dialog.getDialogPane().lookupButton(nieuwButtonType);
        nieuwButton.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            password = newValue.trim();
            flag = !password.isEmpty() && password.equals(passwordRepeat);
            if(!password.equals(passwordRepeat)) {
                lblErrorPasswordRepeat.setVisible(true);
            }
            else{
                lblErrorPasswordRepeat.setVisible(false);

            }
            nieuwButton.setDisable(!flag);
        });
        passwordRepeatField.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordRepeat = newValue.trim();
            flag = !passwordRepeat.isEmpty() && password.equals(passwordRepeat);
            if(!password.equals(passwordRepeat)) {
                lblErrorPasswordRepeat.setVisible(true);
            }
            else{
                lblErrorPasswordRepeat.setVisible(false);

            }
            nieuwButton.setDisable(!flag);
        });
        dialog.getDialogPane().setContent(grid);


// Request focus on the username field by default.
        Platform.runLater(() -> passwordField.requestFocus());

// Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == nieuwButtonType) {
                return Arrays.asList(passwordField.getText(), passwordRepeatField.getText());
            }
            return null;
        });
        Optional<List<String>> result = dialog.showAndWait();

        result.ifPresent(gegevens -> {
            uitvoer =  gegevens.get(0);
        });
        return uitvoer;
    }
}
