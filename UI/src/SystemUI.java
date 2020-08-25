import exceptions.*;
import javax.management.InstanceNotFoundException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationException;
import java.awt.*;
import java.io.FileNotFoundException;
import java.text.DateFormat;
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

    // option 1 in menu messages
    private final String FILE_LOADED_SUCCESSFULLY_MESSAGE = "File loaded successfully.";
    private final String ENTER_FILE_PATH_MESSAGE = "Please enter the path of the desired xml file to load: ";
    private final String LOAD_FILE_FAUILE_MESSAGE = "Failed to load the file. cause of failure:\n";
    // option 2 in menu messages
    private final String ALL_STORES_MESSAGE = "The stores in the system:\n%1$s";
    private final String NO_STORES_MESSAGE = "There are no stores in the system.\n";
    // option 3 in menu messages
    private final String NO_PRODUCTS_IN_SYSTEM_MESSAGE = "There are no products in the system.\n";
    private final String ALL_PRODUCTS_MESSAGE = "The products in the system:\n%1$s";
    // option 4 in menu messages
    private final String QUIT_CHARACTER = "q";
    private final String FINISH_CHARACTER = "f";
    private final String APPROVE_CHARACTER = "t";
    private final String GET_DATE_FROM_USER_MESSAGE = "\nPlease enter date in the format 'day/month-hour:minute': ";
    private final String GET_TYPE_OF_ORDER_FROM_USER_MESSAGE = "\nPlease select which type of order you want.\n";
    private final String GET_APPROVE_ORDER_FROM_USER_MESSAGE = "\nTo proceed with the order press 't', to cancel press 'f':";
    private final String GET_STORE_FROM_USER_MESSAGE = "\nPlease select a store from the list by enter the store id.";
    private final String GET_PRODUCT_AMOUNT_FROM_USER_MESSAGE = "\nPlease enter the amount of this product: ";
    private final String GET_CORDINATE_FROM_USER_MESSAGE = "\nPlease enter %1$s coordinate: ";
    private final String DESIRED_MESSAGE = "The desired %1$s: ";
    private final String SELECT_PRODUCT_FROM_LIST_MESSAGE = "Please select product from the list by enter the product id, or press 'q' to finish.";
    private final String PLACED_ORDER_MESSAGE = "\nOrder was successfully %1$s.";
    private final String THANK_YOU_FOR_BUYING_MESSAGE = "\nThank you for buying in super duper market!";
    private final String STORE_DONT_SELL_PRODUCT_MESSAGE = "\nSorry, the store you have chosen does not sell this product. ";
    private final String ALL_AVAILABLE_PRODUCTS_TO_BUY_MESSAGE = "All available products in the system:\n%1$s";
    private final String ORDER_SUMMERY_MESSAGE = "\nYour order:\n%1$s";
    private final String ORDER_TYPE_MESSAGE = "%1$s. %2$s\n";
    // option 5 in menu messages
    private final String ORDER_NUMBER_MESSAGE = "Order No. %1$s\n";
    private final String NO_ORDERS_IN_SYSTEM_MESSAGE = "There is no orders in the system.\n";
    private final String ALL_ORDERS_IN_SYSTEM_MESSAGE = "The orders in the system:\n%1$s";
    private final String GET_PURCHASE_PRICE_FROM_USER_MESSAGE = "\nPlease enter the purchase price: ";
    private final String PRODUCT_UPDATED_PRICE_SUCCESSFULLY_MESSAGE = "\nProduct %1$s update price successfully in %2$s.";
    private final String GET_PRODUCT_TO_UPDATE_FROM_USER_MESSAGE = "Please select the product you want to %1$s by enter the product id: ";
    // option 6 in menu messages
    private final String PRODUCT_REMOVED_SUCCESSFULLY_MESSAGE = "\nProduct %1$s removed successfully from %2$s.";
    private final String UNABLE_TO_REMOVE_PRODUCT_MESSAGE = "\nUnable to remove this product because its sold only in one store.";
    private final String PRODUCT_ADDED_SUCCESSFULLY_MESSAGE = "\nProduct %1$s add successfully to %2$s.";
    // generic messages
    private final String SEPARATOR_MESSAGE = "=========================\n";
    private final String WELCOME_MESSAGE = "\nHello, welcome to super duper market!";
    private final String OPTION_NOT_VALID_MESSAGE = "\nSorry, this option is not valid! you need to load file before.";
    private final String INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE = "\nSorry, the %1$s you entered is not in the correct format. ";
    private final String INPUT_NOT_IN_CORRECT_RANGE_MESSAGE = "\nSorry, the %1$s you entered is out of range. ";
    private final String QUIT_MESSAGE = "\nBye bye, see you next time!";
    private final String REENTER_INPUT_MESSAGE = "\nPlease reenter the desired %1$s: ";
    private final String TRY_AGAIN_MESSAGE = "\nPlease try again.\n";
    private final String PRODUCT_NOT_EXIST_MESSAGE = "There is not a product with the id: %1$s in the system. ";

    private static final DateFormat DATE_FORMAT;

    static
    {
        DATE_FORMAT = new SimpleDateFormat("dd/MM-HH:mm");
        DATE_FORMAT.setLenient(false);
    }

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
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println(QUIT_MESSAGE);
    }

    private void displayMenu()
    {
        String menuStr = "\nPlease choose one of the following action:\n" +
                "1. Load data from xml file.\n" +
                "2. Show all stores details.\n" +
                "3. Show all products details.\n" +
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
                System.out.print(ex.getMessage() + String.format(REENTER_INPUT_MESSAGE, "action"));
            }
            catch (InputMismatchException ex)
            {
                System.out.print(String.format(INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE, "choice") + String.format(REENTER_INPUT_MESSAGE, "action"));
            }
        }
    }

    private void validateUserChoice(int userStartChoiceInput, Class<? extends Enum> enumClass)
    {
        if (userStartChoiceInput > enumClass.getFields().length || userStartChoiceInput < 1)
        {
            throw new IndexOutOfBoundsException(String.format(INPUT_NOT_IN_CORRECT_RANGE_MESSAGE, "choice"));
        }
    }

    private void executeUserSelectedAction(StartMenuOptions userStartMenuChoice) throws Exception
    {
        if (userStartMenuChoice == StartMenuOptions.LoadFile)
        {
            loadDataFromXmlFile();
        }
        else if(userStartMenuChoice != StartMenuOptions.Quit)
        {
            if (manager.isFileWasLoadSuccessfully())
            {
                switch (userStartMenuChoice)
                {
                    case ShowAllStores:
                        showAllStores();
                        break;
                    case ShowAllProducts:
                        showAllProducts();
                        break;
                    case MakePurchase:
                        placeOrder();
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
                throw new IllegalArgumentException(OPTION_NOT_VALID_MESSAGE);
            }
        }
    }

    private void updateStoreProducts()
    {
        StoreDataContainer selectedStore = getStoreFromUser(MessagesBuilder.createAvailableStoreDetailsToUpdateProduct(manager.getAllStoresData()));
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
        String storeProductsToShow = MessagesBuilder.createStoreProductsDetailsToRemoveProduct(store);
        ProductDataContainer product = getProductToUpdateFromUser(store,"remove",UpdateProductOptions.RemoveProduct,storeProductsToShow);
        manager.removeProductFromStore(store,product);
        System.out.println(String.format(PRODUCT_REMOVED_SUCCESSFULLY_MESSAGE,product.getId(),store.getName()));
    }

    private ProductDataContainer getProductToUpdateFromUser(StoreDataContainer store, String optionDescription, UpdateProductOptions userOptionChoice,String productsToShow)
    {
        while (true)
        {
            try
            {
                System.out.print(productsToShow);
                System.out.print(String.format(GET_PRODUCT_TO_UPDATE_FROM_USER_MESSAGE,optionDescription));
                Scanner scanner = new Scanner(System.in);
                int userProductChoice = scanner.nextInt();
                validateUserProductChoice(store, userProductChoice,userOptionChoice);
                return (manager.getProductDataById(userProductChoice));
            }
            catch (StoreDoesNotSellProductException | InstanceNotFoundException | ValidationException | DuplicateValuesException ex)
            {
                System.out.println(ex.getMessage() + TRY_AGAIN_MESSAGE);
            }
            catch(InputMismatchException ex)
            {
                System.out.println(String.format(INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE + TRY_AGAIN_MESSAGE,"product id"));
            }
        }
    }

    private void validateUserProductChoice(StoreDataContainer store, int productId,UpdateProductOptions userOptionChoice) throws ValidationException, InstanceNotFoundException
    {
        ProductDataContainer product = manager.getProductDataById(productId);
        if(product == null)
        {
            throw new InstanceNotFoundException(String.format(PRODUCT_NOT_EXIST_MESSAGE,productId));
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
                throw new ValidationException(UNABLE_TO_REMOVE_PRODUCT_MESSAGE);
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
        String productsToShow = MessagesBuilder.createProductsDetailsToAddProductToStore(manager.getAllProductsData());
        ProductDataContainer product = getProductToUpdateFromUser(store,"add", UpdateProductOptions.AddProduct,productsToShow);
        int productPrice = getProductPriceFromUser();

        manager.addProductToStore(store,product,productPrice);
        System.out.println(String.format(PRODUCT_ADDED_SUCCESSFULLY_MESSAGE,product.getId(),store.getName()));
    }

    private int getProductPriceFromUser()
    {
        while (true)
        {
            try
            {
                System.out.print(GET_PURCHASE_PRICE_FROM_USER_MESSAGE);
                Scanner scanner = new Scanner(System.in);
                int productPrice = scanner.nextInt();
                validateUserProductPrice(productPrice);
                return productPrice;
            }
            catch(InputMismatchException ex)
            {
                System.out.print(String.format(INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE + TRY_AGAIN_MESSAGE, "price"));
            }
            catch (IllegalArgumentException ex)
            {
                System.out.print("\n" + ex.getMessage() + TRY_AGAIN_MESSAGE);
            }
        }
    }

    private void validateUserProductPrice(int productPrice)
    {
        if(productPrice <= 0)
        {
            throw new IllegalArgumentException("The price must be bigger than 0. ");
        }
    }


    private void updateProductPrice(StoreDataContainer store)
    {
        String storeProductsToShow = MessagesBuilder.createStoreProductsDetailsToUpdateProductPrice(store);
        ProductDataContainer product = getProductToUpdateFromUser(store,"update price", UpdateProductOptions.UpdateProductPrice,storeProductsToShow);
        int productPrice = getProductPriceFromUser();
        manager.updateProductPriceInStore(store,product,productPrice);
        System.out.println(String.format(PRODUCT_UPDATED_PRICE_SUCCESSFULLY_MESSAGE,product.getId(),store.getName()));
    }

    private void loadDataFromXmlFile() throws JAXBException, FileNotFoundException, InstanceNotFoundException
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
        String allStoresMsg = "\n";
        if (manager.getAllStoresData().size() > 0)
        {
            allStoresMsg += String.format(ALL_STORES_MESSAGE, SEPARATOR_MESSAGE);
            allStoresMsg += "\n";
            for (StoreDataContainer storeData : manager.getAllStoresData())
            {
                allStoresMsg += MessagesBuilder.createAllStoreDetails(storeData);
                allStoresMsg += MessagesBuilder.createAllStoreProductsDetails(storeData);
                allStoresMsg += MessagesBuilder.createAllStoreOrdersDetails(storeData);
                allStoresMsg += SEPARATOR_MESSAGE;
                allStoresMsg += "\n";
            }
        }
        else
        {
            allStoresMsg += NO_STORES_MESSAGE;
        }
        System.out.print(allStoresMsg);
    }

    private void showAllProducts()
    {
        String allProductsMsg = "\n";
        if (manager.getAllProductsData().size() > 0)
        {
            allProductsMsg += String.format(ALL_PRODUCTS_MESSAGE, SEPARATOR_MESSAGE);
            allProductsMsg += "\n";
            for (ProductDataContainer productData : manager.getAllProductsData())
            {
                allProductsMsg += MessagesBuilder.createProductDetailsForDisplayingAllProducts(productData);
            }
        }
        else
        {
            allProductsMsg += NO_PRODUCTS_IN_SYSTEM_MESSAGE;
        }
        System.out.print(allProductsMsg);
    }

    private void placeOrder()
    {
        OrderTypeOptions orderType = getOrderTypeFromUser();
        OrderDataContainer newOrderData = createNewOrder(orderType);
        if (newOrderData != null)
        {
            manager.addNewOrder(newOrderData);
            System.out.println(String.format(PLACED_ORDER_MESSAGE, "placed") + THANK_YOU_FOR_BUYING_MESSAGE);
        }
        else
        {
            System.out.println(String.format(PLACED_ORDER_MESSAGE, "canceled"));
        }
    }

    private OrderDataContainer createNewOrder(OrderTypeOptions orderType)
    {
        Integer storeId = null;
        if (orderType == OrderTypeOptions.Static)
        {
            storeId = getStoreFromUser(MessagesBuilder.createAvailableStoreDetailsToAddOrder(manager.getAllStoresData())).getId();
        }
        return getNewOrderFromUser(orderType, storeId);
    }

    private OrderDataContainer getNewOrderFromUser(OrderTypeOptions orderType, Integer ... storeId)
    {
        OrderDataContainer newOrderData = null;
        Date date = getDateFromUser();
        Point userLocation = getLocationFromUser(manager.getAllStoresData());
        Map<Integer, Float> amountPerProduct = getAllProductsAndQuantitiesInOrderFromUser(storeId);
        if (amountPerProduct.size() > 0)
        {
            Collection<ProductDataContainer> allProductsInOrder = getAllProductsDataInOrder(amountPerProduct.keySet());
            Map<ProductDataContainer, StoreDataContainer> productsInOrder = getProductsInOrder(allProductsInOrder, storeId);
            Collection<StoreDataContainer> storesDataParticapatesInOrder = getAllStoresDataParticaptesInOrder(productsInOrder);
            float deliveryCost = manager.getDeliveryCost(userLocation, storesDataParticapatesInOrder);
            if (isOrderApprovedByCustomer(amountPerProduct, productsInOrder, deliveryCost, userLocation, orderType))
            {
                if (orderType == OrderTypeOptions.Static)
                {
                    newOrderData = new OrderDataContainer(date, deliveryCost, storeId[0], amountPerProduct);
                }
                else
                {
                    newOrderData = new OrderDataContainer(date, deliveryCost, amountPerProduct, storesDataParticapatesInOrder.size());
                }
            }
        }
        return newOrderData;
    }

    private boolean isOrderApprovedByCustomer(Map<Integer, Float> amountPerProduct, Map<ProductDataContainer,
            StoreDataContainer> productsInOrder, float deliveryCost, Point userLocation, OrderTypeOptions orderType)
    {
        boolean isOrderApproved = false;
        while (true)
        {
            String orderSummary = createOrderSummary(amountPerProduct, productsInOrder, deliveryCost, userLocation, orderType);
            System.out.println(orderSummary);
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();
            if (userInput.toLowerCase().equals(QUIT_CHARACTER) || userInput.toLowerCase().equals(FINISH_CHARACTER))
            {
                break;
            }
            else
            {
                if (userInput.toLowerCase().equals(APPROVE_CHARACTER))
                {
                    isOrderApproved = true;
                    break;
                }
                else
                {
                    System.out.println((String.format(INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE, "choice")));
                }
            }
        }
        return isOrderApproved;
    }

    private Map<ProductDataContainer, StoreDataContainer> getProductsInOrder(Collection <ProductDataContainer> allProductsInOrder, Integer ... storeId)
    {
        Map<ProductDataContainer, StoreDataContainer> productsInOrder = new HashMap<>();
        if (storeId[0] != null)
        {
            for (ProductDataContainer product: allProductsInOrder)
            {
                productsInOrder.put(product, manager.getStoreDataById(storeId[0]));
            }
        }
        else
        {
            productsInOrder = manager.dynamicStoreAllocation(allProductsInOrder);
        }
        return productsInOrder;
    }

    private Map<Integer, Float> getAllProductsAndQuantitiesInOrderFromUser(Integer ... storeId)
    {
        Map<Integer, Float> allProductsInOrder = new HashMap<>();
        while (true)
        {
            try
            {
                System.out.println(SELECT_PRODUCT_FROM_LIST_MESSAGE);
                showAvailableProductsToBuy();
                System.out.print(String.format(DESIRED_MESSAGE, "product id"));
                Scanner scanner = new Scanner(System.in);
                String userInput = scanner.nextLine();
                if (isUserWantToQuitFromOrder(userInput))
                {
                    break;
                }
                else
                {
                    addNewProductToOrder(allProductsInOrder, userInput, storeId);
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage() + TRY_AGAIN_MESSAGE);
            }
        }
        return allProductsInOrder;
    }

    private void addNewProductToOrder(Map<Integer, Float> allProductsInOrder, String userProductSelection, Integer ... storeId)
    {
        int productId = getValidProductIdFromUser(userProductSelection, storeId[0]);
        float productAmount = getProductAmountFromUser(productId);
        if (allProductsInOrder.keySet().contains(productId))
        {
            allProductsInOrder.put(productId, allProductsInOrder.get(productId) + productAmount);
        }
        allProductsInOrder.putIfAbsent(productId, productAmount);
    }

    private int getValidProductIdFromUser(String userSelection, Integer ... storeId)
    {
        int productId;
        try
        {
            productId = Integer.parseInt(userSelection);
            if (!manager.getAllProductsID().contains(productId))
            {
                throw new IndexOutOfBoundsException(String.format(INPUT_NOT_IN_CORRECT_RANGE_MESSAGE, "product id"));
            }
            if (storeId[0] != null)
            {
                if (!isStoreSellProduct(productId, storeId[0]))
                {
                    throw new NoSuchElementException(STORE_DONT_SELL_PRODUCT_MESSAGE);
                }
            }
        }
        catch (NumberFormatException e)
        {
            throw new NumberFormatException(String.format(INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE, "product id"));
        }
        return productId;
    }

    private Point getLocationFromUser(Collection<StoreDataContainer> allStoresData)
    {
        Point userLocation;
        while (true)
        {
            try
            {
                Scanner scanner = new Scanner(System.in);
                System.out.println(String.format(GET_CORDINATE_FROM_USER_MESSAGE, "x"));
                int xCordinate = scanner.nextInt();
                System.out.println(String.format(GET_CORDINATE_FROM_USER_MESSAGE, "y"));
                int yCordinate = scanner.nextInt();
                userLocation = new Point(xCordinate, yCordinate);
                validateUserLocation(userLocation, allStoresData);
                break;
            }
            catch (InputMismatchException ex)
            {
                System.out.println(String.format(INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE, "cordinate") + TRY_AGAIN_MESSAGE);
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage() + TRY_AGAIN_MESSAGE);
            }
        }
        return userLocation;
    }

    private void validateUserLocation(Point userLocation, Collection<StoreDataContainer> allStoresData)
    {
        if (userLocation.getX() < 1 || userLocation.getX() > 50 ||
                userLocation.getY() < 1 || userLocation.getY() > 50)
        {
            throw new UserLocationNotValidException(userLocation);
        }
        for (StoreDataContainer storeData: allStoresData)
        {
            Point storeLocation = storeData.getPosition();
            if (userLocation.equals(storeLocation))
            {
                throw new UserLocationEqualToStoreException(userLocation, storeLocation);
            }
        }
    }

    private Collection<StoreDataContainer> getAllStoresDataParticaptesInOrder(Map<ProductDataContainer, StoreDataContainer> productsInOrder)
    {
        Collection<StoreDataContainer> allStoresParticaptesInOrder = new HashSet<>();
        for (StoreDataContainer storeParticapatesInOrder : productsInOrder.values())
        {
            allStoresParticaptesInOrder.add(storeParticapatesInOrder);
        }
        return allStoresParticaptesInOrder;
    }

    private Collection<ProductDataContainer> getAllProductsDataInOrder(Collection<Integer> productsIdInOrder)
    {
        Collection<ProductDataContainer> allProductsDataInOrder = new HashSet<>();
        for (Integer productId: productsIdInOrder)
        {
            allProductsDataInOrder.add(manager.getProductDataById(productId));
        }
        return allProductsDataInOrder;
    }

    private boolean isStoreSellProduct(int productId, int storeId)
    {
        StoreDataContainer selectedStore = manager.getStoreDataById(storeId);
        return (selectedStore.getProductDataContainerById(productId) != null);
    }

    private float getProductAmountFromUser(int productId)
    {
        float productAmount;
        while (true)
        {
            try
            {
                System.out.print(GET_PRODUCT_AMOUNT_FROM_USER_MESSAGE);
                Scanner scanner = new Scanner(System.in);
                String userInput = scanner.nextLine();
                productAmount = getValidProductAmountFromUser(userInput, productId);
                break;
            }
            catch (Exception e)
            {
                System.out.println(String.format(INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE, "amount") + TRY_AGAIN_MESSAGE);
            }
        }
        return productAmount;
    }

    private float getValidProductAmountFromUser(String userInput, int productId)
    {
        float productAmount;
        ProductDataContainer product = manager.getProductDataById(productId);
        boolean isProductSoldByWeight = (product.getPurchaseForm() == ProductDataContainer.ProductPurchaseForm.WEIGHT);
        if (!isProductSoldByWeight)
        {
            productAmount = (float) Integer.parseInt(userInput);
        }
        else
        {
            productAmount = Float.parseFloat(userInput);
        }
        return productAmount;
    }


    private OrderTypeOptions getOrderTypeFromUser()
    {
        int userSelection;
        while (true)
        {
            try
            {
                showOrderTypeOption();
                System.out.print(String.format(DESIRED_MESSAGE, "order type"));
                Scanner scanner = new Scanner(System.in);
                userSelection = scanner.nextInt();
                validateUserChoice(userSelection, OrderTypeOptions.class);
                userSelection -= 1;
                break;
            }
            catch (InputMismatchException e)
            {
                System.out.println(String.format(INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE, "choise") + TRY_AGAIN_MESSAGE);
            }
            catch (IndexOutOfBoundsException ex)
            {
                System.out.println(ex.getMessage() + TRY_AGAIN_MESSAGE);
            }
        }
        return OrderTypeOptions.values()[userSelection];
    }

    private StoreDataContainer getStoreFromUser(String storesToShow)
    {
        StoreDataContainer userStoreSelection;
        while (true)
        {
            try
            {
                System.out.print(GET_STORE_FROM_USER_MESSAGE);
                System.out.print(storesToShow);
                System.out.print(String.format(DESIRED_MESSAGE, "store id"));
                Scanner scanner = new Scanner(System.in);
                int userSelection = scanner.nextInt();
                userStoreSelection = getValidStoreFromUser(userSelection);
                break;
            }
            catch (InputMismatchException ex)
            {
                System.out.println(String.format(INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE, "store id") + TRY_AGAIN_MESSAGE);
            }
            catch(IndexOutOfBoundsException ex)
            {
                System.out.println(ex.getMessage() + TRY_AGAIN_MESSAGE);
            }
        }
        return userStoreSelection;
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
        Date userDate;
        while (true)
        {
            try
            {
                System.out.println(GET_DATE_FROM_USER_MESSAGE);
                Scanner scanner = new Scanner(System.in);
                String userDateString = scanner.nextLine();
                userDate = DATE_FORMAT.parse(userDateString);
                break;
            }
            catch (ParseException e)
            {
                System.out.println(String.format(INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE, "date") + TRY_AGAIN_MESSAGE);
            }
        }
        return userDate;
    }

    private StoreDataContainer getValidStoreFromUser(int userSelection)
    {
        if (!manager.getAllStoresID().contains(userSelection))
        {
            throw new IndexOutOfBoundsException(String.format(INPUT_NOT_IN_CORRECT_RANGE_MESSAGE, "store id"));
        }
        return manager.getStoreDataById(userSelection);
    }

    private String createOrderSummary(Map<Integer, Float> amountPerProduct, Map<ProductDataContainer, StoreDataContainer> productsInOrder,
                          float deliveryCost, Point userLocation, OrderTypeOptions orderType)
    {
        String orderSummaryMsg = String.format(ORDER_SUMMERY_MESSAGE, SEPARATOR_MESSAGE);
        StoreDataContainer storeOrderFrom = productsInOrder.values().stream().findFirst().get();
        float distanceFromStore = manager.getDistanceBetweenStoreAndCustomer(userLocation, storeOrderFrom);
        float totalOrderCost = 0;
        for (Integer productId: amountPerProduct.keySet())
        {
            ProductDataContainer productData = manager.getProductDataById(productId);
            StoreDataContainer storeData = productsInOrder.get(productData);
            int productPrice = storeData.getProductDataContainerById(productId).getPricePerStore().get(storeData.getId());
            float totalAmountFromProduct = amountPerProduct.get(productId);
            float totalPriceForProduct = productPrice * totalAmountFromProduct;
            totalOrderCost += totalPriceForProduct;
            if (orderType == OrderTypeOptions.Static)
            {
                storeData = null;
            }
            orderSummaryMsg += MessagesBuilder.createProductDetailsInOrderSummary(productData, productPrice, totalAmountFromProduct, totalPriceForProduct, storeData);
        }
        totalOrderCost += deliveryCost;
        storeOrderFrom = (orderType == OrderTypeOptions.Static ? storeOrderFrom : null);
        orderSummaryMsg += MessagesBuilder.createDeliveryAndCostDetailsInOrderSummary(deliveryCost, distanceFromStore, totalOrderCost, storeOrderFrom);
        orderSummaryMsg += SEPARATOR_MESSAGE;
        orderSummaryMsg += GET_APPROVE_ORDER_FROM_USER_MESSAGE;
        return orderSummaryMsg;
    }

    private void showAvailableProductsToBuy()
    {
        String avialableProductsToBuyMsg = "\n";
        avialableProductsToBuyMsg += String.format(ALL_AVAILABLE_PRODUCTS_TO_BUY_MESSAGE, SEPARATOR_MESSAGE);
        for (ProductDataContainer productData: manager.getAllProductsData())
        {
            avialableProductsToBuyMsg += MessagesBuilder.createProductDetails(productData);
        }
        System.out.println(avialableProductsToBuyMsg);
    }

    private void showAllOrdersHistory()
    {
        String allOrdersMsg = "\n";
        if (manager.getAllOrdersData().size() > 0)
        {
            int orderIndex = 1;
            allOrdersMsg += String.format(ALL_ORDERS_IN_SYSTEM_MESSAGE, SEPARATOR_MESSAGE);
            for (OrderDataContainer orderData: manager.getAllOrdersData())
            {
                allOrdersMsg += String.format(ORDER_NUMBER_MESSAGE, orderIndex);
                allOrdersMsg += MessagesBuilder.createOrderDetailsForDisplayingAllOrdersHistory(orderData);
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
}
