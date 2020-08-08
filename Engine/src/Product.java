import jaxb.generated.SDMItem;

public class Product
{
    public enum ProductPurchaseForm
    {
        WEIGHT, QUANTITY
    }

    private int id;
    private String name;
    private ProductPurchaseForm purchaseForm;
    private float price;

    public Product(int id, String name, ProductPurchaseForm purchaseForm, float price)
    {
        this.id = id;
        this.name = name;
        this.purchaseForm = purchaseForm;
        this.price = price;
    }

    public Product(SDMItem item)
    {
        this.id = item.getId();
        this.name = item.getName();
        this.purchaseForm = (item.getPurchaseCategory().equals("Weight") ? ProductPurchaseForm.WEIGHT : ProductPurchaseForm.QUANTITY);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductPurchaseForm getPurchaseForm() {
        return purchaseForm;
    }

    public void setSaleByWeight(ProductPurchaseForm purchaseForm) {
        this.purchaseForm = purchaseForm;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product details:" + "\r\n" +
                "ID: " + id + "\r\n" +
                "Name: " + name + "\r\n" +
                "The product is purchase by: " + purchaseForm.toString().toLowerCase() + "\r\n";
//                +
//                "price: " + price;
    }
}
