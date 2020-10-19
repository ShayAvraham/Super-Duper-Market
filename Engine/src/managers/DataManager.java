package managers;

import dataContainers.UserDataContainer;
import engineLogic.*;
import exceptions.DuplicateValuesException;
import jaxb.generated.SuperDuperMarketDescriptor;

import javax.management.InstanceNotFoundException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class DataManager
{
    private Map<String, Region> allRegions;
    private Map<Integer, User> allUsers;

    private final String JAXB_PACKAGE_NAME = "jaxb.generated";
    private final String FILE_NOT_EXIST_ERROR_MSG = "No xml file was found in this path: ";
//    private String xmlFilePath;

    public DataManager()
    {
        this.allRegions = new HashMap<>();
        this.allUsers = new HashMap<>();
    }

    /********************************************** Load XML  ****************************************/

    public Region deserializeXMLToRegion(int ownerID,String ownerName,InputStream xmlFileInputStream) throws JAXBException, FileNotFoundException, InstanceNotFoundException
    {
        JAXBContext jc = JAXBContext.newInstance(JAXB_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        Region newRegion = new Region((SuperDuperMarketDescriptor) u.unmarshal(xmlFileInputStream),ownerName);
        if(allRegions.putIfAbsent(newRegion.getName(),newRegion)!=null)//change
        {
            throw new DuplicateValuesException("region",newRegion.getName());
        }
        ((Owner)allUsers.get(ownerID)).addRegion(newRegion);
        return newRegion;
    }

//    private InputStream createInputStreamFromPath() throws FileNotFoundException
//    {
//        validateXmlFileFormat();
//        InputStream inputStream = new FileInputStream(new File(xmlFilePath));
//        if (inputStream == null)
//        {
//            throw new FileNotFoundException(FILE_NOT_EXIST_ERROR_MSG + xmlFilePath);
//        }
//        return inputStream;
//    }

//    private void validateXmlFileFormat()
//    {
//        if(!xmlFilePath.endsWith(".xml"))
//        {
//            throw new IllegalArgumentException(FILE_NOT_XML_ERROR_MSG);
//        }
//    }

    /********************************************** Add New User  ***************************************/

    public User addNewUser(UserDataContainer userData)
    {
        validateUser(userData);
        User newUser = userData.getRole().equals("customer")?
                new Customer(userData.getName(),
                        userData.getBalance(),
                        new ArrayList<Transaction>()):
                new Owner(userData.getName(),
                        userData.getBalance(),
                        new ArrayList<Transaction>());
        allUsers.put(newUser.getId(),newUser);
        return newUser;
    }

    private void validateUser(UserDataContainer userData)
    {
        if(allUsers.size()!=0)
        {
            for (User user : allUsers.values())
            {
                if (user.getName().equals(userData.getName()))
                {
                    throw new DuplicateValuesException("user", user.getName());
                }
            }
        }
    }


//    private final Set<String> usersSet;
//
//
//    public synchronized void removeUser(String username)
//    {
//        usersSet.remove(username);
//    }
//
//    public synchronized Set<String> getUsers()
//    {
//        return Collections.unmodifiableSet(usersSet);
//    }
//

/********************************************** Place Order Logic ****************************************/

/** Dynamic Store Allocation **/

public Store getStoreWithTheCheapestPrice(String regionName, int productId)
{
    Store cheapestStore = null;
    for(Store store : allRegions.get(regionName).getStores().values())
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

    public Discount getDiscount(String regionName, int storeID, String discountName)
    {
       return allRegions.get(regionName).getStores().get(storeID).getDiscountByName(discountName);
    }


}
