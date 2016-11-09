package cf.do7aelnakeeb.moviesapp.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cf.do7aelnakeeb.moviesapp.R;
import cf.do7aelnakeeb.moviesapp.app.AppConst;
import cf.do7aelnakeeb.moviesapp.app.Movie;

/**
 * Created by NakeebMac on 10/29/16.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Movie.Trailer> arrayList;

    public TrailersAdapter(Context context, ArrayList<Movie.Trailer> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());

        ViewGroup viewGroup = (ViewGroup) mInflater.inflate(R.layout.trailer_item, parent, false);

        return new ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Picasso.with(context).load(AppConst.TrailerThumbnail + arrayList.get(position).getKey() + "/mqdefault.jpg").into(holder.trailerThumbnail);

        holder.trailerPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + arrayList.get(holder.getAdapterPosition()).getKey()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView trailerThumbnail;
        ImageView trailerPlay;
        ViewHolder(View itemView) {
            super(itemView);

            trailerThumbnail = (ImageView) itemView.findViewById(R.id.trailerThumbnail);
            trailerPlay = (ImageView) itemView.findViewById(R.id.trailer_play);
        }
    }
}
