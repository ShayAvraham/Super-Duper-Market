package components.updateProducts;

import common.Utilities;
import components.main.MainAppController;
import dataContainers.ProductDataContainer;
import dataContainers.StoreDataContainer;
import engineLogic.SystemManager;
import exceptions.DiscountRemoveException;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.IntegerStringConverter;

import javax.xml.bind.ValidationException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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

    @FXML
    private Label priceLabel;

    private final String ADD = "Add product";
    private final String REMOVE = "Remove product";
    private final String UPDATE_PRICE = "Update product price";
    private final String PRODUCT_REMOVED_SUCCESSFULLY_MESSAGE = "Product %1$s removed successfully from %2$s.";
    private final String PRODUCT_ADDED_SUCCESSFULLY_MESSAGE = "Product %1$s add successfully to %2$s.";
    private final String PRODUCT_UPDATED_PRICE_SUCCESSFULLY_MESSAGE = "Product %1$s update price successfully in %2$s.";

    private SystemManager systemManager;

    private SimpleListProperty<StoreDataContainer> storesProperty;
    private SimpleListProperty<ProductDataContainer> productsProperty;
    private SimpleListProperty<String> updateOptionProperty;

    private SimpleObjectProperty<StoreDataContainer> selectedStoreProperty;
    private SimpleObjectProperty<ProductDataContainer> selectedProductProperty;
    private SimpleStringProperty selectedOptionProperty;
    private SimpleObjectProperty<Integer> selectedPrice;

    ObservableList<String> updateOptionValues = FXCollections.observableArrayList(ADD,REMOVE,UPDATE_PRICE);

    @FXML
    void initialize()
    {
        storeComboBox.itemsProperty().bind(storesProperty);
        productComboBox.itemsProperty().bind(productsProperty);
        updateOptionComboBox.itemsProperty().bind(updateOptionProperty);

        selectedStoreProperty.bind(storeComboBox.valueProperty());
        selectedProductProperty.bind(productComboBox.valueProperty());
        selectedOptionProperty.bind(updateOptionComboBox.valueProperty());
        selectedPrice.bind(priceSpinner.valueProperty());


        productComboBox.disableProperty().bind(selectedStoreProperty.isNull() .or (selectedOptionProperty.isNull()));
        priceSpinner.visibleProperty().bind(selectedOptionProperty.isEqualTo(ADD) .or (selectedOptionProperty.isEqualTo(UPDATE_PRICE)));
        priceLabel.visibleProperty().bind(selectedOptionProperty.isEqualTo(ADD) .or (selectedOptionProperty.isEqualTo(UPDATE_PRICE)));
        submitButton.disableProperty().bind(selectedProductProperty.isNull());
    }

    public UpdateProductsController()
    {
        storesProperty = new SimpleListProperty<>();
        productsProperty = new SimpleListProperty<>();
        updateOptionProperty = new SimpleListProperty<>();

        selectedStoreProperty = new SimpleObjectProperty<>();
        selectedProductProperty = new SimpleObjectProperty<>();
        selectedOptionProperty = new SimpleStringProperty();
        selectedPrice = new SimpleObjectProperty<>();
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
        loadStores();
        updateOptionProperty.setValue(updateOptionValues);
        TextFormatter<Integer> priceFormatter = new TextFormatter<Integer>(new IntegerStringConverter(), 1, Utilities.getNaturalNumbersFilter());
        priceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE,1,1));
        priceSpinner.getEditor().setTextFormatter(priceFormatter);
    }

    private void loadStores()
    {
        storesProperty.setValue(systemManager.getAllStoresData().stream()
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                FXCollections::observableArrayList)));
    }

    @FXML
    void onStoreSelected(ActionEvent event)
    {
        try
        {
            loadProducts();
        }
        catch (Exception e)
        {
//            e.printStackTrace();
            Utilities.ShowErrorAlert(e.getMessage());
        }
    }

    private void loadProducts()
    {
        if(selectedOptionProperty.isNotNull().get() && selectedStoreProperty.isNotNull().get())
        {
            switch (selectedOptionProperty.getValue())
            {
                case ADD:
                    productsProperty.setValue(loadProductToAdd());
                    break;
                case REMOVE:
                case UPDATE_PRICE:
                    productsProperty.setValue(loadStoreProducts());
                    break;
            }
        }

    }

    private ObservableList<ProductDataContainer> loadStoreProducts()
    {
        return systemManager.getAllStoresData().stream()
                .filter(s -> {
                    return s.equals(selectedStoreProperty.getValue());
                })
                .map(StoreDataContainer::getProducts)
                .flatMap(Collection<ProductDataContainer>::stream)
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                FXCollections::observableArrayList));
    }

    private ObservableList<ProductDataContainer> loadProductToAdd()
    {
        return systemManager.getAllProductsData().stream()
                .filter(s -> {
                    return !selectedStoreProperty.getValue().getProducts().contains(s);
                })
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                FXCollections::observableArrayList));
    }

    @FXML
    void onUpdateOptionSelected(ActionEvent event)
    {
        try
        {
            loadProducts();
        }
        catch (Exception e)
        {
//            e.printStackTrace();
            Utilities.ShowErrorAlert(e.getMessage());
        }
    }

    @FXML
    void updateProduct(ActionEvent event)
    {
        try
        {
            String successMassage = activateUpdateProductLogic();
            Utilities.ShowInformationAlert(String.format(successMassage,selectedProductProperty.getValue().getName(),selectedStoreProperty.getValue().getName()));
            loadStores();
            loadProducts();
        }
        catch (DiscountRemoveException e)
        {
            Utilities.ShowInformationAlert(e.getMessage());
            loadStores();
            loadProducts();
        }
        catch (Exception e)
        {
//            e.printStackTrace();
            Utilities.ShowErrorAlert(e.getMessage());
        }
    }

    private String activateUpdateProductLogic() throws ValidationException
    {
        String successMassage = "";
        switch (selectedOptionProperty.getValue())
        {
            case ADD:
                systemManager.addProductToStore(selectedStoreProperty.getValue(),selectedProductProperty.getValue(),selectedPrice.getValue());
                successMassage = PRODUCT_ADDED_SUCCESSFULLY_MESSAGE;
                break;
            case REMOVE:
                systemManager.removeProductFromStore(selectedStoreProperty.getValue(),selectedProductProperty.getValue());
                successMassage = PRODUCT_REMOVED_SUCCESSFULLY_MESSAGE;
                break;
            case UPDATE_PRICE:
                systemManager.updateProductPriceInStore(selectedStoreProperty.getValue(),selectedProductProperty.getValue(),selectedPrice.getValue());
                successMassage = PRODUCT_UPDATED_PRICE_SUCCESSFULLY_MESSAGE;
                break;
        }
        return successMassage;
    }
}
