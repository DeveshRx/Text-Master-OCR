package devesh.app.ocr.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import devesh.app.ocr.HistoryActivity;
import devesh.app.ocr.R;
import devesh.app.ocr.database.ScanFile;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    String TAG = "HistoryAdapter";
    Context mContext;
    private List<ScanFile> localDataSet;

    public HistoryAdapter(Context context, List<ScanFile> dataSet) {
        localDataSet = dataSet;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycleview_history_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position).text);
        viewHolder.getLLItem().setTag(position);
        viewHolder.getLLItem().setOnClickListener(view -> {

            ((HistoryActivity) mContext).OpenHistoryFile(Integer.parseInt(view.getTag().toString()));
        });

        viewHolder.getCopyButton().setOnClickListener(view -> {
            Log.d(TAG, "onBindViewHolder: Copy Button");
            ((HistoryActivity) mContext).CopyText(position);
        });
        viewHolder.getShareButton().setOnClickListener(view -> {
            Log.d(TAG, "onBindViewHolder: Share Button");
            ((HistoryActivity) mContext).ShareText(position);

        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final LinearLayout LLItem;
        private final Button CopyButton;
        private final Button ShareButton;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.textView);
            LLItem = view.findViewById(R.id.LLItem);
            CopyButton = view.findViewById(R.id.CopyButton);
            ShareButton = view.findViewById(R.id.ShareButton);
        }

        public TextView getTextView() {
            return textView;
        }

        public LinearLayout getLLItem() {
            return LLItem;
        }

        public Button getCopyButton() {
            return CopyButton;
        }

        public Button getShareButton() {
            return ShareButton;
        }
    }
}
