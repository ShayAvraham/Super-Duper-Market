public class SystemManager
{
    private SystemData systemData;

    public SystemManager(SystemData systemData)
    {
        this.systemData = systemData;
    }

    public void ShowAllStoresInSystem()
    {
        System.out.println("All stores in the system:");
        System.out.println("---------------------------");
        for (Store store: systemData.getStores().values())
        {
            System.out.println(store);
            System.out.println("All " + store.getName() + " products:");
            System.out.println("---------------------------");
            for (Product product: systemData.getProducts().values())
            {
                System.out.println(product);
                System.out.println("Price: " + store.getProductsInStore().get(product) + "\n");
            }
            System.out.println("---------------------------");
        }
    }

    public void ShowAllProductsInSystem()
    {
        System.out.println("All products in the system:\r\n");
        System.out.println("---------------------------\r\n");
        for (Product product: systemData.getProducts().values())
        {
            System.out.println(product);
            System.out.println(getHowManyStoresSellProduct(product));
            System.out.println(getProductAvgPrice(product));
            System.out.println(getHowManyTimesProductSold(product));
        }
    }

    private int getHowManyStoresSellProduct(Product product)
    {
        int howManyStoresSellProduct = 0;

        for (Store store: systemData.getStores().values())
        {
            if (store.getHowManyTimesProductSold(product) > 0)
            {
                howManyStoresSellProduct++;
            }
        }
        return howManyStoresSellProduct;
    }

    private int getHowManyTimesProductSold(Product product)
    {
        int howManyTimesProductSold = 0;

        for (Store store: systemData.getStores().values())
        {
            howManyTimesProductSold += store.getHowManyTimesProductSold(product);
        }
        return howManyTimesProductSold;
    }

    private float getProductAvgPrice(Product product)
    {
        float productAvgPrice = 0;
        int numOfStoresWhoSellsProduct = getHowManyStoresSellProduct(product);

        for (Store store: systemData.getStores().values())
        {
            if (store.getProductsInStore().get(product) != null)
            {
                productAvgPrice += store.getProductsInStore().get(product);
            }
        }
        return (productAvgPrice/numOfStoresWhoSellsProduct);
    }
}
