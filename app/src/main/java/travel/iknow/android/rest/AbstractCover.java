package travel.iknow.android.rest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by home1 on 14.02.2015.
 */
public abstract class AbstractCover
{
    @SerializedName("_id")
    private String id;
    private String filename;
    private String url;

    public AbstractCover()
    {
        setId("");
        setFilename("");
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

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }
}
