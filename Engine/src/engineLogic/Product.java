package engineLogic;

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
        this.purchaseForm = (item.getPurchaseCategory().equals("Weight") ?
                ProductPurchaseForm.WEIGHT :
                ProductPurchaseForm.QUANTITY);
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

    public ProductPurchaseForm getPurchaseForm() {
        return purchaseForm;
    }

    @Override
    public String toString()
    {
        return "engineLogic.Product details:" + "\n" +
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
