package cf.do7aelnakeeb.moviesapp.helper;

import android.content.Context;
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
 * Created by NakeebMac on 10/21/16.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Movie> arrayList;

    public MoviesAdapter(Context context, ArrayList<Movie> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());

        ViewGroup viewGroup = (ViewGroup) mInflater.inflate(R.layout.movie_item, parent, false);

        return new ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Picasso.with(context).load(AppConst.MoviesDBImageURL + arrayList.get(position).getImage()).into(holder.MoviePoster);
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView MoviePoster;
        ViewHolder(View itemView) {
            super(itemView);

            MoviePoster = (ImageView) itemView.findViewById(R.id.MoviePoster);
        }
    }
}
