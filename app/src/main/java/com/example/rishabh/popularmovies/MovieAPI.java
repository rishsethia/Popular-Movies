package com.example.rishabh.popularmovies;


import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


/**
 * Created by Rishabh on 4/26/16.
 */
public interface MovieAPI {

    @GET("/3/movie/{sort_by}")
    Call<MovieList> loadMovies(
            @Path("sort_by") String sortOrder,
            @Query("api_key") String apiKey
    );

    @GET("/3/movie/{id}/{type}")
    Call<TrailerModelClass> loadTrailers(
            @Path("type") String type,
            @Path("id") String id,
            @Query("api_key") String apiKey
    );

    @GET("/3/movie/{id}/{type}")
    Call<ReviewModelClass> loadReviews(
            @Path("type") String type,
            @Path("id") String id,
            @Query("api_key") String apiKey
    );


}
