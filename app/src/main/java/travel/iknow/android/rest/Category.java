package travel.iknow.android.rest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pristalov Pavel on 13.02.2015 for IKnowTravel.
 */
public class Category
{
    @SerializedName("_id")
    private String id;
    private String locale;
    private String name;

    public Category()
    {
        setId("");
        setLocale("");
        setName("");
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getLocale()
    {
        return locale;
    }

    public void setLocale(String locale)
    {
        this.locale = locale;
    }
}
