import exceptions.*;
import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationException;
import java.awt.*;
import java.io.FileNotFoundException;
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
    private final String FILE_LOADED_SUCCESSFULLY_MESSAGE = "File loaded successfully!";
    private final String ENTER_FILE_PATH_MESSAGE = "Please enter the path of the desired xml file to load: ";
    private final String LOAD_FILE_FAUILE_MESSAGE = "Faild to load the file, Cause of failure:";
    // option 2 in menu messages
    private final String ALL_STORES_MESSAGE = "The stores in the system:\n%1$s";
    private final String ALL_PRODUCTS_OF_STORE_MESSAGE = "The products in %1$s store:\n%2$s";
    private final String NO_STORES_MESSAGE = "There are no stores in the system.\n";
    // option 3 in menu messages
    private final String NO_PRODUCTS_IN_SYSTEM_MESSAGE = "There are no products in the system.\n";
    private final String ALL_PRODUCTS_MESSAGE = "The products in the system:\n%1$s";
    // option 4 in menu messages
    private final String QUIT_CHARACTER = "q";
    private final String FINISH_CHARACTER = "f";
    private final String APPROVE_CHARACTER = "t";
    private final String GET_DATE_FROM_USER_MESSAGE = "Please enter date:";
    private final String GET_TYPE_OF_ORDER_FROM_USER_MESSAGE = "Please select which type of order you want.\n";
    private final String GET_APPROVE_ORDER_FROM_USER_MESSAGE = "To proceed with the order press 't', to cancel press 'f':";
    private final String GET_STORE_FROM_USER_MESSAGE = "\nPlease select a store from the list by enter the store id.";
    private final String GET_PRODUCT_AMOUNT_FROM_USER_MESSAGE = "Please enter the amount of this product: ";
    private final String GET_CORDINATE_FROM_USER_MESSAGE = "Please enter %1$s cordinate: ";
    private final String DESIRED_MESSAGE = "The desired %1$s: ";
    private final String SELECT_PRODUCT_FROM_LIST_MESSAGE = "Please select product from the list by enter the product id, or press 'q' to finish.";
    private final String PLACED_ORDER_MESSAGE = "Order was successfully  %1$s.";
    private final String STORE_DONT_SELL_PRODUCT_MESSAGE = "Sorry, the store you have choosen does not sell this product.";
    private final String ALL_AVAILABLE_STORES_TO_BUY_MESSAGE = "All available stores in the system:\n%1$s";
    private final String AVAILABLE_STORE_TO_BUY_MESSAGE = "ID: %1$s \nName: %2$s\nPPK: %3$s\n\n";
    private final String ALL_AVAILABLE_PRODUCTS_TO_BUY_MESSAGE = "All available products in the system:\n%1$s";
    private final String ORDER_SUMMERY_MESSAGE = "Your order:\n%1$s";
    private final String ORDER_TYPE_MESSAGE = "%1$s. %2$s\n";
    // option 5 in menu messages
    private final String ORDER_NUMBER_MESSAGE = "Order No. %1$s\n";
    private final String NO_ORDERS_IN_SYSTEM_MESSAGE = "Thers is no orders in the system.\n";
    private final String ALL_ORDERS_IN_SYSTEM_MESSAGE = "The orders in the system:\n%1$s";
    private final String GET_PURCHASE_PRICE_FROM_USER_MESSAGE = "\nPlease enter the purchase price: ";
    private final String PRODUCT_UPDATED_PRICE_SUCCESSFULLY_MESSAGE = "\nproduct %1$s update price successfully in %2$s";
    private final String GET_PRODUCT_TO_UPDATE_FROM_USER_MESSAGE = "\nPlease select the product you want to %1$s by enter the product id: ";
    // option 6 in menu messages
    private final String PRODUCT_REMOVED_SUCCESSFULLY_MESSAGE = "\nproduct %1$s removed successfully from %2$s";
    private final String UNABLE_TO_REMOVE_PRODUCT_MESSAGE = "Unable to remove this product because its sold only in one store.";
    private final String PRODUCT_ADDED_SUCCESSFULLY_MESSAGE = "\nproduct %1$s add successfully to %2$s";
    // generic messages
    private final String SEPARATOR_MESSAGE = "=========================\n";
    private final String WELCOME_MESSAGE = "\nHello, welcome to super duper market!";
    private final String OPTION_NOT_VALID_MESSAGE = "\nSorry, this option is not valid! You need to load file before.";
    private final String INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE = "\nSorry, the %1$s you entered is not in the correct format. ";
    private final String INPUT_NOT_IN_CORRECT_RANGE_MESSAGE = "\nSorry, the %1$s you entered is out of range. ";
    private final String QUIT_MESSAGE = "Bye bye, see you next time!";
    private final String DELIVERY_COST_OF_ORDER_MESSAGE = "Delivery cost: %1$s\n";
    private final String REENTER_INPUT_MESSAGE = "Please reenter the desired %1$s: ";
    private final String TRY_AGAIN_MESSAGE = "Please try again.\n";


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
                System.out.print(ex.getMessage() + String.format(REENTER_INPUT_MESSAGE, "action"));
            }
            catch (InputMismatchException ex)
            {
                System.out.print(String.format(INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE, "choise") + String.format(REENTER_INPUT_MESSAGE, "action"));
            }
        }
    }

    private void validateUserChoice(int userStartChoiceInput, Class<? extends Enum> enumClass)
    {
        if (userStartChoiceInput > enumClass.getFields().length || userStartChoiceInput < 1)
        {
            throw new IndexOutOfBoundsException(String.format(INPUT_NOT_IN_CORRECT_RANGE_MESSAGE, "choise"));
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
        System.out.println(String.format(PRODUCT_REMOVED_SUCCESSFULLY_MESSAGE,product.getId(),store.getName()));
    }

    private void showStoreProducts(StoreDataContainer store)
    {
        System.out.print(String.format("\n" + ALL_PRODUCTS_OF_STORE_MESSAGE, store.getName(), SEPARATOR_MESSAGE));
        for(ProductDataContainer product: store.getProducts())
        {
            System.out.print(MessagesBuilder.createProductDetails(product));
        }
    }

    private ProductDataContainer getFromUserProductToUpdate(StoreDataContainer store, String optionDescription,UpdateProductOptions userOptionChoice)
    {
        while (true)
        {
            try
            {
                System.out.print(String.format(GET_PRODUCT_TO_UPDATE_FROM_USER_MESSAGE,optionDescription));
                Scanner scanner = new Scanner(System.in);
                int userProductChoice = scanner.nextInt();
                validateUserProductChoice(store, userProductChoice,userOptionChoice);
                return (manager.getProductDataById(userProductChoice));
            }
            catch (StoreDoesNotSellProductException | InstanceNotExistException | ValidationException | DuplicateValuesException ex)
            {
                System.out.println("\n" + ex.getMessage() + TRY_AGAIN_MESSAGE);
            }
            catch(InputMismatchException ex)
            {
                System.out.println("\n" + INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE + TRY_AGAIN_MESSAGE);
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
        System.out.print(String.format("\n",ALL_PRODUCTS_MESSAGE,SEPARATOR_MESSAGE));
        for(ProductDataContainer product : manager.getAllProductsData())
        {
            System.out.print(MessagesBuilder.createProductDetails(product));
        }
        ProductDataContainer product = getFromUserProductToUpdate(store,"add", UpdateProductOptions.AddProduct);
        int productPrice = getFromUserProductPrice();

        manager.addProductToStore(store,product,productPrice);
        System.out.println(String.format(PRODUCT_ADDED_SUCCESSFULLY_MESSAGE,product.getId(),store.getName()));
    }

    private int getFromUserProductPrice()
    {
        while (true)
        {
            try
            {
                System.out.print(GET_PURCHASE_PRICE_FROM_USER_MESSAGE);
                Scanner scanner = new Scanner(System.in);
                int productPrice = scanner.nextInt();
                return productPrice;
            }
            catch(InputMismatchException ex)
            {
                System.out.println("\n" + INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE + TRY_AGAIN_MESSAGE);
            }
        }
    }


    private void updateProductPrice(StoreDataContainer store)
    {
        showStoreProducts(store);
        ProductDataContainer product = getFromUserProductToUpdate(store,"update price", UpdateProductOptions.UpdateProductPrice);
        int productPrice = getFromUserProductPrice();
        manager.updateProductPriceInStore(store,product,productPrice);
        System.out.println(String.format(PRODUCT_UPDATED_PRICE_SUCCESSFULLY_MESSAGE,product.getId(),store.getName()));
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
            System.out.println(String.format(PLACED_ORDER_MESSAGE, "placed"));
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
            storeId = getStoreFromUser().getId();
        }
        return getNewOrderFromUser(orderType, storeId);
    }

    private OrderDataContainer getNewOrderFromUser(OrderTypeOptions orderType, Integer ... storeId)
    {
        OrderDataContainer newOrderData = null;
        Date date = getDateFromUser();
        Point userLocation = getLocationFromUser(storeId);
        Map<Integer, Float> amountPerProduct = getAllProductsAndQuantitiesInOrderFromUser(storeId);
        if (amountPerProduct.size() > 0)
        {
            Collection<ProductDataContainer> allProductsInOrder = getAllProductsDataInOrder(amountPerProduct.keySet());
            Map<ProductDataContainer, StoreDataContainer> productsInOrder = getProductsInOrder(allProductsInOrder, storeId);
            Collection<StoreDataContainer> storesDataParticapatesInOrder = getAllStoresDataParticaptesInOrder(productsInOrder);
            float deliveryCost = manager.getDeliveryCost(userLocation, storesDataParticapatesInOrder);
            if (isOrderApprovedByCustomer(amountPerProduct, productsInOrder, deliveryCost, orderType))
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

    private boolean isOrderApprovedByCustomer(Map<Integer, Float> amountPerProduct, Map<ProductDataContainer,StoreDataContainer> productsInOrder, float deliveryCost, OrderTypeOptions orderType)
    {
        boolean isOrderApproved = false;
        while (true)
        {
            String orderSummary = createOrderSummary(amountPerProduct, productsInOrder, deliveryCost, orderType);
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
                    System.out.println((String.format(INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE, "choise")));
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
            throw new NumberFormatException(String.format(INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE, "product amount"));
        }
        return productId;
    }

    private Point getLocationFromUser(Integer ... storeId)
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
                validateUserLocation(userLocation, storeId[0]);
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

    private void validateUserLocation(Point userLocation, Integer ... storeId)
    {
        if (userLocation.getX() < 1 || userLocation.getX() > 50 ||
                userLocation.getY() < 1 || userLocation.getY() > 50)
        {
            throw new UserLocationNotValidException(userLocation);
        }
        if (storeId[0] != null)
        {
            Point storeLocation = manager.getStoreDataById(storeId[0]).getPosition();
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
                System.out.println(String.format(INPUT_NOT_IN_CORRECT_FORMAT_MESSAGE, "order type") + TRY_AGAIN_MESSAGE);
            }
            catch (IndexOutOfBoundsException ex)
            {
                System.out.println(ex.getMessage() + TRY_AGAIN_MESSAGE);
            }
        }
        return OrderTypeOptions.values()[userSelection];
    }

    private StoreDataContainer getStoreFromUser()
    {
        StoreDataContainer userStoreSelection;
        while (true)
        {
            try
            {
                System.out.print(GET_STORE_FROM_USER_MESSAGE);
                showAvailableStoresToBuy();
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
                userDate = new SimpleDateFormat("dd/mm-hh:mm").parse(userDateString);
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

    private String createOrderSummary(Map<Integer, Float> amountPerProduct, Map<ProductDataContainer,StoreDataContainer> productsInOrder, float deliveryCost, OrderTypeOptions orderType)
    {
        String orderSummaryMsg = String.format(ORDER_SUMMERY_MESSAGE, SEPARATOR_MESSAGE);
        for (Integer productId: amountPerProduct.keySet())
        {
            ProductDataContainer productData = manager.getProductDataById(productId);
            StoreDataContainer storeSellTheProduct = productsInOrder.get(productData);
            int productPrice = storeSellTheProduct.getProductDataContainerById(productId).getPricePerStore().get(storeSellTheProduct.getId());
            float totalPriceForProduct = productPrice * amountPerProduct.get(productId);
            float totalAmountFromProduct = amountPerProduct.get(productId);
            if (orderType == OrderTypeOptions.Static)
            {
                storeSellTheProduct = null;
            }
            orderSummaryMsg += MessagesBuilder.createProductDetailsInOrderSummary(productData, productPrice, totalAmountFromProduct, totalPriceForProduct, storeSellTheProduct);
        }
        orderSummaryMsg += String.format(DELIVERY_COST_OF_ORDER_MESSAGE, deliveryCost);
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

    private void showAvailableStoresToBuy()
    {
        String avialableStoresToBuyMsg = "\n";
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
