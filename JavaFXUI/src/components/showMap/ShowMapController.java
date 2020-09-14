package components.showMap;

import common.Utilities;
import components.main.MainAppController;
import dataContainers.CustomerDataContainer;
import dataContainers.StoreDataContainer;
import engineLogic.SystemManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.Comparator;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

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
            Tooltip customerInfo = createCustomerInfoToolTip(customer);
            Label label = new Label("c");
            label.setTooltip(customerInfo);
            map.add(label, (int) customer.getPosition().getX(), (int) customer.getPosition().getY());
        }
    }

    private void addAllStoresToMap()
    {
        for (StoreDataContainer store: systemLogic.getAllStoresData())
        {
            Tooltip customerInfo = createStoreInfoToolTip(store);
            Label label = new Label("s");
            label.setTooltip(customerInfo);
            map.add(label, (int) store.getPosition().getX(), (int) store.getPosition().getY());
        }
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
        map.getChildren().removeAll();
    }

    private void updateMapCoordinates()
    {
        maxXCoordinate = max(systemLogic.getAllStoresData().stream().map(store -> (int)store.getPosition().getX()).max(Comparator.naturalOrder()).get(),
                             systemLogic.getAllCustomersData().stream().map(customer -> (int)customer.getPosition().getX()).max(Comparator.naturalOrder()).get()) + 1;
        minXCoordinate = min(systemLogic.getAllStoresData().stream().map(store -> (int)store.getPosition().getX()).min(Comparator.naturalOrder()).get(),
                             systemLogic.getAllCustomersData().stream().map(customer -> (int)customer.getPosition().getX()).min(Comparator.naturalOrder()).get()) - 1;
        maxYCoordinate = max(systemLogic.getAllStoresData().stream().map(store -> (int)store.getPosition().getY()).max(Comparator.naturalOrder()).get(),
                             systemLogic.getAllCustomersData().stream().map(customer -> (int)customer.getPosition().getY()).max(Comparator.naturalOrder()).get()) + 1;
        minYCoordinate = min(systemLogic.getAllStoresData().stream().map(store -> (int)store.getPosition().getY()).min(Comparator.naturalOrder()).get(),
                             systemLogic.getAllCustomersData().stream().map(customer -> (int)customer.getPosition().getY()).min(Comparator.naturalOrder()).get()) - 1;
    }

    private void initMap()
    {
        for(int column = minXCoordinate; column < maxXCoordinate; column++)
        {
            map.getColumnConstraints().add(new ColumnConstraints(50));
        }
        for(int row = minYCoordinate; row < maxYCoordinate; row++)
        {
            map.getColumnConstraints().add(new ColumnConstraints(50));
        }
    }
}
