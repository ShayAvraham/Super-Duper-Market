package engineLogic;

import jaxb.generated.Location;
import jaxb.generated.SDMCustomer;

import java.awt.*;
import java.util.Objects;

public class Customer
{
    private int id;
    private String name;
    private Point position;

    public Customer (SDMCustomer customer,Point position)
    {
        this.id = customer.getId();
        this.name = customer.getName();
        this.position = position;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public Point getPosition()
    {
        return position;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return getId() == customer.getId();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }
}
