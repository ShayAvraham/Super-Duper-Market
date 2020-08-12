import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SystemUI
{
    private enum StartMenuOptions
    {
        LoadFile,
        ShowAllStores,
        ShowAllProducts,
        MakePurchase,
        ViewOrdersHistory,
        Quit
    }

    private static final String WELCOME_MESSAGE = "Hello, welcome to super duper market!";
    private static final String CHOICE_OUT_OF_RANGE_MESSAGE = "Choice is out of range of valid values";
    private static final String OPTION_NOT_VALID_MESSAGE = "Sorry, this option is not valid! You need to load file before.";
    private static final String REENTER_ACTION_MESSAGE = "Please reenter the desired action number:\n";
    private static final String ENTER_FILE_PATH_MESSAGE = "Please enter the path of the desired xml file to load:\n";
    private static final String QUIT_MESSAGE = "Bye bye, see you next time!";
    private static final String ALL_STORES_MESSAGE = "The stores in the system:\n%1$s";
    private static final String ALL_PRODUCTS_MESSAGE = "The products in the system:\n%1$s";
    private static final String SEPARATOR_MESSAGE = "---------------------------\n";
    private static final String ALL_PRODUCTS_OF_STORE_MESSAGE = "The products in %1$s store:\n%2$s";
    private static final String PPK_MESSAGE = "PPK: %1$s \n";
    private static final String TOTAL_CASH_FROM_DELIVERIES_MESSAGE = "Total cash from deliveries: %1$s \n";
    private static final String NO_STORES_MESSAGE = "There are no stores in the system.\n";
    private static final String NO_PRODUCTS_MESSAGE = "There are no products in the system.\n";
    private static final String ID_MESSAGE = "ID: %1$s \n";
    private static final String NAME_MESSAGE = "Name: %1$s \n";
    private static final String PURCHASE_FORM_OF_PRODUCT_MESSAGE = "The product is for sale by: %1$s \n";
    private static final String PRICE_MESSAGE = "Price: %1$s \n";
    private static final String TOTAL_AMOUNT_OF_PRODUCT_SOLD_IN_STORE_MESSAGE = "Total amount of this product sold in this store: %1$s \n";
    private static final String PRODUCT_NOT_SOLD_IN_STORE_MESSAGE = "This product is not yet sold in this store.\n";
    private static final String NUMBER_OF_STORES_SELL_PRODUCT_MESSAGE = "Number of stores who sell this product: %1$s \n";
    private static final String AVERAGE_PRICE_OF_PRODUCT_MESSAGE = "Average price: %1$s \n";
    private static final String TOTAL_AMOUNT_OF_PRODUCT_SOLD_IN_SYSTEM_MESSAGE = "Number of times the product sold in the system: %1$s \n";
    private static final String ORDER_DATE_MESSAGE = "Date: %1$s \n";
    private static final String TOTAL_AMOUNT_OF_PRODUCT_IN_ORDER_MESSAGE = "Total amount of products: %1$s \n";
    private static final String TOTAL_COST_OF_ALL_PRODUCTS_IN_ORDER_MESSAGE = "Total cost of all products: %1$s \n";
    private static final String DELIVERY_COST_OF_ORDER_MESSAGE = "Delivery cost: %1$s \n";
    private static final String TOTAL_COST_OF_ORDER_MESSAGE = "Total order cost: %1$s \n";
    private static final String FILE_LOADED_SUCCESSFULLY_MESSAGE = "File loaded successfully!";
    private static final String ALL_AVAILABLE_STORES_TO_BUY_MESSAGE = "All available stores in the system:\n%1$s";
    private static final String AVAILABLE_STORE_TO_BUY_MESSAGE = "%1$s. %2$s\n   PPK: %3$s\n\n";

    private SystemManager manager = new SystemManager();

    public void run()
    {
        int userStartMenuChoise = 0;

        System.out.println(WELCOME_MESSAGE);
        while (userStartMenuChoise != StartMenuOptions.Quit.ordinal())
        {
            displayMenu();
            userStartMenuChoise = getUserStartMenuChoise();
            try
            {
                executeUserSelectedAction(StartMenuOptions.values()[userStartMenuChoise]);
            }
            catch(Exception ex)
            {
                String errorMessage = "\n" + ex.getMessage() + "\n";
                System.out.println(errorMessage);
            }
        }
        System.out.println(QUIT_MESSAGE);
    }

    private void displayMenu()
    {
        String menuStr = "Please choose one of the following action:\n" +
        "1. Load data from file.\n" +
        "2. Show all stores details.\n" +
        "3. Show all products in the system.\n" +
        "4. Place an order in the system.\n" +
        "5. View orders history in the system.\n" +
        "6. Quit.\n" +
        "The desired action number:\n";
        System.out.println(menuStr);
    }

    private int getUserStartMenuChoise()
    {
        int userStartMenuChoise = 0;
        boolean isInputValid = false;

        while (!isInputValid)
        {
            try
            {
                Scanner scanner = new Scanner(System.in);
                int userStartChoiseInput = scanner.nextInt();
                userStartMenuChoise = getValidEnumChoise(userStartChoiseInput);
                break;
            }
            catch (Exception ex)
            {
                String errorMessage = "\n" + ex.getMessage() + "\n" + REENTER_ACTION_MESSAGE;
                System.out.println(errorMessage);
            }
        }

        return userStartMenuChoise;
    }

    private int getValidEnumChoise(int userStartChoiseInput) throws Exception
    {
        if (userStartChoiseInput > StartMenuOptions.values().length || userStartChoiseInput < 0)
        {
            throw new Exception(CHOICE_OUT_OF_RANGE_MESSAGE);
        }
        return userStartChoiseInput - 1;
    }


    private void executeUserSelectedAction(StartMenuOptions userStartMenuChoise) throws Exception
    {
        if (userStartMenuChoise == StartMenuOptions.LoadFile)
        {
            loadDataFromXmlFile();
        }
        else
        {
            if (manager.isFileWasLoadSuccessfully())
            {
                switch (userStartMenuChoise)
                {
                    case ShowAllStores:
                        showAllStores();
                        break;
                    case ShowAllProducts:
                        showAllProducts();
                        break;
                    case MakePurchase:
                        makePurchase();
                        break;
                    case ViewOrdersHistory:
                        showAllOrdersHistory();
                        break;
                    case Quit:
                        break;
                }
            }
            else
            {
                throw new Exception(OPTION_NOT_VALID_MESSAGE);
            }
        }
    }

    private void loadDataFromXmlFile() throws JAXBException, FileNotFoundException
    {
        System.out.println(ENTER_FILE_PATH_MESSAGE);
        Scanner scanner = new Scanner(System.in);
        String xmlFilePath = scanner.nextLine();
        manager.loadDataFromXmlFile(xmlFilePath);
        System.out.println(FILE_LOADED_SUCCESSFULLY_MESSAGE);
    }

    private void showAllOrdersHistory()
    {

    }

    private void makePurchase()
    {
        showAvailableStoresToBuy();
    }

    private void showAllStores()
    {
        String allStoresMsg = "";
        if (manager.getAllStores().size() > 0)
        {
            allStoresMsg += String.format(ALL_STORES_MESSAGE, SEPARATOR_MESSAGE);
            for (Store store : manager.getAllStores())
            {
                allStoresMsg += getStoreDetails(store);
                allStoresMsg += String.format(ALL_PRODUCTS_OF_STORE_MESSAGE, store.getName(), SEPARATOR_MESSAGE);
                for (StoreProduct product : store.getProductsInStore())
                {
                    allStoresMsg += getProductStatisticsForShowingStoresInSystem(store, product);
                }
                for (Order order: manager.getAllOrders(store))
                {
                    allStoresMsg += getOrderDetails(order);
                }
                allStoresMsg +=
                    String.format(PPK_MESSAGE, store.getPpk()) +
                    String.format(TOTAL_CASH_FROM_DELIVERIES_MESSAGE, manager.getStoreTotalCashFromDeliveries(store));
            }
        }
        else
        {
            allStoresMsg += NO_STORES_MESSAGE;
        }
        allStoresMsg += SEPARATOR_MESSAGE;
        System.out.println(allStoresMsg);
    }

    private void showAllProducts()
    {
        String allProductsMsg = "";
        if (manager.getAllProducts().size() > 0)
        {
            allProductsMsg += String.format(ALL_PRODUCTS_MESSAGE, SEPARATOR_MESSAGE);
            for (Product product : manager.getAllProducts())
            {
                allProductsMsg += getProductStatisticsForShowingProductsInSystem(product);
            }
        }
        else
        {
            allProductsMsg += NO_PRODUCTS_MESSAGE;
        }
        System.out.println(allProductsMsg);
    }


    private String getStoreDetails(Store store)
    {
        String storeDetails = "";
        storeDetails +=
                String.format(ID_MESSAGE, store.getId()) +
                String.format(NAME_MESSAGE, store.getName()) +
                SEPARATOR_MESSAGE;
        return storeDetails;
    }

    private String getProductDetails(Product product)
    {
        String productDetails =
                String.format(ID_MESSAGE, product.getId()) +
                String.format(NAME_MESSAGE, product.getName()) +
                String.format(PURCHASE_FORM_OF_PRODUCT_MESSAGE, product.getPurchaseForm().toString().toLowerCase());

        return productDetails;
    }

    private String getProductStatisticsForShowingStoresInSystem(Store store, StoreProduct product)
    {
        String productStatisticsDetails = getProductDetails(product.getProduct());
        productStatisticsDetails += String.format(PRICE_MESSAGE, product.getPrice());
        int howManyTimesProductSoldByStore = manager.getHowManyTimesProductSoldBySpecificStore(store, product.getProduct());

        if (howManyTimesProductSoldByStore > 0)
        {
            productStatisticsDetails += String.format(TOTAL_AMOUNT_OF_PRODUCT_SOLD_IN_STORE_MESSAGE, howManyTimesProductSoldByStore);
        }
        else
        {
            productStatisticsDetails += PRODUCT_NOT_SOLD_IN_STORE_MESSAGE;
        }
        productStatisticsDetails += SEPARATOR_MESSAGE;

        return productStatisticsDetails;
    }

    private String getProductStatisticsForShowingProductsInSystem(Product product)
    {
        String productStatisticsDetails = getProductDetails(product);
        int howManyTimesProductSoldInSystem = manager.getHowManyTimesProductSold(product);

        if (howManyTimesProductSoldInSystem > 0)
        {
            productStatisticsDetails +=
                String.format(NUMBER_OF_STORES_SELL_PRODUCT_MESSAGE, manager.getHowManyStoresSellProduct(product)) +
                String.format(AVERAGE_PRICE_OF_PRODUCT_MESSAGE, manager.getProductAvgPrice(product)) +
                String.format(TOTAL_AMOUNT_OF_PRODUCT_SOLD_IN_SYSTEM_MESSAGE, manager.getHowManyTimesProductSold(product));
        }
        else
        {
            productStatisticsDetails += PRODUCT_NOT_SOLD_IN_STORE_MESSAGE;
        }
        productStatisticsDetails += SEPARATOR_MESSAGE;

        return productStatisticsDetails;
    }

    private String getOrderDetails(Order order)
    {
        String orderDetails =
            String.format(ORDER_DATE_MESSAGE, order.getOrderDate()) +
            String.format(TOTAL_AMOUNT_OF_PRODUCT_IN_ORDER_MESSAGE, manager.getTotalAmountOfProductsInOrder(order)) +
            String.format(TOTAL_COST_OF_ALL_PRODUCTS_IN_ORDER_MESSAGE, manager.getTotalCostOfAllProductsInOrder(order) ) +
            String.format(DELIVERY_COST_OF_ORDER_MESSAGE, order.getDeliveryCost()) +
            String.format(TOTAL_COST_OF_ORDER_MESSAGE, manager.getTotalCostOfOrder(order)) +
            SEPARATOR_MESSAGE;
        return orderDetails;
    }

    private void showAvailableStoresToBuy()
    {
        String avialableStoresToBuyMsg = "";
        avialableStoresToBuyMsg += String.format(ALL_AVAILABLE_STORES_TO_BUY_MESSAGE, SEPARATOR_MESSAGE);
        for (Store store: manager.getAllStores())
        {
            avialableStoresToBuyMsg += String.format(
                    AVAILABLE_STORE_TO_BUY_MESSAGE, store.getId(), store.getName(), store.getPpk());
        }
        System.out.println(avialableStoresToBuyMsg);
    }
}
