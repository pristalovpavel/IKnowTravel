package travel.iknow.android.rest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Pristalov Pavel on 13.02.2015 for IKnowTravel.
 */
public class Content
{
    private String title;
    private String type;

    private ArrayList<Category> categories;

    @SerializedName("\"address_cover\"")
    private AddressCover cover;

    public Content()
    {
        setTitle("");
        setType("");
        setCategories(new ArrayList<Category>());
        setCover(new AddressCover());
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public ArrayList<Category> getCategories()
    {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories)
    {
        this.categories = categories;
    }

    public AddressCover getCover()
    {
        return cover;
    }

    public void setCover(AddressCover cover)
    {
        this.cover = cover;
    }
}
