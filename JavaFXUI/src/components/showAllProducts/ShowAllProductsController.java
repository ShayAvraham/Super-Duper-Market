package components.showAllProducts;

import components.main.MainAppController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class ShowAllProductsController {
    private MainAppController mainAppController;

    @FXML
    private AnchorPane rootPane;

    public AnchorPane getRootPane() {
        return rootPane;
    }

    public void setMainController(MainAppController mainAppController) {
        this.mainAppController = mainAppController;
    }
}
