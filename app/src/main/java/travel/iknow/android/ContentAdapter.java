package travel.iknow.android;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import travel.iknow.android.data.DataSource;
import travel.iknow.android.data.model.AddressCover;
import travel.iknow.android.data.model.ArticleCover;
import travel.iknow.android.data.model.Category;
import travel.iknow.android.data.model.Content;
import travel.iknow.android.db.ContentArticleCoverRelation;
import travel.iknow.android.db.ContentCategoriesRelation;
import travel.iknow.android.db.DbHelper;

/**
 * Created by Pristalov Pavel on 13.02.2015 for IKnowTravel.
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder>
{
    private Cursor dataset;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView title;
        public TextView category;
        public ImageView icon;

        public ViewHolder(View layout)
        {
            super(layout);
            title = (TextView) layout.findViewById(R.id.title);
            category = (TextView) layout.findViewById(R.id.category);
            icon = (ImageView) layout.findViewById(R.id.icon);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContentAdapter(Context context, Cursor newDataset)
    {
        dataset = newDataset;
        this.context = context;
    }

    public Cursor swapCursor(Cursor newCursor)
    {
        Cursor oldCursor = dataset;

        dataset = newCursor;
        notifyDataSetChanged();

        return oldCursor;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        if (dataset == null) return;

        dataset.moveToPosition(position);

        Content content = DbHelper.findContent(dataset.getString(dataset.getColumnIndex("_id")));
        holder.title.setText(content.getTitle());

        /*String allCategories = "";
        for(Category cat : content.getCategories())
        {
            allCategories = allCategories + cat.getName() + ", ";
        }
        if(allCategories.length() > 2) allCategories = allCategories.substring(0, allCategories.length() - 2);
        */
        ContentCategoriesRelation relation = DbHelper.getContentCategories(content.getContentId());
        if(relation != null)
        {
            String categories = DbHelper.getCategoriesNamesByIds(relation.getCategoryIdsArray());
            holder.category.setText(categories);
        }

        String coverId = "";

        if(content.getType().equals(DataSource.TYPE_ARTICLE))
            coverId = DbHelper.getArticleCoverRelation(content.getContentId()).getArticleCoverId();

        if(content.getType().equals(DataSource.TYPE_ADDRESS))
            coverId = DbHelper.getAddressCoverRelation(content.getContentId()).getAddressCoverId();

        if (!coverId.isEmpty())
        {
            String imageUrl = makeImageUrl(coverId);
            if (!imageUrl.isEmpty()) Picasso.with(context).load(imageUrl).into(holder.icon);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return dataset != null ?
                dataset.getCount():
                0;
    }

    private String makeImageUrl(String id)
    {
        /*Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = (int)(width / 1.5);
        */

        return new StringBuilder()
                    .append(DataSource.API_DOMAIN_ADDRESS)
                    .append(DataSource.PHOTO_SUFFIX)
                    .append(id)
                    .append("_144x144.jpg")
//                    .append(width)
//                    .append("x")
//                    .append(height)
//                    .append(".jpg")
                    .toString();
    }
}

