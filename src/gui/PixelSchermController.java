/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import domein.Observer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author donovandesmedt
 */
public class PixelSchermController extends GridPane implements Observer {

    @FXML
    private Label lblLoadDecrypted;
    @FXML
    private Label lblExtensionDecrypt;
    @FXML
    private Label lblFileNameDecrypt;
    @FXML
    private Label lblExtensionEncrypt;
    @FXML
    private Label lblSizeDecrypt;
    @FXML
    private Label lblSizeEncrypt;
    @FXML
    private Label lblFileNameEncrypt;
    @FXML
    private Label lblLoadEncrypted;
    @FXML
    private Button btnTextLocation;
    @FXML
    private Button btnImageLocation;
    @FXML
    private Label lblMessage;
    @FXML
    private Button btnEncrypt;
    @FXML
    private Button btnDecrypt;
    @FXML
    private Button btnQuit;
    private Button btnSave;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private ImageView ImgEncrypt;
    @FXML
    private ImageView ImgDecrypt;

    private DomeinController dc;
    private String path;
    private final String TXT = ".txt";
    private final String IMG = ".png";
    private final String DOCX = ".docx";
    private String usedExt;
    private String key;
    private long startTime;

    public PixelSchermController() {
        FXMLLoader load = new FXMLLoader();
        load.setLocation(getClass().getResource("PixelScherm.fxml"));
        load.setRoot(this);
        load.setController(this);
        try {
            load.load();
        } catch (IOException ex) {
            throw new RuntimeException();
        }
        this.dc = new DomeinController();
        dc.addObserver(this);

        btnDecrypt.setDisable(true);
        btnEncrypt.setDisable(true);
        ImgDecrypt.setImage(new Image("images/png.png"));
        ImgEncrypt.setImage(new Image("images/text.png"));
        ImgEncrypt.setOpacity(0.3);
        ImgDecrypt.setOpacity(0.3);
        progressBar.setVisible(false);
        progressIndicator.setVisible(false);
    }

    private String fileChooser(String ext) {
        Stage stage = (Stage) this.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter;
        if (ext.equals(".txt")) {
            extFilter = new FileChooser.ExtensionFilter("txt, docx files (*.txt, *.docx)", "*.txt", "*.docx");
        } else {
            extFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        }
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            path = file.getPath();
            usedExt = path.substring(path.lastIndexOf("."));
        }
        return path;
    }

    @FXML
    private void btnTextLocationOnAction(ActionEvent event) {
        fileChooser(TXT);
        if (path != null) {

            btnEncrypt.setDisable(false);
            ImgEncrypt.setOpacity(1);
            lblMessage.setText("");
        }
        updateFileInfo("encrypt");
        btnEncrypt.requestFocus();
    }

    @FXML
    private void btnImageLocationOnAction(ActionEvent event) {
        fileChooser(IMG);
        if (path != null) {

            btnDecrypt.setDisable(false);
            ImgDecrypt.setOpacity(1);
            lblMessage.setText("");
        }
        updateFileInfo("decrypt");
        btnDecrypt.requestFocus();
    }

    @FXML
    private void btnEncryptOnAction(ActionEvent event) {
        /**
         * Er wordt een onderscheidt gemaakt in de encryptemethode op basis van
         * de gebruikte extensie
         * De encryptiemethode wordt opgeroepen en met de key die we terugkrijgen
         * wordt de message getoond.
         */
        progressBar.setVisible(false);
        progressIndicator.setVisible(false);

        String password = new PasswordDialog().password("Insert new password");
        if (password != null && !password.isEmpty()) {
            if (usedExt.equals(".docx")) {
                dc.encrypt(path, ".docx", password);
            } else {
                dc.encrypt(path, ".txt", password);
            }
        }

    }

    @FXML
    private void btnDecryptOnAction(ActionEvent event) {
        String password = passwordInput();
        if (password != null && !password.isEmpty()) {
            startTime = System.currentTimeMillis();
            progressBar.setVisible(true);
            progressIndicator.setVisible(true);
            dc.decrypt(path, password);
            btnDecrypt.setDisable(true);
        }

    }

    @FXML
    private void btnQuitOnAction(ActionEvent event) {
        System.exit(0);
    }

    public void setProgressbar(Double amount) {
        progressIndicator.setProgress(amount);
        progressBar.setProgress(amount);

    }

    public void decrypted(double time, int amountOfWords) {
        Platform.runLater(new Runnable() {
            public void run() {
                long endTime = System.currentTimeMillis();

                System.out.println(startTime);
                System.out.println(endTime);
                long duration = (endTime - startTime);
                if (duration > 1000) {
                    duration = TimeUnit.MILLISECONDS.toSeconds(duration);
                    alert("Success", "File was successfully decrypted", "Time: " + duration + " seconds \nWords: " + amountOfWords);
                } else {
                    alert("Success", "File was successfully decrypted", "Time: " + duration + " milliseconds \nWords: " + amountOfWords);
                }
            }
        });
    }

    public void encrypted(String key) {
        Platform.runLater(new Runnable() {
            public void run() {
                alert("Confirmation of encryption", "The text-file was successful encrypted to an image file.", key);

                path = null;
                btnEncrypt.setDisable(true);
                btnImageLocation.requestFocus();
            }
        });
    }

    private void inputDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Encryptionkey");
        dialog.setHeaderText("Encryptionkey is needed to decrypt image ");
        dialog.setContentText("Please enter your key:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            key = result.get();
        }
        if (key == null || !key.matches("\\d{1,2}[x-zX-Z]\\d{1,2}")) {
            inputDialog();
        }
    }

    private void alert2(String titel, String header) {
        String openPath = "";
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(titel);
        alert.setHeaderText(header);

        Optional<ButtonType> result = alert.showAndWait();
    }

    private void alert(String titel, String header, String contextText) {
        String openPath = "";
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(titel);
        alert.setHeaderText(header);
        alert.setContentText(contextText);


        ButtonType View = new ButtonType("View file");
        ButtonType Cancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(View, Cancel);

        Optional<ButtonType> result = alert.showAndWait();

        for (int i = path.length() - 1; i > 1; i--) {
            if (path.charAt(i) == '/') {
                openPath = path.substring(0, i);
                break;
            }
        }

        if (result.get() == View) {
            Desktop desktop = Desktop.getDesktop();
            File file = new File(openPath);
            try {
                desktop.open(file);
            } catch (IOException ex) {
                throw new RuntimeException();
            }
        }
    }

    private String passwordInput() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setGraphic(new ImageView(this.getClass().getResource("/images/lock.png").toString()));
        String css = this.getClass().getResource("/styleSheet/dialog.css").toExternalForm();
        dialog.getDialogPane().getStylesheets().add(css);

        List<ButtonType> list = dialog.getDialogPane().getButtonTypes();
        Node loginButton = dialog.getDialogPane().lookupButton(list.get(0));
        loginButton.setDisable(true);


        dialog.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                loginButton.setDisable(false);
            } else {
                loginButton.setDisable(true);
            }
        });

        dialog.setTitle("Enter password");
        dialog.setHeaderText("Enter password to decrypt file");
        dialog.setContentText("Please enter your password:");

        Optional<String> result = dialog.showAndWait();
        return result.isPresent() ? result.get() : null;
    }

    @Override
    public void update(Double amount) {
        progressIndicator.setProgress(amount);
        progressBar.setProgress(amount);
    }

    @Override
    public void updateEncryptedStatus(String key) {
        encrypted(key);
    }

    @Override
    public void updateDecryptedStatus(Double time, int amountWords) {
        decrypted(time, amountWords);
    }

    private void updateFileInfo(String type) {
        if (path != null && !path.isEmpty()) {


            String fileName = new File(path).getName();
            String extension = "";
            Long size;


            //extension
            int i = path.lastIndexOf('.');
            int p = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
            if (i > p) {
                extension = path.substring(i + 1);
            }

            //file size
            File file = new File(path);
            long sizeKb = file.length() / 1024;
            long sizeMb = -1;
            if (sizeKb > 1000) {
                sizeMb = sizeKb / 1024;
            }


            if (type.equals("encrypt")) {
                lblFileNameEncrypt.setText(fileName);
                lblExtensionEncrypt.setText(extension);
                if (sizeMb != -1) {
                    lblSizeEncrypt.setText(String.format("%d Mb", sizeMb));
                } else {
                    lblSizeEncrypt.setText(String.format("%d Kb", sizeKb));
                }
            } else {
                lblFileNameDecrypt.setText(fileName);
                lblExtensionDecrypt.setText(extension);
                if (sizeMb != -1) {
                    lblSizeDecrypt.setText(String.format("%d Mb", sizeMb));
                } else {
                    lblSizeDecrypt.setText(String.format("%d Kb", sizeKb));
                }
            }
        }
    }
}
