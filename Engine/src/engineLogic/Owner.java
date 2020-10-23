package engineLogic;

import java.util.*;

public class Owner extends User
{
    private Map<String,Region> regions;
    private Collection<Feedback> feedbacks;
    public Owner (String name,float balance, Collection<Transaction>transactions)
    {
        super(name,balance,transactions);
        regions = new HashMap<>();
        feedbacks = new ArrayList<>();
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public void addRegion(Region region)
    {
        regions.put(region.getName(),region);
    }

    public Collection<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void addFeedback(Feedback newFeedback)
    {
        feedbacks.add(newFeedback);
    }

}
