package engineLogic;

public abstract class User
{
    private String name;
    private int id;
    private static int idNumber = 1;

    protected User(int id, String name)
    {
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }
}
