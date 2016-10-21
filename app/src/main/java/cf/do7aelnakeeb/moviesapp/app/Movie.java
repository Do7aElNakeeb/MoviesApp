package cf.do7aelnakeeb.moviesapp.app;

import java.io.Serializable;

/**
 * Created by NakeebMac on 10/21/16.
 */

public class Movie implements Serializable{

    private String name;
    private String description;
    private String rating;
    private String image;
    private String releaseDate;



    // Movie Object Constructors

    public Movie(String name, String description, String rating, String image, String releaseDate){
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.image = image;
        this.releaseDate = releaseDate;
    }

    // Setters and Getters

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
