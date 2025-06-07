package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Sheet;
import service.SheetCrawler;
import util.ClipboardUtil;
import util.LoadingUtil;

import java.io.File;


public class MainPageController {


    @FXML
    private TextField tfSheetSelector;

    @FXML
    private TextField tfDeliveryInfo;

    @FXML
    private Button btnGenerate;

    @FXML
    private Button btnCopy;

    @FXML
    private TextArea taResult;

    @FXML
    private AnchorPane apMain;

    private FileChooser.ExtensionFilter extensionFilter;

    private File file;

    @FXML
    public void initialize() {
        this.extensionFilter = new FileChooser.ExtensionFilter("Arquivos Excel", "*.xls", "*.xlsx");
    }

    @FXML
    public void onClickSheetSelector (MouseEvent event) {
        openFileSelector();
    }

    @FXML
    public void OnGenerateButtonClicked (MouseEvent event) {
        LoadingUtil.runWithLoading(apMain, () -> {
            SheetCrawler.getInstance().initializeCrawler(file);
            SheetCrawler.getInstance().setDelivery(tfDeliveryInfo.getText());
            SheetCrawler.getInstance().generateTextResult();
        }, () -> {
            taResult.setText(SheetCrawler.getInstance().getResultText().toString());
        });
    }

    @FXML
    public void OnCopyeButtonClicked (MouseEvent event) {
       ClipboardUtil.copyToClipboard(taResult.getText());
    }

    private void openFileSelector() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolha um arquivo");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File fileOpened = fileChooser.showOpenDialog(tfSheetSelector.getScene().getWindow());

        if (fileOpened != null) {
            file = fileOpened;
            tfSheetSelector.setText(file.getName());
            btnGenerate.setDisable(false);
        }
    }

}
