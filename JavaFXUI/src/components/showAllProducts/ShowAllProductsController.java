package components.showAllProducts;

import common.Utilities;
//import components.main.MainAppController;
//import dataContainers.ProductDataContainer;
//import engineLogic.SystemManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;


public class ShowAllProductsController
{
//    private MainAppController mainAppController;
//    private SystemManager systemLogic;
//
//    @FXML
//    private AnchorPane rootPane;
//
//    @FXML
//    private TableView<ProductDataContainer> productsView;
//
//    @FXML
//    private TableColumn<ProductDataContainer, Integer> id;
//
//    @FXML
//    private TableColumn<ProductDataContainer, String> name;
//
//    @FXML
//    private TableColumn<ProductDataContainer, String> purchaseForm;
//
//    @FXML
//    private TableColumn<ProductDataContainer, Integer> numberOfStoresSellThisProduct;
//
//    @FXML
//    private TableColumn<ProductDataContainer, Float> averagePrice;
//
//    @FXML
//    private TableColumn<ProductDataContainer, Float> totalNumberOfSales;
//
//    @FXML
//    private void initialize()
//    {
//        setProductsTableColumnsProperties();
//    }
//
//    public AnchorPane getRootPane()
//    {
//        return rootPane;
//    }
//
//    public void setMainController(MainAppController mainAppController)
//    {
//        this.mainAppController = mainAppController;
//    }
//
//    public void setSystemLogic(SystemManager systemManager)
//    {
//        this.systemLogic = systemManager;
//    }
//
//    public void updateProductsTable()
//    {
//        productsView.refresh();
//        ObservableList<ProductDataContainer> productsList = FXCollections.observableArrayList();
//        productsList.addAll(systemLogic.getAllProductsData());
//        productsView.setItems(productsList);
//    }
//
//    private void setProductsTableColumnsProperties()
//    {
//        id.setCellValueFactory(new PropertyValueFactory<>("id"));
//        name.setCellValueFactory(new PropertyValueFactory<>("name"));
//        purchaseForm.setCellValueFactory(new PropertyValueFactory<>("purchaseForm"));
//        numberOfStoresSellThisProduct.setCellValueFactory(new PropertyValueFactory<>("numberOfStoresSellProduct"));
//        averagePrice.setCellValueFactory(new PropertyValueFactory<>("averagePrice"));
//        totalNumberOfSales.setCellValueFactory(cellData -> new SimpleObjectProperty<>(Float.valueOf(
//                Utilities.DECIMAL_FORMAT.format(cellData.getValue().getNumOfProductWasOrdered()))));
//    }
}