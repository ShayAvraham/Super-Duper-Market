import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;

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

    private final String WELCOME_MESSAGE = "Hello, welcome to super duper market!";
    private final String CHOICE_OUT_OF_RANGE_MESSAGE = "Choice is out of range of valid values";
    private final String OPTION_NOT_VALID_MESSAGE = "Sorry, this option is not valid! You need to load file before.";
    private final String REENTER_ACTION_MESSAGE = "Please reenter the desired action number:\n";
    private final String ENTER_FILE_PATH_MESSAGE = "Please enter the path of the desired xml file to load:\n";
    private final String LOAD_FILE_FAUILE_MESSAGE = "Unable to load the file";
    private final String QUIT_MESSAGE = "Bye bye, see you next time!";
    private final String ALL_STORES_MESSAGE = "The stores in the system:\n%1$s";
    private final String ALL_PRODUCTS_MESSAGE = "The products in the system:\n%1$s";
    private final String SEPARATOR_MESSAGE = "=========================\n";
    private final String ALL_PRODUCTS_OF_STORE_MESSAGE = "The products in %1$s store:\n%2$s";
    private static final String ALL_ORDERS_OF_STORE_MESSAGE = "The orders of %1$s store:\n%2$s";
    private final String PPK_MESSAGE = "PPK: %1$s \n";
    private final String TOTAL_INCOME_FROM_DELIVERIES_MESSAGE = "Total in come from deliveries: %1$s \n";
    private final String NO_STORES_MESSAGE = "There are no stores in the system.\n";
    private final String NO_PRODUCTS_IN_SYSTEM_MESSAGE = "There are no products in the system.\n";
    private final String NO_PRODUCTS_IN_STORE_MESSAGE = "This store has no products for sale.\n";
    private final String NO_ORDERS_IN_STORE_MESSAGE = "This store has no orders.\n";
    private final String ID_MESSAGE = "ID: %1$s \n";
    private final String NAME_MESSAGE = "Name: %1$s \n";
    private final String PURCHASE_FORM_OF_PRODUCT_MESSAGE = "The product is for sale by: %1$s \n";
    private final String PRICE_MESSAGE = "Price: %1$s \n";
    private final String TOTAL_AMOUNT_OF_PRODUCT_SOLD_IN_STORE_MESSAGE = "Total amount of this product sold in this store: %1$s \n";
    private final String PRODUCT_NOT_SOLD_IN_ANY_STORE_MESSAGE = "This product is not sold in any store.\n";
    private final String NUMBER_OF_STORES_SELL_PRODUCT_MESSAGE = "Number of stores who sell this product: %1$s \n";
    private final String AVERAGE_PRICE_OF_PRODUCT_MESSAGE = "Average price: %1$s \n";
    private final String TOTAL_AMOUNT_OF_PRODUCT_SOLD_IN_SYSTEM_MESSAGE = "Number of times the product sold in the system: %1$s \n";
    private final String ORDER_DATE_MESSAGE = "Date: %1$s \n";
    private final String TOTAL_AMOUNT_OF_PRODUCT_IN_ORDER_MESSAGE = "Total amount of products: %1$s \n";
    private final String TOTAL_COST_OF_ALL_PRODUCTS_IN_ORDER_MESSAGE = "Total cost of all products: %1$s \n";
    private final String DELIVERY_COST_OF_ORDER_MESSAGE = "Delivery cost: %1$s \n";
    private final String TOTAL_COST_OF_ORDER_MESSAGE = "Total order cost: %1$s \n";
    private final String FILE_LOADED_SUCCESSFULLY_MESSAGE = "File loaded successfully!";
    private final String ALL_AVAILABLE_STORES_TO_BUY_MESSAGE = "All available stores in the system:\n%1$s";
    private final String AVAILABLE_STORE_TO_BUY_MESSAGE = "%1$s. %2$s\n   PPK: %3$s\n\n";
    private final String ALL_AVAILABLE_PRODUCTS_TO_BUY_MESSAGE = "All available products in the system:\n%1$s";
    private final String AVAILABLE_PRODUCT_TO_BUY_MESSAGE = "%1$s. %2$s\n   Purchase form: %3$s\n\n";
    private final String STORE_NUMBER_MESSAGE = "Store No. %1$s\n";
    private final String PRODUCT_NUMBER_MESSAGE = "Product No. %1$s\n";
    private final String ORDER_NUMBER_MESSAGE = "Order No. %1$s\n";

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


    private void loadDataFromXmlFile() throws JAXBException, FileNotFoundException //change1
    {
        try
        {
            System.out.println(ENTER_FILE_PATH_MESSAGE);
            Scanner scanner = new Scanner(System.in);
            String xmlFilePath = scanner.nextLine();
            manager.loadDataFromXmlFile(xmlFilePath);
            System.out.println(FILE_LOADED_SUCCESSFULLY_MESSAGE);
        }
        catch(Exception exp)
        {
            System.out.println(LOAD_FILE_FAUILE_MESSAGE);
            throw  exp;
        }
    }

    private void showAllOrdersHistory()
    {

    }

    private void showAllStores()
    {
        String allStoresMsg = "";
        if (manager.getAllStoresData().size() > 0)
        {
            int storeIndex = 1;
            allStoresMsg += String.format(ALL_STORES_MESSAGE, SEPARATOR_MESSAGE);
            for (StoreDataContainer storeData : manager.getAllStoresData())
            {
                allStoresMsg += String.format(STORE_NUMBER_MESSAGE, storeIndex);
                allStoresMsg += createStoreDetails(storeData);
                if (storeData.getProducts().size() > 0)
                {
                    int productIndex = 1;
                    allStoresMsg += String.format(ALL_PRODUCTS_OF_STORE_MESSAGE, storeData.getName(), SEPARATOR_MESSAGE);
                    for (ProductDataContainer productData : storeData.getProducts())
                    {
                        allStoresMsg += String.format(PRODUCT_NUMBER_MESSAGE, productIndex);
                        allStoresMsg += createProductDetailsForDisplayingAllStores(storeData, productData);
                        productIndex++;
                    }
                }
                else
                {
                    allStoresMsg += NO_PRODUCTS_IN_STORE_MESSAGE;
                }
                if (storeData.getOrders().size() > 0)
                {
                    int orderIndex = 1;
                    allStoresMsg += String.format(ALL_ORDERS_OF_STORE_MESSAGE, storeData.getName(), SEPARATOR_MESSAGE);
                    for (OrderDataContainer orderData : storeData.getOrders())
                    {
                        allStoresMsg += String.format(ORDER_NUMBER_MESSAGE, orderIndex);
                        allStoresMsg += createOrderDetails(orderData);
                        orderIndex++;
                    }
                }
                else
                {
                    allStoresMsg += NO_ORDERS_IN_STORE_MESSAGE;
                }
                allStoresMsg +=
                    String.format(PPK_MESSAGE, storeData.getPPK()) +
                    String.format(TOTAL_INCOME_FROM_DELIVERIES_MESSAGE, storeData.getTotalIncomeFromDeliveries());
                allStoresMsg += SEPARATOR_MESSAGE;
                storeIndex++;
            }
        }
        else
        {
            allStoresMsg += NO_STORES_MESSAGE;
        }
        System.out.println(allStoresMsg);
    }

    private void showAllProducts()
    {
        String allProductsMsg = "";
        if (manager.getAllProductsData().size() > 0)
        {
            int productIndex = 1;
            allProductsMsg += String.format(ALL_PRODUCTS_MESSAGE, SEPARATOR_MESSAGE);
            for (ProductDataContainer productData : manager.getAllProductsData())
            {
                allProductsMsg += String.format(PRODUCT_NUMBER_MESSAGE, productIndex);
                allProductsMsg += createProductDetailsForDisplayingAllProducts(productData);
                productIndex++;
            }
        }
        else
        {
            allProductsMsg += NO_PRODUCTS_IN_SYSTEM_MESSAGE;
        }
        System.out.println(allProductsMsg);
    }


    private String createStoreDetails(StoreDataContainer storeData)
    {
        String storeDetails = "";
        storeDetails +=
                String.format(ID_MESSAGE, storeData.getId()) +
                String.format(NAME_MESSAGE, storeData.getName());
        return storeDetails;
    }

    private String createProductDetails(ProductDataContainer productData)
    {
        String productDetails =
                String.format(ID_MESSAGE, productData.getId()) +
                String.format(NAME_MESSAGE, productData.getName()) +
                String.format(PURCHASE_FORM_OF_PRODUCT_MESSAGE, productData.getPurchaseForm().toString().toLowerCase());
        return productDetails;
    }

    private String createProductDetailsForDisplayingAllStores(StoreDataContainer storeData, ProductDataContainer productData)
    {
        String productStatisticsDetails = createProductDetails(productData);
        productStatisticsDetails += String.format(PRICE_MESSAGE, productData.getPricePerStore().get(storeData.getId()));
        float howManyTimesProductSoldByStore = productData.getSoldAmountPerStore().get(storeData.getId());

        if (howManyTimesProductSoldByStore > 0)
        {
            productStatisticsDetails += String.format(TOTAL_AMOUNT_OF_PRODUCT_SOLD_IN_STORE_MESSAGE, howManyTimesProductSoldByStore);
        }
        else
        {
            productStatisticsDetails += PRODUCT_NOT_SOLD_IN_ANY_STORE_MESSAGE;
        }
        productStatisticsDetails += SEPARATOR_MESSAGE;

        return productStatisticsDetails;
    }

    private String createProductDetailsForDisplayingAllProducts(ProductDataContainer productData)
    {
        String productStatisticsDetails = createProductDetails(productData);
        int numOfStoresSellProduct = productData.getNumberOfStoresSellProduct();

        if (numOfStoresSellProduct > 0)
        {
            productStatisticsDetails +=
                String.format(NUMBER_OF_STORES_SELL_PRODUCT_MESSAGE, productData.getNumberOfStoresSellProduct()) +
                String.format(AVERAGE_PRICE_OF_PRODUCT_MESSAGE, productData.getAveragePrice()) +
                String.format(TOTAL_AMOUNT_OF_PRODUCT_SOLD_IN_SYSTEM_MESSAGE, productData.getNumOfProductWasOrdered());
        }
        else
        {
            productStatisticsDetails += PRODUCT_NOT_SOLD_IN_ANY_STORE_MESSAGE;
        }
        productStatisticsDetails += SEPARATOR_MESSAGE;

        return productStatisticsDetails;
    }

    private String createOrderDetails(OrderDataContainer orderData)
    {
        String orderDetails =
            String.format(ORDER_DATE_MESSAGE, orderData.getDate()) +
            String.format(TOTAL_AMOUNT_OF_PRODUCT_IN_ORDER_MESSAGE, orderData.getNumOfProducts()) +
            String.format(TOTAL_COST_OF_ALL_PRODUCTS_IN_ORDER_MESSAGE, orderData.getCostOfAllProducts()) +
            String.format(DELIVERY_COST_OF_ORDER_MESSAGE, orderData.getDeliveryCost()) +
            String.format(TOTAL_COST_OF_ORDER_MESSAGE, orderData.getTotalCost()) +
            SEPARATOR_MESSAGE;
        return orderDetails;
    }

    private void makePurchase()
    {
//        try
//        {
//            int storeId = getStoreIdFromUser();
//            Date date = getDateFromUser();
//            Point location = getLocationFromUser(storeId);
//            Product product = getProdeuctFromUser(storeId);
//
//        }
//        catch (Exception e)
//        {
//            System.out.println(e.getMessage());
//        }
    }

//    private int getStoreIdFromUser() throws Exception
//    {
//        showAvailableStoresToBuy();
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Please select a store from the list:");
//        int userInput = scanner.nextInt();
//        int userStoreSelection = getValidChoiseFromUser(userInput, manager.getAllStoresID());
//        return userStoreSelection;
//    }
//
//    private Date getDateFromUser() throws ParseException
//    {
//        System.out.println("Please enter date:");
//        Scanner scanner = new Scanner(System.in);
//        String userDateString = scanner.nextLine();
//        Date userDate = new SimpleDateFormat("dd/mm-hh:mm").parse(userDateString);
//        return userDate;
//    }
//
//    private Point getLocationFromUser(int storeId)
//    {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Please enter location:");
//        System.out.println("Please enter X cordinate:");
//        int xCordinate = scanner.nextInt();
//        System.out.println("Please enter Y cordinate:");
//        int yCordinate = scanner.nextInt();
//        Point userLocation = new Point(xCordinate, yCordinate);
//        manager.checkIsUserLocationValid(userLocation, storeId);
//        return userLocation;
//    }
//
//    private Product getProdeuctFromUser(int storeId) throws Exception
//    {
//        showAvailableProductsToBuy(storeId);
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Please select product from the list:");
//        int userInput = scanner.nextInt();
//        int productId = getValidChoiseFromUser(userInput, manager.getAllProductsID());
//        manager.checkIsStoreSellProduct(productId, storeId);
//        return manager.getProductById(productId);
//    }
//
//    private void showAvailableProductsToBuy(int storeId)
//    {
//        String avialableProductsToBuyMsg = "";
//        avialableProductsToBuyMsg += String.format(ALL_AVAILABLE_PRODUCTS_TO_BUY_MESSAGE, SEPARATOR_MESSAGE);
//        for (Product product: manager.getAllProducts())
//        {
//            avialableProductsToBuyMsg += String.format(
//                    AVAILABLE_PRODUCT_TO_BUY_MESSAGE, product.getId(), product.getName(),
//                    product.getPurchaseForm().toString().toLowerCase());
//        }
//
//        System.out.println(avialableProductsToBuyMsg);
//    }
//
//    private void showAvailableStoresToBuy()
//    {
//        String avialableStoresToBuyMsg = "";
//        avialableStoresToBuyMsg += String.format(ALL_AVAILABLE_STORES_TO_BUY_MESSAGE, SEPARATOR_MESSAGE);
//        for (Store store: manager.getAllStores())
//        {
//            avialableStoresToBuyMsg += String.format(
//                    AVAILABLE_STORE_TO_BUY_MESSAGE, store.getId(), store.getName(), store.getPpk());
//        }
//        System.out.println(avialableStoresToBuyMsg);
//    }

    private int getValidChoiseFromUser(int userSelection, Set<Integer> idSet) throws Exception
    {
        if (!idSet.contains(userSelection))
        {
            throw new Exception(CHOICE_OUT_OF_RANGE_MESSAGE + REENTER_ACTION_MESSAGE);
        }
        return userSelection;
    }
}
