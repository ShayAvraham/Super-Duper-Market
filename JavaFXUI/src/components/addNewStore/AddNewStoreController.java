package components.addNewStore;

import components.main.MainAppController;
import engineLogic.SystemManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AddNewStoreController
{

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Spinner<?> storeIDSpinner;

    @FXML
    private TextField storeNameTextField;

    @FXML
    private Spinner<?> storeXPosSpinner;

    @FXML
    private Spinner<?> storeYPosSpinner;

    @FXML
    private Spinner<?> storePPKSpinner;

    @FXML
    private TableView<?> productsTableView;

    @FXML
    private TableColumn<?, ?> selectProductColumn;

    @FXML
    private TableColumn<?, ?> productIDColumn;

    @FXML
    private TableColumn<?, ?> productNameColumn;

    @FXML
    private TableColumn<?, ?> productPurchaseFormColumn;

    @FXML
    private TableColumn<?, ?> selectProductPriceColumn;

    @FXML
    private Button submitButton;

    private MainAppController mainAppController;
    private SystemManager systemManager;

    public AnchorPane getRootPane() {
        return rootPane;
    }

    @FXML
    void OnStoreSubmit(ActionEvent event)
    {

    }

    public void LoadDataToController()
    {
    }

    public void setMainController(MainAppController mainAppController)
    {
        this.mainAppController = mainAppController;
    }

    public void setSystemLogic(SystemManager systemManager)
    {
        this.systemManager = systemManager;
    }
}

