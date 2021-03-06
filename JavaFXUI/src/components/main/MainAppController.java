//package components.main;
//import common.Utilities;
//import components.addNewStore.AddNewStoreController;
//import components.changeSkin.ChangeSkinController;
//import components.loadXml.LoadXmlController;
//import components.orderDetails.OrderDetailsController;
////import components.placeOrder.PlaceOrderController;
//import components.showAllCustomers.ShowAllCustomersController;
//import components.showAllProducts.ShowAllProductsController;
//import components.showAllStores.ShowAllStoresController;
//import components.showMap.ShowMapController;
//import components.showOrdersHistory.ShowOrdersHistoryController;
//import components.updateProducts.UpdateProductsController;
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.AnchorPane;
//import javafx.stage.Stage;
//import java.io.IOException;
//import java.net.URL;
//import engineLogic.*;
//
//public class MainAppController
//{
////    private LoadXmlController loadXmlController;
////    private ShowAllCustomersController showAllCustomersController;
////    private ShowAllProductsController showAllProductsController;
////    private ShowAllStoresController showAllStoresController;
////    private ShowMapController showMapController;
////    private ShowOrdersHistoryController showOrdersHistoryController;
////    private UpdateProductsController updateProductsController;
////    private PlaceOrderController placeOrderController;
////    private OrderDetailsController orderDetailsController;
////    private AddNewStoreController addNewStoreController;
////    private ChangeSkinController changeSkinController;
////
////    private SystemManager systemManager;
////    private Stage stage;
////    private SimpleBooleanProperty isFileLoadedProperty;
////
////    @FXML
////    private ScrollPane mainScrollPane;
////
////    @FXML
////    private AnchorPane mainPane;
////
////    @FXML
////    private AnchorPane menuBar;
////
////    @FXML
////    private AnchorPane mainWindow;
////
////    @FXML
////    private ImageView superDuperMarketImage;
////
////    @FXML
////    private Label superDuperMarketLabel;
////
////    @FXML
////    private Label welcomeLabel;
////
////    @FXML
////    private Button loadXmlButton;
////
////    @FXML
////    private Button showAllCustomersButton;
////
////    @FXML
////    private Button showAllStoresButton;
////
////    @FXML
////    private Button showAllProductsButton;
////
////    @FXML
////    private Button showOrdersHistoryButton;
////
////    @FXML
////    private Button updateProductsButton;
////
////    @FXML
////    private Button placeOrderButton;
////
////    @FXML
////    private Button showMapButton;
////
////    @FXML
////    private Button addNewStoreButton;
////
////    @FXML
////    private Button changeSkinButton;
////
////
////    public MainAppController()
////    {
////        try
////        {
////            systemManager = new SystemManager();
////            isFileLoadedProperty = new SimpleBooleanProperty(false);
////            loadXmlController = initializeLoadXMLController(this.systemManager);
////            orderDetailsController = initializeOrderDetailsController(this.systemManager);
////            showAllCustomersController = initializeShowAllCustomersController(this.systemManager);
////            showAllProductsController = initializeShowAllProductsController(this.systemManager);
////            showAllStoresController = initializeShowAllStoresController(this.systemManager);
////            showMapController = initializeShowMapController(this.systemManager);
////            updateProductsController = initializeUpdateProductsController(this.systemManager);
////            placeOrderController = initializePlaceOrderController(this.systemManager);
////            showOrdersHistoryController = initializeShowOrdersHistoryController(this.systemManager);
////            addNewStoreController = initializeAddNewStoreController(this.systemManager);
////            changeSkinController = initializeChangeSkinController();
////        }
////        catch (Exception e)
////        {
////            Utilities.ShowErrorAlert(e.getMessage());
////        }
////    }
////
////    @FXML
////    void initialize()
////    {
////        showAllCustomersButton.disableProperty().bind(isFileLoadedProperty.not());
////        showAllStoresButton.disableProperty().bind(isFileLoadedProperty.not());
////        showAllProductsButton.disableProperty().bind(isFileLoadedProperty.not());
////        showOrdersHistoryButton.disableProperty().bind(isFileLoadedProperty.not());
////        updateProductsButton.disableProperty().bind(isFileLoadedProperty.not());
////        placeOrderButton.disableProperty().bind(isFileLoadedProperty.not());
////        showMapButton.disableProperty().bind(isFileLoadedProperty.not());
////        addNewStoreButton.disableProperty().bind(isFileLoadedProperty.not());
////        changeSkinButton.disableProperty().bind(isFileLoadedProperty.not());
////    }
////
////    public void setIsFileLoadedProperty(boolean isFileLoaded)
////    {
////        isFileLoadedProperty.set(isFileLoaded);
////    }
////
////    private LoadXmlController initializeLoadXMLController(SystemManager systemManager) throws IOException
////    {
////        FXMLLoader loader = new FXMLLoader();
////        URL mainFXML = getClass().getResource("/components/loadXml/LoadXml.fxml");
////        loader.setLocation(mainFXML);
////        loader.load();
////        LoadXmlController xmlLoadController = loader.getController();
////        xmlLoadController.setMainController(this);
////        xmlLoadController.setSystemLogic(systemManager);
////        return xmlLoadController;
////    }
////
////    private ShowAllCustomersController initializeShowAllCustomersController(SystemManager systemManager) throws IOException
////    {
////        FXMLLoader loader = new FXMLLoader();
////        URL mainFXML = getClass().getResource("/components/showAllCustomers/ShowAllCustomers.fxml");
////        loader.setLocation(mainFXML);
////        loader.load();
////        ShowAllCustomersController showAllCustomersController = loader.getController();
////        showAllCustomersController.setMainController(this);
////        showAllCustomersController.setSystemLogic(systemManager);
////        return showAllCustomersController;
////    }
////
////    private ShowAllStoresController initializeShowAllStoresController(SystemManager systemManager) throws IOException
////    {
////        FXMLLoader loader = new FXMLLoader();
////        URL mainFXML = getClass().getResource("/components/showAllStores/ShowAllStores.fxml");
////        loader.setLocation(mainFXML);
////        loader.load();
////        ShowAllStoresController showAllStoresController = loader.getController();
////        showAllStoresController.setMainController(this);
////        showAllStoresController.setSystemLogic(systemManager);
////        return showAllStoresController;
////    }
////
////    private ShowAllProductsController initializeShowAllProductsController(SystemManager systemManager) throws IOException
////    {
////        FXMLLoader loader = new FXMLLoader();
////        URL mainFXML = getClass().getResource("/components/showAllProducts/ShowAllProducts.fxml");
////        loader.setLocation(mainFXML);
////        loader.load();
////        ShowAllProductsController showAllProductsController = loader.getController();
////        showAllProductsController.setMainController(this);
////        showAllProductsController.setSystemLogic(systemManager);
////        return showAllProductsController;
////    }
////
////    private ShowOrdersHistoryController initializeShowOrdersHistoryController(SystemManager systemManager) throws IOException
////    {
////        FXMLLoader loader = new FXMLLoader();
////        URL mainFXML = getClass().getResource("/components/showOrdersHistory/ShowOrdersHistory.fxml");
////        loader.setLocation(mainFXML);
////        loader.load();
////        ShowOrdersHistoryController showOrdersHistoryController = loader.getController();
////        showOrdersHistoryController.setMainController(this);
////        showOrdersHistoryController.setSystemLogic(systemManager);
////        showOrdersHistoryController.setOrderDetailsController(orderDetailsController);
////        return showOrdersHistoryController;
////    }
////
////    private ShowMapController initializeShowMapController(SystemManager systemManager) throws IOException
////    {
////        FXMLLoader loader = new FXMLLoader();
////        URL mainFXML = getClass().getResource("/components/showMap/ShowMap.fxml");
////        loader.setLocation(mainFXML);
////        loader.load();
////        ShowMapController showMapController = loader.getController();
////        showMapController.setMainController(this);
////        showMapController.setSystemLogic(systemManager);
////        return showMapController;
////    }
////
////    private UpdateProductsController initializeUpdateProductsController(SystemManager systemManager) throws IOException
////    {
////        FXMLLoader loader = new FXMLLoader();
////        URL mainFXML = getClass().getResource("/components/updateProducts/UpdateProducts.fxml");
////        loader.setLocation(mainFXML);
////        loader.load();
////        UpdateProductsController updateProductsController = loader.getController();
////        updateProductsController.setMainController(this);
////        updateProductsController.setSystemLogic(systemManager);
////        return updateProductsController;
////    }
////
////    private PlaceOrderController initializePlaceOrderController(SystemManager systemManager) throws IOException
////    {
////        FXMLLoader loader = new FXMLLoader();
////        URL mainFXML = getClass().getResource("/components/placeOrder/PlaceOrder.fxml");
////        loader.setLocation(mainFXML);
////        loader.load();
////        PlaceOrderController placeOrderController = loader.getController();
////        placeOrderController.setMainController(this);
////        placeOrderController.setSystemLogic(systemManager);
////        placeOrderController.setOrderDetailsController(orderDetailsController);
////        return placeOrderController;
////    }
////
////    private AddNewStoreController initializeAddNewStoreController(SystemManager systemManager) throws IOException
////    {
////        FXMLLoader loader = new FXMLLoader();
////        URL mainFXML = getClass().getResource("/components/addNewStore/AddNewStore.fxml");
////        loader.setLocation(mainFXML);
////        loader.load();
////        AddNewStoreController addNewStoreController = loader.getController();
////        addNewStoreController.setMainController(this);
////        addNewStoreController.setSystemLogic(systemManager);
////        return addNewStoreController;
////    }
////
////    private ChangeSkinController initializeChangeSkinController() throws IOException
////    {
////        FXMLLoader loader = new FXMLLoader();
////        URL mainFXML = getClass().getResource("/components/changeSkin/ChangeSkin.fxml");
////        loader.setLocation(mainFXML);
////        loader.load();
////        ChangeSkinController changeSkinController = loader.getController();
////        return changeSkinController;
////    }
////
////    private OrderDetailsController initializeOrderDetailsController(SystemManager systemManager) throws IOException
////    {
////        FXMLLoader loader = new FXMLLoader();
////        URL mainFXML = getClass().getResource("/components/orderDetails/OrderDetails.fxml");
////        loader.setLocation(mainFXML);
////        loader.load();
////        OrderDetailsController orderDetailsController = loader.getController();
////        orderDetailsController.setMainController(this);
////        orderDetailsController.setSystemLogic(systemManager);
////        return orderDetailsController;
////    }
////
////    @FXML
////    void loadXmlFile(ActionEvent event)
////    {
////        mainWindow.getChildren().clear();
////        mainWindow.setBottomAnchor(loadXmlController.getRootPane(),0.);
////        mainWindow.setTopAnchor(loadXmlController.getRootPane(), 0.);
////        mainWindow.setLeftAnchor(loadXmlController.getRootPane(), 0.);
////        mainWindow.setRightAnchor(loadXmlController.getRootPane(), 0.);
////        mainWindow.getChildren().add(loadXmlController.getRootPane());
////    }
////
////    @FXML
////    void placeOrder(ActionEvent event)
////    {
////        mainWindow.getChildren().clear();
////        placeOrderController.LoadDataToControllers();
////        mainWindow.setBottomAnchor(placeOrderController.getRootPane(),0.);
////        mainWindow.setTopAnchor(placeOrderController.getRootPane(), 0.);
////        mainWindow.setLeftAnchor(placeOrderController.getRootPane(), 0.);
////        mainWindow.setRightAnchor(placeOrderController.getRootPane(), 0.);
////        mainWindow.getChildren().add(placeOrderController.getRootPane());
////    }
////
////    @FXML
////    void showAllCustomers(ActionEvent event)
////    {
////        mainWindow.getChildren().clear();
////        showAllCustomersController.updateCustomersTable();
////        mainWindow.setBottomAnchor(showAllCustomersController.getRootPane(),0.);
////        mainWindow.setTopAnchor(showAllCustomersController.getRootPane(), 0.);
////        mainWindow.setLeftAnchor(showAllCustomersController.getRootPane(), 0.);
////        mainWindow.setRightAnchor(showAllCustomersController.getRootPane(), 0.);
////        mainWindow.getChildren().add(showAllCustomersController.getRootPane());
////    }
////
////    @FXML
////    void showAllProducts(ActionEvent event)
////    {
////        mainWindow.getChildren().clear();
////        showAllProductsController.updateProductsTable();
////        mainWindow.setBottomAnchor(showAllProductsController.getRootPane(),0.);
////        mainWindow.setTopAnchor(showAllProductsController.getRootPane(), 0.);
////        mainWindow.setLeftAnchor(showAllProductsController.getRootPane(), 0.);
////        mainWindow.setRightAnchor(showAllProductsController.getRootPane(), 0.);
////        mainWindow.getChildren().add(showAllProductsController.getRootPane());
////    }
////
////    @FXML
////    void showAllStores(ActionEvent event)
////    {
////        mainWindow.getChildren().clear();
////        showAllStoresController.updateStoresTable();
////        mainWindow.setBottomAnchor(showAllStoresController.getRootPane(),0.);
////        mainWindow.setTopAnchor(showAllStoresController.getRootPane(), 0.);
////        mainWindow.setLeftAnchor(showAllStoresController.getRootPane(), 0.);
////        mainWindow.setRightAnchor(showAllStoresController.getRootPane(), 0.);
////        mainWindow.getChildren().add(showAllStoresController.getRootPane());
////    }
////
////    @FXML
////    void showMap(ActionEvent event)
////    {
////        mainWindow.getChildren().clear();
////        showMapController.updateMap();
////        mainWindow.setBottomAnchor(showMapController.getRootPane(),0.);
////        mainWindow.setTopAnchor(showMapController.getRootPane(), 0.);
////        mainWindow.setLeftAnchor(showMapController.getRootPane(), 0.);
////        mainWindow.setRightAnchor(showMapController.getRootPane(), 0.);
////        mainWindow.getChildren().add(showMapController.getRootPane());
////    }
////
////    @FXML
////    void showOrdersHistory(ActionEvent event)
////    {
////        mainWindow.getChildren().clear();
////        showOrdersHistoryController.updateOrders();
////        mainWindow.setBottomAnchor(showOrdersHistoryController.getRootPane(),0.);
////        mainWindow.setTopAnchor(showOrdersHistoryController.getRootPane(), 0.);
////        mainWindow.setLeftAnchor(showOrdersHistoryController.getRootPane(), 0.);
////        mainWindow.setRightAnchor(showOrdersHistoryController.getRootPane(), 0.);
////        mainWindow.getChildren().add(showOrdersHistoryController.getRootPane());
////    }
////
////    @FXML
////    void updateProducts(ActionEvent event)
////    {
////        mainWindow.getChildren().clear();
////        updateProductsController.LoadDataToControllers();
////        mainWindow.setBottomAnchor(updateProductsController.getRootPane(),0.);
////        mainWindow.setTopAnchor(updateProductsController.getRootPane(), 0.);
////        mainWindow.setLeftAnchor(updateProductsController.getRootPane(), 0.);
////        mainWindow.setRightAnchor(updateProductsController.getRootPane(), 0.);
////        mainWindow.getChildren().add(updateProductsController.getRootPane());
////    }
////
////    @FXML
////    void addNewStore(ActionEvent event)
////    {
////        mainWindow.getChildren().clear();
////        addNewStoreController.LoadDataToController();
////        mainWindow.setBottomAnchor(addNewStoreController.getRootPane(),0.);
////        mainWindow.setTopAnchor(addNewStoreController.getRootPane(), 0.);
////        mainWindow.setLeftAnchor(addNewStoreController.getRootPane(), 0.);
////        mainWindow.setRightAnchor(addNewStoreController.getRootPane(), 0.);
////        mainWindow.getChildren().add(addNewStoreController.getRootPane());
////    }
////
////    @FXML
////    void changeSkin(ActionEvent event)
////    {
////        mainWindow.getChildren().clear();
////        changeSkinController.setMainAppPane(this.mainScrollPane);
////        mainWindow.setTopAnchor(changeSkinController.getRootPane(), 0.);
////        mainWindow.setLeftAnchor(changeSkinController.getRootPane(), 0.);
////        mainWindow.setRightAnchor(changeSkinController.getRootPane(), 0.);
////        mainWindow.getChildren().add(changeSkinController.getRootPane());
////    }
////
////    public void setMainStage(Stage primaryStage)
////    {
////        this.stage = primaryStage;
////    }
//}
