package travel.iknow.android.data.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.google.gson.annotations.SerializedName;

/**
 * Created by home1 on 14.02.2015.
 */
public abstract class AbstractCover extends Model
{
    @SerializedName("_id")
    @Column(name = "_id")
    private String id;
    @Column(name = "filename")
    private String filename;
    @Column(name = "url")
    private String url;

    public AbstractCover()
    {
        setCoverId("");
        setFilename("");
        setUrl("");
    }

    public String getCoverId()
    {
        return id;
    }

    public void setCoverId(String id)
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
