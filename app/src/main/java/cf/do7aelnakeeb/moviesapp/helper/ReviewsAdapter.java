package cf.do7aelnakeeb.moviesapp.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import cf.do7aelnakeeb.moviesapp.R;
import cf.do7aelnakeeb.moviesapp.app.Movie;

/**
 * Created by NakeebMac on 10/29/16.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Movie.Review> arrayList;

    public ReviewsAdapter(Context context, ArrayList<Movie.Review> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());

        ViewGroup viewGroup = (ViewGroup) mInflater.inflate(R.layout.review_item, parent, false);

        return new ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.authorName.setText(arrayList.get(holder.getAdapterPosition()).getAuthor());

        holder.content.setText(arrayList.get(holder.getAdapterPosition()).getContent());
        holder.reviewUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(arrayList.get(holder.getAdapterPosition()).getUrl()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView authorName;
        TextView content;
        ImageButton reviewUrl;
        public ViewHolder(View itemView) {
            super(itemView);

            authorName = (TextView) itemView.findViewById(R.id.authorName);
            content = (TextView) itemView.findViewById(R.id.content);
            reviewUrl = (ImageButton) itemView.findViewById(R.id.reviewUrl);
        }
    }
}
