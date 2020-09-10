package components.updateProducts;

import common.Utilities;
import components.main.MainAppController;
import dataContainers.ProductDataContainer;
import dataContainers.StoreDataContainer;
import engineLogic.SystemManager;
import exceptions.DiscountRemoveException;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;

public class UpdateProductsController
{
    private MainAppController mainAppController;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ComboBox<StoreDataContainer> storeComboBox;

    @FXML
    private ComboBox<ProductDataContainer> productComboBox;

    @FXML
    private ComboBox<String> updateOptionComboBox;

    @FXML
    private Spinner<Integer> priceSpinner;

    @FXML
    private Button submitButton;

    private final String ADD = "Add product";
    private final String REMOVE = "Remove product";
    private final String UPDATE_PRICE = "Update product price";
    private final String PRODUCT_REMOVED_SUCCESSFULLY_MESSAGE = "Product %1$s removed successfully from %2$s.";
    private final String PRODUCT_ADDED_SUCCESSFULLY_MESSAGE = "Product %1$s add successfully to %2$s.";
    private final String PRODUCT_UPDATED_PRICE_SUCCESSFULLY_MESSAGE = "Product %1$s update price successfully in %2$s.";


    private SystemManager systemManager;

    private SimpleListProperty<StoreDataContainer> storesProperty;
    private SimpleListProperty<ProductDataContainer> productsProperty;
    private SimpleListProperty<String> updateOptionPropertyProperty;
    private SimpleIntegerProperty newPriceProperty;
    private BooleanProperty isStoreSelected;
    private BooleanProperty isUpdateOptionSelected;
    private BooleanProperty isProductSelected;
    private BooleanProperty isNotRemoveProductSelected;



    ObservableList<String> updateOptionValues = FXCollections.observableArrayList(ADD,REMOVE,UPDATE_PRICE);


    public AnchorPane getRootPane()
    {
        return rootPane;
    }

    public UpdateProductsController()
    {
        storesProperty = new SimpleListProperty<>();
        productsProperty = new SimpleListProperty<>();
        updateOptionPropertyProperty = new SimpleListProperty<>();
        newPriceProperty = new SimpleIntegerProperty();

        isStoreSelected = new SimpleBooleanProperty(false);
        isUpdateOptionSelected = new SimpleBooleanProperty(false);
        isProductSelected = new SimpleBooleanProperty(false);
        isNotRemoveProductSelected = new SimpleBooleanProperty(false);
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
        storesProperty.setValue(systemManager.getAllStoresData().stream()
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                FXCollections::observableArrayList)));
        updateOptionPropertyProperty.setValue(updateOptionValues);
        priceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE,1,1));
        newPriceProperty.setValue(1);
    }
    @FXML
    void initialize()
    {
      //  storeComboBox.valueProperty().bind(storesNamesProperty);
        storeComboBox.setItems(storesProperty);
        productComboBox.setItems(productsProperty);
        updateOptionComboBox.setItems(updateOptionPropertyProperty);
        productComboBox.disableProperty().bind(isUpdateOptionSelected.not() .or (isStoreSelected.not()));
        priceSpinner.visibleProperty().bind(isNotRemoveProductSelected);
        submitButton.disableProperty().bind(isProductSelected.not());

    }

    @FXML
    void onStoreSelected(ActionEvent event)
    {
        isStoreSelected.setValue(true);
      //  productsProperty.setValue(null);
        switch (updateOptionPropertyProperty.getName())
        {
            case ADD:
                productsProperty.setValue(loadProducts());
                break;
            default:
                productsProperty.setValue(loadStoreProducts());
                break;
        }
    }

    private ObservableList<ProductDataContainer> loadStoreProducts()
    {
        return systemManager.getAllStoresData().stream()
                .filter(s -> {
                    return s.equals(storesProperty.getValue());
                })
                .map(StoreDataContainer::getProducts)
                .flatMap(Collection<ProductDataContainer>::stream)
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                FXCollections::observableArrayList));
    }

    private ObservableList<ProductDataContainer> loadProducts()
    {
       return systemManager.getAllProductsData().stream()
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                FXCollections::observableArrayList));
    }

    @FXML
    void onUpdateOptionSelected(ActionEvent event)
    {
        isUpdateOptionSelected.setValue(true);
        if(!updateOptionPropertyProperty.getName().equals(REMOVE))
        {
            isNotRemoveProductSelected.setValue(true);
        }
        else
        {
            isNotRemoveProductSelected.setValue(false);
        }
    }

    @FXML
    void onProductSelected(ActionEvent event)
    {
        isProductSelected.setValue(true);
    }

    @FXML
    void updateProduct(ActionEvent event)
    {
        try
        {
            String successMassage = "";
            switch (updateOptionPropertyProperty.getName())
            {
                case ADD:
                    systemManager.addProductToStore(storeComboBox.getValue(),productComboBox.getValue(),priceSpinner.getValue());
                    successMassage = PRODUCT_ADDED_SUCCESSFULLY_MESSAGE;
                    break;
                case REMOVE:
                    systemManager.removeProductFromStore(storeComboBox.getValue(),productComboBox.getValue());
                    successMassage = PRODUCT_REMOVED_SUCCESSFULLY_MESSAGE;
                    break;
                case UPDATE_PRICE:
                    systemManager.updateProductPriceInStore(storeComboBox.getValue(),productComboBox.getValue(),priceSpinner.getValue());
                    successMassage = PRODUCT_UPDATED_PRICE_SUCCESSFULLY_MESSAGE;
                    break;
            }
            Utilities.ShowInformationAlert(String.format(successMassage,productsProperty.getName(),storesProperty.getName()));
        }
        catch (DiscountRemoveException e)
        {
            Utilities.ShowInformationAlert(e.getMessage());
        }
        catch (Exception e)
        {
            Utilities.ShowErrorAlert(e.getMessage());
        }
    }
}
