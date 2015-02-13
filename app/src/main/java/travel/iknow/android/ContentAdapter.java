package travel.iknow.android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import travel.iknow.android.rest.Category;
import travel.iknow.android.rest.Content;

/**
 * Created by Pristalov Pavel on 13.02.2015 for IKnowTravel.
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder>
{
    private List<Content> dataset;
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
    public ContentAdapter(Context context, List<Content> myDataset)
    {
        dataset = myDataset;
        this.context = context;
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
        Content content = dataset.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(content.getTitle());

        String allCategories = "";
        for(Category cat : content.getCategories())
        {
            allCategories = allCategories + cat.getName() + ", ";
        }
        allCategories = allCategories.substring(0, allCategories.length() - 2);
        holder.category.setText(allCategories);
        String url = content.getCover().getUrl();
        if(!url.isEmpty()) Picasso.with(context).load(url).into(holder.icon);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return dataset.size();
    }
}

