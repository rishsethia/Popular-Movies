package com.example.rishabh.popularmovies;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.rishabh.popularmovies.data.FavouriteProvider;
import com.example.rishabh.popularmovies.data.MovieColumns;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class MovieGridFragment extends Fragment implements Callback<MovieList> {

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Movie movie);

        public boolean getMTwoPane();
    }


    // API Key
    public static String appKey = "19629b4db4450b91a5dd7a9a2fbc4ed3";
    // An arraylist of movies which can be passed anytime and an array list of images which may be used for image adapter
    public ArrayList<Movie> myMovies = new ArrayList<>();


    // a custom flag to see if something is selected or not
    boolean flagselected;

    // The default constructor
    public MovieGridFragment() {
        flagselected = false;
    }

    protected FragmentActivity mActivity;

    // Before this getActivity() should never be called
    // This is the handy method to avoid getActivity and should be used
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }

    // Declaring an image adpater initially and not assigning the values or calling the constructor
    ImageAdapter myImageAdapter;

    // The default oncreate method to initialize and set up variables (Un-graphical stuff)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Get all the data back when the thing is rotaetd
        if (savedInstanceState != null) {

            myMovies = savedInstanceState.getParcelableArrayList("movies");

        }
        super.onCreate(savedInstanceState);

    }



    // onCreateView method for graphical initialisations
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        // This should always be done in onCreateView
        //Setting up the adapter

        SharedPreferences sortPreference = PreferenceManager.getDefaultSharedPreferences(mActivity);
        String sortByIntValue = sortPreference.getString(getString(R.string.sortByKey), getString(R.string.sortByDefaultValue));
        int sortByValue = Integer.parseInt(sortByIntValue);


        myImageAdapter = new ImageAdapter(mActivity, 0, new ArrayList<String>());
        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);


        gridview.setAdapter(myImageAdapter);
        // Updation Of the Movie here causes duplication , Either do here or in the onstart method!





        String sortByParameter = "popular";

        if (sortByValue == 1) {

            sortByParameter = "popular";
        } else if (sortByValue == 2) {
            sortByParameter = "top_rated";
        } else if (savedInstanceState == null && sortByValue == 3) {

            // Get the data for the movie from the database


            Cursor c = mActivity.getContentResolver().query(FavouriteProvider.MovieLists.URI_MOVIE_LISTS, null
                    , null, null, null);

            c.moveToFirst();
            // Checking if the cursor is after the last position
            int title = c.getColumnIndex(MovieColumns.TITLE);
            int vote = c.getColumnIndex(MovieColumns.VOTE);
            int overview = c.getColumnIndex(MovieColumns.OVERVIEW);
            int date = c.getColumnIndex(MovieColumns.DATE);
            int id = c.getColumnIndex(MovieColumns.ID);
            int path = c.getColumnIndex(MovieColumns.PATH);
            while (!c.isAfterLast()) {
                Movie movie = new Movie(
                        c.getString(title),
                        c.getString(overview),
                        c.getString(path),
                        Float.parseFloat(c.getString(vote)),
                        c.getString(date),
                        c.getString(id)

                );
                myMovies.add(movie);
                c.moveToNext();

            }

        }


        if (savedInstanceState == null && (sortByValue != 3)) {

            // Retrofit intialisation goes in here :

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Prepare call in Retrofit 2.0

            MovieAPI movieAPI = retrofit.create(MovieAPI.class);
            Call<MovieList> call = movieAPI.loadMovies(sortByParameter, appKey);

            //asynchronous call


            call.enqueue(this);

        } else {
            if (myMovies != null) {
                myImageAdapter.clear();
                for (Movie movie : myMovies) {
                    myImageAdapter.add((movie.getPosterPath()));

                }


            }
        }

        if (savedInstanceState != null){
            flagselected = true;
        }else {
            flagselected = false;
        }

        // This is a listener , defining it anywhere doesn't matter
        // Making our images in gridview clickable
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                flagselected = true;

                // as soon as the item is clicked , we want to callback the function
                Callback callback = (Callback) mActivity;
                ((Callback) mActivity).onItemSelected(myMovies.get(i));

            }
        });

        if (!flagselected) {
            Callback callback = (Callback) mActivity;
            if (callback.getMTwoPane())
                ((Callback) mActivity).onItemSelected(myMovies.get(0));

        }

        // Think about updating
        return rootView;

    }

    @Override
    public void onResponse(Response<MovieList> response, Retrofit retrofit) {

        // Incorrect response is being received
        // Grabbing the response and updating the adapter
        MovieList movieList = response.body();
        myMovies = movieList.results;
        if (myMovies != null) {
            myImageAdapter.clear();
            for (Movie movie : myMovies) {
                myImageAdapter.add((movie.getPosterPath()));

            }

            // TODO this should only be when no movie is selected
            // for the default click so that dummy data is not displayed


        }

        // Currently displaying no internet connection Think This
        else {
            Toast.makeText(mActivity, "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onFailure(Throwable t) {

        Log.e("getMovies threw", t.getMessage());
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", myMovies);
        super.onSaveInstanceState(outState);
    }


}

