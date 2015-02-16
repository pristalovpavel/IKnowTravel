package travel.iknow.android.db;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import travel.iknow.android.data.model.Content;

/**
 * Created by home1 on 14.02.2015.
 */
public class ContentCursorLoader extends CursorLoader
{
    public ContentCursorLoader(Context context)
    {
        super(context);
    }

    @Override
    public Cursor loadInBackground()
    {
        String sql = new Select("_id")
                .from(Content.class)
                .toSql();

        return ActiveAndroid.getDatabase().rawQuery(sql, null);
    }
}
