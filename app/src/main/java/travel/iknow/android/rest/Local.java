package travel.iknow.android.rest;

/**
 * Created by Pristalov Pavel on 12.02.2015 for IKnowTravel.
 */
public class Local
{
    private String name;
    private String email;
    private String password;
    private boolean create;

    public Local()
    {
        setName("");
        setEmail("");
        setPassword("");
        setCreate(false);
    }

    public Local(String name, String email, String password, boolean create)
    {
        setName(name);
        setEmail(email);
        setPassword(password);
        setCreate(create);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
    public boolean isCreate()
    {
        return create;
    }

    public void setCreate(boolean create)
    {
        this.create = create;
    }
}
