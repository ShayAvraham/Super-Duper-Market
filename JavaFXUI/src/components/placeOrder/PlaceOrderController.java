package components.placeOrder;
import common.Utilities;
import components.main.MainAppController;
import components.orderDetails.OrderDetailsController;
import dataContainers.*;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import engineLogic.SystemManager;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class PlaceOrderController
{
    private MainAppController mainAppController;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private GridPane placeOrderGridPane;

    @FXML
    private ComboBox<CustomerDataContainer> customersComboBox;

    @FXML
    private DatePicker deliveryDatePicker;

    @FXML
    private ComboBox<String> orderTypesComboBox;

    @FXML
    private GridPane storesGridPane;

    @FXML
    private Label selectStoreLabel;

    @FXML
    private ComboBox<StoreDataContainer> storesComboBox;

    @FXML
    private Label deliveryCostValueLabel;

    @FXML
    private Label deliveryCostLabel;

    @FXML
    private AnchorPane displayOrderDetailsPane;

    @FXML
    private TabPane tablesTabPane;

    @FXML
    private Tab productToOrderTab;

    @FXML
    private TableView<ProductDataContainer> productsTableView;

    @FXML
    private TableColumn<ProductDataContainer,Boolean> productToOrderColumn;

    @FXML
    private TableColumn<ProductDataContainer, Integer> productIDColumn;

    @FXML
    private TableColumn<ProductDataContainer, String> productPurchaseFormColumn;

    @FXML
    private TableColumn<ProductDataContainer, Integer> productPriceColumn;

    @FXML
    private TableColumn<ProductDataContainer, String> productNameColumn;

    @FXML
    private TableColumn<ProductDataContainer,Spinner<Number>> productAmountColumn;

    @FXML
    private Tab storesOrderedFromTab;

    @FXML
    private TableView<StoreDataContainer> storesOrderedFromTableView;

    @FXML
    private TableColumn<StoreDataContainer, Integer> storeIDColumn;

    @FXML
    private TableColumn<StoreDataContainer, String> storeNameColumn;

    @FXML
    private TableColumn<StoreDataContainer, String> storeLocationColumn;

    @FXML
    private TableColumn<StoreDataContainer, Float> storeDistanceColumn;

    @FXML
    private TableColumn<StoreDataContainer, Float> storePPKColumn;

    @FXML
    private TableColumn<StoreDataContainer, Float> storeDeliveryCostColumn;

    @FXML
    private TableColumn<StoreDataContainer, Integer> storeNumOfProductsTypesColumn;

    @FXML
    private TableColumn<StoreDataContainer, Float> storeProductsCostColumn;

    @FXML
    private Tab availableDiscountsTab;

    @FXML
    private TableView<DiscountDataContainer> availableDiscountsTableView;

    @FXML
    private TableColumn<DiscountDataContainer, Boolean> discountToGet;

    @FXML
    private TableColumn<DiscountDataContainer, String> discountNameColumn;

    @FXML
    private TableColumn<DiscountDataContainer, String> discountForBuyingColumn;

    @FXML
    private TableColumn<DiscountDataContainer, String> discountYouGetColumn;

    @FXML
    private TableColumn<DiscountDataContainer,ProductDataContainer> discountChosenProductColumn;

    @FXML
    private Button submitProductsButton;

    @FXML
    private Button submitDiscountsButton;

    @FXML
    private Button submitOrderButton;

    private final String STATIC = "Static order";
    private final String DYNAMIC = "Dynamic order";

    private SystemManager systemManager;
    private OrderDetailsController orderDetailsController;
    private Collection<ProductDataContainer> selectedProducts;
    private Map <StoreDataContainer,Collection<DiscountDataContainer>> selectedDiscounts;
    private Map <StoreDataContainer,Collection<ProductDataContainer>> storeToPurchaseFrom;
    private Map<StoreDataContainer,Collection<DiscountDataContainer>> availableDiscounts;
    private OrderDataContainer order;


    private SimpleListProperty<CustomerDataContainer> customersProperty;
    private SimpleListProperty<String> orderTypesProperty;
    private SimpleListProperty<StoreDataContainer> storesProperty;
    private SimpleFloatProperty deliveryCostProperty;

    private SimpleListProperty<ProductDataContainer> productsProperty;
    private SimpleListProperty<StoreDataContainer> storesOrderedFromProperty;
    private SimpleListProperty<DiscountDataContainer> availableDiscountsProperty;

    private SimpleObjectProperty<CustomerDataContainer> selectedCustomerProperty;
    private SimpleObjectProperty<LocalDate> selectedDeliveryDateProperty;
    private SimpleStringProperty selectedOrderTypeProperty;
    private SimpleObjectProperty<StoreDataContainer> selectedStoreProperty;

    private SimpleObjectProperty<OrderPhase> currentPhaseProperty;
    private SimpleBooleanProperty isOrderTypeStaticProperty;

    ObservableList<String> orderTypeValues = FXCollections.observableArrayList(STATIC,DYNAMIC);

    public void setOrderDetailsController(OrderDetailsController orderDetailsController)
    {
        this.orderDetailsController = orderDetailsController;
    }

    public enum OrderPhase
    {
        SELECT_PRODUCTS,SELECT_DISCOUNTS,SUBMIT_ORDER,
    }
    @FXML
    void initialize()
    {
        initSelectProductsPhaseProperties();
        initSelectDiscountsPhaseProperties();
        initSubmitOrderPhaseProperties();
        initMultiPhasesProperties();
    }

    private void initSelectProductsPhaseProperties()
    {
        customersComboBox.itemsProperty().bind(customersProperty);
        customersComboBox.disableProperty().bind(currentPhaseProperty.isNotEqualTo(OrderPhase.SELECT_PRODUCTS));

        orderTypesComboBox.itemsProperty().bind(orderTypesProperty);
        orderTypesComboBox.disableProperty().bind(currentPhaseProperty.isNotEqualTo(OrderPhase.SELECT_PRODUCTS));

        storesComboBox.itemsProperty().bind(storesProperty);
        storesComboBox.setConverter(Utilities.getStoreConverterInPlaceOrder());
        storesComboBox.disableProperty().bind(currentPhaseProperty.isNotEqualTo(OrderPhase.SELECT_PRODUCTS));

        storesGridPane.visibleProperty().bind(isOrderTypeStaticProperty);
        storesGridPane.visibleProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                if(oldValue)
                {
                    storesProperty.setValue(null);
                    storesComboBox.getSelectionModel().clearSelection();
                    deliveryCostProperty.setValue(null);
                }
            }
        });

        deliveryCostLabel.visibleProperty().bind(selectedStoreProperty.isNotNull() .and (selectedCustomerProperty.isNotNull()));
        deliveryCostValueLabel.textProperty().bind(deliveryCostProperty.asString());
        deliveryCostValueLabel.visibleProperty().bind(selectedStoreProperty.isNotNull() .and (selectedCustomerProperty.isNotNull()));

        productToOrderTab.disableProperty().bind(currentPhaseProperty.isNotEqualTo(OrderPhase.SELECT_PRODUCTS));
        productsTableView.itemsProperty().bind(productsProperty);
        productPriceColumn.visibleProperty().bindBidirectional(isOrderTypeStaticProperty);

        submitProductsButton.disableProperty().bind(currentPhaseProperty.isNotEqualTo(OrderPhase.SELECT_PRODUCTS)
                .or(selectedCustomerProperty.isNull()));
    }

    private void initSelectDiscountsPhaseProperties()
    {
        storesOrderedFromTab.disableProperty().bind(currentPhaseProperty.isNotEqualTo(OrderPhase.SELECT_DISCOUNTS).or(isOrderTypeStaticProperty));
        storesOrderedFromTableView.itemsProperty().bind(storesOrderedFromProperty);

        availableDiscountsTab.disableProperty().bind(currentPhaseProperty.isNotEqualTo(OrderPhase.SELECT_DISCOUNTS));
        availableDiscountsTableView.itemsProperty().bind(availableDiscountsProperty);

        submitDiscountsButton.disableProperty().bind(currentPhaseProperty.isNotEqualTo(OrderPhase.SELECT_DISCOUNTS).or(selectedDeliveryDateProperty.isNull()));
    }

    private void initSubmitOrderPhaseProperties()
    {
        submitOrderButton.disableProperty().bind(currentPhaseProperty.isNotEqualTo(OrderPhase.SUBMIT_ORDER));
    }

    private void initMultiPhasesProperties()
    {
        selectedCustomerProperty.bind(customersComboBox.selectionModelProperty().get().selectedItemProperty());
        selectedOrderTypeProperty.bind(orderTypesComboBox.selectionModelProperty().get().selectedItemProperty());
        selectedDeliveryDateProperty.bind(deliveryDatePicker.valueProperty());
        selectedStoreProperty.bind(storesComboBox.selectionModelProperty().getValue().selectedItemProperty());

        isOrderTypeStaticProperty.bind(selectedOrderTypeProperty.isEqualTo(STATIC));
    }

    public PlaceOrderController()
    {
        customersProperty = new SimpleListProperty<>();
        orderTypesProperty = new SimpleListProperty<>();
        storesProperty = new SimpleListProperty<>();

        deliveryCostProperty = new SimpleFloatProperty();

        productsProperty = new SimpleListProperty<>();
        storesOrderedFromProperty = new SimpleListProperty<>();
        availableDiscountsProperty = new SimpleListProperty<>();

        selectedCustomerProperty = new SimpleObjectProperty<>();
        selectedDeliveryDateProperty = new SimpleObjectProperty<>();
        selectedOrderTypeProperty = new SimpleStringProperty();
        selectedStoreProperty = new SimpleObjectProperty<>();

        currentPhaseProperty = new SimpleObjectProperty<>();
        isOrderTypeStaticProperty = new SimpleBooleanProperty();
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
        this.systemManager = systemManager;
    }

    public void LoadDataToControllers()
    {
        initDataMembers();
        initControllers();
        initProperties();

    }

    private void initDataMembers()
    {
        selectedDiscounts = new HashMap<>();
        selectedProducts = new HashSet<>();
        storeToPurchaseFrom = new HashMap<>();
        availableDiscounts = new HashMap<>();
        order = null;
    }

    private void initProperties()
    {
        loadCustomers();

        storesProperty.setValue(null);

        deliveryCostProperty.setValue(null);
        currentPhaseProperty.setValue(OrderPhase.SELECT_PRODUCTS);
        orderTypesProperty.setValue(orderTypeValues);

        productsProperty.clear();
        storesOrderedFromProperty.clear();
        availableDiscountsProperty.clear();;
    }

    private void initControllers()
    {
        customersComboBox.getSelectionModel().clearSelection();
        orderTypesComboBox.getSelectionModel().clearSelection();
        deliveryDatePicker.valueProperty().setValue(null);
        storesComboBox.getSelectionModel().clearSelection();

        displayOrderDetailsPane.getChildren().clear();
        displayOrderDetailsPane.getChildren().add(tablesTabPane);

        tablesTabPane.getSelectionModel().select(productToOrderTab);

        availableDiscountsTableView.setPlaceholder(new Label("No available discounts to display"));
    }

    private void loadCustomers()
    {
        customersProperty.setValue(systemManager.getAllCustomersData().stream()
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                FXCollections::observableArrayList)));
    }

    @FXML
    void OnCustomerSelected(ActionEvent event)
    {
        try
        {
            loadDeliveryCost();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void loadDeliveryCost()
    {
        if(selectedCustomerProperty.isNotNull().get()&& selectedStoreProperty.isNotNull().get())
        {
            deliveryCostProperty.set(systemManager.getDeliveryCostFromStore(selectedStoreProperty.getValue(), selectedCustomerProperty.getValue()));
        }
    }

    @FXML
    void OnOrderTypeSelected(ActionEvent event)
    {
        try
        {
            loadStores();
            loadProducts();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void loadStores()
    {
        if(isOrderTypeStaticProperty.getValue())
        {
            storesProperty.setValue(systemManager.getAllStoresData().stream()
                    .collect(Collectors
                            .collectingAndThen(Collectors.toList(),
                                    FXCollections::observableArrayList)));
        }
    }

    private void loadProducts()
    {
        setProductsTableColumnsProperties();
        productsProperty.setValue(getProductForTableView());
        productsTableView.refresh();

    }

    private ObservableList<ProductDataContainer> getProductForTableView()
    {
        return systemManager.getAllProductsData().stream()
                .map(product ->{
                    product.checkedProperty().setValue(false);
                    return product;})
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                FXCollections::observableArrayList));
    }


    private void setProductsTableColumnsProperties()
    {
        setProductToOrderColumn();
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPurchaseFormColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseForm"));
        setPriceColumn();
        setAmountColumn();
    }

    private void setPriceColumn()
    {
        if(isOrderTypeStaticProperty.getValue() && selectedStoreProperty.isNotNull().get())
        {
            setStaticPriceColumn();
        }
        else
        {
            productPriceColumn.setCellValueFactory(null);
        }
    }

    private void setStaticPriceColumn()
    {
        productPriceColumn.setCellValueFactory(cellData ->
        {
            if (selectedStoreProperty.getValue().getProducts().contains(cellData.getValue()))
            {
                return new SimpleObjectProperty<>(selectedStoreProperty.getValue()
                        .getProductDataContainerById(cellData.getValue().getId())
                        .getPricePerStore().get(selectedStoreProperty.getValue()
                                .getId()));
            }
            else
            {
                return new SimpleObjectProperty<>(null);
            }
        });
    }

    private void setProductToOrderColumn()
    {
        productToOrderColumn.setCellFactory(column ->
        {
            CheckBoxTableCell checkbox = new CheckBoxTableCell();
            if(isOrderTypeStaticProperty.get() && selectedStoreProperty.getValue() == null)
            {
                checkbox.disableProperty().setValue(true);
            }


            return  checkbox;
        });


        productToOrderColumn.setCellValueFactory(c ->
        {
            if((isOrderTypeStaticProperty.get() && selectedStoreProperty.getValue() == null)||
                    (selectedStoreProperty.getValue() != null
                    && !selectedStoreProperty.getValue().getProducts().contains(c.getValue())))
            {
              c.getValue().checkedProperty().setValue(false);
            }
           return  c.getValue().checkedProperty();
        });

    }


    private void setAmountColumn()
    {
        productAmountColumn.setCellValueFactory(data ->
        {
            Spinner spinner = new Spinner();
            spinner = data.getValue().getPurchaseForm().toLowerCase().equals("weight") ?
                    Utilities.SetSpinnerToPositiveRealNumbers(spinner) :
                    Utilities.SetSpinnerToNaturalNumbers(spinner,null);
            data.getValue().amountProperty().bind(spinner.valueProperty());
            return new SimpleObjectProperty<Spinner<Number>>(spinner);
        });
    }

    @FXML
    void OnStoreSelected(ActionEvent event)
    {
        try
        {
            loadDeliveryCost();
            loadProducts();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void OnSubmitProductSelected(ActionEvent event)
    {
        try
        {
            getSelectedProducts();
            systemManager.validateSelectedProducts(selectedProducts);
            currentPhaseProperty.set(OrderPhase.SELECT_DISCOUNTS);
            if (selectedOrderTypeProperty.getValue().equals(DYNAMIC))
            {
                loadStoresOrderedFrom();
                tablesTabPane.getSelectionModel().select(storesOrderedFromTab);

            }
            else
            {
                storeToPurchaseFrom.putIfAbsent(selectedStoreProperty.get(), selectedProducts);
                tablesTabPane.getSelectionModel().select(availableDiscountsTab);

            }
            loadAvailableDiscounts();
        }
        catch (Exception e)
        {
            Utilities.ShowErrorAlert(e.getMessage());
        }
    }

    private void getSelectedProducts()
    {
        selectedProducts = new HashSet<>();
        if(!productsProperty.isEmpty())
        {
            for (ProductDataContainer item: productsProperty.get())
            {
                if (productToOrderColumn.getCellObservableValue(item).getValue().booleanValue())
                {
                    selectedProducts.add(item);
                }
            }
        }
    }

    private void loadStoresOrderedFrom()
    {

        setStoresOrderedFromTableColumnsProperties();
        storeToPurchaseFrom = systemManager.dynamicStoreAllocation(selectedProducts);
        storesOrderedFromProperty.setValue(storeToPurchaseFrom.keySet().stream()
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),FXCollections::observableArrayList)));
        storesOrderedFromTableView.refresh();

    }

    private void setStoresOrderedFromTableColumnsProperties()
    {
        storeIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        storeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        storeLocationColumn.setCellValueFactory(data -> new SimpleStringProperty(Utilities.convertPositionFormat(data.getValue().getPosition())));
        storeDistanceColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(
                systemManager.getDistanceBetweenStoreAndCustomer(data.getValue(),selectedCustomerProperty.get())));
        storePPKColumn.setCellValueFactory(new PropertyValueFactory<>("ppk"));
        storeDeliveryCostColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(
                systemManager.getDeliveryCostFromStore(data.getValue(),selectedCustomerProperty.get())));
        storeNumOfProductsTypesColumn.setCellValueFactory(data-> new SimpleObjectProperty<>(storeToPurchaseFrom.get(data.getValue()).size()));
        storeProductsCostColumn.setCellValueFactory(data ->new SimpleObjectProperty<>(systemManager.getProductsCostFromStore
                (data.getValue(),storeToPurchaseFrom.get(data.getValue()))));


    }

    private void loadAvailableDiscounts()
    {
        setAvailableDiscountsTableColumnsProperties();
        availableDiscountsProperty.setValue(getAvailableDiscountsForTableView());
        availableDiscountsTableView.refresh();
    }

    private ObservableList<DiscountDataContainer> getAvailableDiscountsForTableView()
    {
        availableDiscounts = systemManager.getAvailableDiscounts(storeToPurchaseFrom);
        return availableDiscounts.values()
                .stream()
                .flatMap(Collection<DiscountDataContainer>::stream)
                .collect(Collectors.collectingAndThen(Collectors.toList(),FXCollections::observableArrayList));
    }

    private void setAvailableDiscountsTableColumnsProperties()
    {
        setDiscountToGetColumn();
        discountNameColumn.setCellValueFactory(new PropertyValueFactory<>("discountName"));
        discountForBuyingColumn.setCellValueFactory(new PropertyValueFactory<>("ifYouBuyDescription"));
        discountYouGetColumn.setCellValueFactory(new PropertyValueFactory<>("thenYouGetDescription"));
        setDiscountChosenProductColumn();
    }

    private void setDiscountToGetColumn()
    {
        discountToGet.setCellFactory(CheckBoxTableCell.forTableColumn(discountToGet));
        discountToGet.setCellValueFactory(c -> c.getValue().checkedProperty());
    }

    private void setDiscountChosenProductColumn()
    {
        discountChosenProductColumn.setCellFactory(new Callback<TableColumn<DiscountDataContainer, ProductDataContainer>, TableCell<DiscountDataContainer, ProductDataContainer>>()
        {
            @Override
            public TableCell<DiscountDataContainer, ProductDataContainer> call(TableColumn<DiscountDataContainer, ProductDataContainer> param)
            {
                ComboBox<ProductDataContainer> box = new ComboBox<>();
                TableCell<DiscountDataContainer, ProductDataContainer> cell = new TableCell<DiscountDataContainer, ProductDataContainer>();
                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        int row = availableDiscountsTableView.getSelectionModel().getSelectedIndex();
                        DiscountDataContainer discount = availableDiscountsTableView.getSelectionModel().getSelectedItem();
                        if (event.getButton().equals(MouseButton.PRIMARY)) {
                            discount.selectedOfferProductProperty().bind(box.selectionModelProperty().get().selectedItemProperty());
                            if (discount != null && discount.getDiscountType() == "ONE_OF")
                            {
                                box.setItems(FXCollections.observableArrayList(discount.getAmountForOfferProduct().keySet()
                                        .stream()
                                        .collect(Collectors.toList())));

                            }
                        }
                        if (event.getClickCount() == 1 && row == cell.getIndex())
                        {
                            box.getSelectionModel().select(0);
                            cell.setText(null);
                            if (!box.itemsProperty().get().isEmpty() && box.itemsProperty().get().size() != 1) {
                                cell.setGraphic(box);
                            }
                        }
                    }
                });
                return cell;
            }
        });
    }

    @FXML
    void OnSubmitDiscountsSelected(ActionEvent event)
    {
        try
        {
            getSelectedDiscounts();
            if(!selectedDiscounts.isEmpty())
            {
                systemManager.validateSelectedDiscounts(selectedDiscounts.values().stream()
                        .flatMap(Collection<DiscountDataContainer>::stream)
                        .collect(Collectors.collectingAndThen(Collectors.toList(), FXCollections::observableArrayList)), selectedProducts);
            }
            currentPhaseProperty.set(OrderPhase.SUBMIT_ORDER);
            createNewOrder();
            showOrderDetails();
        }
        catch (Exception e)
        {
            Utilities.ShowInformationAlert(e.getMessage());
        }
    }

    private void getSelectedDiscounts()
    {
        selectedDiscounts = new HashMap<>();
        for (StoreDataContainer store: availableDiscounts.keySet())
        {
            Collection<DiscountDataContainer> selectedStoreDiscounts = new ArrayList();
            for (DiscountDataContainer discount: availableDiscounts.get(store))
            {
                if (discountToGet.getCellObservableValue(discount).getValue().booleanValue())
                {
                    selectedStoreDiscounts.add(discount);
                }
            }
            if(!selectedStoreDiscounts.isEmpty())
            {
                selectedDiscounts.putIfAbsent(store, selectedStoreDiscounts);
            }
        }
    }

    private void createNewOrder()
    {
        Map<StoreDataContainer,Collection<DiscountDataContainer>> discounts = systemManager.CreateOrderDataDiscounts(selectedDiscounts);
        float costOfAllProducts =  systemManager.getOrderCostOfAllProducts(storeToPurchaseFrom,discounts);
        float deliveryCost = systemManager.getOrderDeliveryCost(storeToPurchaseFrom.keySet(),selectedCustomerProperty.get());

        order = new OrderDataContainer(selectedDeliveryDateProperty.getValue(),
                selectedCustomerProperty.get(),
                storeToPurchaseFrom,
                discounts,
                selectedOrderTypeProperty.get().equals(DYNAMIC)?true:false,
                costOfAllProducts,
                deliveryCost,
                costOfAllProducts + deliveryCost);
    }


    private void showOrderDetails()
    {
        displayOrderDetailsPane.getChildren().clear();
        orderDetailsController.setOrderDetails(order);
        displayOrderDetailsPane.getChildren().add(orderDetailsController.getRootPane());
    }

    @FXML
    void OnSubmitOrder(ActionEvent event)
    {
        try
        {
            systemManager.addNewOrder(order);
            LoadDataToControllers();
            Utilities.ShowInformationAlert("Order added successfully");
        }
        catch (Exception e)
        {
            Utilities.ShowErrorAlert(e.getMessage());
        }
    }
}