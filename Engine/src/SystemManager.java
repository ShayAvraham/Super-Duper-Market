public class SystemManager
{
    private SystemData systemData;

    public SystemManager(SystemData systemData)
    {
        this.systemData = systemData;
    }

    public void ShowAllStoresInSystem()
    {
        for (Store store: systemData.getStores())
        {
            System.out.println(store);
            for (Product product: systemData.getProducts())
            {
                System.out.println(product);
            }
        }
    }
}
