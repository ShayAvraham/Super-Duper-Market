package components.showOrdersHistory;

import components.main.MainAppController;
import dataContainers.CustomerDataContainer;
import dataContainers.OrderDataContainer;
import engineLogic.SystemManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.Date;

public class ShowOrdersHistoryController
{
    private MainAppController mainAppController;
    private SystemManager systemLogic;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<OrderDataContainer> ordersView;

    @FXML
    private TableColumn<OrderDataContainer, Integer> id;

    @FXML
    private TableColumn<OrderDataContainer, Date> date;

    @FXML
    private TableColumn<OrderDataContainer, Integer> storeId;

    @FXML
    private TableColumn<OrderDataContainer, String> storeName;

    @FXML
    private TableColumn<OrderDataContainer, Integer> numOfProductTypes;

    @FXML
    private TableColumn<OrderDataContainer, Integer> numOfProducts;

    @FXML
    private TableColumn<OrderDataContainer, Float> productsCost;

    @FXML
    private TableColumn<OrderDataContainer, Float> deliveryCost;

    @FXML
    private TableColumn<OrderDataContainer, Float> totalCost;

    @FXML
    private void initialize()
    {
        setOrdersHistoryTableColumnsProperties();
    }

    public void updateOrdersHistoryTable()
    {
        ObservableList<OrderDataContainer> ordersList = FXCollections.observableArrayList();
        ordersList.addAll(systemLogic.getAllOrdersData());
        ordersView.setItems(ordersList);
    }

    private void setOrdersHistoryTableColumnsProperties()
    {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        date.setCellValueFactory(new PropertyValueFactory<>("name"));
        storeId.setCellValueFactory(new PropertyValueFactory<>("storeId"));
        storeName.setCellValueFactory(new PropertyValueFactory<>("storeName"));
        numOfProductTypes.setCellValueFactory(new PropertyValueFactory<>("numOfProductTypes"));
        numOfProducts.setCellValueFactory(new PropertyValueFactory<>("numOfProducts"));
        productsCost.setCellValueFactory(new PropertyValueFactory<>("costOfAllProducts"));
        deliveryCost.setCellValueFactory(new PropertyValueFactory<>("deliveryCost"));
        totalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
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
        this.systemLogic = systemManager;
    }
}
