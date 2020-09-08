package components.loadXml;
import components.main.MainAppController;
import engineLogic.SystemManager;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class LoadXmlController
{
    private MainAppController mainAppController;
    private SystemManager systemManager;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label StatusMessageLabel;

    @FXML
    private Label ProgressPercentLabel;

    @FXML
    private Button OpenFileButton;

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
    void initialize() {
        assert rootPane != null : "fx:id=\"rootPane\" was not injected: check your FXML file 'LoadXml.fxml'.";
        assert StatusMessageLabel != null : "fx:id=\"StatusMessageLabel\" was not injected: check your FXML file 'LoadXml.fxml'.";
        assert ProgressPercentLabel != null : "fx:id=\"ProgressPrecentLabel\" was not injected: check your FXML file 'LoadXml.fxml'.";
        assert OpenFileButton != null : "fx:id=\"OpenFileButton\" was not injected: check your FXML file 'LoadXml.fxml'.";
    }

    @FXML
    void loadXmlFile(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null)
        {
        //    systemManager.loadDataFromXmlFile(file.getPath());
        }
    }
}
