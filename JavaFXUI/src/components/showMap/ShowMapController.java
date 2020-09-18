package components.showMap;

import common.Utilities;
import components.main.MainAppController;
import dataContainers.CustomerDataContainer;
import dataContainers.StoreDataContainer;
import engineLogic.SystemManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.Comparator;

import static java.lang.Integer.*;

public class ShowMapController
{
    private final String STORE_INFO_MSG = "Location: %1$s\nID: %2$s\nName: %3$s\nPPK: %4$s\nOrders: %5$s\n";
    private final String CUSTOMER_INFO_MSG = "Location: %1$s\nID: %2$s\nName: %3$s\nOrders: %4$s\n";
    private MainAppController mainAppController;
    private SystemManager systemLogic;

    private GridPane map;
    private int maxXCoordinate;
    private int minXCoordinate;
    private int maxYCoordinate;
    private int minYCoordinate;

    @FXML
    private AnchorPane rootPane;

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
        this.systemLogic = systemManager;
    }

    public ShowMapController()
    {
        map = new GridPane();
        maxXCoordinate = 0;
        minXCoordinate = 0;
        maxYCoordinate = 0;
        minYCoordinate = 0;
    }

    @FXML
    void initialize()
    {
        rootPane.getChildren().add(map);
        rootPane.setLeftAnchor(map, 30.);
        rootPane.setTopAnchor(map, 30.);
        map.setStyle("-fx-border-color: #8b46db; -fx-border-radius: 20 20 20 20;");
    }

    public void updateMap()
    {
        clearMap();
        updateMapCoordinates();
        initMap();
        createNewMap();
    }

    private void createNewMap()
    {
        addAllStoresToMap();
        addAllCustomersToMap();
    }

    private void addAllCustomersToMap()
    {
        for (CustomerDataContainer customer: systemLogic.getAllCustomersData())
        {
            addCustomerInfoToMap(customer);
        }
    }

    private void addAllStoresToMap()
    {
        for (StoreDataContainer store: systemLogic.getAllStoresData())
        {
            addStoreInfoToMap(store);
        }
    }

    private void addCustomerInfoToMap(CustomerDataContainer customer)
    {
        Tooltip customerInfo = createCustomerInfoToolTip(customer);
        ImageView customerImage = new ImageView(new Image(getClass().getResourceAsStream("customerIcon.png")));
        Label label = new Label();
        label.setGraphic(customerImage);
        label.setTooltip(customerInfo);
        map.add(label, (int) customer.getPosition().getX(), maxYCoordinate - 1 - (int) customer.getPosition().getY());
    }

    private void addStoreInfoToMap(StoreDataContainer store)
    {
        Tooltip customerInfo = createStoreInfoToolTip(store);
        ImageView storeImage = new ImageView(new Image(getClass().getResourceAsStream("storeIcon.png")));
        Label label = new Label();
        label.setGraphic(storeImage);
        label.setTooltip(customerInfo);
        map.add(label, (int) store.getPosition().getX(), maxYCoordinate - 1 - (int) store.getPosition().getY());
    }

    private Tooltip createCustomerInfoToolTip(CustomerDataContainer customer)
    {
        Tooltip customerInfo = new Tooltip();
        customerInfo.setText(String.format(CUSTOMER_INFO_MSG, Utilities.convertPositionFormat(customer.getPosition()),
                customer.getId(),
                customer.getName(),
                customer.getNumOfOrders()));
        return customerInfo;
    }

    private Tooltip createStoreInfoToolTip(StoreDataContainer store)
    {
        Tooltip storeInfo = new Tooltip();
        storeInfo.setText(String.format(STORE_INFO_MSG, Utilities.convertPositionFormat(store.getPosition()),
                store.getId(),
                store.getName(),
                store.getPpk(),
                store.getOrders().size()));
        return storeInfo;
    }

    private void clearMap()
    {
        map.getChildren().removeAll(map.getChildren());
        map.getRowConstraints().removeAll(map.getRowConstraints());
        map.getColumnConstraints().removeAll(map.getColumnConstraints());
    }

    private void updateMapCoordinates()
    {
        maxXCoordinate = max(systemLogic.getAllStoresData().stream().map(store -> (int)store.getPosition().getX()).max(Comparator.naturalOrder()).get(),
                             systemLogic.getAllCustomersData().stream().map(customer -> (int)customer.getPosition().getX()).max(Comparator.naturalOrder()).get()) + 2;
        minXCoordinate = min(systemLogic.getAllStoresData().stream().map(store -> (int)store.getPosition().getX()).min(Comparator.naturalOrder()).get(),
                             systemLogic.getAllCustomersData().stream().map(customer -> (int)customer.getPosition().getX()).min(Comparator.naturalOrder()).get()) - 2;
        maxYCoordinate = max(systemLogic.getAllStoresData().stream().map(store -> (int)store.getPosition().getY()).max(Comparator.naturalOrder()).get(),
                             systemLogic.getAllCustomersData().stream().map(customer -> (int)customer.getPosition().getY()).max(Comparator.naturalOrder()).get()) + 2;
        minYCoordinate = min(systemLogic.getAllStoresData().stream().map(store -> (int)store.getPosition().getY()).min(Comparator.naturalOrder()).get(),
                             systemLogic.getAllCustomersData().stream().map(customer -> (int)customer.getPosition().getY()).min(Comparator.naturalOrder()).get()) - 2;
    }

    private void initMap()
    {
        for(int column = minXCoordinate; column < maxXCoordinate; column++)
        {
            map.getColumnConstraints().add(new ColumnConstraints(15));
        }
        for(int row = minYCoordinate; row < maxYCoordinate; row++)
        {
            map.getRowConstraints().add(new RowConstraints(15));
        }
    }
}
