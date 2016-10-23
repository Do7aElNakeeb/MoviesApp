package cf.do7aelnakeeb.moviesapp.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cf.do7aelnakeeb.moviesapp.R;
import cf.do7aelnakeeb.moviesapp.activity.MovieDetails;
import cf.do7aelnakeeb.moviesapp.app.AppConst;
import cf.do7aelnakeeb.moviesapp.app.Movie;
import cf.do7aelnakeeb.moviesapp.activity.MoviesGrid;

/**
 * Created by NakeebMac on 10/21/16.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Movie> arrayList;
    private MoviesGrid.OnMovieSelected onMovieSelected;

    public MoviesAdapter(Context context, ArrayList<Movie> arrayList, MoviesGrid.OnMovieSelected onMovieSelected) {
        this.context = context;
        this.arrayList = arrayList;
        this.onMovieSelected = onMovieSelected;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());

        ViewGroup viewGroup = (ViewGroup) mInflater.inflate(R.layout.movie_item, parent, false);

        return new ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Picasso.with(context).load(AppConst.MoviesDBImageURL + arrayList.get(position).getImage()).into(holder.MoviePoster);

        holder.MoviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMovieSelected.onMovieSelected(arrayList.get(holder.getAdapterPosition()));

            }
        });

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
