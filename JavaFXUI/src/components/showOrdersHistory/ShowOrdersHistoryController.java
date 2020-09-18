package components.showOrdersHistory;

import common.Utilities;
import components.main.MainAppController;
import components.orderDetails.OrderDetailsController;
import dataContainers.CustomerDataContainer;
import dataContainers.OrderDataContainer;
import engineLogic.SystemManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
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

    @FXML
    private Button displayOrderDetailsButton;

    public ShowOrdersHistoryController()
    {
        ordersProperty = new SimpleListProperty<>();
        selectedOrderProperty = new SimpleObjectProperty<>();
    }

    @FXML
    private void initialize()
    {
        selectOrderComboBox.itemsProperty().bind(ordersProperty);
        selectOrderComboBox.setConverter(Utilities.getOrderConverterInShowOrdersHistory());
        selectedOrderProperty.bind(selectOrderComboBox.selectionModelProperty().getValue().selectedItemProperty());
        displayOrderDetailsButton.disableProperty().bind(selectedOrderProperty.isNull());
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

    public void setOrderDetailsController(OrderDetailsController orderDetailsController)
    {
        this.orderDetailsController = orderDetailsController;
    }

    public void updateOrders()
    {
        displayOrderDetailsPane.setVisible(false);
        selectOrderComboBox.getSelectionModel().clearSelection();
        ordersProperty.setValue(systemLogic.getAllOrdersData()
                .stream()
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                FXCollections::observableArrayList)));
    }

    @FXML
    void OnDisplayOrderButtonPressed(ActionEvent event)
    {
        displayOrderDetailsPane.getChildren().clear();
        displayOrderDetailsPane.setVisible(true);
        orderDetailsController.setOrderDetails(selectedOrderProperty.getValue());
        displayOrderDetailsPane.getChildren().add(orderDetailsController.getRootPane());
    }
}
