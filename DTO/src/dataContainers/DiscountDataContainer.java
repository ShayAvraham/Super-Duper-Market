package dataContainers;

public class DiscountDataContainer
{
    private String discountName;
    private String ifYouBuyDescription;
    private String thenYouGetDescription;

    public DiscountDataContainer(String discountName, String ifYouBuyDescription, String thenYouGetDescription)
    {
        this.discountName = discountName;
        this.ifYouBuyDescription = ifYouBuyDescription;
        this.thenYouGetDescription = thenYouGetDescription;
    }

    public String getDiscountName() {
        return discountName;
    }

    public String getIfYouBuyDescription() {
        return ifYouBuyDescription;
    }

    public String getThenYouGetDescription() {
        return thenYouGetDescription;
    }
}
