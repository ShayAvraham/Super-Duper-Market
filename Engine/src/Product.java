import jaxb.generated.SDMItem;

import java.util.Objects;

public class Product
{
    private int id;
    private String name;
    private ProductDataContainer.ProductPurchaseForm purchaseForm;

    public Product(SDMItem item)
    {
        this.id = item.getId();
        this.name = item.getName();
        this.purchaseForm = (item.getPurchaseCategory().equals("Weight") ?
                ProductDataContainer.ProductPurchaseForm.WEIGHT :
                ProductDataContainer.ProductPurchaseForm.QUANTITY);
    }

    protected Product(Product product)
    {
        this.id = product.getId();
        this.name = product.getName();
        this.purchaseForm = product.getPurchaseForm();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProductDataContainer.ProductPurchaseForm getPurchaseForm() {
        return purchaseForm;
    }

    @Override
    public String toString()
    {
        return "Product details:" + "\n" +
                "ID: " + id + "\n" +
                "Name: " + name + "\n" +
                "The product is purchase by: " + purchaseForm.toString().toLowerCase();
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
