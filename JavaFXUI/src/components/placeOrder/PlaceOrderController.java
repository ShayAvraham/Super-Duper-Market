package components.placeOrder;
import common.Utilities;
import components.main.MainAppController;
import dataContainers.*;
import engineLogic.Discount;
import engineLogic.Product;
import engineLogic.Store;
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
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.input.MouseButton;
//import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

//import java.beans.EventHandler;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.util.*;
import java.util.stream.Collectors;

public class PlaceOrderController
{
    private MainAppController mainAppController;

    @FXML
    private AnchorPane rootPane;

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
    private TableColumn<ProductDataContainer,Spinner<Double>> productAmountColumn;

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
    private Tab storesOrderSummaryTab;

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

    @FXML
    private Button submitProductsButton;

    @FXML
    private Button submitDiscountsButton;

    @FXML
    private Button submitOrderButton;

    private final String STATIC = "Static order";
    private final String DYNAMIC = "Dynamic order";

    private SystemManager systemManager;
    private Collection<ProductDataContainer> selectedProducts;
    private Map <StoreDataContainer,Collection<DiscountDataContainer>> selectedDiscounts;
    private Map <StoreDataContainer,Collection<ProductDataContainer>> storeToPurchaseFrom;
    private Map<StoreDataContainer,Collection<DiscountDataContainer>> availableDiscounts;

    private SimpleListProperty<CustomerDataContainer> customersProperty;
    private SimpleListProperty<String> orderTypesProperty;
    private SimpleListProperty<StoreDataContainer> storesProperty;
    private SimpleFloatProperty deliveryCostProperty;
    private SimpleListProperty<ProductDataContainer> productsProperty;
    private SimpleListProperty<StoreDataContainer> storesOrderedFromProperty;
    private SimpleListProperty<DiscountDataContainer> availableDiscountsProperty;
    private SimpleListProperty<ProductDataContainer> productsSummaryProperty;
    private SimpleListProperty<DiscountDataContainer> discountsSummaryProperty;


    private SimpleObjectProperty<CustomerDataContainer> selectedCustomerProperty;
    private SimpleObjectProperty<LocalDate> selectedDeliveryDate;
    private SimpleStringProperty selectedOrderTypeProperty;
    private SimpleObjectProperty<StoreDataContainer> selectedStoreProperty;
    private SimpleObjectProperty<StoreDataContainer> selectedStoreSummaryProperty;

    private SimpleBooleanProperty isOrderTypeStatic;

    ObservableList<String> orderTypeValues = FXCollections.observableArrayList(STATIC,DYNAMIC);

    public enum OrderSteps
    {
        SELECT_PRODUCTS,SHOW_DISCOUNTS, SHOW_PRE_ORDER_SUMMARY
    }
    @FXML
    void initialize()
    {
        customersComboBox.itemsProperty().bind(customersProperty);
        orderTypesComboBox.itemsProperty().bind(orderTypesProperty);
        storesComboBox.itemsProperty().bind(storesProperty);
        storesComboBox.setConverter(Utilities.getStoreConverterInPlaceOrder());

        deliveryCostValueLabel.textProperty().bind(deliveryCostProperty.asString());
        productsTableView.itemsProperty().bind(productsProperty);
        storesOrderedFromTableView.itemsProperty().bind(storesOrderedFromProperty);
        availableDiscountsTableView.itemsProperty().bind(availableDiscountsProperty);
        storeSummaryTableView.itemsProperty().bind(storesOrderedFromProperty);
        productsSummaryTableView.itemsProperty().bind(productsSummaryProperty);
        discountsSummaryTableView.itemsProperty().bind(discountsSummaryProperty);

        selectedCustomerProperty.bind(customersComboBox.selectionModelProperty().get().selectedItemProperty());
        selectedOrderTypeProperty.bind(orderTypesComboBox.selectionModelProperty().get().selectedItemProperty());
        selectedDeliveryDate.bind(deliveryDatePicker.valueProperty());
        selectedStoreProperty.bind(storesComboBox.selectionModelProperty().getValue().selectedItemProperty());
        selectedStoreSummaryProperty.bind(storeSummaryTableView.selectionModelProperty().getValue().selectedItemProperty());

//        productsTableView.disableProperty().bind(selectedCustomerProperty.isNull() .or (selectedOrderTypeProperty.isNull()));
//        productsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        productPriceColumn.visibleProperty().bindBidirectional(isOrderTypeStatic);

        isOrderTypeStatic.bind(selectedOrderTypeProperty.isEqualTo(STATIC));
        storesGridPane.visibleProperty().bind(isOrderTypeStatic);
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
        deliveryCostValueLabel.visibleProperty().bind(selectedStoreProperty.isNotNull() .and (selectedCustomerProperty.isNotNull()));


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
        productsSummaryProperty = new SimpleListProperty<>();
        discountsSummaryProperty = new SimpleListProperty<>();

        selectedCustomerProperty = new SimpleObjectProperty<>();
        selectedDeliveryDate = new SimpleObjectProperty<>();
        selectedOrderTypeProperty = new SimpleStringProperty();
        selectedStoreProperty = new SimpleObjectProperty<>();
        selectedStoreSummaryProperty = new SimpleObjectProperty<>();

        isOrderTypeStatic = new SimpleBooleanProperty();
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
        selectedDiscounts = new HashMap<>();
        selectedProducts = new HashSet<>();
        storeToPurchaseFrom = new HashMap<>();
        availableDiscounts = new HashMap<>();
        loadCustomers();
        orderTypesProperty.setValue(orderTypeValues);
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
        if(isOrderTypeStatic.getValue())
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
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                FXCollections::observableArrayList));
    }


    private void setProductsTableColumnsProperties()
    {
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPurchaseFormColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseForm"));
        setPriceColumn();
        setProductToOrderColumn();
        setAmountColumn();
    }

    private void setPriceColumn()
    {
        if(isOrderTypeStatic.getValue() && selectedStoreProperty.isNotNull().get())
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
            if(isOrderTypeStatic.get() && selectedStoreProperty.getValue() == null)
            {
                checkbox.disableProperty().setValue(true);
            }

            return  checkbox;
        });

        productToOrderColumn.setCellValueFactory(c ->
        {
            if((isOrderTypeStatic.get() && selectedStoreProperty.getValue() == null)||
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
        productAmountColumn.setCellValueFactory(column ->
        {
            Spinner<Double> spinner = new Spinner<>();
            TextFormatter<Double> amountFormatter = new TextFormatter<Double>(new DoubleStringConverter(), 1d, Utilities.getPositiveRealNumbersFilter());
            spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, Integer.MAX_VALUE, 1, 0.1));
            spinner.getEditor().setTextFormatter(amountFormatter);
            spinner.setEditable(true);
            column.getValue().amountProperty().bind(spinner.valueProperty());
            return new SimpleObjectProperty<Spinner<Double>>(spinner);
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
        getSelectedProducts();
        if(!selectedProducts.isEmpty())
        {
            if(selectedOrderTypeProperty.getValue().equals(DYNAMIC))
            {
                loadStoresOrderedFrom();
            }
            else
            {
                storeToPurchaseFrom.putIfAbsent(selectedStoreProperty.get(),selectedProducts);
            }
            loadAvailableDiscounts();
        }
    }

    private void getSelectedProducts()
    {
        selectedProducts = new HashSet<>();
        for (ProductDataContainer item: productsTableView.getItems())
        {
            if (productToOrderColumn.getCellObservableValue(item).getValue().booleanValue())
            {
                selectedProducts.add(item);
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
                        if (event.getButton().equals(MouseButton.PRIMARY)) {
                            DiscountDataContainer discount = availableDiscountsTableView.getSelectionModel().getSelectedItem();
                            discount.selectedOfferProductProperty().bind(box.selectionModelProperty().get().selectedItemProperty());
                            if (discount != null && discount.getDiscountType() == "ONE_OF")
                            {
                                box.setItems(FXCollections.observableArrayList(discount.getAmountForOfferProduct().keySet()
                                        .stream()
                                        .collect(Collectors.toList())));

                            }
                        }
                        if (event.getClickCount() == 1 && row == cell.getIndex()) {
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
     //   discountChosenProductColumn.setCellValueFactory(c -> c.getValue().selectedOfferProductProperty());

    }

    @FXML
    void OnSubmitDiscountsSelected(ActionEvent event) /** move to new controller **/
    {
        try
        {
            getSelectedDiscounts();
            systemManager.validateSelectedDiscounts(selectedDiscounts.values().stream()
                    .flatMap(Collection<DiscountDataContainer>::stream)
                    .collect(Collectors.collectingAndThen(Collectors.toList(),FXCollections::observableArrayList)),selectedProducts);


            float costOfAllProducts =  systemManager.getOrderCostOfAllProducts(storeToPurchaseFrom,selectedDiscounts);
            float deliveryCost = systemManager.getOrderDeliveryCost(storeToPurchaseFrom.keySet(),selectedCustomerProperty.get());
            OrderDataContainer order = new OrderDataContainer(selectedDeliveryDate.get(),
                    selectedCustomerProperty.get(),
                    storeToPurchaseFrom,
                    selectedDiscounts,
                    selectedOrderTypeProperty.get().equals(DYNAMIC)?true:false,
                    costOfAllProducts,
                    deliveryCost,
                    costOfAllProducts + deliveryCost);
            systemManager.addNewOrder(order);


/*****************************************/
            loadOrderSummary();
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
            selectedDiscounts.putIfAbsent(store,selectedStoreDiscounts);
        }
    }

    private void loadOrderSummary()
    {
        loadStoresSummary();
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

    private void loadProductsSummary()
    {

        setProductsSummaryTableColumnsProperties();
        productsSummaryProperty.setValue(
                storeToPurchaseFrom.get(selectedStoreSummaryProperty.get())
                .stream()
                .collect(Collectors
                .collectingAndThen(Collectors.toList(),
                FXCollections::observableArrayList)));
    }

    private void setProductsSummaryTableColumnsProperties()
    {
        productsSummaryIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productsSummaryNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsSummaryPurchaseFormColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseForm"));
        productsSummaryAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        productsSummaryPriceColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(systemManager.getProductPrice(selectedStoreSummaryProperty.get(),cell.getValue())));
        productsSummaryTotalPriceColumn.setCellValueFactory(cell ->new SimpleObjectProperty<>(
                cell.getValue().amountProperty().get() *
                systemManager.getProductPrice(selectedStoreSummaryProperty.get(),cell.getValue())));
    }



    private void loadDiscountsSummary()
    {
        setDiscountsSummaryTableColumnsProperties();
        discountsSummaryProperty.setValue(systemManager.createSubDiscounts(
                selectedDiscounts.get(selectedStoreSummaryProperty.getValue()))
            .stream()
            .collect(Collectors
                    .collectingAndThen(Collectors.toList(),
                            FXCollections::observableArrayList)));
    }

    private void setDiscountsSummaryTableColumnsProperties()
    {
        discountsSummaryIDColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmountForOfferProduct().keySet().stream().findFirst().get().getId()));
        discountsSummaryNameColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmountForOfferProduct().keySet().stream().findFirst().get().getName()));
        discountsSummaryPurchaseFormColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmountForOfferProduct().keySet().stream().findFirst().get().getPurchaseForm()));
        discountsSummaryAmountColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmountForOfferProduct().values().stream().findFirst().get().floatValue()));
        discountsSummaryPriceColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(systemManager.getProductPrice(selectedStoreSummaryProperty.get(),
                data.getValue().getAmountForOfferProduct().keySet().stream().findFirst().get())));
        discountsSummaryTotalPriceColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPriceForOfferProduct().values().stream().findFirst().get()));
    }

    @FXML
    void OnSubmitOrder(ActionEvent event)
    {
        OrderDataContainer order;
        if(selectedOrderTypeProperty.getValue().equals(STATIC))
        {
//            LocalDate date1 = deliveryDatePicker.valueProperty().get();
//            System.out.println(date1);
//             order = new OrderDataContainer(selectedDeliveryDate.get(),deliveryCostProperty.get(),selectedStoreProperty.get().getId(),);
        }
        else
        {
//            order = ;
        }
//        systemManager.addNewOrder(order);
    }


}