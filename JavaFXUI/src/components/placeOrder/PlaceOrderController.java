package components.placeOrder;
import common.Utilities;
import components.main.MainAppController;
import dataContainers.CustomerDataContainer;
import dataContainers.DiscountDataContainer;
import dataContainers.ProductDataContainer;
import dataContainers.StoreDataContainer;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
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
    private TableView<ArrayList<StringProperty>> storesOrderedFromTableView;

    @FXML
    private TableColumn<ArrayList<StringProperty>, String> storeIDColumn;

    @FXML
    private TableColumn<ArrayList<StringProperty>, String> storeNameColumn;

    @FXML
    private TableColumn<ArrayList<StringProperty>, String> storeLocationColumn;

    @FXML
    private TableColumn<ArrayList<StringProperty>, String> storeDistanceColumn;

    @FXML
    private TableColumn<ArrayList<StringProperty>, String> storePPKColumn;

    @FXML
    private TableColumn<ArrayList<StringProperty>, String> storeDeliveryCostColumn;

    @FXML
    private TableColumn<ArrayList<StringProperty>, String> storeNumOfProductsTypesColumn;

    @FXML
    private TableColumn<ArrayList<StringProperty>, String> storeProductsCostColumn;

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
    private Tab productsOrderSummaryTab;

    @FXML
    private Button submitProductsButton;

    @FXML
    private Button submitDiscountsButton;

    private final String STATIC = "Static order";
    private final String DYNAMIC = "Dynamic order";

    private SystemManager systemManager;
    private Collection<ProductDataContainer> selectedProducts;
    private Collection <DiscountDataContainer> selectedDiscounts;

    private SimpleListProperty<CustomerDataContainer> customersProperty;
    private SimpleListProperty<String> orderTypesProperty;
    private SimpleListProperty<StoreDataContainer> storesProperty;
    private SimpleFloatProperty deliveryCostProperty;
    private SimpleListProperty<ProductDataContainer> productsProperty;
//    private SimpleListProperty<String> storesOrderedFromProperty;
    private SimpleListProperty<DiscountDataContainer> availableDiscountsProperty;



    private SimpleObjectProperty<CustomerDataContainer> selectedCustomerProperty;
    private SimpleObjectProperty<LocalDate> selectedDeliveryDate;
    private SimpleStringProperty selectedOrderTypeProperty;
    private SimpleObjectProperty<StoreDataContainer> selectedStoreProperty;

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
//        storesComboBox.itemsProperty().bind(storesProperty);
        storesComboBox.itemsProperty().bind(storesProperty);
        storesComboBox.setConverter(Utilities.getStoreConverterInPlaceOrder());

        deliveryCostValueLabel.textProperty().bind(deliveryCostProperty.asString());
        productsTableView.itemsProperty().bind(productsProperty);
//        storesOrderedFromTableView.itemsProperty().bind(storesOrderedFromProperty);
        availableDiscountsTableView.itemsProperty().bind(availableDiscountsProperty);

        selectedCustomerProperty.bind(customersComboBox.selectionModelProperty().get().selectedItemProperty());
        selectedOrderTypeProperty.bind(orderTypesComboBox.selectionModelProperty().get().selectedItemProperty());
        selectedDeliveryDate.bind(deliveryDatePicker.valueProperty());
        selectedStoreProperty.bind(storesComboBox.selectionModelProperty().getValue().selectedItemProperty());

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
//        storesOrderedFromProperty = new SimpleListProperty<>();
        availableDiscountsProperty = new SimpleListProperty<>();

        selectedCustomerProperty = new SimpleObjectProperty<>();
        selectedDeliveryDate = new SimpleObjectProperty<>();
        selectedOrderTypeProperty = new SimpleStringProperty();
        selectedStoreProperty = new SimpleObjectProperty<>();

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
        selectedDiscounts = new HashSet<>();
        selectedProducts = new HashSet<>();
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
            deliveryCostProperty.set(systemManager.getStaticOrderDeliveryCost(selectedStoreProperty.getValue(), selectedCustomerProperty.getValue()));
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
//        productToOrderColumn.setCellFactory(CheckBoxTableCell.forTableColumn(productToOrderColumn));
////        productToOrderColumn.setCellValueFactory(c -> c.getValue().checkedProperty());

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
            Map <StoreDataContainer,Collection<ProductDataContainer>> storeToPurchaseFrom = systemManager.dynamicStoreAllocation(selectedProducts);
            if(selectedOrderTypeProperty.getValue().equals(DYNAMIC))
            {
                loadStoresOrderedFrom(storeToPurchaseFrom);
            }
            loadAvailableDiscounts(storeToPurchaseFrom);
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

    private void loadStoresOrderedFrom( Map <StoreDataContainer,Collection<ProductDataContainer>> storeToPurchaseFrom)
    {

        setStoresOrderedFromTableColumnsProperties();
        ObservableList<ArrayList<StringProperty>> data = getStoresOrderedFromForTableView(storeToPurchaseFrom);
        storesOrderedFromTableView.setItems(data);
        storesOrderedFromTableView.refresh();

    }

    private void setStoresOrderedFromTableColumnsProperties()
    {
        storeIDColumn.setCellValueFactory(data -> data.getValue().get(0));
        storeNameColumn.setCellValueFactory(data -> data.getValue().get(1));
        storeLocationColumn.setCellValueFactory(data -> data.getValue().get(2));
        storeDistanceColumn.setCellValueFactory(data -> data.getValue().get(3));
        storePPKColumn.setCellValueFactory(data -> data.getValue().get(4));
        storeDeliveryCostColumn.setCellValueFactory(data -> data.getValue().get(5));
        storeNumOfProductsTypesColumn.setCellValueFactory(data -> data.getValue().get(6));
        storeProductsCostColumn.setCellValueFactory(data -> data.getValue().get(7));
    }

    private ObservableList<ArrayList<StringProperty>> getStoresOrderedFromForTableView
            (Map <StoreDataContainer,Collection<ProductDataContainer>> storeToPurchaseFrom)
    {
        ObservableList<ArrayList<StringProperty>> data = FXCollections.observableArrayList();

        for(StoreDataContainer store: storeToPurchaseFrom.keySet())
        {
            ArrayList<StringProperty> row = new ArrayList<>();
            row.add(0, new SimpleStringProperty(Integer.toString(store.getId())));
            row.add(1, new SimpleStringProperty(store.getName()));
            row.add(2, new SimpleStringProperty(Utilities.convertPositionFormat(store.getPosition())));
            row.add(3, new SimpleStringProperty(
                    Float.toString(systemManager.getDistanceBetweenStoreAndCustomer(
                                store,selectedCustomerProperty.getValue()))));

            row.add(4, new SimpleStringProperty(Float.toString(store.getPpk())));
            row.add(5, new SimpleStringProperty(
                    Float.toString(systemManager.getStaticOrderDeliveryCost(
                                store,selectedCustomerProperty.getValue()))));
            row.add(6, new SimpleStringProperty(
                    Integer.toString(storeToPurchaseFrom.get(store).size())));
            row.add(7, new SimpleStringProperty(
                    Float.toString(systemManager.getProductCostFromStore(store,storeToPurchaseFrom.get(store)))));
            data.add(row);
        }

        return data;
    }

    private void loadAvailableDiscounts(Map<StoreDataContainer, Collection<ProductDataContainer>> storeToPurchaseFrom)
    {
        setAvailableDiscountsTableColumnsProperties();

        availableDiscountsProperty.setValue(getAvailableDiscountsForTableView(storeToPurchaseFrom));
        setOfferProductComboBox();
        availableDiscountsTableView.refresh();
    }

    private void setOfferProductComboBox()
    {
//        for (DiscountDataContainer item: availableDiscountsTableView.getItems())
//        {
//            if(item.)
//        }
//        return selectedProducts;
    }

    private ObservableList<DiscountDataContainer> getAvailableDiscountsForTableView(Map<StoreDataContainer, Collection<ProductDataContainer>> storeToPurchaseFrom)
    {
//        return systemManager.getAvailableDiscounts(storeToPurchaseFrom).stream()
//                .collect(Collectors
//                        .collectingAndThen(Collectors.toList(),
//                                FXCollections::observableArrayList));

        return systemManager.getAvailableDiscounts(storeToPurchaseFrom).values()
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
    void OnSubmitDiscountsSelected(ActionEvent event)
    {
        try
        {
            getSelectedDiscounts();
            systemManager.validateSelectedDiscounts(selectedDiscounts,selectedProducts);
        }
        catch (Exception e)
        {
            Utilities.ShowInformationAlert(e.getMessage());
        }
    }

    private void getSelectedDiscounts()
    {
        selectedDiscounts = new HashSet<>();
        for (DiscountDataContainer item: availableDiscountsTableView.getItems())
        {
            if (discountToGet.getCellObservableValue(item).getValue().booleanValue())
            {
                selectedDiscounts.add(item);
            }
        }
    }


}