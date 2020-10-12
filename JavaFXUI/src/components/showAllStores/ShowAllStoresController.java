package components.showAllStores;

import common.Utilities;
//import components.main.MainAppController;
//import dataContainers.DiscountDataContainer;
//import dataContainers.OrderDataContainer;
//import dataContainers.ProductDataContainer;
//import dataContainers.StoreDataContainer;
//import engineLogic.SystemManager;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class ShowAllStoresController
{
//    private MainAppController mainAppController;
//    private SystemManager systemLogic;
//
//    private SimpleListProperty<StoreDataContainer> storesProperty;
//    private SimpleListProperty<ProductDataContainer> productsProperty;
//    private SimpleListProperty<OrderDataContainer> ordersProperty;
//    private SimpleListProperty<DiscountDataContainer> discountsProperty;
//    private SimpleObjectProperty<StoreDataContainer> selectedStoreProperty;
//
//    @FXML
//    private AnchorPane rootPane;
//
//    @FXML
//    private TabPane mainTabPane;
//
//    @FXML
//    private Tab storesTab;
//
//    @FXML
//    private TableView<StoreDataContainer> storesView;
//
//    @FXML
//    private TableColumn<StoreDataContainer, Integer> storeId;
//
//    @FXML
//    private TableColumn<StoreDataContainer, String> storeName;
//
//    @FXML
//    private TableColumn<StoreDataContainer, Float> ppk;
//
//    @FXML
//    private TableColumn<StoreDataContainer, Float> totalDeliveriesCost;
//
//    @FXML
//    private Tab productsTab;
//
//    @FXML
//    private TableView<ProductDataContainer> storeProductsView;
//
//    @FXML
//    private TableColumn<ProductDataContainer, Integer> productId;
//
//    @FXML
//    private TableColumn<ProductDataContainer, String> productName;
//
//    @FXML
//    private TableColumn<ProductDataContainer, String> purchaseForm;
//
//    @FXML
//    private TableColumn<ProductDataContainer, Integer> price;
//
//    @FXML
//    private TableColumn<ProductDataContainer, Float> howMuchSoldInStore;
//
//    @FXML
//    private Tab ordersTab;
//
//    @FXML
//    private TableView<OrderDataContainer> storeOrdersView;
//
//    @FXML
//    private TableColumn<OrderDataContainer, Integer> id;
//
//    @FXML
//    private TableColumn<OrderDataContainer, Date> date;
//
//    @FXML
//    private TableColumn<OrderDataContainer, Integer> totalNumOfProducts;
//
//    @FXML
//    private TableColumn<OrderDataContainer, Float> totalProductsCost;
//
//    @FXML
//    private TableColumn<OrderDataContainer, Float> deliveryCost;
//
//    @FXML
//    private TableColumn<OrderDataContainer, Float> totalOrderCost;
//
//    @FXML
//    private Tab discountsTab;
//
//    @FXML
//    private TableView<DiscountDataContainer> storeDiscountsView;
//
//    @FXML
//    private TableColumn<DiscountDataContainer, String> discountName;
//
//    @FXML
//    private TableColumn<DiscountDataContainer, String> ifYouBuyDescription;
//
//    @FXML
//    private TableColumn<DiscountDataContainer, String> thenYouGetDescription;
//
//    public ShowAllStoresController()
//    {
//        storesProperty = new SimpleListProperty<>();
//        productsProperty = new SimpleListProperty<>();
//        ordersProperty = new SimpleListProperty<>();
//        discountsProperty = new SimpleListProperty<>();
//        selectedStoreProperty = new SimpleObjectProperty<>();
//    }
//
//    @FXML
//    private void initialize()
//    {
//        storesView.itemsProperty().bind(storesProperty);
//        storeProductsView.itemsProperty().bind(productsProperty);
//        storeOrdersView.itemsProperty().bind(ordersProperty);
//        storeDiscountsView.itemsProperty().bind(discountsProperty);
//        productsTab.disableProperty().bind(storesView.getSelectionModel().selectedItemProperty().isNull());
//        ordersTab.disableProperty().bind(storesView.getSelectionModel().selectedItemProperty().isNull());
//        discountsTab.disableProperty().bind(storesView.getSelectionModel().selectedItemProperty().isNull());
//        selectedStoreProperty.bind(storesView.selectionModelProperty().getValue().selectedItemProperty());
//        setStoresTableColumnsProperties();
//    }
//
//    public void updateStoresTable()
//    {
//        mainTabPane.getSelectionModel().select(storesTab);
//        storesView.getSelectionModel().clearSelection();
//        storesProperty.setValue(loadStores());
//        storesView.setOnMouseClicked(event -> updateSelectedStoreDataTables());
//        storesView.refresh();
//    }
//
//    private void updateSelectedStoreDataTables()
//    {
//        if(storesView.getSelectionModel().getSelectedItem() != null)
//        {
//            updateStoreProductsTable();
//            updateStoreOrdersTable();
//            updateStoreDiscountsTable();
//        }
//    }
//
//    private void updateStoreProductsTable()
//    {
//        setProductsTableColumnsProperties();
//        productsProperty.setValue(loadStoreProducts());
//        storeProductsView.refresh();
//    }
//
//    private void updateStoreOrdersTable()
//    {
//        if (selectedStoreProperty.getValue().getOrders().size() > 0)
//        {
//            setOrdersTableColumnsProperties();
//            ordersProperty.setValue(loadStoreOrders());
//            storeOrdersView.refresh();
//        }
//        else
//        {
//            ordersProperty.clear();
//            storeOrdersView.setPlaceholder(new Label("No orders to display"));
//        }
//    }
//
//    private void updateStoreDiscountsTable()
//    {
//        if (selectedStoreProperty.getValue().getDiscounts().size() > 0)
//        {
//            setDiscountsTableColumnsProperties();
//            discountsProperty.setValue(loadStoreDiscounts());
//            storeDiscountsView.refresh();
//        }
//        else
//        {
//            discountsProperty.clear();
//            storeDiscountsView.setPlaceholder(new Label("No discounts to display"));
//        }
//    }
//
//    private void setStoresTableColumnsProperties()
//    {
//        storeId.setCellValueFactory(new PropertyValueFactory<>("id"));
//        storeName.setCellValueFactory(new PropertyValueFactory<>("name"));
//        ppk.setCellValueFactory(new PropertyValueFactory<>("ppk"));
//        totalDeliveriesCost.setCellValueFactory(new PropertyValueFactory<>("totalIncomeFromDeliveries"));
//    }
//
//    private void setProductsTableColumnsProperties()
//    {
//        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
//        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
//        purchaseForm.setCellValueFactory(new PropertyValueFactory<>("purchaseForm"));
//        price.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPricePerStore().get(selectedStoreProperty.getValue().getId())));
//        howMuchSoldInStore.setCellValueFactory(cellData -> new SimpleObjectProperty<>(Float.valueOf(Utilities.DECIMAL_FORMAT.format(cellData.getValue().getSoldAmountPerStore().get(selectedStoreProperty.getValue().getId())))));
//    }
//
//    private void setOrdersTableColumnsProperties()
//    {
//        id.setCellValueFactory(new PropertyValueFactory<>("id"));
//        date.setCellValueFactory(new PropertyValueFactory<>("date"));
//        totalNumOfProducts.setCellValueFactory(new PropertyValueFactory<>("amountOfProductsTypes"));
//        totalProductsCost.setCellValueFactory(new PropertyValueFactory<>("costOfAllProducts"));
//        deliveryCost.setCellValueFactory(new PropertyValueFactory<>("deliveryCost"));
//        totalOrderCost.setCellValueFactory(cellData -> new SimpleObjectProperty<>(Float.valueOf(Utilities.DECIMAL_FORMAT.format(cellData.getValue().getTotalCost()))));
//
//    }
//
//    private void setDiscountsTableColumnsProperties()
//    {
//        discountName.setCellValueFactory(new PropertyValueFactory<>("discountName"));
//        ifYouBuyDescription.setCellValueFactory(new PropertyValueFactory<>("ifYouBuyDescription"));
//        thenYouGetDescription.setCellValueFactory(new PropertyValueFactory<>("thenYouGetDescription"));
//    }
//
//    private ObservableList<StoreDataContainer> loadStores()
//    {
//        return systemLogic.getAllStoresData().stream()
//                .collect(Collectors
//                        .collectingAndThen(Collectors.toList(),
//                                FXCollections::observableArrayList));
//    }
//
//    private ObservableList<ProductDataContainer> loadStoreProducts()
//    {
//        return systemLogic.getAllStoresData().stream()
//                .filter(s -> {
//                    return s.equals(selectedStoreProperty.getValue());
//                })
//                .map(StoreDataContainer::getProducts)
//                .flatMap(Collection<ProductDataContainer>::stream)
//                .collect(Collectors
//                        .collectingAndThen(Collectors.toList(),
//                                FXCollections::observableArrayList));
//    }
//
//    private ObservableList<OrderDataContainer> loadStoreOrders()
//    {
//        return systemLogic.getAllStoresData().stream()
//                .filter(s -> {
//                    return s.equals(selectedStoreProperty.getValue());
//                })
//                .map(StoreDataContainer::getOrders)
//                .flatMap(Collection<OrderDataContainer>::stream)
//                .collect(Collectors
//                        .collectingAndThen(Collectors.toList(),
//                                FXCollections::observableArrayList));
//    }
//
//    private ObservableList<DiscountDataContainer> loadStoreDiscounts()
//    {
//        return systemLogic.getAllStoresData().stream()
//                .filter(s -> {
//                    return s.equals(selectedStoreProperty.getValue());
//                })
//                .map(StoreDataContainer::getDiscounts)
//                .flatMap(Collection<DiscountDataContainer>::stream)
//                .collect(Collectors
//                        .collectingAndThen(Collectors.toList(),
//                                FXCollections::observableArrayList));
//    }
//
//    public AnchorPane getRootPane()
//    {
//        return rootPane;
//    }
//
//    public void setMainController(MainAppController mainAppController)
//    {
//        this.mainAppController = mainAppController;
//    }
//
//    public void setSystemLogic(SystemManager systemManager)
//    {
//        this.systemLogic = systemManager;
//    }
}