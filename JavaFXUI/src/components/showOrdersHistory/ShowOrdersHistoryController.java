package components.showOrdersHistory;

import common.Utilities;
import components.main.MainAppController;
import components.orderDetails.OrderDetailsController;
import dataContainers.CustomerDataContainer;
import dataContainers.OrderDataContainer;
import engineLogic.SystemManager;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.Date;
import java.util.stream.Collectors;

public class ShowOrdersHistoryController
{
    private MainAppController mainAppController;
    private OrderDetailsController orderDetailsController;
    private SystemManager systemLogic;
    private SimpleListProperty<OrderDataContainer> ordersProperty;
    private SimpleObjectProperty<OrderDataContainer> selectedOrderProperty;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private AnchorPane selectOrderPane;

    @FXML
    private Label selectOrderLabel;

    @FXML
    private ComboBox<OrderDataContainer> selectOrderComboBox;

    @FXML
    private AnchorPane displayOrderDetailsPane;

    public ShowOrdersHistoryController()
    {
        ordersProperty = new SimpleListProperty<>();
        selectedOrderProperty = new SimpleObjectProperty<>();
        orderDetailsController = new OrderDetailsController();
    }

    @FXML
    private void initialize()
    {
        selectOrderComboBox.itemsProperty().bind(ordersProperty);
        selectOrderComboBox.setConverter(Utilities.getOrderConverterInShowOrdersHistory());
        selectedOrderProperty.bind(selectOrderComboBox.selectionModelProperty().getValue().selectedItemProperty());
        displayOrderDetailsPane.getChildren().add(orderDetailsController.getRootPane());
    }

    public void updateOrders()
    {
        ordersProperty.setValue(systemLogic.getAllOrdersData()
                .stream()
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                FXCollections::observableArrayList)));
    }

    @FXML
    void OnSelectedOrder(ActionEvent event)
    {
        orderDetailsController.setOrderDetails(selectedOrderProperty.getValue());
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
