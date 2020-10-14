package engineLogic;

import java.util.*;

public class Owner extends User
{
    private Map<String,Collection<Region>> regions;
    public Owner (int id, String name)
    {
        super(id,name);
        regions = new HashMap<>();
    }

    public void addRegion(Region region)
    {
        regions.put(region.getName(),new ArrayList<Region>(){{add(region);}});
    }

    public Map<String,Collection<Region>> getRegions() {
        return regions;
    }
}
