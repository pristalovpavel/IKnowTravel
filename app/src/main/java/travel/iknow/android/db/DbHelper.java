package travel.iknow.android.db;

import com.activeandroid.query.Select;

import java.util.List;

import travel.iknow.android.data.model.Category;
import travel.iknow.android.data.model.Content;

/**
 * Created by home1 on 14.02.2015.
 */
public class DbHelper
{
    public static boolean isIdInDB(Class clazz, String id)
    {
        String where = new StringBuilder("_id")
                .append(" = ?")
                .toString();

        return new Select()
                .from(clazz)
                .where(where, id)
                .count() > 0;
    }

    public static boolean isRelationInDB(Class clazz, String contentId)
    {
        String where = new StringBuilder("content_id")
                .append(" = ?")
                .toString();

        return new Select()
                .from(clazz)
                .where(where, contentId)
                .count() > 0;
    }

    public static Content findContent(String id)
    {
        return new Select()
                .from(Content.class)
                .where("_id = ?", id)
                .executeSingle();
    }

    public static ContentCategoriesRelation getContentCategories(String contentId)
    {
        return new Select()
                .from(ContentCategoriesRelation.class)
                .where("content_id = ?", contentId)
                .executeSingle();
    }

    public static String getCategoriesNamesByIds(String [] ids)
    {
        StringBuilder builder = new StringBuilder("");

        for (int i = 0; i < ids.length; ++i)
        {
            builder.append("'").append(ids[i]).append("'");
            if(i < ids.length - 1) builder.append(", ");
        }

        String query = "_id IN ( " + builder.toString() + " )";

        List<Category> categories =  new Select()
                .from(Category.class)
                .where(query)
                .execute();

        String result = "";
        for (int i = 0; i < categories.size(); i++)
        {
            result += categories.get(i).getName();//String.valueOf(values[i]);
            if (i < categories.size() - 1)
            {
                result += ", ";
            }
        }

        return result;
    }

    public static ContentArticleCoverRelation getArticleCoverRelation(String contentId)
    {
        return new Select()
                .from(ContentArticleCoverRelation.class)
                .where("content_id = ?", contentId)
                .executeSingle();
    }

    public static ContentAddressCoverRelation getAddressCoverRelation(String contentId)
    {
        return new Select()
                .from(ContentAddressCoverRelation.class)
                .where("content_id = ?", contentId)
                .executeSingle();
    }
}
