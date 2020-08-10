import java.util.Scanner;

public class SystemUI
{
    private enum StartMenuOptions
    {
        LoadFile,
        ShowAllStores,
        ShowAllProducts,
        MakePurchase,
        ViewOrdersHistory,
        Quit
    }

    private SystemManager manager;


    public void run()
    {
        int userStartMenuChoise = 0;

        System.out.println("Welcome to super duper market!\n");
        while (userStartMenuChoise != StartMenuOptions.Quit.ordinal())
        {
            displayMenu();
            userStartMenuChoise = getUserStartMenuChoise();
            try
            {
                executeUserSelectedAction(StartMenuOptions.values()[userStartMenuChoise]);
            }
            catch(Exception ex)
            {
                String errorMessage = "\nOps, An error occurred. \n"
                        + ex.getMessage() + "\n";
                System.out.println(errorMessage);
            }
        }
        System.out.println("Bye bye, see you next time!");
    }

    private void displayMenu()
    {
        String menuStr = "Please choose one of the following action: \r\n" +
        "1. Load data from file. \r\n" +
        "2. Show all stores details. \r\n" +
        "3. Show all products in the system. \r\n" +
        "4. Place an order in the system. \r\n" +
        "5. View orders history in the system. \r\n" +
        "6. Quit. \r\n" +
        "The desired action number: \r\n";
        System.out.println(menuStr);
    }

    private int getUserStartMenuChoise()
    {
        int userStartMenuChoise = 0;
        boolean isInputValid = false;

        while (!isInputValid)
        {
            try
            {
                Scanner scanner = new Scanner(System.in);
                int userStartChoiseInput = scanner.nextInt();
                userStartMenuChoise = getValidEnumChoise(userStartChoiseInput);
                break;
            }
            catch (Exception ex)
            {
                String errorMessage = "Ops, An error occurred. \n"
                        + ex.getMessage() + "\n"
                        + "Please reenter the desired action number: \n";
                System.out.println(errorMessage);
            }
        }

        return userStartMenuChoise;
    }

    private int getValidEnumChoise(int userStartChoiseInput) throws Exception
    {
        if (userStartChoiseInput > StartMenuOptions.values().length || userStartChoiseInput < 0)
        {
            throw new Exception("Choise is out of range of valid values");
        }
        return userStartChoiseInput - 1;
    }


    private void executeUserSelectedAction(StartMenuOptions userStartMenuChoise)
    {
        switch (userStartMenuChoise)
        {
            case LoadFile:
                loadDataFromFile();
                break;
            case ShowAllStores:
                showAllStores();
                break;
            case ShowAllProducts:
                showAllProducts();
                break;
            case MakePurchase:
                makePurchase();
                break;
            case ViewOrdersHistory:
                showAllOrdersHistory();
                break;
            case Quit:
                break;
        }
    }

    private void showAllOrdersHistory() {

    }

    private void makePurchase()
    {
        showAvailableStoresToBuy();
    }

    private void showAllProducts()
    {
        if (manager.getAllProducts().size() > 0)
        {
            System.out.println("All products in the system:");
            System.out.println("---------------------------");
            for (Product product : manager.getAllProducts()) {
                System.out.println(product);
                if (manager.getHowManyStoresSellProduct(product) > 0) {
                    System.out.println("Number of stores who sells the product: " + manager.getHowManyStoresSellProduct(product));
                    System.out.println("Average price of the product: " + manager.getProductAvgPrice(product));
                    System.out.println("Number of times the product was sold: " + manager.getHowManyTimesProductSold(product));
                } else {
                    System.out.println("This product is not sold in any store.");
                }
                System.out.println("---------------------------");
            }
        }
        else
        {
            System.out.println("There are no products in the system.");
        }
    }

    private void showAllStores()
    {
        if(manager.getAllStores().size() > 0)
        {
            System.out.println("All stores in the system:");
            System.out.println("---------------------------");
            for (Store store : manager.getAllStores()) {
                System.out.println(store);
                System.out.println("All " + store.getName() + " products:");
                System.out.println("---------------------------");
                for (Product product : manager.getAllProducts()) {
                    System.out.println(product);
                    System.out.println("Price: " + store.getProductsInStore().get(product) + "\n");
                }
                System.out.println("---------------------------");
            }
        }
        else
        {
            System.out.println("There are no stores in the system.");
        }
    }

    private void showProduct(Store store, Product product)
    {
        System.out.println(product);
        System.out.println("Price: " + store.getProductsInStore().get(product));
        if(manager.getHowManyTimesProductSoldBySpecificStore(store, product) > 0) {
            System.out.println("Total amount of this product sold in this store: "
                    + manager.getHowManyTimesProductSoldBySpecificStore(store, product) + "\n");
        }
        else
        {
            System.out.println("This product is not yet sold in this store.");
        }
    }

    private void showOrder(Store store, Order order)
    {
        System.out.println(order);
        System.out.println("Total amount of products: " + order.getProductsInOrder().keySet().size());

    }

    private void loadDataFromFile()
    {
        try
        {
            XmlSystemDataBuilder xmlBuilder = new XmlSystemDataBuilder();

            SystemData systemData = xmlBuilder.deserializeXmlToSystemData("/resources/ex1-small.xml");
            this.manager = new SystemManager(systemData);
        }
        catch (Exception exp)
        {
            System.out.print("Error:" + exp.getMessage());
        }
    }

    private void showAvailableStoresToBuy()
    {
        System.out.println("All avialable stores in the system:");
        System.out.println("-----------------------------------");
        for (Store store: manager.getAllStores())
        {
            String storeDetails = store.getId() + ". " + store.getName() + "\n"
                                + "Price per kilometer: " + store.getPpk();
            System.out.println(storeDetails);
        }
    }
}
