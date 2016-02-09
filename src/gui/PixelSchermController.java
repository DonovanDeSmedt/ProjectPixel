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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author donovandesmedt
 */
public class PixelSchermController extends GridPane implements Observer
{
    
    @FXML
    private TextField txfTxtFileLocation;
    @FXML
    private TextField txfImageLocation;
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

    private DomeinController dc;
    private String path;
    private final String TXT = ".txt";
    private final String IMG = ".png";
    private final String DOCX = ".docx";
    private String usedExt;
    private String key;

    public PixelSchermController()
    {
        FXMLLoader load = new FXMLLoader();
        load.setLocation(getClass().getResource("PixelScherm.fxml"));
        load.setRoot(this);
        load.setController(this);
        try
        {
            load.load();
        } catch (IOException ex)
        {
            throw new RuntimeException();
        }
        this.dc = new DomeinController();
        dc.addObserver(this);
        btnEncrypt.setDisable(true);
        btnDecrypt.setDisable(true);
    }

    private String fileChooser(String ext)
    {
        Stage stage = (Stage) this.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter;
        if (ext.equals(".txt"))
        {
            extFilter = new FileChooser.ExtensionFilter("txt, docx files (*.txt, *.docx)", "*.txt", "*.docx");
        } else
        {
            extFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        }
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);

        if (file != null)
        {
            path = file.getPath();
            usedExt = path.substring(path.lastIndexOf("."));
            /**
             * Door de list van extensions loopen en kijken of de gebruikte
             * extensie in die lijst zit.
             */
//            String extension = path.substring(path.lastIndexOf("."));
//            boolean correctExt = false;
//            for (String ext : extensions)
//            {
//                if (extension.contains(ext))
//                {
//                    correctExt = true;
//                    usedExt = extension;
//                    break;
//                }
//            }
//            /**
//             * Als de correcte extensie niet in de lijst zit wordt een
//             * foutmelding getoond.
//             */
//            if (!correctExt)
//            {
//                lblMessage.setText(String.format("Please choose a file with a %s extension", extensions.get(0).equals(".txt")?".txt or .docx":".png"));
//                path = null;
//            }
        }
        return path;
    }

    @FXML
    private void btnTextLocationOnAction(ActionEvent event)
    {
        fileChooser(TXT);
        if (path != null)
        {
            txfTxtFileLocation.setText(path);
            btnEncrypt.setDisable(false);
            lblMessage.setText("");
        }
        btnEncrypt.requestFocus();
    }

    @FXML
    private void btnImageLocationOnAction(ActionEvent event)
    {
        fileChooser(IMG);
        if (path != null)
        {
            txfImageLocation.setText(path);
            btnDecrypt.setDisable(false);
            lblMessage.setText("");
        }
        btnDecrypt.requestFocus();
    }

    @FXML
    private void btnEncryptOnAction(ActionEvent event)
    {
        /**
         * Er wordt een onderscheidt gemaakt in de encryptemethode op basis van
         * de gebruikte extensie
         * De encryptiemethode wordt opgeroepen en met de key die we terugkrijgen
         * wordt de message getoond.
         */
        if (usedExt.equals(".docx"))
        {
            dc.encrypt(path, ".docx");
        } 
        else
        {
            dc.encrypt(path, ".txt");
        }
    }

    @FXML
    private void btnDecryptOnAction(ActionEvent event)
    {
        inputDialog();
        dc.decrypt(path, key);
        txfImageLocation.setText("");
        btnDecrypt.setDisable(true);
    }

    @FXML
    private void btnQuitOnAction(ActionEvent event)
    {
        System.exit(0);
    }

    public void setProgressbar(Double amount)
    {
        progressIndicator.setProgress(amount);
        progressBar.setProgress(amount);

    }

    public void decrypted(double time, int amountOfWords)
    {
        Platform.runLater(new Runnable()
        {
            public void run()
            {
                alert("Success", "File was successfully decrypted", "Time: " + time + " sec \nWords: " + amountOfWords);
            }
        });
    }

    public void encrypted(String key)
    {
        Platform.runLater(new Runnable()
        {
            public void run()
            {
                alert("Confirmation of encryption", "The text-file was successful encrypted to an image file.", key);
                txfTxtFileLocation.setText("");
                path = null;
                btnEncrypt.setDisable(true);
                btnImageLocation.requestFocus();
            }
        });
    }

    private void inputDialog()
    {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Encryptionkey");
        dialog.setHeaderText("Encryptionkey is needed to decrypt image ");
        dialog.setContentText("Please enter your key:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
        {
            key = result.get();
        }
        if (key == null || !key.matches("\\d{1,2}[x-zX-Z]\\d{1,2}"))
        {
            alert2("Encryptionkey incorrect", "Please enter a valid key");
            inputDialog();
        }
    }

    private void alert2(String titel, String header)
    {
        String openPath = "";
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(titel);
        alert.setHeaderText(header);

        Optional<ButtonType> result = alert.showAndWait();
    }

    private void alert(String titel, String header, String contextText)
    {
        String openPath = "";
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(titel);
        alert.setHeaderText(header);
        alert.setContentText(contextText);

        ButtonType View = new ButtonType("View file");
        ButtonType Cancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(View, Cancel);

        Optional<ButtonType> result = alert.showAndWait();

        for (int i = path.length() - 1; i > 1; i--)
        {
            if (path.charAt(i) == '/')
            {
                openPath = path.substring(0, i);
                break;
            }
        }

        if (result.get() == View)
        {
            Desktop desktop = Desktop.getDesktop();
            File file = new File(openPath);
            try
            {
                desktop.open(file);
            } catch (IOException ex)
            {
                throw new RuntimeException();
            }
        }
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
}
