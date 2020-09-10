package components.showAllStores;

import components.main.MainAppController;
import dataContainers.*;
import engineLogic.SystemManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.Date;
import java.util.stream.Collectors;

public class ShowAllStoresController
{
    private MainAppController mainAppController;
    private SystemManager systemLogic;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Tab storesTab;

    @FXML
    private TableView<StoreDataContainer> storesView;

    @FXML
    private TableColumn<StoreDataContainer, Integer> storeId;

    @FXML
    private TableColumn<StoreDataContainer, String> storeName;

    @FXML
    private TableColumn<StoreDataContainer, Float> ppk;

    @FXML
    private TableColumn<StoreDataContainer, Float> totalDeliveriesCost;

    @FXML
    private Tab productsTab;

    @FXML
    private TableView<ProductDataContainer> storeProductsView;

    @FXML
    private TableColumn<ProductDataContainer, Integer> productId;

    @FXML
    private TableColumn<ProductDataContainer, String> productName;

    @FXML
    private TableColumn<ProductDataContainer, String> purchaseForm;

    @FXML
    private TableColumn<ProductDataContainer, Integer> price;

    @FXML
    private TableColumn<ProductDataContainer, Float> howMuchSoldInStore;

    @FXML
    private Tab ordersTab;

    @FXML
    private TableView<OrderDataContainer> storeOrdersView;

    @FXML
    private TableColumn<OrderDataContainer, Date> date;

    @FXML
    private TableColumn<OrderDataContainer, Integer> totalNumOfProducts;

    @FXML
    private TableColumn<OrderDataContainer, Float> totalProductsCost;

    @FXML
    private TableColumn<OrderDataContainer, Float> deliveryCost;

    @FXML
    private TableColumn<OrderDataContainer, Float> totalOrderCost;

    @FXML
    private Tab discountsTab;

    @FXML
    private TableView<DiscountDataContainer> storeDiscountsView;

    @FXML
    private TableColumn<DiscountDataContainer, String> discountName;

    @FXML
    private TableColumn<DiscountDataContainer, String> ifYouBuyDescription;

    @FXML
    private TableColumn<DiscountDataContainer, String> thenYouGetDescription;

    @FXML
    private void initialize()
    {
        setStoresTableColumnsProperties();
    }

    public void updateStoresTable()
    {
        if (systemLogic.getAllStoresData().size() != 0)
        {
            if (storesView.getSelectionModel().getSelectedItems() == null)
            {
                storesView.getSelectionModel().select(0);
            }
            ObservableList<StoreDataContainer> selectedStore = storesView.getSelectionModel().getSelectedItems();
            storesView.setOnMouseClicked(event -> updateSelectedStoreDataTables(selectedStore.get(0)));
            ObservableList<StoreDataContainer> storesList = FXCollections.observableArrayList();
            storesList.addAll(systemLogic.getAllStoresData());
            storesView.setItems(storesList);
        }
        else
        {
            storesView.setPlaceholder(new Label("No stores to display"));
        }
    }

    private void updateSelectedStoreDataTables(StoreDataContainer selectedStore)
    {
        updateStoreProductsTable(selectedStore);
        updateStoreOrdersTable(selectedStore);
        updateStoreDiscountsTable(selectedStore);
    }

    private void updateStoreProductsTable(StoreDataContainer selectedStore)
    {
        if (selectedStore.getProducts().size() != 0)
        {
            setProductsTableColumnsProperties(selectedStore);
            ObservableList<ProductDataContainer> storeProductsList = FXCollections.observableArrayList();
            storeProductsList.addAll(selectedStore.getProducts());
            storeProductsView.setItems(storeProductsList);
        }
        else
        {
            storeProductsView.setPlaceholder(new Label("No products to display"));
        }
    }

    private void updateStoreOrdersTable(StoreDataContainer selectedStore)
    {
        if (selectedStore.getOrders().size() != 0)
        {
            setOrdersTableColumnsProperties();
            ObservableList<OrderDataContainer> storeOrdersList = FXCollections.observableArrayList();
            storeOrdersList.addAll(selectedStore.getOrders());
            storeOrdersView.setItems(storeOrdersList);
        }
        else
        {
            storeOrdersView.setPlaceholder(new Label("No orders to display"));
        }
    }

    private void updateStoreDiscountsTable(StoreDataContainer selectedStore)
    {
        if (selectedStore.getDiscounts().size() != 0)
        {
            setDiscountsTableColumnsProperties();
            ObservableList<DiscountDataContainer> storeDiscountsList = FXCollections.observableArrayList();
            storeDiscountsList.addAll(selectedStore.getDiscounts());
            storeDiscountsView.setItems(storeDiscountsList);
        }
        else
        {
            storeDiscountsView.setPlaceholder(new Label("No discounts to display"));
        }
    }

    private void setStoresTableColumnsProperties()
    {
        storeId.setCellValueFactory(new PropertyValueFactory<>("id"));
        storeName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ppk.setCellValueFactory(new PropertyValueFactory<>("ppk"));
        totalDeliveriesCost.setCellValueFactory(new PropertyValueFactory<>("totalIncomeFromDeliveries"));
    }

    private void setProductsTableColumnsProperties(StoreDataContainer selectedStore)
    {
        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        purchaseForm.setCellValueFactory(new PropertyValueFactory<>("purchaseForm"));
        price.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPricePerStore().get(selectedStore.getId())));
        howMuchSoldInStore.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSoldAmountPerStore().get(selectedStore.getId())));
    }

    private void setOrdersTableColumnsProperties()
    {
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        totalNumOfProducts.setCellValueFactory(new PropertyValueFactory<>("numOfProducts"));
        totalProductsCost.setCellValueFactory(new PropertyValueFactory<>("costOfAllProducts"));
        deliveryCost.setCellValueFactory(new PropertyValueFactory<>("deliveryCost"));
        totalOrderCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
    }

    private void setDiscountsTableColumnsProperties()
    {
        discountName.setCellValueFactory(new PropertyValueFactory<>("discountName"));
        ifYouBuyDescription.setCellValueFactory(new PropertyValueFactory<>("ifYouBuyDescription"));
        thenYouGetDescription.setCellValueFactory(new PropertyValueFactory<>("thenYouGetDescription"));
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