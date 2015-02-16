package travel.iknow.android.data.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Pristalov Pavel on 13.02.2015 for IKnowTravel.
 */
@Table(name = "Content")
public class Content extends Model
{
    @Column(name = "_id")
    @SerializedName("_id")
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    private String type;

    private ArrayList<Category> categories;

    @SerializedName("address_cover_1x")
    @Column(name = "address_cover")
    private AddressCover addressCover;

    @SerializedName("article_cover_1x")
    @Column(name = "article_cover")
    private ArticleCover articleCover;

    public Content()
    {
        setTitle("");
        setType("");
        setCategories(new ArrayList<Category>());
        setAddressCover(new AddressCover());
        setArticleCover(new ArticleCover());
    }

    public String getContentId() {
        return id;
    }

    public void setContentId(String id) {
        this.id = id;
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

    public AddressCover getAddressCover()
    {
        return addressCover;
    }

    public void setAddressCover(AddressCover addressCover)
    {
        this.addressCover = addressCover;
    }

    public ArticleCover getArticleCover()
    {
        return articleCover;
    }

    public void setArticleCover(ArticleCover articleCover)
    {
        this.articleCover = articleCover;
    }
}
