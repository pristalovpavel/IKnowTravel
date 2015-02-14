package travel.iknow.android.data.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Pristalov Pavel on 13.02.2015 for IKnowTravel.
 */
@Table(name = "Categories")
public class Category extends Model
{
    @SerializedName("_id")
    @Column(name = "_id")
    private String id;
    @Column(name = "locale")
    private String locale;
    @Column(name = "name")
    private String name;

    public Category()
    {
        setCategoryId("");
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

    public String getCategoryId()
    {
        return id;
    }

    public void setCategoryId(String id)
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
