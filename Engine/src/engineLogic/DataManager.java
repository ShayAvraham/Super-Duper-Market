package engineLogic;

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
import java.util.HashMap;
import java.util.Map;

public class DataManager
{
    private Map<String,Region> allRegions;
    private Map<Integer,User> allUsers;

    private final String JAXB_PACKAGE_NAME = "jaxb.generated";
    private final String FILE_NOT_EXIST_ERROR_MSG = "No xml file was found in this path: ";
    private final String FILE_NOT_XML_ERROR_MSG = "The file in the path is not xml file.";
    private String xmlFilePath;

    public DataManager()
    {
        this.allRegions = new HashMap<>();
        this.allUsers = new HashMap<>();
    }

    /********************************************** Load XML  ****************************************/

    public Region deserializeXMLToRegion(int ownerID,String xmlFilePath) throws JAXBException, FileNotFoundException, InstanceNotFoundException
    {
        this.xmlFilePath = xmlFilePath;
        InputStream inputStream = createInputStreamFromPath();
        JAXBContext jc = JAXBContext.newInstance(JAXB_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        Region newRegion = new Region((SuperDuperMarketDescriptor) u.unmarshal(inputStream));
        if(allRegions.putIfAbsent(newRegion.getName(),newRegion)!=null)
        {
            throw new DuplicateValuesException("region",newRegion.getName());
        }
        ((Owner)allUsers.get(ownerID)).addRegion(newRegion);
        return newRegion;
    }

    private InputStream createInputStreamFromPath() throws FileNotFoundException
    {
        validateXmlFileFormat();
        InputStream inputStream = new FileInputStream(new File(xmlFilePath));
        if (inputStream == null)
        {
            throw new FileNotFoundException(FILE_NOT_EXIST_ERROR_MSG + xmlFilePath);
        }
        return inputStream;
    }

    private void validateXmlFileFormat()
    {
        if(!xmlFilePath.endsWith(".xml"))
        {
            throw new IllegalArgumentException(FILE_NOT_XML_ERROR_MSG);
        }
    }
}
