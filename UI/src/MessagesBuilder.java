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
    private static final String PRODUCT_NOT_SOLD_IN_ANY_STORE_MESSAGE = "This product is not sold in any store.\n";
    private static final String NUMBER_OF_STORES_SELL_PRODUCT_MESSAGE = "Number of stores who sell this product: %1$s\n";
    private static final String AVERAGE_PRICE_OF_PRODUCT_MESSAGE = "Average price: %1$s \n";
    private static final String TOTAL_AMOUNT_OF_PRODUCT_SOLD_IN_SYSTEM_MESSAGE = "Number of times the product sold in the system: %1$s \n";
    private static final String ORDER_DATE_MESSAGE = "Date: %1$s \n";
    private static final String TOTAL_AMOUNT_OF_PRODUCT_IN_ORDER_MESSAGE = "Total amount of products: %1$s\n";
    private static final String TOTAL_COST_OF_ALL_PRODUCTS_IN_ORDER_MESSAGE = "Total cost of all products: %1$s\n";
    private static final String DELIVERY_COST_OF_ORDER_MESSAGE = "Delivery cost: %1$s\n";
    private static final String TOTAL_COST_OF_ORDER_MESSAGE = "Total order cost: %1$s\n";
    private static final String AVAILABLE_PRODUCT_TO_BUY_MESSAGE = "%1$s. %2$s\n   Purchase form: %3$s\n\n";
    private static final String STORE_NUMBER_MESSAGE = "Store No. %1$s\n";
    private static final String PRODUCT_NUMBER_MESSAGE = "Product No. %1$s\n";
    private static final String NUM_OF_STORES_IN_ORDER_MESSAGE = "Number of stores: %1$s\n";
    private static final String NUM_OF_PRODUCT_TYPES_IN_ORDER_MESSAGE = "Number of product types: %1$s\n";
    private static final String STORE_ID_MESSAGE = "Store id: %1$s\n";
    private static final String STORE_NAME_MESSAGE = "Store name: %1$s\n";
    private static final String GET_APPROVE_ORDER_FROM_USER_MESSAGE = "To proceed with the order press 't', to cancel press 'f':";
    private static final String ORDER_SUMMERY_MESSAGE = "Your order:\n%1$s";
    private static final String ALL_PRODUCTS_MESSAGE = "The products in the system:\n%1$s";
    private static final String ALL_PRODUCTS_OF_STORE_MESSAGE = "The products in %1$s store:\n%2$s";
    private static final String SEPARATOR_MESSAGE = "=========================\n";
    private static final String PRODUCT_IN_ORDER_SUMMERY_MESSAGE = "ID: %1$s\nName: %2$s\nPurchase form: %3$s\n" +
            "Price: %4$s\nAmount: %5$s\n" +
            "Total price: %6$s\n";


    private MessagesBuilder() {
    }

    public static String createOrderDetailsForDisplayingAllOrdersHistory(OrderDataContainer orderData)
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


    public static String createAllStoreDetails(StoreDataContainer storeData)
    {
        String storeDetails = "";
        storeDetails +=
                String.format(ID_MESSAGE, storeData.getId()) +
                        String.format(NAME_MESSAGE, storeData.getName())+
                        String.format(PPK_MESSAGE, storeData.getPPK()) +
                        String.format(TOTAL_INCOME_FROM_DELIVERIES_MESSAGE, storeData.getTotalIncomeFromDeliveries());;
        return storeDetails;
    }

    public static String createAllStoreProductsDetails(StoreDataContainer storeData) //change
    {
        String allStoreProductsMsg = "";
        if (storeData.getProducts().size() > 0)
        {
            allStoreProductsMsg += String.format(ALL_PRODUCTS_OF_STORE_MESSAGE, storeData.getName(), "\n");
            for (ProductDataContainer productData : storeData.getProducts())
            {
                allStoreProductsMsg += createProductDetailsForDisplayingAllStores(storeData,productData);
            }
        }
        else
        {
            allStoreProductsMsg += NO_PRODUCTS_IN_STORE_MESSAGE;
        }
        return allStoreProductsMsg;
    }

    public static String createProductDetailsForDisplayingAllStores(StoreDataContainer storeData, ProductDataContainer productData) //change
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
        productStatisticsDetails += "\n";

        return productStatisticsDetails;
    }


    public static String createProductDetails(ProductDataContainer productData) //change
    {
        String productDetails =
                String.format(ID_MESSAGE, productData.getId()) +
                        String.format(NAME_MESSAGE, productData.getName()) +
                        String.format(PURCHASE_FORM_OF_PRODUCT_MESSAGE, productData.getPurchaseForm().toString().toLowerCase());
        return productDetails;
    }

    public static String createProductDetailsForDisplayingAllProducts(ProductDataContainer productData)//change
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
        productStatisticsDetails += "\n";

        return productStatisticsDetails;
    }

    public static String createAllStoreOrdersDetails(StoreDataContainer storeData) // change
    {
        String allStoreOrdersMsg = "";
        if (storeData.getOrders().size() > 0)
        {
            allStoreOrdersMsg += String.format(ALL_ORDERS_OF_STORE_MESSAGE, storeData.getName(), "\n");
            for (OrderDataContainer orderData : storeData.getOrders())
            {
                allStoreOrdersMsg += createOrderDetailsForDisplayingAllStores(orderData);
            }
        }
        else
        {
            allStoreOrdersMsg += NO_ORDERS_IN_STORE_MESSAGE;
        }
        return allStoreOrdersMsg;
    }

    public static String createOrderDetailsForDisplayingAllStores(OrderDataContainer orderData)
    {
        String orderDetails =
                String.format(ORDER_DATE_MESSAGE, orderData.getDate()) +
                        String.format(TOTAL_AMOUNT_OF_PRODUCT_IN_ORDER_MESSAGE, orderData.getNumOfProducts()) +
                        String.format(TOTAL_COST_OF_ALL_PRODUCTS_IN_ORDER_MESSAGE, orderData.getCostOfAllProducts()) +
                        String.format(DELIVERY_COST_OF_ORDER_MESSAGE, orderData.getDeliveryCost()) +
                        String.format(TOTAL_COST_OF_ORDER_MESSAGE, orderData.getTotalCost()) +
                        "\n";
        return orderDetails;
    }


    public static String createOrderSummaryForDynamicOrder(Map<Integer, Float> amountPerProduct, Map<ProductDataContainer, StoreDataContainer> productsInOrder, float deliveryCost)
    {
        String orderSummaryMsg = String.format(ORDER_SUMMERY_MESSAGE, SEPARATOR_MESSAGE);
        int productIndex = 1;
        for (ProductDataContainer productData: productsInOrder.keySet())
        {
            StoreDataContainer storeSellTheProduct = productsInOrder.get(productData);
            int productPrice = storeSellTheProduct.getProductDataContainerById(productData.getId())
                    .getPricePerStore().get(storeSellTheProduct.getId());
            float totalPriceForProduct = productPrice * amountPerProduct.get(productData.getId());
            orderSummaryMsg += String.format(PRODUCT_NUMBER_MESSAGE, productIndex);
            orderSummaryMsg += String.format(PRODUCT_IN_ORDER_SUMMERY_MESSAGE,
                    productData.getId(), productData.getName(),
                    productData.getPurchaseForm().toString().toLowerCase(),
                    productPrice, amountPerProduct.get(productData.getId()), totalPriceForProduct);
            orderSummaryMsg += String.format("Store id: %1$s\nStore name: %2$s\n", storeSellTheProduct.getId(), storeSellTheProduct.getName());
            orderSummaryMsg += SEPARATOR_MESSAGE;
            productIndex++;
        }
        orderSummaryMsg += String.format(DELIVERY_COST_OF_ORDER_MESSAGE, deliveryCost);
        orderSummaryMsg += SEPARATOR_MESSAGE;
        orderSummaryMsg += GET_APPROVE_ORDER_FROM_USER_MESSAGE;
        return orderSummaryMsg;
    }

    public static String createProductDetailsInOrderSummary(ProductDataContainer productData, int productPrice, float totalAmountFromProduct, float totalPriceForProduct, int productIndex)
    {
        String productInOrderMsg = "";
        productInOrderMsg += String.format(PRODUCT_NUMBER_MESSAGE, productIndex);
        productInOrderMsg += String.format(PRODUCT_IN_ORDER_SUMMERY_MESSAGE,
                productData.getId(), productData.getName(),
                productData.getPurchaseForm().toString().toLowerCase(),
                productPrice, totalAmountFromProduct, totalPriceForProduct);
        productInOrderMsg += SEPARATOR_MESSAGE;
        return productInOrderMsg;
    }
}
