package components.placeOrder;
import components.main.MainAppController;
import engineLogic.SystemManager;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class PlaceOrderController
{
    private MainAppController mainAppController;

    @FXML
    private AnchorPane rootPane;
    private SystemManager systemLogic;

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
        this.systemLogic = systemManager;
    }
}