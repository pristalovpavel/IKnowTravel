package travel.iknow.android.data.model;

import com.activeandroid.annotation.Column;

/**
 * Created by home1 on 14.02.2015.
 */
public class ArticleCover extends AbstractCover
{
    @Column(name = "copyright")
    private String copyright;

    public ArticleCover()
    {
        super();
        setCopyright("");
    }

    public String getCopyright()
    {
        return copyright;
    }

    public void setCopyright(String copyright)
    {
        this.copyright = copyright;
    }
}