package travel.iknow.android.db;

import com.activeandroid.query.Select;

import travel.iknow.android.data.model.Content;

/**
 * Created by home1 on 14.02.2015.
 */
public class DbHelper
{
    public static boolean isContentInDB(String id)
    {
        String where = new StringBuilder("_id")
                .append(" = ?")
                .toString();

        return new Select()
                .from(Content.class)
                .where(where, id)
                .count() > 0;
    }

    public static Content findContent(String id)
    {
        return new Select()
                .from(Content.class)
                .where("_id = ?", id)
                .executeSingle();
    }
}
