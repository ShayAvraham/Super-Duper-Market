import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public final class MessagesBuilder
{
    private static final String ALL_ORDERS_OF_STORE_MESSAGE = "The orders of %1$s store:\n%2$s";
    private static final String PPK_MESSAGE = "PPK: %1$s \n";
    private static final String TOTAL_INCOME_FROM_DELIVERIES_MESSAGE = "Total in come from deliveries: %1$s \n";
    private static final String NO_PRODUCTS_IN_STORE_MESSAGE = "This store has no products for sale.\n";
    private static final String NO_ORDERS_IN_STORE_MESSAGE = "This store has no orders.\n";
    private static final String ID_MESSAGE = "ID: %1$s \n";
    private static final String NAME_MESSAGE = "Name: %1$s \n";
    private static final String PURCHASE_FORM_OF_PRODUCT_MESSAGE = "The product is for sale by: %1$s \n";
    private static final String PRICE_MESSAGE = "Price: %1$s \n";
    private static final String TOTAL_AMOUNT_OF_PRODUCT_SOLD_IN_STORE_MESSAGE = "Total amount of this product sold in this store: %1$s \n";
    private static final String PRODUCT_NOT_SOLD_IN_ANY_STORE_MESSAGE = "This product is not sold yet.\n";
    private static final String NUMBER_OF_STORES_SELL_PRODUCT_MESSAGE = "Number of stores who sell this product: %1$s\n";
    private static final String AVERAGE_PRICE_OF_PRODUCT_MESSAGE = "Average price: %1$s \n";
    private static final String TOTAL_AMOUNT_OF_PRODUCT_SOLD_IN_SYSTEM_MESSAGE = "Number of times the product sold in the system: %1$s \n";
    private static final String ORDER_DATE_MESSAGE = "Date: %1$s \n";
    private static final String TOTAL_AMOUNT_OF_PRODUCT_IN_ORDER_MESSAGE = "Total amount of products: %1$s\n";
    private static final String TOTAL_COST_OF_ALL_PRODUCTS_IN_ORDER_MESSAGE = "Total cost of all products: %1$s\n";
    private static final String DELIVERY_COST_OF_ORDER_MESSAGE = "Delivery cost: %1$s\n";
    private static final String TOTAL_COST_OF_ORDER_MESSAGE = "Total order cost: %1$s\n";
    private static final String NUM_OF_STORES_IN_ORDER_MESSAGE = "Number of stores: %1$s\n";
    private static final String NUM_OF_PRODUCT_TYPES_IN_ORDER_MESSAGE = "Number of product types: %1$s\n";
    private static final String STORE_ID_MESSAGE = "Store id: %1$s\n";
    private static final String STORE_NAME_MESSAGE = "Store name: %1$s\n";
    private static final String ALL_PRODUCTS_OF_STORE_MESSAGE = "The products in %1$s store:\n%2$s";
    private static final String SEPARATOR_MESSAGE = "=========================\n";
    private static final String ORDER_PPK_MESSAGE = "PPK: %1$s\n";
    private static final String DISTANCE_BETWEEN_CUSTOMER_AND_STORE_MESSAGE = "Distance to store: %1$s\n";
    private static final String TOTAL_ORDER_COST_MESSAGE = "Total to pay: %1$s\n";
    private static final String ALL_PRODUCTS_MESSAGE = "The products in the system:\n%1$s";
    private static final String AVAILABLE_STORE_TO_BUY_MESSAGE = "ID: %1$s \nName: %2$s\nPPK: %3$s\n\n";
    private static final String AVAILABLE_STORE_TO_UPDATE_PRODUCT_MESSAGE = "ID: %1$s \nName: %2$s\n\n";
    private static final String ALL_AVAILABLE_STORES_TO_BUY_MESSAGE = "All available stores in the system:\n%1$s";
    private static final String STORE_DETAILS_FOR_ORDER_SUMMARY_MESSAGE = "Store id: %1$s\nStore name: %2$s\n";
    private static final String PRODUCT_IN_ORDER_SUMMERY_MESSAGE = "ID: %1$s\nName: %2$s\nPurchase form: %3$s\n" +
                                                                    "Price: %4$s\nAmount: %5$s\n" +
                                                                    "Total price: %6$s\n";

    private static final DateFormat DATE_FORMAT;

    static
    {
        DATE_FORMAT = new SimpleDateFormat("dd/MM-HH:mm");
        DATE_FORMAT.setLenient(false);
    }

    private MessagesBuilder()
    {
    }

    public static String createOrderDetailsForDisplayingAllOrdersHistory(OrderDataContainer orderData)
    {
        String orderDetails =
                String.format(ID_MESSAGE, orderData.getId()) +
                        String.format(ORDER_DATE_MESSAGE, DATE_FORMAT.format(orderData.getDate()));
        if (orderData.isDynamic()) {
            orderDetails += String.format(NUM_OF_STORES_IN_ORDER_MESSAGE, orderData.getNumberOfStoresOrderedFrom());
        } else {
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

    public static String createAllStoreDetails(StoreDataContainer storeData)
    {
        String storeDetails = "";
        storeDetails +=
                String.format(ID_MESSAGE, storeData.getId()) +
                        String.format(NAME_MESSAGE, storeData.getName()) +
                        String.format(PPK_MESSAGE, storeData.getPPK()) +
                        String.format(TOTAL_INCOME_FROM_DELIVERIES_MESSAGE, storeData.getTotalIncomeFromDeliveries());
        ;
        return storeDetails;
    }

    public static String createAllStoreProductsDetails(StoreDataContainer storeData)
    {
        String allStoreProductsMsg = "";
        if (storeData.getProducts().size() > 0) {
            allStoreProductsMsg += String.format(ALL_PRODUCTS_OF_STORE_MESSAGE, storeData.getName(), "\n");
            for (ProductDataContainer productData : storeData.getProducts()) {
                allStoreProductsMsg += createProductDetailsForDisplayingAllStores(storeData, productData);
            }
        } else {
            allStoreProductsMsg += NO_PRODUCTS_IN_STORE_MESSAGE;
        }
        return allStoreProductsMsg;
    }

    public static String createProductDetailsForDisplayingAllStores(StoreDataContainer storeData, ProductDataContainer productData)
    {
        String productStatisticsDetails = createProductDetails(productData);
        productStatisticsDetails += String.format(PRICE_MESSAGE, productData.getPricePerStore().get(storeData.getId()));
        float howManyTimesProductSoldByStore = productData.getSoldAmountPerStore().get(storeData.getId());

        if (howManyTimesProductSoldByStore > 0) {
            productStatisticsDetails += String.format(TOTAL_AMOUNT_OF_PRODUCT_SOLD_IN_STORE_MESSAGE, howManyTimesProductSoldByStore);
        } else {
            productStatisticsDetails += PRODUCT_NOT_SOLD_IN_ANY_STORE_MESSAGE;
        }
        productStatisticsDetails += "\n";

        return productStatisticsDetails;
    }

    public static String createProductDetails(ProductDataContainer productData)
    {
        String productDetails =
                String.format(ID_MESSAGE, productData.getId()) +
                String.format(NAME_MESSAGE, productData.getName()) +
                String.format(PURCHASE_FORM_OF_PRODUCT_MESSAGE, productData.getPurchaseForm().toString().toLowerCase());
        return productDetails;
    }

    public static String createProductDetailsForDisplayingAllProducts(ProductDataContainer productData)
    {
        String productStatisticsDetails = createProductDetails(productData);
        int numOfStoresSellProduct = productData.getNumberOfStoresSellProduct();

        if (numOfStoresSellProduct > 0) {
            productStatisticsDetails +=
                    String.format(NUMBER_OF_STORES_SELL_PRODUCT_MESSAGE, productData.getNumberOfStoresSellProduct()) +
                            String.format(AVERAGE_PRICE_OF_PRODUCT_MESSAGE, productData.getAveragePrice()) +
                            String.format(TOTAL_AMOUNT_OF_PRODUCT_SOLD_IN_SYSTEM_MESSAGE, productData.getNumOfProductWasOrdered());
        } else {
            productStatisticsDetails += PRODUCT_NOT_SOLD_IN_ANY_STORE_MESSAGE;
        }
        productStatisticsDetails += SEPARATOR_MESSAGE;
        productStatisticsDetails += "\n";

        return productStatisticsDetails;
    }

    public static String createAllStoreOrdersDetails(StoreDataContainer storeData)
    {
        String allStoreOrdersMsg = "";
        if (storeData.getOrders().size() > 0) {
            allStoreOrdersMsg += String.format(ALL_ORDERS_OF_STORE_MESSAGE, storeData.getName(), "\n");
            for (OrderDataContainer orderData : storeData.getOrders()) {
                allStoreOrdersMsg += createOrderDetailsForDisplayingAllStores(orderData);
            }
        } else {
            allStoreOrdersMsg += NO_ORDERS_IN_STORE_MESSAGE;
        }
        return allStoreOrdersMsg;
    }

    public static String createOrderDetailsForDisplayingAllStores(OrderDataContainer orderData)
    {
        String orderDetails =
                String.format(ORDER_DATE_MESSAGE, DATE_FORMAT.format(orderData.getDate())) +
                        String.format(TOTAL_AMOUNT_OF_PRODUCT_IN_ORDER_MESSAGE, orderData.getNumOfProducts()) +
                        String.format(TOTAL_COST_OF_ALL_PRODUCTS_IN_ORDER_MESSAGE, orderData.getCostOfAllProducts()) +
                        String.format(DELIVERY_COST_OF_ORDER_MESSAGE, orderData.getDeliveryCost()) +
                        String.format(TOTAL_COST_OF_ORDER_MESSAGE, orderData.getTotalCost()) +
                        "\n";
        return orderDetails;
    }

    public static String createStoreDetailsForOrderSummary(StoreDataContainer... storeData)
    {
        String storeDetails = "";
        if (storeData[0] != null)
        {
            storeDetails += String.format(STORE_DETAILS_FOR_ORDER_SUMMARY_MESSAGE, storeData[0].getId(), storeData[0].getName());
        }
        return storeDetails;
    }

    public static String createProductDetailsInOrderSummary(ProductDataContainer productData, int productPrice, float totalAmountFromProduct, float totalPriceForProduct, StoreDataContainer ... storeData)
    {
        String productInOrderMsg = "";
        productInOrderMsg += String.format(PRODUCT_IN_ORDER_SUMMERY_MESSAGE,
                productData.getId(), productData.getName(),
                productData.getPurchaseForm().toString().toLowerCase(),
                productPrice, totalAmountFromProduct, totalPriceForProduct);
        productInOrderMsg += createStoreDetailsForOrderSummary(storeData[0]);
        productInOrderMsg += SEPARATOR_MESSAGE;
        return productInOrderMsg;
    }

    public static String createDeliveryAndCostDetailsInOrderSummary(float deliveryCost, float distanceFromStore, float totalOrderCost, StoreDataContainer... storeOrderFrom)
    {
        String orderDetailsMsg = "";
        if (storeOrderFrom[0] != null)
        {
            orderDetailsMsg += String.format(ORDER_PPK_MESSAGE, storeOrderFrom[0].getPPK());
            orderDetailsMsg += String.format(DISTANCE_BETWEEN_CUSTOMER_AND_STORE_MESSAGE, distanceFromStore);
        }
        orderDetailsMsg += String.format(DELIVERY_COST_OF_ORDER_MESSAGE, deliveryCost);
        orderDetailsMsg += String.format(TOTAL_ORDER_COST_MESSAGE, totalOrderCost);
        return orderDetailsMsg;
    }

    public static String createStoreProductsDetailsToUpdateProductPrice(StoreDataContainer store)
    {
        String storeProductsDetails = String.format("\n" + ALL_PRODUCTS_OF_STORE_MESSAGE, store.getName(), SEPARATOR_MESSAGE);
        for(ProductDataContainer product: store.getProducts())
        {
            storeProductsDetails += createProductDetails(product);
            storeProductsDetails += "Price: " + product.getPricePerStore().get(store.getId()) +"\n";
            storeProductsDetails += "\n";
        }
        return storeProductsDetails;
    }

    public static String createStoreProductsDetailsToRemoveProduct(StoreDataContainer store)
    {
        String storeProductsDetails = String.format("\n" + ALL_PRODUCTS_OF_STORE_MESSAGE, store.getName(), SEPARATOR_MESSAGE);
        for(ProductDataContainer product: store.getProducts())
        {
            storeProductsDetails += createProductDetails(product);
            storeProductsDetails += "\n";
        }
        return storeProductsDetails;
    }

    public static String createProductsDetailsToAddProductToStore(Collection<ProductDataContainer> products)
    {
        String productsDetails = String.format("\n" + ALL_PRODUCTS_MESSAGE,SEPARATOR_MESSAGE);
        for(ProductDataContainer product : products)
        {
            productsDetails += MessagesBuilder.createProductDetails(product);
            productsDetails += "\n";
        }
        return productsDetails;
    }

    public static String createAvailableStoreDetailsToAddOrder(Collection<StoreDataContainer> stores)
    {
        String availableStoresToBuyMsg = "\n";
        availableStoresToBuyMsg += String.format(ALL_AVAILABLE_STORES_TO_BUY_MESSAGE, SEPARATOR_MESSAGE);
        for (StoreDataContainer storeData: stores)
        {
            availableStoresToBuyMsg += String.format(
                    AVAILABLE_STORE_TO_BUY_MESSAGE, storeData.getId(), storeData.getName(), storeData.getPPK());
        }
        return availableStoresToBuyMsg;
    }

    public static String createAvailableStoreDetailsToUpdateProduct(Collection<StoreDataContainer> stores)
    {
        String availableStoresMsg = "\n";
        availableStoresMsg += String.format(ALL_AVAILABLE_STORES_TO_BUY_MESSAGE, SEPARATOR_MESSAGE);
        for (StoreDataContainer storeData: stores)
        {
            availableStoresMsg += String.format(
                    AVAILABLE_STORE_TO_UPDATE_PRODUCT_MESSAGE, storeData.getId(), storeData.getName());
        }
        return availableStoresMsg;
    }


}
