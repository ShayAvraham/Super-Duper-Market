public class SystemManager
{
    private SystemData systemData;

    public SystemManager(SystemData systemData)
    {
        this.systemData = systemData;
    }

    public void ShowAllStoresInSystem()
    {
        for (Store store: systemData.getStores().values())
        {
            System.out.println(store);
            for (Product product: systemData.getProducts().values())
            {
                System.out.println(product);
            }
        }
    }
}
