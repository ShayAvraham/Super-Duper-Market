package engineLogic;

import java.util.*;

public class Owner extends User
{
    private Map<String,Region> regions;
    public Owner (String name,float balance, Collection<Transaction>transactions)
    {
        super(name,balance,transactions);
        regions = new HashMap<>();
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public void addRegion(Region region)
    {
        regions.put(region.getName(),region);
    }
}
