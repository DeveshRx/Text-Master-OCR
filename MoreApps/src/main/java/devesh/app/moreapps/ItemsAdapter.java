package devesh.app.moreapps;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    private ArrayList<HashMap<String,String>> localDataSet;
Context mContext;
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView TitleTextview;
        private final TextView SummaryText;
        private final LinearLayout LLItem;
        private final ImageView ThumbIcon;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            TitleTextview = (TextView) view.findViewById(R.id.TitleTextview);
            SummaryText=view.findViewById(R.id.SummaryText);
            LLItem=view.findViewById(R.id.LLItem);
            ThumbIcon=view.findViewById(R.id.ThumbIcon);
        }

        public TextView getTitleTextview() {
            return TitleTextview;
        }
        public LinearLayout getLLItem() {
            return LLItem;
        }
        public ImageView getThumbIcon() {
            return ThumbIcon;
        }

        public TextView getSummaryText() {
            return SummaryText;
        }
    }


    public ItemsAdapter(Context context, ArrayList<HashMap<String,String>> dataSet) {
        localDataSet = dataSet;
        mContext=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycleview_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTitleTextview().setText(localDataSet.get(position).get("name"));
        viewHolder.getTitleTextview().setText(localDataSet.get(position).get("desc"));
        Glide.with(mContext).load(localDataSet.get(position).get("icon")).into(viewHolder.ThumbIcon);
        viewHolder.getLLItem().setTag(localDataSet.get(position).get("url"));
        viewHolder.getLLItem().setOnClickListener(view -> {
            Log.d("TAG", "onBindViewHolder: ");

        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
