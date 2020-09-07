package components.main;
import com.sun.xml.internal.ws.api.pipe.Engine;
import components.loadXml.LoadXmlController;
import components.placeOrder.PlaceOrderController;
import components.showAllCustomers.ShowAllCustomersController;
import components.showAllProducts.ShowAllProductsController;
import components.showAllStores.ShowAllStoresController;
import components.showMap.ShowMapController;
import components.showOrdersHistory.ShowOrdersHistoryController;
import components.updateProducts.UpdateProductsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import exceptions.*;

import java.io.IOException;
import java.net.URL;

public class MainAppController
{
    private LoadXmlController loadXmlController;
    private ShowAllCustomersController showAllCustomersController;
    private ShowAllProductsController showAllProductsController;
    private ShowAllStoresController showAllStoresController;
    private ShowMapController showMapController;
    private ShowOrdersHistoryController showOrdersHistoryController;
    private UpdateProductsController updateProductsController;
    private PlaceOrderController placeOrderController;


    private Stage stage;

    @FXML
    private AnchorPane menuBar;

    @FXML
    private AnchorPane mainWindow;

    @FXML
    private Button loadXmlButton;

    @FXML
    private Button showAllCustomersButton;

    @FXML
    private Button showAllStoresButton;

    @FXML
    private Button showAllProductsButton;

    @FXML
    private Button showOrdersHistoryButton;

    @FXML
    private Button updateProductsButton;

    @FXML
    private Button placeOrderButton;

    @FXML
    private Button showMapButton;


    public MainAppController() {
        try
        {
            this.loadXmlController = initializeLoadXMLController();
        }
        catch (Exception e)
        {

        }
    }

    private LoadXmlController initializeLoadXMLController() throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/components/loadXml/LoadXml.fxml");
        loader.setLocation(mainFXML);
        loader.load();
        LoadXmlController xmlLoadController = loader.getController();
        xmlLoadController.setMainController(this);

        return xmlLoadController;
    }


    @FXML
    void loadXmlFile(ActionEvent event)
    {
        mainWindow.getChildren().clear();
        mainWindow.getChildren().add(loadXmlController.getRootPane());
    }

    @FXML
    void placeOrder(ActionEvent event)
    {
        mainWindow.getChildren().clear();
        mainWindow.getChildren().add(placeOrderController.getRootPane());
    }

    @FXML
    void showAllCustomers(ActionEvent event)
    {
        mainWindow.getChildren().clear();
        mainWindow.getChildren().add(showAllCustomersController.getRootPane());
    }

    @FXML
    void showAllProducts(ActionEvent event)
    {
        mainWindow.getChildren().clear();
        mainWindow.getChildren().add(showAllProductsController.getRootPane());
    }

    @FXML
    void showAllStores(ActionEvent event)
    {
        mainWindow.getChildren().clear();
        mainWindow.getChildren().add(showAllStoresController.getRootPane());
    }

    @FXML
    void showMap(ActionEvent event)
    {
        mainWindow.getChildren().clear();
        mainWindow.getChildren().add(showMapController.getRootPane());
    }

    @FXML
    void showOrdersHistory(ActionEvent event)
    {
        mainWindow.getChildren().clear();
        mainWindow.getChildren().add(showOrdersHistoryController.getRootPane());
    }

    @FXML
    void updateProducts(ActionEvent event)
    {
        mainWindow.getChildren().clear();
        mainWindow.getChildren().add(updateProductsController.getRootPane());
    }

    public void setMainStage(Stage primaryStage)
    {
        this.stage = primaryStage;
    }
}
