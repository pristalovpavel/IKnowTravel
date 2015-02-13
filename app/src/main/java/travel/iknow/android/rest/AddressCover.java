package travel.iknow.android.rest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pristalov Pavel on 13.02.2015 for IKnowTravel.
 */
public class AddressCover
{
    @SerializedName("_id")
    private String id;
    private String url;

    public AddressCover()
    {
        setId("");
        setUrl("");
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}
