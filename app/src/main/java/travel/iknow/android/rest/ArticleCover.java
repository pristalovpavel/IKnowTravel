package travel.iknow.android.rest;

/**
 * Created by home1 on 14.02.2015.
 */
public class ArticleCover extends AbstractCover
{
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
