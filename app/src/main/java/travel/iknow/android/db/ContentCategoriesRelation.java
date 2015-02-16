package travel.iknow.android.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;

import travel.iknow.android.data.model.Category;
import travel.iknow.android.data.model.Content;

/**
 * Created by Pristalov Pavel on 16.02.2015 for IKnowTravel.
 */
@Table(name="content_categories")
public class ContentCategoriesRelation extends Model
{
    @Column(name="content_id")
    private String contentId;

    @Column(name="category_ids")
    private String categoryIds;

    public ContentCategoriesRelation()
    {
        setContentId("");
        setCategoryIds("");
    }

    public ContentCategoriesRelation(Content content)
    {
        this.contentId = content.getContentId();

        String result = "";
        ArrayList<Category> categories = content.getCategories();
        for (int i = 0; i < categories.size(); i++)
        {
            result += categories.get(i).getCategoryId();//String.valueOf(values[i]);
            if (i < categories.size() - 1)
            {
                result += ", ";
            }
        }
        this.categoryIds = result;
    }

    public String[] getCategoryIdsArray()
    {
        return getCategoryIds().split(", ");
    }

    public String getContentId()
    {
        return contentId;
    }

    public void setContentId(String contentId)
    {
        this.contentId = contentId;
    }

    public String getCategoryIds()
    {
        return categoryIds;
    }

    public void setCategoryIds(String categoryIds)
    {
        this.categoryIds = categoryIds;
    }
}
