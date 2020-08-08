public class SystemUI
{
    public static void main(String[] args)
    {
        try
        {
            XmlSystemDataBuilder temp1 = new XmlSystemDataBuilder();
            SystemData sd = temp1.deserializeXmlToSystemData("/resources/ex1-small.xml");
//            for (Product product: sd.getProducts())
//            {
//                System.out.print(product.toString());
//            }
            for (Store store: sd.getStores())
            {
                System.out.print(store.toString());
            }

        }
        catch (Exception exp) {
            System.out.print("Error:" + exp.getMessage());
        }
    }
}
