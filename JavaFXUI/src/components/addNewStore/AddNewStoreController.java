package components.addNewStore;

import common.Utilities;
import components.main.MainAppController;
import dataContainers.*;
import engineLogic.SystemManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class AddNewStoreController
{

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Spinner<Integer> storeIDSpinner;

    @FXML
    private TextField storeNameTextField;

    @FXML
    private Spinner<Integer> storeXPosSpinner;

    @FXML
    private Spinner<Integer> storeYPosSpinner;

    @FXML
    private Spinner<Integer> storePPKSpinner;

    @FXML
    private TableView<ProductDataContainer> productsTableView;

    @FXML
    private TableColumn<ProductDataContainer, Boolean> selectProductColumn;

    @FXML
    private TableColumn<ProductDataContainer, Integer> productIDColumn;

    @FXML
    private TableColumn<ProductDataContainer, String> productNameColumn;

    @FXML
    private TableColumn<ProductDataContainer, String> productPurchaseFormColumn;

    @FXML
    private TableColumn<ProductDataContainer, Spinner<Number>> selectProductPriceColumn;

    @FXML
    private Button submitButton;

    private MainAppController mainAppController;
    private SystemManager systemManager;
    private StoreDataContainer store;


    private SimpleIntegerProperty selectedStoreID;
    private SimpleStringProperty selectedStoreName;
    private SimpleIntegerProperty selectedStoreXPos;
    private SimpleIntegerProperty selectedStoreYPos;
    private SimpleIntegerProperty selectedStorePPK;

    private SimpleListProperty<ProductDataContainer> productsProperty;

    public AnchorPane getRootPane() {
        return rootPane;
    }


    @FXML
    void initialize()
    {
        selectedStoreID.bind(storeIDSpinner.valueProperty());
        selectedStoreName.bind(storeNameTextField.textProperty());
        selectedStoreXPos.bind(storeXPosSpinner.valueProperty());
        selectedStoreYPos.bind(storeYPosSpinner.valueProperty());
        selectedStorePPK.bind(storePPKSpinner.valueProperty());

        productsTableView.itemsProperty().bind(productsProperty);

        submitButton.disableProperty().bind(storeNameTextField.textProperty().isEmpty());
    }

    public AddNewStoreController()
    {
        selectedStoreID = new SimpleIntegerProperty();
        selectedStoreName = new SimpleStringProperty();
        selectedStoreXPos = new SimpleIntegerProperty();
        selectedStoreYPos = new SimpleIntegerProperty();
        selectedStorePPK = new SimpleIntegerProperty();

        productsProperty = new SimpleListProperty<>();
    }

    public void setMainController(MainAppController mainAppController)
    {
        this.mainAppController = mainAppController;
    }

    public void setSystemLogic(SystemManager systemManager)
    {
        this.systemManager = systemManager;
    }

    public void LoadDataToController()
    {
        store = null;
        initControllers();
        setProductsTableColumnsProperties();
        loadProducts();
    }

    private void initControllers()
    {
        storeIDSpinner = Utilities.SetSpinnerToNaturalNumbers(storeIDSpinner,null);
        storeXPosSpinner = Utilities.SetSpinnerToNaturalNumbers(storeXPosSpinner,50);
        storeYPosSpinner = Utilities.SetSpinnerToNaturalNumbers(storeYPosSpinner,50);
        storePPKSpinner = Utilities.SetSpinnerToNaturalNumbers(storePPKSpinner,null);
    }

    private void loadProducts()
    {
        productsProperty.clear();
        productsProperty.setValue(systemManager.getAllProductsData().stream()
                .map(product ->{
                    product.checkedProperty().setValue(false);
                    return product;})
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                FXCollections::observableArrayList)));
    }

    private void setProductsTableColumnsProperties()
    {
        setProductToOrderColumn();
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPurchaseFormColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseForm"));
        setSelectPriceColumn();
    }

    private void setProductToOrderColumn()
    {
        selectProductColumn.setCellFactory(column -> new CheckBoxTableCell());
        selectProductColumn.setCellValueFactory(data -> data.getValue().checkedProperty());
    }

    private void setSelectPriceColumn()
    {
        selectProductPriceColumn.setCellValueFactory(data ->
        {
            Spinner spinner = new Spinner();
            spinner =  Utilities.SetSpinnerToNaturalNumbers(spinner,null);
            data.getValue().priceProperty().bind(spinner.valueProperty());
            return new SimpleObjectProperty<Spinner<Number>>(spinner);
        });
    }

    @FXML
    void OnStoreSubmit(ActionEvent event)
    {
        try
        {
            createNewStore();
            systemManager.ValidateStore(store);
            systemManager.addNewStore(store);
            LoadDataToController();
            Utilities.ShowInformationAlert("Store added successfully");
        }
        catch (Exception e)
        {
            store = null;
            Utilities.ShowErrorAlert(e.getMessage());
        }
    }

    private void createNewStore()
    {
        store = new StoreDataContainer(selectedStoreID.get(),
                selectedStoreName.get(),
                new Point(selectedStoreXPos.get(),selectedStoreYPos.get()),
                selectedStorePPK.get(),
                0,
                getSelectedProducts(),
                new ArrayList<OrderDataContainer>(),
                new ArrayList<DiscountDataContainer>());
    }

    private Collection<ProductDataContainer> getSelectedProducts()
    {
        Collection<ProductDataContainer> selectedProducts = new HashSet<>();
        if(!productsProperty.isEmpty())
        {
            for (ProductDataContainer item: productsProperty.get())
            {
                if (selectProductColumn.getCellObservableValue(item).getValue().booleanValue())
                {
                    selectedProducts.add(item);
                }
            }
        }
        return selectedProducts;
    }
}