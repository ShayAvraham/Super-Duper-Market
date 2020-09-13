package components.placeOrder;
import common.Utilities;
import components.main.MainAppController;
import dataContainers.CustomerDataContainer;
import dataContainers.ProductDataContainer;
import dataContainers.StoreDataContainer;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import engineLogic.SystemManager;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.time.LocalDate;
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
    private TableView<ProductDataContainer> productsTableView;

    @FXML
    private TableColumn<ProductDataContainer,CheckBox> productToOrderColumn;

    @FXML
    private TableColumn<ProductDataContainer, Integer> productIDColumn;

    @FXML
    private TableColumn<ProductDataContainer, String> productPurchaseFormColumn;

    @FXML
    private TableColumn<ProductDataContainer, Integer> productPriceColumn;

    @FXML
    private TableColumn<ProductDataContainer, String> productNameColumn;

    private final String STATIC = "Static order";
    private final String DYNAMIC = "Dynamic order";

    private SystemManager systemManager;

    private SimpleListProperty<CustomerDataContainer> customersProperty;
    private SimpleListProperty<String> orderTypesProperty;
    private SimpleListProperty<StoreDataContainer> storesProperty;
    private SimpleFloatProperty deliveryCostProperty;
    private SimpleListProperty<ProductDataContainer> productsProperty;


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

        selectedCustomerProperty.bind(customersComboBox.selectionModelProperty().get().selectedItemProperty());
        selectedOrderTypeProperty.bind(orderTypesComboBox.selectionModelProperty().get().selectedItemProperty());
        selectedDeliveryDate.bind(deliveryDatePicker.valueProperty());
        selectedStoreProperty.bind(storesComboBox.selectionModelProperty().getValue().selectedItemProperty());

//        productsTableView.disableProperty().bind(selectedCustomerProperty.isNull() .or (selectedOrderTypeProperty.isNull()));

        productsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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
        setProductToOrderColumnColumn();
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
            if (selectedStoreProperty.getValue().getProducts().contains(cellData.getValue())) {
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

    private void setProductToOrderColumnColumn()
    {
        productToOrderColumn.setCellValueFactory(cellData ->
        {
            if(!isOrderTypeStatic.getValue()||
                    (selectedStoreProperty.isNotNull().get() &&
                    selectedStoreProperty.getValue().getProducts().contains(cellData.getValue())))
            {
                return new SimpleObjectProperty<CheckBox>(new CheckBox());
            }
            else
            {
                CheckBox checkBox = new CheckBox();
                checkBox.disableProperty().setValue(true);
                return new SimpleObjectProperty<CheckBox>(checkBox);
            }
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
}