import jaxb.generated.SuperDuperMarketDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class XmlSystemDataBuilder
{
    private final String JAXB_PACKAGE_NAME = "jaxb.generated";
    private final String FILE_NOT_EXIST_ERROR_MSG = "Cause of failure : No xml file was found in this path: ";
    private final String FILE_NOT_XML_ERROR_MSG = "Cause of failure: The file in the path is not xml file ";

    private String xmlFilePath;

    public SystemData deserializeXmlToSystemData(String xmlFilePath) throws JAXBException, FileNotFoundException
    {
        this.xmlFilePath = xmlFilePath;
        InputStream inputStream = createInputStreamFromPath();
        JAXBContext jc = JAXBContext.newInstance(JAXB_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return new SystemData((SuperDuperMarketDescriptor) u.unmarshal(inputStream));

    }

    private InputStream createInputStreamFromPath() throws FileNotFoundException
    {
        if(!xmlFilePath.endsWith(".xml"))
        {
            throw new IllegalArgumentException(FILE_NOT_XML_ERROR_MSG);
        }
        InputStream inputStream = XmlSystemDataBuilder.class.getResourceAsStream(xmlFilePath);
        if (inputStream == null)
        {
            throw new FileNotFoundException(FILE_NOT_EXIST_ERROR_MSG + xmlFilePath);
        }
        return inputStream;
    }

}
