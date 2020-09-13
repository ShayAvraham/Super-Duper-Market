package components.showAllStores;

import components.main.MainAppController;
import dataContainers.*;
import engineLogic.Discount;
import engineLogic.SystemManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
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

import javax.swing.*;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class ShowAllStoresController
{
    private MainAppController mainAppController;
    private SystemManager systemLogic;

    private SimpleListProperty<StoreDataContainer> storesProperty;
    private SimpleListProperty<ProductDataContainer> productsProperty;
    private SimpleListProperty<OrderDataContainer> ordersProperty;
    private SimpleListProperty<DiscountDataContainer> discountsProperty;

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

    public ShowAllStoresController()
    {
        storesProperty = new SimpleListProperty<>();
        productsProperty = new SimpleListProperty<>();
        ordersProperty = new SimpleListProperty<>();
        discountsProperty = new SimpleListProperty<>();
    }

    @FXML
    private void initialize()
    {
        storesView.itemsProperty().bind(storesProperty);
        storeProductsView.itemsProperty().bind(productsProperty);
        storeOrdersView.itemsProperty().bind(ordersProperty);
        storeDiscountsView.itemsProperty().bind(discountsProperty);
        productsTab.disableProperty().bind(storesView.getSelectionModel().selectedItemProperty().isNull());
        ordersTab.disableProperty().bind(storesView.getSelectionModel().selectedItemProperty().isNull());
        discountsTab.disableProperty().bind(storesView.getSelectionModel().selectedItemProperty().isNull());
        setStoresTableColumnsProperties();
    }

    public void updateStoresTable()
    {
        storesView.refresh();
        mainTabPane.getSelectionModel().select(storesTab);
        storesView.getSelectionModel().clearSelection();
        if (systemLogic.getAllStoresData().size() != 0)
        {
            storesProperty.setValue(loadStores());
            storesView.setOnMouseClicked(event -> updateSelectedStoreDataTables(storesView.getSelectionModel().getSelectedItem()));
        }
        else
        {
            storesView.setPlaceholder(new Label("No stores to display"));
        }
    }

    private void updateSelectedStoreDataTables(StoreDataContainer selectedStore)
    {
        if (selectedStore != null)
        {
            updateStoreProductsTable(selectedStore);
            updateStoreOrdersTable(selectedStore);
            updateStoreDiscountsTable(selectedStore);
        }
    }

    private void updateStoreProductsTable(StoreDataContainer selectedStore)
    {
        if (selectedStore.getProducts().size() != 0)
        {
            setProductsTableColumnsProperties(selectedStore);
            productsProperty.setValue(loadStoreProducts(selectedStore));
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
            ordersProperty.setValue(loadStoreOrders(selectedStore));
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
            discountsProperty.setValue(loadStoreDiscounts(selectedStore));
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

    private ObservableList<StoreDataContainer> loadStores()
    {
        return systemLogic.getAllStoresData().stream()
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                FXCollections::observableArrayList));
    }

    private ObservableList<ProductDataContainer> loadStoreProducts(StoreDataContainer selectedStore)
    {
        return systemLogic.getAllStoresData().stream()
                .filter(s -> {
                    return s.equals(selectedStore);
                })
                .map(StoreDataContainer::getProducts)
                .flatMap(Collection<ProductDataContainer>::stream)
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                FXCollections::observableArrayList));
    }

    private ObservableList<OrderDataContainer> loadStoreOrders(StoreDataContainer selectedStore)
    {
        return systemLogic.getAllStoresData().stream()
                .filter(s -> {
                    return s.equals(selectedStore);
                })
                .map(StoreDataContainer::getOrders)
                .flatMap(Collection<OrderDataContainer>::stream)
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                FXCollections::observableArrayList));
    }

    private ObservableList<DiscountDataContainer> loadStoreDiscounts(StoreDataContainer selectedStore)
    {
        return systemLogic.getAllStoresData().stream()
                .filter(s -> {
                    return s.equals(selectedStore);
                })
                .map(StoreDataContainer::getDiscounts)
                .flatMap(Collection<DiscountDataContainer>::stream)
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                FXCollections::observableArrayList));
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