package components.updateProducts;

import common.Utilities;
import components.main.MainAppController;
import dataContainers.StoreDataContainer;
import engineLogic.SystemManager;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.ResourceBundle;
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
    private ComboBox<String> storeComboBox;

    @FXML
    private ComboBox<String> productComboBox;

    @FXML
    private ComboBox<?> updateOptionComboBox;

    @FXML
    private Spinner<?> priceSpinner;

    @FXML
    private Button submitButton;

    private SystemManager systemManager;

    private SimpleListProperty<String> storesNamesProperty;
    private SimpleListProperty<String> storeProductsNamesProperty;
    private SimpleListProperty<String> updateOptionPropertyProperty;

    public AnchorPane getRootPane()
    {
        return rootPane;
    }

    public UpdateProductsController()
    {
        storesNamesProperty = new SimpleListProperty<>();
        storeProductsNamesProperty = new SimpleListProperty<>();
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
        storesNamesProperty.setValue(systemManager.getAllStoresData().stream()
                .map(store -> store.getName())
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                            l -> FXCollections.observableArrayList(l))));
    }

    @FXML
    void initialize()
    {
        assert rootPane != null : "fx:id=\"rootPane\" was not injected: check your FXML file 'UpdateProducts.fxml'.";
        assert storeComboBox != null : "fx:id=\"storeComboBox\" was not injected: check your FXML file 'UpdateProducts.fxml'.";
        assert productComboBox != null : "fx:id=\"productComboBox\" was not injected: check your FXML file 'UpdateProducts.fxml'.";
        assert updateOptionComboBox != null : "fx:id=\"updateOptionComboBox\" was not injected: check your FXML file 'UpdateProducts.fxml'.";
        assert priceSpinner != null : "fx:id=\"priceSpinner\" was not injected: check your FXML file 'UpdateProducts.fxml'.";
        assert submitButton != null : "fx:id=\"submitButton\" was not injected: check your FXML file 'UpdateProducts.fxml'.";
        storeComboBox.setItems(storesNamesProperty);
        productComboBox.setItems(storeProductsNamesProperty);
    }

    @FXML
    void updateProduct(ActionEvent event)
    {

    }

    @FXML
    void showAllStoreProducts(ActionEvent event)
    {
        storeProductsNamesProperty.setValue(systemManager.getAllStoresData().stream()
                .filter(s -> {return s.getName().equals(storeComboBox.getValue());})
                .map(store -> store.getName())
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),
                                l -> FXCollections.observableArrayList(l))));
    }

}
