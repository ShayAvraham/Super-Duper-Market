import jaxb.generated.SuperDuperMarketDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class XmlSystemDataBuilder
{
    private final  String JAXB_PACKAGE_NAME = "jaxb.generated";
    private String xmlFilePath;

    public SystemData deserializeXmlToSystemData(String xmlFilePath)throws JAXBException
    {
        this.xmlFilePath = xmlFilePath;
        InputStream inputStream = XmlSystemDataBuilder.class.getResourceAsStream(xmlFilePath);
        JAXBContext jc = JAXBContext.newInstance(JAXB_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return new SystemData((SuperDuperMarketDescriptor) u.unmarshal(inputStream));
    }

}
