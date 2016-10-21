package cf.do7aelnakeeb.moviesapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cf.do7aelnakeeb.moviesapp.R;

/**
 * Created by NakeebMac on 10/21/16.
 */

public class MovieDetails extends Fragment {

    TextView Title;
    TextView ReleaseDate;
    TextView Rating;
    TextView Description;

    ImageView Image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.movie_details, container, false);

        Title = (TextView) view.findViewById(R.id.MovieTitle);
        ReleaseDate = (TextView) view.findViewById(R.id.ReleaseDate);
        Rating = (TextView) view.findViewById(R.id.MovieRating);
        Description = (TextView) view.findViewById(R.id.MovieDescription);

        Image = (ImageView) view.findViewById(R.id.MovieImage);

        return view;
    }
}
