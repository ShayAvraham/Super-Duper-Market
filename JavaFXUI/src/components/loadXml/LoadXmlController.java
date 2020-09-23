package components.loadXml;
import common.Utilities;
import components.main.MainAppController;
import engineLogic.SystemManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import tasks.LoadXmlTask;


public class LoadXmlController
{
    private final String LOAD_FILE_FAILURE_MESSAGE = "Failed to load the file, cause of failure:\n";


    private MainAppController mainAppController;
    private SystemManager systemManager;
    private LoadXmlTask loadXmlTask;

    private SimpleStringProperty selectedFileProperty;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label statusMessageLabel;

    @FXML
    private Label progressPercentLabel;

    @FXML
    private Button openFileButton;

    @FXML
    private ProgressBar loadProgressBar;

    @FXML
    private Label filePathLabel;

    public LoadXmlController()
    {
        selectedFileProperty = new SimpleStringProperty();
    }

    public AnchorPane getRootPane()
    {
        return rootPane;
    }

    public void setMainController(MainAppController mainAppController)
    {
        this.mainAppController = mainAppController;
    }

    public void setSystemLogic(SystemManager systemManager)
    {
        this.systemManager = systemManager;
    }

    @FXML
    void initialize()
    {
        filePathLabel.textProperty().bind(selectedFileProperty);
    }

    @FXML
    void loadXmlFile(ActionEvent event)
    {
        try
        {
            File file = getFileFromUser();
            if (file != null)
            {
                startLoadXml(file);
            }
        }
        catch (Exception e)
        {
          Utilities.ShowErrorAlert(LOAD_FILE_FAILURE_MESSAGE + e.getMessage());
        }
    }

    private void resetControllers()
    {
        statusMessageLabel.setText("");
        progressPercentLabel.setText("");
        loadProgressBar.setProgress(0);
    }

    private File getFileFromUser()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        File file = fileChooser.showOpenDialog(new Stage());
        if(file != null)
        {
            resetControllers();
            selectedFileProperty.set(file.getAbsolutePath());
        }
        return file;
    }

    private void startLoadXml(File file)
    {
        loadXmlTask = new LoadXmlTask(systemManager,file,() -> mainAppController.setIsFileLoadedProperty(true));
        Thread loadXmlThread = new Thread(loadXmlTask);
        loadXmlThread.setName("loadXmlThread!!");
        bindTaskToUIComponents();
        loadXmlThread.start();
    }

    private void bindTaskToUIComponents()
    {
        statusMessageLabel.textProperty().bind(loadXmlTask.messageProperty());
        loadProgressBar.progressProperty().bind(loadXmlTask.progressProperty());
        progressPercentLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        loadXmlTask.progressProperty(),
                                        100)),
                        " %"));

        loadXmlTask.valueProperty().addListener((observable, oldValue, newValue) -> onTaskFinished());
    }

    public void onTaskFinished()
    {
        statusMessageLabel.textProperty().unbind();
        progressPercentLabel.textProperty().unbind();
        loadProgressBar.progressProperty().unbind();
    }



}
