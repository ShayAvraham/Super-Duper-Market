package components.showAllCustomers;

import components.main.MainAppController;
import dataContainers.CustomerDataContainer;
import engineLogic.SystemManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class ShowAllCustomersController
{
    private MainAppController mainAppController;
    private SystemManager systemLogic;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<CustomerDataContainer> customersView;

    @FXML
    private TableColumn<CustomerDataContainer, Integer> id;

    @FXML
    private TableColumn<CustomerDataContainer, String> name;

    @FXML
    private TableColumn<CustomerDataContainer, String> location;

    @FXML
    private TableColumn<CustomerDataContainer, Integer> numOfOrders;

    @FXML
    private TableColumn<CustomerDataContainer, Float> avgOrdersCost;

    @FXML
    private TableColumn<CustomerDataContainer, Float> avgDeliveriesCost;

    @FXML
    private void initialize()
    {
        setCustomersTableColumsProperties();
    }

    public void updateCustomersTable()
    {
        ObservableList<CustomerDataContainer> customersList = FXCollections.observableArrayList();
        customersList.addAll(systemLogic.getAllCustomersData());
        customersView.setItems(customersList);
    }

    private void setCustomersTableColumsProperties()
    {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        location.setCellValueFactory(new PropertyValueFactory<>("name"));
        numOfOrders.setCellValueFactory(new PropertyValueFactory<>("numOfOrders"));
        avgOrdersCost.setCellValueFactory(new PropertyValueFactory<>("orderCostAvg"));
        avgDeliveriesCost.setCellValueFactory(new PropertyValueFactory<>("deliveryCostAvg"));
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
