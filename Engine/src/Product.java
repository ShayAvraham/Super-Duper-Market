import jaxb.generated.SDMItem;

import java.util.Objects;

public class Product
{
    public enum ProductPurchaseForm
    {
        WEIGHT, QUANTITY
    }

    private int id;
    private String name;
    private ProductPurchaseForm purchaseForm;

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

    @Override
    public String toString()
    {
        return "Product details:" + "\r\n" +
                "ID: " + id + "\r\n" +
                "Name: " + name + "\r\n" +
                "The product is purchase by: " + purchaseForm.toString().toLowerCase() + "\r\n";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }
}
