package cf.do7aelnakeeb.moviesapp.app;

import java.util.ArrayList;

/**
 * Created by NakeebMac on 10/21/16.
 */

public class Movie {

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


}
