package engineLogic;

import java.util.*;

public class Owner extends User
{
    private Map<String,Region> regions;
    private Collection<Notice> notices;
    public Owner (String name,float balance, Collection<Transaction>transactions)
    {
        super(name,balance,transactions);
        regions = new HashMap<>();
        notices = new ArrayList<>();
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public void addRegion(Region region)
    {
        regions.put(region.getName(),region);
    }

    public Collection<Notice> getNotices() {
        return notices;
    }

    public void addFeedback(Notice newFeedback)
    {
        notices.add(newFeedback);
    }

}
