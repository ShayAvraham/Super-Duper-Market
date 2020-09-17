package components.orderDetails;

import dataContainers.DiscountDataContainer;
import dataContainers.OrderDataContainer;
import dataContainers.ProductDataContainer;
import dataContainers.StoreDataContainer;
import engineLogic.SystemManager;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.stream.Collectors;

public class OrderDetailsController
{
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab storesSummaryTab;

    @FXML
    private TableView<StoreDataContainer> storeSummaryTableView;

    @FXML
    private TableColumn<StoreDataContainer, Integer> storeSummaryIDColumn;

    @FXML
    private TableColumn<StoreDataContainer, String> storeSummaryNameColumn;

    @FXML
    private TableColumn<StoreDataContainer, Float> storeSummaryPPKColumn;

    @FXML
    private TableColumn<StoreDataContainer, Float> storeSummaryDistanceColumn;

    @FXML
    private TableColumn<StoreDataContainer, Float> storeSummaryDeliveryCostColumn;

    @FXML
    private Tab productsOrderSummaryTab;

    @FXML
    private TableView<ProductDataContainer> productsSummaryTableView;

    @FXML
    private TableColumn<ProductDataContainer, Integer> productsSummaryIDColumn;

    @FXML
    private TableColumn<ProductDataContainer, String> productsSummaryNameColumn;

    @FXML
    private TableColumn<ProductDataContainer, String> productsSummaryPurchaseFormColumn;

    @FXML
    private TableColumn<ProductDataContainer, Float> productsSummaryAmountColumn;

    @FXML
    private TableColumn<ProductDataContainer, Integer> productsSummaryPriceColumn;

    @FXML
    private TableColumn<ProductDataContainer, Double> productsSummaryTotalPriceColumn;

    @FXML
    private Tab discountsOrderSummaryTab;

    @FXML
    private TableView<DiscountDataContainer> discountsSummaryTableView;

    @FXML
    private TableColumn<DiscountDataContainer, Integer> discountsSummaryIDColumn;

    @FXML
    private TableColumn<DiscountDataContainer, String> discountsSummaryNameColumn;

    @FXML
    private TableColumn<DiscountDataContainer, String> discountsSummaryPurchaseFormColumn;

    @FXML
    private TableColumn<DiscountDataContainer, Float> discountsSummaryAmountColumn;

    @FXML
    private TableColumn<DiscountDataContainer, Integer> discountsSummaryPriceColumn;

    @FXML
    private TableColumn<DiscountDataContainer, Integer> discountsSummaryTotalPriceColumn;

    private SystemManager systemManager;
    private OrderDataContainer orderDetails;
    private SimpleListProperty<StoreDataContainer> storesOrderProperty;
    private SimpleListProperty<ProductDataContainer> productsOrderProperty;
    private SimpleListProperty<DiscountDataContainer> discountsOrderProperty;
    private SimpleObjectProperty<StoreDataContainer> selectedStoreProperty;

    public OrderDetailsController()
    {
        storesOrderProperty = new SimpleListProperty<>();
        productsOrderProperty = new SimpleListProperty<>();
        discountsOrderProperty = new SimpleListProperty<>();
        selectedStoreProperty = new SimpleObjectProperty<>();
    }

    @FXML
    void initialize()
    {
        storeSummaryTableView.itemsProperty().bind(storesOrderProperty);
        productsSummaryTableView.itemsProperty().bind(productsOrderProperty);
        discountsSummaryTableView.itemsProperty().bind(discountsOrderProperty);
        selectedStoreProperty.bind(storeSummaryTableView.selectionModelProperty().getValue().selectedItemProperty());
        setStoresSummaryTableColumnsProperties();
    }

    public void loadOrderSummary()  // starting point
    {
        loadStoresSummary();
    }

    private void setStoresSummaryTableColumnsProperties()
    {
        storeSummaryIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        storeSummaryNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        storeSummaryPPKColumn.setCellValueFactory(new PropertyValueFactory<>("ppk"));
        storeSummaryDistanceColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(
                                    systemManager.getDistanceBetweenStoreAndCustomer(cell.getValue(),selectedCustomerProperty.get())));
        storeSummaryDeliveryCostColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(
                                    systemManager.getDeliveryCostFromStore(cell.getValue(),selectedCustomerProperty.get())));
    }

    private void setProductsSummaryTableColumnsProperties()
    {
        productsSummaryIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productsSummaryNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsSummaryPurchaseFormColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseForm"));
        productsSummaryAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        productsSummaryPriceColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(systemManager.getProductPrice(selectedStoreProperty.get(),cell.getValue())));
        productsSummaryTotalPriceColumn.setCellValueFactory(cell ->new SimpleObjectProperty<>(
                cell.getValue().amountProperty().get() *
                        systemManager.getProductPrice(selectedStoreProperty.get(),cell.getValue())));
    }

    private void setDiscountsSummaryTableColumnsProperties()
    {
        discountsSummaryIDColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmountForOfferProduct().keySet().stream().findFirst().get().getId()));
        discountsSummaryNameColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmountForOfferProduct().keySet().stream().findFirst().get().getName()));
        discountsSummaryPurchaseFormColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmountForOfferProduct().keySet().stream().findFirst().get().getPurchaseForm()));
        discountsSummaryAmountColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmountForOfferProduct().values().stream().findFirst().get().floatValue()));
        discountsSummaryPriceColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(systemManager.getProductPrice(selectedStoreProperty.get(),
                data.getValue().getAmountForOfferProduct().keySet().stream().findFirst().get())));
        discountsSummaryTotalPriceColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPriceForOfferProduct().values().stream().findFirst().get()));
    }

    private void loadStoresSummary()
    {
        setStoresSummaryTableColumnsProperties();
        storeSummaryTableView.setOnMouseClicked(event ->
        {
            loadProductsSummary();
            loadDiscountsSummary();
        } );
        storeSummaryTableView.refresh();
    }

    private void loadProductsSummary()
    {
        setProductsSummaryTableColumnsProperties();
        productsOrderProperty.setValue(
                storeToPurchaseFrom.get(selectedStoreProperty.get())
                        .stream()
                        .collect(Collectors
                                .collectingAndThen(Collectors.toList(),
                                        FXCollections::observableArrayList)));
    }


    private void loadDiscountsSummary()
    {
        setDiscountsSummaryTableColumnsProperties();
        discountsOrderProperty.setValue(systemManager.createSubDiscounts(
                selectedDiscounts.get(selectedStoreProperty.getValue()))
                .stream()
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                FXCollections::observableArrayList)));
    }

    public void setOrderDetails(OrderDataContainer orderDetails)
    {
        this.orderDetails = orderDetails;
    }

    public void setSystemLogic(SystemManager systemManager)
    {
        this.systemManager = systemManager;
    }
}
