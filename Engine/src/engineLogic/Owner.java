package engineLogic;

import java.util.*;

public class Owner extends User
{
    private Map<String,Region> regions;
    public Owner (int id, String name)
    {
        super(id,name);
        regions = new HashMap<>();
    }

    public void addRegion(Region region)
    {
        regions.put(region.getName(),region);
    }
}
