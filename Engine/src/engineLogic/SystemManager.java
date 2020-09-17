package engineLogic;
import dataContainers.*;
import exceptions.DiscountRemoveException;
import javafx.concurrent.Task;
import jaxb.generated.ThenYouGet;

import javax.management.InstanceNotFoundException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationException;
import java.awt.*;
import java.io.Console;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class SystemManager
{
    private final String UNABLE_TO_REMOVE_PRODUCT_ONE_STORE_MESSAGE = "Unable to remove this product because its sold only in this store.";
    private final String UNABLE_TO_REMOVE_PRODUCT_ONE_PRODUCT_MESSAGE = "Unable to remove this product because the store sold only this product.";

    private SystemData systemData;
    private XmlSystemDataBuilder xmlSystemDataBuilder;
    private Collection<StoreDataContainer> allStoresData;
    private Collection<ProductDataContainer> allProductsData;
    private Collection<OrderDataContainer> allOrdersData;
    private Collection<CustomerDataContainer> allCustomersData;
    private boolean isFileWasLoadSuccessfully = false;
    private Map <StoreDataContainer,Collection<ProductDataContainer>> storeToPurchaseFrom;
    private Map <Integer,Float> deliveryCostFromStores;
    private static DecimalFormat DECIMAL_FORMAT;

    static
    {
        DECIMAL_FORMAT = new DecimalFormat("#.##");
    }

    public SystemManager()
    {
        this.xmlSystemDataBuilder = new XmlSystemDataBuilder();
    }

    public Collection<StoreDataContainer> getAllStoresData() {
        return allStoresData;
    }

    public Collection<ProductDataContainer> getAllProductsData() {
        return allProductsData;
    }

    public Collection<OrderDataContainer> getAllOrdersData() {
        return allOrdersData;
    }

    public Collection<CustomerDataContainer> getAllCustomersData() {
        return allCustomersData;
    }

    public boolean isFileWasLoadSuccessfully() {
        return isFileWasLoadSuccessfully;
    }

    /********************************************** Load XML Logic ****************************************/


    public void loadDataFromXmlFile(String xmlFilePath) throws JAXBException, FileNotFoundException, InstanceNotFoundException
    {
        SystemData newSystemData = xmlSystemDataBuilder.deserializeXmlToSystemData(xmlFilePath);
        systemData = newSystemData;
        updateDataContainers();
        isFileWasLoadSuccessfully = true;
    }

    /********************************************** Update Data Containers ****************************************/
    private void updateDataContainers()
    {
        createAllCustomersData();
        createAllProductsData();
        createAllStoresData();
        createAllOrdersData();
    }

    /** Create Customers Data Containers **/
    private void createAllCustomersData()
    {
        allCustomersData = new ArrayList<>();
        for (Customer customer: systemData.getCustomers().values())
        {
            allCustomersData.add(createCustomerData(customer));
        }
    }

    private CustomerDataContainer createCustomerData(Customer customer)
    {
        return new CustomerDataContainer(
                customer.getId(),
                customer.getName(),
                getCustomerNumOfOrders(customer),
                getCustomerOrderCostAvg(customer),
                getCustomerDeliveryCostAvg(customer),
                customer.getPosition());
    }

    private int getCustomerNumOfOrders(Customer customer)
    {
        int numOfOrders = 0;
        for(Order order: systemData.getOrders())
        {
            if(order.getCustomer().equals(customer))
            {
                numOfOrders++;
            }
        }
        return numOfOrders;
    }

    private float getCustomerOrderCostAvg(Customer customer)
    {
        return (float) systemData.getOrders().stream()
                .filter(order -> order.getCustomer().equals(customer))
                .mapToDouble(Order::getCostOfAllProducts)
                .average()
                .orElse(0.0);
    }

    private float getCustomerDeliveryCostAvg(Customer customer)
    {
        return (float) systemData.getOrders().stream()
                .filter(order -> order.getCustomer().equals(customer))
                .mapToDouble(Order::getDeliveryCost)
                .average()
                .orElse(0.0);
    }

    /** Create Products Data Containers **/
    private void createAllProductsData()
    {
        allProductsData = new ArrayList<>();
        for (Product product: systemData.getProducts().values())
        {
            allProductsData.add(new ProductDataContainer(
                    product.getId(),
                    product.getName(),
                    product.getPurchaseForm().name(),
                    getHowManyStoresSellProduct(product),
                    getProductAvgPrice(product),
                    getHowManyTimesProductSold(product)));
        }
    }

    private int getHowManyStoresSellProduct(Product product)
    {
        int howManyStoresSellProduct = 0;

        for (Store store: systemData.getStores().values())
        {
            if (store.isProductInStore(product))
            {
                howManyStoresSellProduct++;
            }
        }
        return howManyStoresSellProduct;
    }

    private float getProductAvgPrice(Product selectedProduct)
    {
        float sumOfProductPrices = 0;
        int numOfStoresWhoSellsProduct = getHowManyStoresSellProduct(selectedProduct);

        for (Store store: systemData.getStores().values())
        {
            StoreProduct product = store.getProductById(selectedProduct.getId());
            if (product != null)
            {
                sumOfProductPrices += product.getPrice();
            }
        }
        return (sumOfProductPrices/numOfStoresWhoSellsProduct);
    }

    private float getHowManyTimesProductSold(Product product)
    {
        float howManyTimesProductSold = 0;

        for (Store store: systemData.getStores().values())
        {
            howManyTimesProductSold += store.getHowManyTimesProductSold(product);
        }
        return howManyTimesProductSold;
    }

    /** Create Stores Data Containers **/
    private void createAllStoresData()
    {
        allStoresData = new ArrayList<>();
        for (Store store: systemData.getStores().values())
        {
            allStoresData.add(new StoreDataContainer(
                    store.getId(),
                    store.getName(),
                    store.getPosition(),
                    store.getPPK(),
                    getStoreTotalIncomeFromDeliveries(store),
                    getStoreProductsData(store),
                    getStoreOrdersData(store),
                    getStoreDiscountsData(store)));
        }
    }

    private float getStoreTotalIncomeFromDeliveries(Store store)
    {
        float totalIncomeFromDeliveries = 0;

        for (Order order: store.getStoreOrders())
        {
            totalIncomeFromDeliveries += order.getDeliveryCost();
        }
        return totalIncomeFromDeliveries;
    }

    private Collection<ProductDataContainer> getStoreProductsData(Store store)
    {
        Collection<ProductDataContainer> allProductsData = new ArrayList<>();
        for (StoreProduct product: store.getStoreProducts())
        {
            allProductsData.add(new ProductDataContainer(
                    product.getId(),
                    product.getName(),
                    product.getPurchaseForm().name(),
                    getProductPricePerStore(product),
                    getSoldAmountPerStore(product)));
        }

        return allProductsData;
    }


    private Map<Integer, Integer> getProductPricePerStore(StoreProduct selectedProduct)
    {
        Map<Integer,Integer> productPricePerStore = new HashMap<>();
        for (Store store: systemData.getStores().values())
        {
            for (StoreProduct storeProduct: store.getStoreProducts())
            {
                if (selectedProduct.equals(storeProduct))
                {
                    productPricePerStore.put(store.getId(), storeProduct.getPrice());
                    break;
                }
            }
        }
        return productPricePerStore;
    }

    private Map<Integer, Float> getSoldAmountPerStore(StoreProduct selectedProduct)
    {
        Map<Integer,Float> soldAmountPerStore = new HashMap<>();
        float soldAmount;
        for(Store store: systemData.getStores().values())
        {
            soldAmount = store.getHowManyTimesProductSold(selectedProduct);
            soldAmountPerStore.put(store.getId(),soldAmount);
        }
        return soldAmountPerStore;
    }

    private Collection<OrderDataContainer> getStoreOrdersData(Store store)
    {
        Collection<OrderDataContainer> allOrdersData = new ArrayList<>();
        for (Order order: store.getStoreOrders())
        {
            allOrdersData.add(createOrderData(order));
        }

        return allOrdersData;
    }

    private Collection<DiscountDataContainer> getStoreDiscountsData(Store store)
    {
        Collection<DiscountDataContainer> allDiscountData = new ArrayList<>();
        if(store.getStoreDiscounts()!=null) {
            for (Discount discount : store.getStoreDiscounts()) {
                allDiscountData.add(createDiscountData(discount));
            }
        }

        return allDiscountData;
    }

    private DiscountDataContainer createDiscountData(Discount discount)
    {
        return new DiscountDataContainer(discount.getName(),
                discount.getDiscountType().name(),
                getProductDataById(discount.getDiscountProduct().getId()),
                discount.getDiscountProduct().getAmountForDiscount(),
                createPriceForOfferProduct(discount.getProductsToOffer()),
                createAmountForOfferProduct(discount.getProductsToOffer()));
    }

    private Map<ProductDataContainer,Integer> createPriceForOfferProduct(Collection<OfferProduct> productsToOffer)
    {
        Map <ProductDataContainer,Integer> priceForOfferProduct = new HashMap<>();
        for (OfferProduct offerProduct:productsToOffer)
        {
            priceForOfferProduct.put(getProductDataById(offerProduct.getId()),offerProduct.getOfferPrice());
        }
        return priceForOfferProduct;
    }

    private Map<ProductDataContainer,Double> createAmountForOfferProduct(Collection<OfferProduct> productsToOffer)
    {
        Map <ProductDataContainer,Double> amountForOfferProduct = new HashMap<>();
        for (OfferProduct offerProduct:productsToOffer)
        {
            amountForOfferProduct.put(getProductDataById(offerProduct.getId()),offerProduct.getOfferAmount());
        }
        return amountForOfferProduct;
    }

    /** Create Orders Data Containers **/
    private void createAllOrdersData()
    {
        allOrdersData = new ArrayList<>();
        for (Order order: systemData.getOrders())
        {
            allOrdersData.add(createOrderData(order));
        }
    }

    private OrderDataContainer createOrderData(Order order)
    {
        return new OrderDataContainer(
                order.getId(),
                order.getDate(),
                getCustomerDataById(order.getCustomer().getId()),
                createOrderProductsData(order),
                createOrderDiscountsData(order),
                order.isDynamic(),
                order.getCostOfAllProducts(),
                order.getDeliveryCost(),
                order.getTotalCost());
    }

    private Map<StoreDataContainer, Collection<ProductDataContainer>> createOrderProductsData(Order order)
    {
        Map<StoreDataContainer, Collection<ProductDataContainer>> orderProducts = new HashMap<>();
        for(Store store : order.getProducts().keySet())
        {
            Collection<ProductDataContainer> productsData = new ArrayList<>();
            for(Product product : order.getProducts().get(store))
            {
                productsData.add(getProductDataById(product.getId()));
            }
            orderProducts.put(getStoreDataById(store.getId()),productsData);
        }
        return  orderProducts;
    }

    private Map<StoreDataContainer, Collection<DiscountDataContainer>> createOrderDiscountsData(Order order)
    {
        Map<StoreDataContainer, Collection<DiscountDataContainer>> orderDiscounts = new HashMap<>();
        for(Store store : order.getDiscounts().keySet())
        {
            Collection<DiscountDataContainer> discountsData = new ArrayList<>();
            for(Discount discount : order.getDiscounts().get(store))
            {
                discountsData.add(createDiscountData(discount));
            }
            orderDiscounts.put(getStoreDataById(store.getId()),discountsData);
        }
        return orderDiscounts;
    }

    /********************************************** Update Products Logic ****************************************/

    /** Remove Product Logic **/
    public void removeProductFromStore(StoreDataContainer store, ProductDataContainer productToRemove) throws ValidationException
    {
        validateRemoveProduct(store,productToRemove);
        try
        {
             systemData.removeProductFromStore(store.getId(),productToRemove.getId());
        }
        finally
        {
            updateDataContainers();
        }
    }

    private void validateRemoveProduct(StoreDataContainer store, ProductDataContainer product) throws ValidationException
    {
        if(!isOthersStoresSellThisProduct(store, product))
        {
            throw new ValidationException(UNABLE_TO_REMOVE_PRODUCT_ONE_STORE_MESSAGE);
        }
        if(store.getProducts().size() == 1)
        {
            throw new ValidationException(UNABLE_TO_REMOVE_PRODUCT_ONE_PRODUCT_MESSAGE);
        }
    }

    private boolean isOthersStoresSellThisProduct(StoreDataContainer selectedStore, ProductDataContainer product)
    {
        for(StoreDataContainer store: getAllStoresData())
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

    /** Add Product Logic **/
    public void addProductToStore(StoreDataContainer store, ProductDataContainer productToAdd, int price)
    {
        systemData.addProductToStore(store.getId(),productToAdd.getId(),price);
        updateDataContainers();
    }

    /** Update Product Price Logic **/
    public void updateProductPriceInStore(StoreDataContainer store, ProductDataContainer productToRemove, int newPrice)
    {
        systemData.updateProductPriceInStore(store.getId(), productToRemove.getId(), newPrice);
        updateDataContainers();
    }

    /********************************************** Place Order Logic ****************************************/

    /** Dynamic Store Allocation **/
    public Map<StoreDataContainer, Collection<ProductDataContainer>> dynamicStoreAllocation(Collection <ProductDataContainer> productsToPurchase)
    {
        storeToPurchaseFrom = new HashMap();
        for (ProductDataContainer productToPurchase : productsToPurchase)
        {
         //   StoreDataContainer store = getStoreDataContainer(getStoreWithTheCheapestPrice(productToPurchase.getId()));
            StoreDataContainer store = getStoreDataById(getStoreWithTheCheapestPrice(productToPurchase.getId()).getId());

            ArrayList<ProductDataContainer> products = new ArrayList<ProductDataContainer>();
            products.add(productToPurchase);
            if(storeToPurchaseFrom.putIfAbsent(store, products) != null)
            {
                storeToPurchaseFrom.get(store).add(productToPurchase);
            }
        }
        return storeToPurchaseFrom;

    }

    private Store getStoreWithTheCheapestPrice(int productId)
    {
        Store cheapestStore = null;
        for(Store store : systemData.getStores().values())
        {
            StoreProduct product = store.getProductById(productId);
            if(product != null)
            {
                if(cheapestStore == null)
                {
                    cheapestStore = store;
                }
                else if(cheapestStore.getProductById(productId).getPrice() > product.getPrice())
                {
                    cheapestStore = store;
                }
            }
        }
        return cheapestStore;
    }

    /** Available Discounts **/

    public Map<StoreDataContainer,Collection<DiscountDataContainer>> getAvailableDiscounts(Map<StoreDataContainer, Collection<ProductDataContainer>> storeToPurchaseFrom)
    {
        Map<StoreDataContainer,Collection<DiscountDataContainer>> availableDiscounts = new HashMap<>();
        for (StoreDataContainer store: storeToPurchaseFrom.keySet())
        {
            Collection <DiscountDataContainer> availableStoreDiscounts = getAvailableDiscountsInStore(store,storeToPurchaseFrom.get(store));
            if(availableStoreDiscounts != null)
            {
                availableDiscounts.put(store,availableStoreDiscounts);
            }
        }
        return availableDiscounts;
    }

    private Collection<DiscountDataContainer> getAvailableDiscountsInStore(StoreDataContainer store, Collection<ProductDataContainer> products)
    {
        Collection<DiscountDataContainer> availableStoreDiscounts = new ArrayList<>();
        for(DiscountDataContainer discount : store.getDiscounts())
        {
            for (ProductDataContainer product : products)
            {
                if (discount.getDiscountProduct().equals(product))
                {
                    for(double i = product.amountProperty().get(); i >= discount.getAmountForDiscount(); i-=discount.getAmountForDiscount())
                    {
                        availableStoreDiscounts.add(createDiscountData(systemData.getStores().get
                                (store.getId())
                                .getDiscountByName(discount.getDiscountName())));
                    }
                }
            }
        }
        if(availableStoreDiscounts.isEmpty())
        {
            return null;
        }
        return availableStoreDiscounts;
    }

    public void validateSelectedDiscounts(Collection<DiscountDataContainer> selectedDiscounts,Collection<ProductDataContainer> selectedProducts)
    {
        validatedOneOfDiscounts(selectedDiscounts);
        validatedNumOfDiscounts(selectedDiscounts,selectedProducts);
    }

    private void validatedOneOfDiscounts(Collection<DiscountDataContainer> selectedDiscounts)
    {
        for (DiscountDataContainer discount:selectedDiscounts)
        {
            if(discount.getDiscountType().equals("ONE_OF") && discount.selectedOfferProductProperty().isNull().get())
            {
                throw new IllegalArgumentException(String.format("You must select a product if you want to get %1$s discount",discount.getDiscountName()));
            }
        }
    }

    private void validatedNumOfDiscounts(Collection<DiscountDataContainer> selectedDiscounts,Collection<ProductDataContainer> selectedProducts)
    {
        for(ProductDataContainer product: selectedProducts)
        {
            double sum = 0;
            for (DiscountDataContainer discount : selectedDiscounts)
            {
                if(product.equals(discount.getDiscountProduct()))
                {
                    sum += discount.getAmountForDiscount();
                }
            }
            if(sum>product.amountProperty().doubleValue())
            {
                throw new IllegalArgumentException(String.format("You chosen to many discounts for the product %1$s, please reselect the discounts",product.getName()));
            }
        }
    }

    /** Create Order For Order Summary **/


    public float getOrderCostOfAllProducts(Map <StoreDataContainer,Collection<ProductDataContainer>> products,
                                      Map <StoreDataContainer,Collection<DiscountDataContainer>> discounts)
    {
        float orderCostOfAllProducts = 0;
        for(StoreDataContainer store: products.keySet())
        {
            orderCostOfAllProducts += getStoreCostOfAllProducts(store,products.get(store),discounts.get(store));
        }

        return orderCostOfAllProducts;
    }

    private float getStoreCostOfAllProducts(StoreDataContainer store,Collection<ProductDataContainer> products,
                                            Collection<DiscountDataContainer> discounts)
    {
        float storeCostOfAllProducts = 0;
        storeCostOfAllProducts += getProductsCostFromStore(store,products);
        storeCostOfAllProducts += getDiscountsCostFromStore(store,discounts);
        return storeCostOfAllProducts;
    }

    private float getDiscountsCostFromStore(StoreDataContainer store, Collection<DiscountDataContainer> discounts)
    {
        float costOfAllDiscounts = 0;
        for (DiscountDataContainer discount : discounts)
        {
            costOfAllDiscounts += discount.getPriceForOfferProduct().values().stream().findFirst().get() *
                    discount.getAmountForOfferProduct().values().stream().findFirst().get();
        }
        return costOfAllDiscounts;
    }

    public float getOrderDeliveryCost(Collection<StoreDataContainer> stores, CustomerDataContainer customer)
    {
        float deliveryCost = 0;
        for(StoreDataContainer store : stores)
        {
            deliveryCost += getDeliveryCostFromStore(store,customer);
        }
        return deliveryCost;
    }

    public float getDeliveryCostFromStore(StoreDataContainer store, CustomerDataContainer customer)
    {
        float deliveryCost  = getDistanceBetweenStoreAndCustomer(store, customer) * store.getPpk();
        return Float.valueOf(DECIMAL_FORMAT.format(deliveryCost));
    }


    /**     Add New Order    **/
    public void addNewOrder(OrderDataContainer newOrderDataContainer)
    {
        Map <Integer,Order> newSubOrders = new HashMap<>();
        Order newOrder = createNewOrder(newOrderDataContainer);
        newSubOrders = createSubOrders(newOrderDataContainer, newOrder.getId());

        systemData.addNewOrder(newOrder,newSubOrders);
        storeToPurchaseFrom = null;
        deliveryCostFromStores = null;
        updateDataContainers();
    }


    private Order createNewOrder(OrderDataContainer newOrderDataContainer)
    {
        return new Order(newOrderDataContainer.getDate(),
                systemData.getCustomers().get(newOrderDataContainer.getCustomer().getId()),
                createOrderProducts(newOrderDataContainer.getProducts()),
                createOrderDiscounts(newOrderDataContainer.getDiscounts()),
                newOrderDataContainer.isDynamic(),
                newOrderDataContainer.getCostOfAllProducts(),
                newOrderDataContainer.getDeliveryCost(),
                newOrderDataContainer.getTotalCost());
    }

    private Map<Store, Collection<OrderProduct>> createOrderProducts(Map<StoreDataContainer, Collection<ProductDataContainer>> products)
    {
        Map<Store, Collection<OrderProduct>> orderStoreAndProducts = new HashMap<>();
        for(StoreDataContainer store: products.keySet())
        {
            Store orderStore = systemData.getStores().get(store.getId());
            orderStoreAndProducts.put(orderStore,createOrderStoreProducts(store,products.get(store)));
        }
        return orderStoreAndProducts;
    }

    private Collection<OrderProduct> createOrderStoreProducts(StoreDataContainer store, Collection<ProductDataContainer> products)
    {
        Collection<OrderProduct> orderStoreProducts = new ArrayList<>();
        for(ProductDataContainer product : products)
        {
            orderStoreProducts.add(new OrderProduct(
                    systemData.getStores().get(store.getId()).getProductById(product.getId()),
                    new Float(product.getAmount())));
        }
        return orderStoreProducts;
    }

    private Map<Store, Collection<Discount>> createOrderDiscounts(Map<StoreDataContainer, Collection<DiscountDataContainer>> discounts)
    {
        Map<Store, Collection<Discount>> orderStoreAndProducts = new HashMap<>();
        for(StoreDataContainer store: discounts.keySet())
        {
            Store orderStore = systemData.getStores().get(store.getId());
            orderStoreAndProducts.put(orderStore,createOrderStoreDiscounts(store,discounts.get(store)));
        }
        return orderStoreAndProducts;
    }

    private Collection<Discount> createOrderStoreDiscounts(StoreDataContainer store, Collection<DiscountDataContainer> discounts)
    {
        Collection<Discount> orderStoreDiscounts = new ArrayList<>();
        for(DiscountDataContainer discount : discounts)
        {
            orderStoreDiscounts.add(new Discount(discount.getDiscountName(),
                    discount.getDiscountType(),
                    createDiscountProduct(store,discount),
                    createProductsToOffer(store ,discount)));
        }
        return orderStoreDiscounts;
    }

    private DiscountProduct createDiscountProduct(StoreDataContainer store, DiscountDataContainer discount)
    {
        return new DiscountProduct( systemData.getStores().get(store.getId()).getProductById(discount.getDiscountProduct().getId()),
                discount.getAmountForDiscount());
    }

    private Collection<OfferProduct> createProductsToOffer(StoreDataContainer store,DiscountDataContainer discount)
    {
        Collection<OfferProduct> offerProducts = new ArrayList<>();
        for(ProductDataContainer product: discount.getPriceForOfferProduct().keySet())
        {
            offerProducts.add(new OfferProduct(systemData.getStores().get(store.getId()).getProductById(product.getId()),
                    discount.getPriceForOfferProduct().get(product),
                    discount.getAmountForOfferProduct().get(product)));
        }
        return offerProducts;
    }

    private Map<Integer,Order> createSubOrders(OrderDataContainer newOrderDataContainer,int orderID)
    {
        Map <Integer,Order> subOrders = new HashMap<>();
        for(StoreDataContainer store : newOrderDataContainer.getProducts().keySet())
        {
            subOrders.put(store.getId(),createSubOrder(newOrderDataContainer,orderID ,store));
        }
        return subOrders;
    }

    private Order createSubOrder(OrderDataContainer newOrder, int orderID, StoreDataContainer storeData)
    {
        Store store = systemData.getStores().get(storeData.getId());
        float costOfAllProducts = getStoreCostOfAllProducts(storeData,newOrder.getProducts().get(storeData),
                newOrder.getDiscounts().get(storeData));
        float deliveryCost = getDeliveryCostFromStore(storeData,newOrder.getCustomer());;

        return new Order(orderID,
                newOrder.getDate(),
                systemData.getCustomers().get(newOrder.getCustomer().getId()),
                new HashMap<Store,Collection<OrderProduct>>()
                {{put(store,createOrderStoreProducts(storeData,newOrder.getProducts().get(storeData)));}},
                new HashMap<Store,Collection<Discount>>()
                {{put(store,createOrderStoreDiscounts(storeData,newOrder.getDiscounts().get(storeData)));}},
                newOrder.isDynamic(),
                costOfAllProducts,
                deliveryCost,
                costOfAllProducts + deliveryCost);
    }

    /********************************************** Order Summary Logic ****************************************/

    public Collection<DiscountDataContainer> createSubDiscounts(Collection <DiscountDataContainer> discounts)
    {
        Collection<DiscountDataContainer> subDiscounts = new ArrayList<>();
        for(DiscountDataContainer discount : discounts)
        {
            switch (discount.getDiscountType())
            {
                case "ONE_OF":
                    subDiscounts.add(createOneOfSubDiscount(discount));
                    break;
                case "ALL_OR_NOTHING":
                    subDiscounts.addAll(createAllOrNothingSubDiscounts(discount));
                    break;
                case "IRRELEVANT":
                    subDiscounts.add(discount);
                    break;
            }
        }
        return subDiscounts;
    }

    private DiscountDataContainer createOneOfSubDiscount(DiscountDataContainer discount)
    {
        ProductDataContainer selectedOfferProduct = discount.getSelectedOfferProduct();

        return new DiscountDataContainer(discount.getDiscountName(),
                discount.getDiscountType(),
                discount.getDiscountProduct(),
                discount.getAmountForDiscount(),
                new HashMap<ProductDataContainer,Integer>(){{put(selectedOfferProduct,discount.getPriceForOfferProduct().get(selectedOfferProduct));}},
                new HashMap<ProductDataContainer,Double>(){{put(selectedOfferProduct,discount.getAmountForOfferProduct().get(selectedOfferProduct));}});
    }


    private Collection<DiscountDataContainer> createAllOrNothingSubDiscounts(DiscountDataContainer discount)
    {
        Collection<DiscountDataContainer> subDiscounts = new ArrayList<>();
        for(ProductDataContainer offerProduct : discount.getPriceForOfferProduct().keySet())
        {
            subDiscounts.add(new DiscountDataContainer(discount.getDiscountName(),
                    discount.getDiscountType(),
                    discount.getDiscountProduct(),
                    discount.getAmountForDiscount(),
                    new HashMap<ProductDataContainer,Integer>(){{put(offerProduct,discount.getPriceForOfferProduct().get(offerProduct));}},
                    new HashMap<ProductDataContainer,Double>(){{put(offerProduct,discount.getAmountForOfferProduct().get(offerProduct));}}));
        }
        return subDiscounts;
    }

    /********************************************** Utilities ****************************************/

    public StoreDataContainer getStoreDataById(int storeId)
    {
        StoreDataContainer storeDataContainer = null;
        for (StoreDataContainer storeData : allStoresData)
        {
            if(storeData.getId() == storeId)
            {
                storeDataContainer = storeData;
                break;
            }
        }
        return storeDataContainer;
    }

    public ProductDataContainer getProductDataById(int productId)
    {
        ProductDataContainer productDataContainer = null;
        for (ProductDataContainer productData: getAllProductsData())
        {
            if (productData.getId() == productId)
            {
                productDataContainer = productData;
                break;
            }
        }
        return productDataContainer;
    }

    public CustomerDataContainer getCustomerDataById(int customerID)
    {
        CustomerDataContainer customerDataContainer = null;
        for (CustomerDataContainer customerData: getAllCustomersData())
        {
            if (customerData.getId() == customerID)
            {
                customerDataContainer = customerData;
                break;
            }
        }
        return customerDataContainer;
    }

    public float getDistanceBetweenStoreAndCustomer(StoreDataContainer store, CustomerDataContainer customer)
    {
        return Float.valueOf(DECIMAL_FORMAT.format(Math.abs((float) store.getPosition().distance(customer.getPosition()))));
    }

    public float getProductsCostFromStore(StoreDataContainer storeData, Collection<ProductDataContainer> products)
    {
        float cost = 0;
        Store store = systemData.getStores().get(storeData.getId());
        for (ProductDataContainer product:products)
        {
            cost += store.getProductById(product.getId()).getPrice() * product.amountProperty().get();
        }

        return  cost;
    }
}