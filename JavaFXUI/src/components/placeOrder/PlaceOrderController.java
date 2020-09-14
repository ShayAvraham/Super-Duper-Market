package components.placeOrder;
import common.Utilities;
import components.main.MainAppController;
import dataContainers.CustomerDataContainer;
import dataContainers.ProductDataContainer;
import dataContainers.StoreDataContainer;
import engineLogic.Store;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import engineLogic.SystemManager;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

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
    private TableColumn<ProductDataContainer,Spinner<Integer>> productAmountColumn;

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
    private Tab storesOrderSummaryTab;

    @FXML
    private Tab productsOrderSummaryTab;

    @FXML
    private Button submitProductsButton;

    private final String STATIC = "Static order";
    private final String DYNAMIC = "Dynamic order";

    private SystemManager systemManager;

    private SimpleListProperty<CustomerDataContainer> customersProperty;
    private SimpleListProperty<String> orderTypesProperty;
    private SimpleListProperty<StoreDataContainer> storesProperty;
    private SimpleFloatProperty deliveryCostProperty;
    private SimpleListProperty<ProductDataContainer> productsProperty;
//    private SimpleListProperty<String> storesOrderedFromProperty;


    private SimpleObjectProperty<CustomerDataContainer> selectedCustomerProperty;
    private SimpleObjectProperty<LocalDate> selectedDeliveryDate;
    private SimpleStringProperty selectedOrderTypeProperty;
    private SimpleObjectProperty<StoreDataContainer> selectedStoreProperty;

    private SimpleBooleanProperty isOrderTypeStatic;

    ObservableList<String> orderTypeValues = FXCollections.observableArrayList(STATIC,DYNAMIC);


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
        productsProperty.setValue(loadProductToTableView());
        productsTableView.refresh();

    }

    private ObservableList<ProductDataContainer> loadProductToTableView()
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
//        productToOrderColumn.setCellValueFactory(c -> c.getValue().checkedProperty());

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
            Spinner<Integer> spinner = new Spinner<>();
            TextFormatter<Integer> amountFormatter = new TextFormatter<Integer>(new IntegerStringConverter(), 1, Utilities.getPositiveRealNumbersFilter());
            spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1, 1));
            spinner.getEditor().setTextFormatter(amountFormatter);
            spinner.setEditable(true);
            column.getValue().amountProperty().bind(spinner.valueProperty());
            return new SimpleObjectProperty<Spinner<Integer>>(spinner);
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
        Collection <ProductDataContainer > selectedProducts = getSelectedProducts();
        if(!selectedProducts.isEmpty())
        {
            if(selectedOrderTypeProperty.getValue().equals(DYNAMIC))
            {
                Map <StoreDataContainer,Collection<ProductDataContainer>> storeToPurchaseFrom= systemManager.dynamicStoreAllocation(selectedProducts);
                loadStoresOrderedFrom(storeToPurchaseFrom);
            }
            else
            {

            }
        }
    }

    private Collection<ProductDataContainer> getSelectedProducts()
    {
        int sum = 0;
        Collection <ProductDataContainer > selectedProducts = new HashSet<>();
        for (ProductDataContainer item: productsTableView.getItems())
        {
            if (productToOrderColumn.getCellObservableValue(item).getValue().booleanValue())
            {
                selectedProducts.add(item);
                sum += item.amountProperty().get();
            }
        }
        return selectedProducts;
    }

    private void loadStoresOrderedFrom( Map <StoreDataContainer,Collection<ProductDataContainer>> storeToPurchaseFrom)
    {

        setStoresOrderedFromTableColumnsProperties();
        ObservableList<ArrayList<StringProperty>> data = storesOrderedFromData(storeToPurchaseFrom);
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

    private ObservableList<ArrayList<StringProperty>> storesOrderedFromData
            (Map <StoreDataContainer,Collection<ProductDataContainer>> storeToPurchaseFrom)
    {
        ObservableList<ArrayList<StringProperty>> data = FXCollections.observableArrayList();

        for(StoreDataContainer store: storeToPurchaseFrom.keySet())
        {
            ArrayList<StringProperty> row = new ArrayList<>();
            row.add(0, new SimpleStringProperty(Integer.toString(store.getId())));
            row.add(1, new SimpleStringProperty(store.getName()));
            row.add(2, new SimpleStringProperty(store.getPosition().toString()));
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
}