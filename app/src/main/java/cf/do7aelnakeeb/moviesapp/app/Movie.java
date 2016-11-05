package cf.do7aelnakeeb.moviesapp.app;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by NakeebMac on 10/21/16.
 */

public class Movie implements Parcelable{

    private String id;
    private String name;
    private String description;
    private String rating;
    private String image;
    private String releaseDate;
    public ArrayList<Review> reviewsArrayList;
    public ArrayList<Trailer> trailersArrayList;

    public static class Review {
        private String id;
        private String author;
        private String content;
        private String url;

        public Review (String id, String author, String content, String url){
            this.id = id;
            this.author = author;
            this.content = content;
            this.url = url;
        }

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }



        public String getUrl() {
            return url;
        }

    }

    public static class Trailer {
        private String id;
        private String key;
        private String name;
        private String type;
        private String site;

        public Trailer (String id, String key, String name, String type, String site){
            this.id = id;
            this.key = key;
            this.name = name;
            this.type = type;
            this.site = site;
        }



        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public String getSite() {
            return site;
        }

        public String getType() {
            return type;
        }
    }

    // Movie Object Constructors

    public Movie(String id, String name, String description, String rating, String image, String releaseDate){
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.image = image;
        this.releaseDate = releaseDate;
    }

    // Setters and Getters

    protected Movie(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        rating = in.readString();
        image = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getRating(){
        return this.rating;
    }

    public void setRating(String rating){
        this.rating = rating;
    }

    public String getImage(){
        return this.image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getReleaseDate(){
        return this.releaseDate;
    }

    public void setReleaseDate(String releaseDate){
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(rating);
        dest.writeString(image);
        dest.writeString(releaseDate);
    }
}
