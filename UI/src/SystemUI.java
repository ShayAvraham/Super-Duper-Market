import com.sun.org.apache.bcel.internal.generic.RETURN;
import exceptions.DuplicateValuesException;
import exceptions.InstanceNotExistException;
import exceptions.StoreDoesNotSellProductException;
import exceptions.UserLocationEqualToStoreException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationException;
import java.awt.*;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SystemUI
{
    public enum StartMenuOptions
    {
        LoadFile,
        ShowAllStores,
        ShowAllProducts,
        MakePurchase,
        ViewOrdersHistory,
        UpdateStoreProducts,
        Quit
    }

    private enum UpdateProductOptions
    {
        RemoveProduct,
        AddProduct,
        UpdateProductPrice
    }

    private enum OrderTypeOptions
    {
        Static,
        Dynamic
    }

    private final String WELCOME_MESSAGE = "\nHello, welcome to super duper market!";
    private final String CHOICE_OUT_OF_RANGE_MESSAGE = "Choice is out of range of valid values";
    private final String OPTION_NOT_VALID_MESSAGE = "Sorry, this option is not valid! You need to load file before.";
    private final String REENTER_ACTION_MESSAGE = "Please reenter the desired action number: ";
    private final String ENTER_FILE_PATH_MESSAGE = "Please enter the path of the desired xml file to load: ";
    private final String LOAD_FILE_FAUILE_MESSAGE = "Faild to load the file, Cause of failure:";
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
    private final String NUMBER_OF_STORES_SELL_PRODUCT_MESSAGE = "Number of stores who sell this product: %1$s\n";
    private final String AVERAGE_PRICE_OF_PRODUCT_MESSAGE = "Average price: %1$s \n";
    private final String TOTAL_AMOUNT_OF_PRODUCT_SOLD_IN_SYSTEM_MESSAGE = "Number of times the product sold in the system: %1$s \n";
    private final String ORDER_DATE_MESSAGE = "Date: %1$s \n";
    private final String TOTAL_AMOUNT_OF_PRODUCT_IN_ORDER_MESSAGE = "Total amount of products: %1$s\n";
    private final String TOTAL_COST_OF_ALL_PRODUCTS_IN_ORDER_MESSAGE = "Total cost of all products: %1$s\n";
    private final String DELIVERY_COST_OF_ORDER_MESSAGE = "Delivery cost: %1$s\n";
    private final String TOTAL_COST_OF_ORDER_MESSAGE = "Total order cost: %1$s\n";
    private final String FILE_LOADED_SUCCESSFULLY_MESSAGE = "File loaded successfully!";
    private final String ALL_AVAILABLE_STORES_TO_BUY_MESSAGE = "All available stores in the system:\n%1$s";
    private final String AVAILABLE_STORE_TO_BUY_MESSAGE = "%1$s. %2$s\n   PPK: %3$s\n\n";
    private final String ALL_AVAILABLE_PRODUCTS_TO_BUY_MESSAGE = "All available products in the system:\n%1$s";
    private final String AVAILABLE_PRODUCT_TO_BUY_MESSAGE = "%1$s. %2$s\n   Purchase form: %3$s\n\n";
    private final String STORE_NUMBER_MESSAGE = "Store No. %1$s\n";
    private final String PRODUCT_NUMBER_MESSAGE = "Product No. %1$s\n";
    private final String ORDER_NUMBER_MESSAGE = "Order No. %1$s\n";
    private final String NO_ORDERS_IN_SYSTEM_MESSAGE = "Thers is no orders in the system.\n";
    private final String NUM_OF_STORES_IN_ORDER_MESSAGE = "Number of stores: %1$s\n";
    private final String NUM_OF_PRODUCT_TYPES_IN_ORDER_MESSAGE = "Number of product types: %1$s\n";
    private final String STORE_ID_MESSAGE = "Store id: %1$s\n";
    private final String STORE_NAME_MESSAGE = "Store name: %1$s\n";
    private final String ALL_ORDERS_IN_SYSTEM_MESSAGE = "The orders in the system:\n%1$s";
    private final String GET_TYPE_OF_ORDER_FROM_USER_MESSAGE = "Please select which type of order you want:\n";
    private final String ORDER_TYPE_MESSAGE = "%1$s. %2$s\n";
    private final String SHOW_OBJECT_MESSAGE = "%1$s. %2$s\n";
    private final String QUIT_CHARACTER = "q";
    private final String FINISH_CHARACTER = "f";
    private final String APPROVE_CHARACTER = "t";
    private static final String GET_APPROVE_ORDER_FROM_USER_MESSAGE = "To proceed with the order press 't', to cancel press 'f':";
    private final String GET_DATE_FROM_USER_MESSAGE = "Please enter date:";
    private final String ORDER_SUMMERY_MESSAGE = "Your order:\n%1$s";
    private final String PRODUCT_IN_ORDER_SUMMERY_MESSAGE = "ID: %1$s\nName: %2$s\nPurchase form: %3$s\n" +
                                                             "Price: %4$s\nAmount: %5$s\n" +
                                                             "Total price: %6$s\n";


    private SystemManager manager = new SystemManager();

    public void run()
    {
        int userStartMenuChoice = 0;

        System.out.println(WELCOME_MESSAGE);
        while (userStartMenuChoice != StartMenuOptions.Quit.ordinal())
        {
            displayMenu();
            userStartMenuChoice = getUserMenuChoice(StartMenuOptions.class);
            try
            {
                executeUserSelectedAction(StartMenuOptions.values()[userStartMenuChoice]);
            }
            catch(Exception ex)
            {
                System.out.println("\n" + ex.getMessage());
            }
        }
        System.out.println("\n" + QUIT_MESSAGE);
    }

    private void displayMenu()
    {
        String menuStr = "\nPlease choose one of the following action:\n" +
        "1. Load data from file.\n" +
        "2. Show all stores details.\n" +
        "3. Show all products in the system.\n" +
        "4. Place an order in the system.\n" +
        "5. View orders history in the system.\n" +
        "6. Update prices/product of a store.\n"+
        "7. Quit.\n" +
        "The desired action number: ";
        System.out.print(menuStr);
    }

    private int getUserMenuChoice(Class<? extends Enum> enumClass)
    {
        while (true)
        {
            try
            {
                Scanner scanner = new Scanner(System.in);
                int userMenuChoice = scanner.nextInt();
                validateUserChoice(userMenuChoice,enumClass);
                return userMenuChoice - 1;
            }
            catch (IndexOutOfBoundsException ex)
            {
                System.out.print("\n" + ex.getMessage() + "\n" + REENTER_ACTION_MESSAGE);
            }
            catch (InputMismatchException ex)
            {
                System.out.print("\n" + "your choice is not in the right format" + "\n" + REENTER_ACTION_MESSAGE);
            }
        }
    }

    private void validateUserChoice(int userStartChoiceInput, Class<? extends Enum> enumClass)
    {
        if (userStartChoiceInput > enumClass.getFields().length || userStartChoiceInput < 1)
        {
            throw new IndexOutOfBoundsException(CHOICE_OUT_OF_RANGE_MESSAGE);
        }
    }


    private void executeUserSelectedAction(StartMenuOptions userStartMenuChoise) throws Exception
    {
        if (userStartMenuChoise == StartMenuOptions.LoadFile)
        {
            loadDataFromXmlFile();
        }
        else if(userStartMenuChoise != StartMenuOptions.Quit)
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
                        makeOrder();
                        break;
                    case ViewOrdersHistory:
                        showAllOrdersHistory();
                        break;
                    case UpdateStoreProducts:
                        updateStoreProducts();
                }
            }
            else
            {
                throw new Exception(OPTION_NOT_VALID_MESSAGE);
            }
        }
    }

    private void updateStoreProducts()
    {
        StoreDataContainer selectedStore = getStoreFromUser();
        showUpdateStoreProductsMenu(selectedStore.getName());
        int userMenuChoice = getUserMenuChoice(UpdateProductOptions.class);
        switch (UpdateProductOptions.values()[userMenuChoice])
        {
            case RemoveProduct:
                removeProduct(selectedStore);
                break;
            case AddProduct:
                addProduct(selectedStore);
                break;
            case UpdateProductPrice:
                updateProductPrice(selectedStore);
                break;
        }
    }

    private void showUpdateStoreProductsMenu(String storeName)
    {
        System.out.print(String.format(
                "\nPlease choose one of the following action:\n" +
                "1. Remove product from %1$s.\n" +
                "2. Add product to %1$s.\n" +
                "3. Update price of existing product in %1$s.\n" +
                "The desired action number: ",storeName));
    }

    private void removeProduct(StoreDataContainer store)
    {
        showStoreProducts(store);
        ProductDataContainer product = getFromUserProductToUpdate(store,"remove",UpdateProductOptions.RemoveProduct);
        manager.removeProductFromStore(store,product);
        System.out.println(String.format("\nproduct %1$s remove successfully from %2$s",product.getId(),store.getName()));
    }

    private void showStoreProducts(StoreDataContainer store)
    {
        System.out.print(String.format("\n" + ALL_PRODUCTS_OF_STORE_MESSAGE, store.getName(), SEPARATOR_MESSAGE));
        for(ProductDataContainer product: store.getProducts())
        {
            System.out.print(createProductDetails(product));
        }
    }

    private ProductDataContainer getFromUserProductToUpdate(StoreDataContainer store, String optionDescription,UpdateProductOptions userOptionChoice)
    {
        while (true)
        {
            try
            {
                System.out.print(String.format("\nPlease select the product you want to %1$s by enter the product id: ",optionDescription));
                Scanner scanner = new Scanner(System.in);
                int userProductChoice = scanner.nextInt();
                validateUserProductChoice(store, userProductChoice,userOptionChoice);
                return (manager.getProductDataById(userProductChoice));
            }
            catch (StoreDoesNotSellProductException | InstanceNotExistException | ValidationException | DuplicateValuesException ex)
            {
                System.out.println("\n" + ex.getMessage() + " Please try again");
            }
            catch(InputMismatchException ex)
            {
                System.out.println("\n" + "your choice is not in the right format, Please try again");
            }
        }
    }

    private void validateUserProductChoice(StoreDataContainer store, int productId,UpdateProductOptions userOptionChoice) throws ValidationException
    {
        ProductDataContainer product = manager.getProductDataById(productId);
        if(product == null)
        {
            throw new InstanceNotExistException("product", productId);
        }
        if(userOptionChoice.equals(UpdateProductOptions.AddProduct))
        {
            if(store.getProducts().contains(product))
            {
                throw new DuplicateValuesException("product", productId);
            }
        }
        else
        {
            if(!store.getProducts().contains(product))
            {
                throw new StoreDoesNotSellProductException();
            }
            if(userOptionChoice.equals(UpdateProductOptions.RemoveProduct) && !isOthersStoresSellThisProduct(store, product))
            {
                throw new ValidationException("Unable to remove this product because its sold only in one store.");
            }
        }

    }

    private boolean isOthersStoresSellThisProduct(StoreDataContainer selectedStore, ProductDataContainer product)
    {
        for(StoreDataContainer store: manager.getAllStoresData())
        {
            if(!selectedStore.equals(store))
            {
                if(store.getProducts().contains(product))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private void addProduct(StoreDataContainer store)
    {
        System.out.print(String.format("\n",ALL_PRODUCTS_MESSAGE,SEPARATOR_MESSAGE));
        for(ProductDataContainer product : manager.getAllProductsData())
        {
            System.out.print(createProductDetails(product));
        }
        ProductDataContainer product = getFromUserProductToUpdate(store,"add", UpdateProductOptions.AddProduct);
        int productPrice = getFromUserProductPrice();

        manager.addProductToStore(store,product,productPrice);
        System.out.println(String.format("\nproduct %1$s add successfully to %2$s",product.getId(),store.getName()));
    }

    private int getFromUserProductPrice()
    {
        while (true)
        {
            try
            {
                System.out.print("\nPlease enter the purchase price: ");
                Scanner scanner = new Scanner(System.in);
                int productPrice = scanner.nextInt();
                return productPrice;
            }
            catch(InputMismatchException ex)
            {
                System.out.println("\n" + "your choice is not in the right format, Please try again");
            }
        }
    }


    private void updateProductPrice(StoreDataContainer store)
    {
        showStoreProducts(store);
        ProductDataContainer product = getFromUserProductToUpdate(store,"update price", UpdateProductOptions.UpdateProductPrice);
        int productPrice = getFromUserProductPrice();
        manager.updateProductPriceInStore(store,product,productPrice);
        System.out.println(String.format("\nproduct %1$s update price successfully in %2$s",product.getId(),store.getName()));
    }

    private void loadDataFromXmlFile() throws JAXBException, FileNotFoundException
    {
        try
        {
            System.out.print("\n" + ENTER_FILE_PATH_MESSAGE);
            Scanner scanner = new Scanner(System.in);
            String xmlFilePath = scanner.nextLine();
            manager.loadDataFromXmlFile(xmlFilePath);
            System.out.println("\n" + FILE_LOADED_SUCCESSFULLY_MESSAGE);
        }
        catch(Exception ex)
        {
            System.out.print("\n" + LOAD_FILE_FAUILE_MESSAGE);
            throw ex;
        }
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
                        allStoresMsg += createOrderDetailsForDisplayingAllStores(orderData);
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

    private void makeOrder()
    {
        try
        {
            OrderTypeOptions orderType = getOrderTypeFromUser();
            OrderDataContainer newOrderData = getNewOrderFromUser(orderType);
            if (newOrderData != null)
            {
                manager.addNewOrder(newOrderData);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private OrderDataContainer getNewOrderFromUser(OrderTypeOptions orderType)
    {
        OrderDataContainer newOrderData = null;
        switch (orderType)
        {
            case Static:
                newOrderData = getNewStaticOrderFromUser();
                break;
//            case Dynamic:
//                newOrderData = getNewDynamicOrderFromUser();
//                break;
        }
        return newOrderData;
    }

    private OrderDataContainer getNewStaticOrderFromUser()
    {
        OrderDataContainer newOrderData = null;
        int storeId = getStoreFromUser().getId();
        Date date = getDateFromUser();
        Point userLocation = getLocationFromUser(storeId);
        Collection<Integer> storeIdCollection = new ArrayList<>();
        storeIdCollection.add(storeId);
        Map<Integer, Float> amountPerProduct = getAllProductsAndQuantitiesInOrderFromUser(storeId);
        float deliveryCost = manager.getDeliveryCost(userLocation, storeIdCollection);
        boolean isOrderApproved =  getIsOrderApprovedByCustomer(amountPerProduct, storeId, deliveryCost);
        if (isOrderApproved)
        {
            newOrderData = new OrderDataContainer(date, deliveryCost, storeId, amountPerProduct);
        }
        return newOrderData;
    }

    //    private OrderDataContainer getNewDynamicOrderFromUser()
//    {
//        OrderDataContainer newOrderData = null;
//        Date date = getDateFromUser();
//        Point userLocation = getLocationFromUser();
//        Collection<ProductDataContainer> productsInOrder = getAllProductsInOrderFromUser();
//        Map<ProductDataContainer, StoreDataContainer> productPerStore = manager.dynamicStoreAllocation(productsInOrder);
//        Map<Integer, Float> amountPerProduct = getAmountPerProductInOrder(productsInOrder);
//        float deliveryCost = getDeliveryCost(userLocation);
//        boolean isOrderApproved = getIsOrderApprovedByCustomer(productsInOrder);
//        if (isOrderApproved)
//        {
//            newOrderData = new OrderDataContainer(date, deliveryCost,, amountPerProduct);
//        }
//        return newOrderData;
//    }

    private Map<Integer, Float> getAllProductsAndQuantitiesInOrderFromUser(int storeId)
    {
        Map<Integer, Float> allProductsInOrder = new HashMap<>();
        boolean isUserFinished = false;

        while (!isUserFinished)
        {
            try
            {
                System.out.println("Please select product from the list by enter the product number, or press 'q' to finish.");
                showAvailableProductsToBuy();
                Scanner scanner = new Scanner(System.in);
                String userInput = scanner.nextLine(); //note why not scanner.nextInt();
                if (isUserWantToQuitFromOrder(userInput))
                {
                    isUserFinished = true;
                }
                else
                {
                    int productId = getValidProductFromUser(userInput, storeId, manager.getAllProductsID());
                    float productAmount = getProductAmountFromUser(productId);
                    allProductsInOrder.put(productId, productAmount);
                }
            }
            catch (NumberFormatException e)
            {
                String errorMessage = "Sorry, the id you entered is not in the right format.\nPlease try again.";
                System.out.println(errorMessage);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
        return allProductsInOrder;
    }

    private float getProductAmountFromUser(int productId)
    {
        float productAmount = 0;
        boolean isUserFinished = false;

        while (!isUserFinished)
        {
            try
            {
                System.out.println("Please enter the amount of this product:");
                Scanner scanner = new Scanner(System.in);
                String userInput = scanner.nextLine();
                if (isUserWantToQuitFromOrder(userInput))
                {
                    isUserFinished = true;
                }
                else
                {
                    productAmount = getValidProductAmountFromUser(userInput, productId);
                    isUserFinished = true;
                }
            }
            catch (Exception e)
            {
                String errorMessage = "Sorry, the amount you entered is not in the right format.\nEnter the date again.";
                System.out.println(errorMessage);
            }
        }
        return productAmount;
    }

    private float getValidProductAmountFromUser(String userInput, int productId)
    {
        float productAmount = 0;
        try
        {
            ProductDataContainer product = manager.getProductDataById(productId);
            boolean isProductSoldByWieght = (product.getPurchaseForm() == ProductDataContainer.ProductPurchaseForm.WEIGHT);
            if (!isProductSoldByWieght)
            {
                productAmount = (float) Integer.parseInt(userInput);
            }
            else
            {
                productAmount = Float.parseFloat(userInput);
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("This amount is not in the right format.");
        }
        return productAmount;
    }


    private OrderTypeOptions getOrderTypeFromUser()
    {
        showOrderTypeOption();
        Scanner scanner = new Scanner(System.in);
        int userInput = scanner.nextInt();
        int userSelection = getValidOrderTypeFromUser(userInput);
        return OrderTypeOptions.values()[userSelection];
    }

    private StoreDataContainer getStoreFromUser()
    {
        StoreDataContainer userStoreSelection = null;
        while (true)
        {
            try
            {
                System.out.println("\nPlease select a store from the list by enter the store id, or press 'q' to finish:");
                showAvailableStoresToBuy();
                System.out.print("The desired store id: ");
                Scanner scanner = new Scanner(System.in);
                int userSelection = scanner.nextInt();
                userStoreSelection = getValidStoreFromUser(userSelection);
                break;
            }
            catch(IndexOutOfBoundsException ex)
            {
                System.out.println("\n" + ex.getMessage() + ", Please try again");
            }
            catch (InputMismatchException ex)
            {
                System.out.println("\nSorry, your choice is not in the right format, Please try again");
            }
        }
        return userStoreSelection;
    }

    private int getValidOrderTypeFromUser(int userSelection)
    {
        if (userSelection < 1 || userSelection > OrderTypeOptions.values().length)
        {
            throw new IndexOutOfBoundsException("Sorry, no such option.\nPlease try again.");
        }
        return userSelection - 1;
    }

    private void showOrderTypeOption()
    {
        String orderTypeMsg = "";
        orderTypeMsg += GET_TYPE_OF_ORDER_FROM_USER_MESSAGE;
        int orderTypeIndex = 1;
        for (OrderTypeOptions orderType: OrderTypeOptions.values())
        {
            orderTypeMsg += String.format(ORDER_TYPE_MESSAGE, orderTypeIndex, orderType);
            orderTypeIndex++;
        }
        System.out.println(orderTypeMsg);
    }

    private Date getDateFromUser()
    {
        Date userDate = null;
        System.out.println(GET_DATE_FROM_USER_MESSAGE);

        while (true)
        {
            try
            {
                Scanner scanner = new Scanner(System.in);
                String userDateString = scanner.nextLine();
                userDate = new SimpleDateFormat("dd/mm-hh:mm").parse(userDateString);
                break;
            }
            catch (ParseException e)
            {
                String errorMessage = "Sorry, the date you entered is not in the right format.\nPlease try again.";
                System.out.println(errorMessage);
            }
        }
        return userDate;
    }

    private Point getLocationFromUser(int storeId)
    {
        Point userLocation = null;
        while (true)
        {
            try
            {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please enter X cordinate:");
                int xCordinate = scanner.nextInt();
                System.out.println("Please enter Y cordinate:");
                int yCordinate = scanner.nextInt();
                userLocation = getValidLocationFromUser(new Point(xCordinate, yCordinate), storeId);
                break;
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
        return userLocation;
    }

    private Point getValidLocationFromUser(Point userLocation, int storeId)
    {
        if (userLocation.getX() < 1 || userLocation.getX() > 50 ||
                userLocation.getY() < 1 || userLocation.getY() > 50)
        {
            throw new IllegalArgumentException("This location is not valid!");
        }
        Point storeLocation = manager.getStoreDataById(storeId).getPosition();
        if (userLocation.equals(storeLocation))
        {
            throw new UserLocationEqualToStoreException(userLocation, storeLocation);
        }
        return userLocation;
    }

    private StoreDataContainer getValidStoreFromUser(int userSelection)
    {

        if (!manager.getAllStoresID().contains(userSelection))
        {
            throw new IndexOutOfBoundsException(CHOICE_OUT_OF_RANGE_MESSAGE);
        }
        return manager.getStoreDataById(userSelection);
    }


    private int getValidProductFromUser(String userInput, int storeId, Set<Integer> IdSet)
    {
        int userSelection = 0;

        try
        {
            userSelection = Integer.parseInt(userInput);
            if (!IdSet.contains(userSelection))
            {
                throw new IndexOutOfBoundsException(CHOICE_OUT_OF_RANGE_MESSAGE);
            }
            StoreDataContainer store = manager.getStoreDataById(storeId);
            Set<Integer> storeProductsId = new HashSet<>();
            for (ProductDataContainer productData: store.getProducts())
            {
                storeProductsId.add(productData.getId());
            }
            if (!storeProductsId.contains(userSelection))
            {
                throw new NoSuchElementException("This product is not for sale in this store.");
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Sorry, your choise is not in the right format.\nPlease try again.");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return userSelection;
    }


    private boolean getIsOrderApprovedByCustomer(Map<Integer, Float> amountPerProduct, int storeId, float deliveryCost)
    {
        boolean isOrderApproved = false;
        boolean isUserFinished = false;

        while (!isUserFinished)
        {
            String orderSummary = createOrderSummary(amountPerProduct, storeId, deliveryCost);
            System.out.println(orderSummary);
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();
            if (userInput.toLowerCase().equals(QUIT_CHARACTER) || userInput.toLowerCase().equals(FINISH_CHARACTER))
            {
                isUserFinished = true;
            }
            else
            {
                if (userInput.toLowerCase().equals(APPROVE_CHARACTER))
                {
                    isOrderApproved = true;
                    isUserFinished = true;
                }
            }
        }
        return isOrderApproved;
    }

    private String createOrderSummary(Map<Integer, Float> amountPerProduct, int storeId, float deliveryCost)
    {
        String orderSummaryMsg = String.format(ORDER_SUMMERY_MESSAGE, SEPARATOR_MESSAGE);
        int productIndex = 1;
        for (Integer productId: amountPerProduct.keySet())
        {
            ProductDataContainer productData = manager.getProductDataById(productId);
            int productPrice = manager.getStoreDataById(storeId).getProductDataContainerById(productId)
                    .getPricePerStore().get(storeId);
            float totalPriceForProduct = productPrice * amountPerProduct.get(productId);
            orderSummaryMsg += String.format(PRODUCT_NUMBER_MESSAGE, productIndex);
            orderSummaryMsg += String.format(PRODUCT_IN_ORDER_SUMMERY_MESSAGE,
                    productData.getId(), productData.getName(),
                    productData.getPurchaseForm().toString().toLowerCase(),
                    productPrice, amountPerProduct.get(productId), totalPriceForProduct);
            orderSummaryMsg += SEPARATOR_MESSAGE;
            productIndex++;
        }
        orderSummaryMsg += String.format(DELIVERY_COST_OF_ORDER_MESSAGE, deliveryCost);
        orderSummaryMsg += SEPARATOR_MESSAGE;
        orderSummaryMsg += GET_APPROVE_ORDER_FROM_USER_MESSAGE;
        return orderSummaryMsg;
    }

    private void showAvailableProductsToBuy()
    {
        String avialableProductsToBuyMsg = "";
        avialableProductsToBuyMsg += String.format(ALL_AVAILABLE_PRODUCTS_TO_BUY_MESSAGE, SEPARATOR_MESSAGE);
        for (ProductDataContainer productData: manager.getAllProductsData())
        {
            avialableProductsToBuyMsg += String.format(
                    AVAILABLE_PRODUCT_TO_BUY_MESSAGE, productData.getId(), productData.getName(),
                    productData.getPurchaseForm().toString().toLowerCase());
        }
        System.out.println(avialableProductsToBuyMsg);
    }

    private void showAvailableStoresToBuy()
    {
        String avialableStoresToBuyMsg = "";
        avialableStoresToBuyMsg += String.format(ALL_AVAILABLE_STORES_TO_BUY_MESSAGE, SEPARATOR_MESSAGE);
        for (StoreDataContainer storeData: manager.getAllStoresData())
        {
            avialableStoresToBuyMsg += String.format(
                    AVAILABLE_STORE_TO_BUY_MESSAGE, storeData.getId(), storeData.getName(), storeData.getPPK());
        }
        System.out.print(avialableStoresToBuyMsg);
    }


    private void showAllOrdersHistory()
    {
        String allOrdersMsg = "";
        if (manager.getAllOrdersData().size() > 0)
        {
            int orderIndex = 1;
            allOrdersMsg += String.format(ALL_ORDERS_IN_SYSTEM_MESSAGE, SEPARATOR_MESSAGE);
            for (OrderDataContainer orderData: manager.getAllOrdersData())
            {
                allOrdersMsg += String.format(ORDER_NUMBER_MESSAGE, orderIndex);
                allOrdersMsg += createOrderDetailsForDisplayingAllOrdersHistory(orderData);
                orderIndex++;
            }
        }
        else
        {
            allOrdersMsg += NO_ORDERS_IN_SYSTEM_MESSAGE;
        }
        System.out.println(allOrdersMsg);
    }

    private boolean isUserWantToQuitFromOrder(String userInput)
    {
        if (userInput.toLowerCase().equals(QUIT_CHARACTER))
        {
            return true;
        }
        return false;
    }

    private String createOrderDetailsForDisplayingAllOrdersHistory(OrderDataContainer orderData)
    {
        String orderDetails =
                String.format(ID_MESSAGE, orderData.getId()) +
                        String.format(ORDER_DATE_MESSAGE, orderData.getDate());
        if (orderData.isDynamic())
        {
            orderDetails += String.format(NUM_OF_STORES_IN_ORDER_MESSAGE, orderData.getNumOfProducts());
        }
        else
        {
            orderDetails += String.format(STORE_ID_MESSAGE, orderData.getStoreId());
            orderDetails += String.format(STORE_NAME_MESSAGE, orderData.getStoreName());
        }
        orderDetails +=
                String.format(NUM_OF_PRODUCT_TYPES_IN_ORDER_MESSAGE, orderData.getNumOfProductTypes()) +
                        String.format(TOTAL_AMOUNT_OF_PRODUCT_IN_ORDER_MESSAGE, orderData.getNumOfProducts()) +
                        String.format(TOTAL_COST_OF_ALL_PRODUCTS_IN_ORDER_MESSAGE, orderData.getCostOfAllProducts()) +
                        String.format(DELIVERY_COST_OF_ORDER_MESSAGE, orderData.getDeliveryCost()) +
                        String.format(TOTAL_COST_OF_ORDER_MESSAGE, orderData.getTotalCost()) +
                        SEPARATOR_MESSAGE;
        return orderDetails;
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

    private String createOrderDetailsForDisplayingAllStores(OrderDataContainer orderData)
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
}
