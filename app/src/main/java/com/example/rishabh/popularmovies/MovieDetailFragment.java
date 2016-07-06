package com.example.rishabh.popularmovies;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rishabh.popularmovies.data.DetailColumns;
import com.example.rishabh.popularmovies.data.FavouriteProvider;
import com.example.rishabh.popularmovies.data.MovieColumns;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {


    //Declarations
    ArrayList<TrailerModelClass.Trailer> myTrailerList = new ArrayList<>();
    ReviewsAdapter myReviewsAdapter;
    ArrayList<ReviewModelClass.Reviews> myReviewList = new ArrayList<>();
    Movie myMovie;
    ArrayList<Movie> myMovieList = new ArrayList<>();
    Activity mActivity;
    private android.support.v7.widget.ShareActionProvider mShareActionProvider;

    // Setting up the shareactionprovider
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail_fragment, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);
        // Share intent should be set as soon as the layout is inflated , otherwise it won't work
        if (mShareActionProvider != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            if (myTrailerList.size() >= 1) {
                shareIntent.putExtra(Intent.EXTRA_TEXT, getUrlForKey(myTrailerList.get(0).key));

            } else {
                shareIntent.putExtra(Intent.EXTRA_TEXT, "There were no trailers to share !");

            }
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }


    public MovieDetailFragment() {
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        myMovieList.add(myMovie);
        outState.putParcelableArrayList("movies", myMovieList);
        outState.putParcelableArrayList("trailers", myTrailerList);
        outState.putParcelableArrayList("reviews", myReviewList);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

            myMovieList = savedInstanceState.getParcelableArrayList("movies");
            myMovie = myMovieList.get(0);
            myTrailerList = savedInstanceState.getParcelableArrayList("trailers");
            myReviewList = savedInstanceState.getParcelableArrayList("reviews");
            setHasOptionsMenu(true);
        }


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);


        // Getting the shared preferences and deciding on basis of that
        SharedPreferences sortPreference = PreferenceManager.getDefaultSharedPreferences(mActivity);
        String sortByIntValue = sortPreference.getString(getString(R.string.sortByKey), getString(R.string.sortByDefaultValue));
        int sortByValue = Integer.parseInt(sortByIntValue);

        // Getting the movie values from the intent
        Bundle arguments = getArguments();

        // this is how the comm between an activity and a fragment should be
        //using arguments
        if (arguments != null && savedInstanceState == null) {
            myMovie = arguments.getParcelable(MainActivity.key);

        }

        // Retrieving the data if favourites
        if (sortByValue == 3 && myMovie != null) {
            Cursor c = mActivity.getContentResolver().query(FavouriteProvider.Detail.withId(myMovie.id), null
                    , null, null, null);

            c.moveToFirst();
            // Checking if the cursor is after the last position
            int key = c.getColumnIndex(DetailColumns.KEY);
            int name = c.getColumnIndex(DetailColumns.NAME);
            int author = c.getColumnIndex(DetailColumns.AUTHOR);
            int content = c.getColumnIndex(DetailColumns.CONTENT);
            while (!c.isAfterLast()) {

                ReviewModelClass.Reviews review = new ReviewModelClass.Reviews();
                TrailerModelClass.Trailer trailer = new TrailerModelClass.Trailer(c.getString(key), c.getString(name));
                review.author = c.getString(author);
                review.content = c.getString(content);
                // Due to bad design of database
                if (!review.author.equals("") && !review.content.equals("")) {
                    myReviewList.add(review);
                }
                if (!trailer.key.equals("") && !trailer.name.equals("")) {
                    myTrailerList.add(trailer);
                }

                c.moveToNext();

            }
            setHasOptionsMenu(true);
        }


        if (myMovie != null) {

            // Detail Layout Stuff
            TextView userRating = (TextView) view.findViewById(R.id.userRatingTextView);
            Float userRatingFloat = myMovie.vote_average;
            userRating.setText("Ratings : " + userRatingFloat.toString());
            TextView plotSynopsis = (TextView) view.findViewById(R.id.plotSynopsistextView);
            plotSynopsis.setText(myMovie.overview);
            TextView releaseDate = (TextView) view.findViewById(R.id.releaseDatetextView);
            releaseDate.setText("Release Date : " + myMovie.release_date);
            final ImageView imageView = (ImageView) view.findViewById(R.id.moviePosterThumbNail);
            if (sortByValue == 3) {
                // Load for the favourites here
                String filename = myMovie.poster_path.substring(1);
                loadImageFromStorage(filename, imageView);

            } else {
                try {
                    Picasso.with(getActivity()).load(ImageAdapter.buildUrlForPoster(myMovie.poster_path)).into(imageView);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            TextView headingText = (TextView) view.findViewById(R.id.HeadingView);
            headingText.setText(myMovie.original_title);


            // fav view is dynamic too
            // Check whether the favourites table contain an entry or not
            String args[] = {myMovie.id};

            Cursor c = mActivity.getContentResolver().query(FavouriteProvider.MovieLists.URI_MOVIE_LISTS,
                    null, "id = ?", args, null
            );
            final ImageView favView = (ImageView) view.findViewById(R.id.favButtonImage);
            if (!c.isAfterLast()) {
                favView.setImageResource(R.drawable.fav);
            }

            favView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // this is the method where all data is inserted and deleted and image manipulation is done handle with care

                    Drawable myDrawable = favView.getDrawable();
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    if (myDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.def).getConstantState())) {
                        // default currently to be added to favourites(db)
                        //image changed
                        favView.setImageResource(R.drawable.fav);
                        //add the data to the database
                        //add to movie columns
                        ContentValues movieValues = new ContentValues();
                        movieValues.put(MovieColumns.DATE, myMovie.release_date);
                        movieValues.put(MovieColumns.ID, myMovie.id);
                        movieValues.put(MovieColumns.OVERVIEW, myMovie.overview);
                        movieValues.put(MovieColumns.TITLE, myMovie.original_title);
                        movieValues.put(MovieColumns.VOTE, myMovie.vote_average);
                        movieValues.put(MovieColumns.PATH, myMovie.poster_path);

                        mActivity.getContentResolver().insert(FavouriteProvider.MovieLists.URI_MOVIE_LISTS, movieValues);
                        //add to detail columns
                        //very poor database design handle with care
                        // too much duplication
                        int i = 0;
                        while (myReviewList.size() > i && myTrailerList.size() >= i) {
                            ContentValues detailValues = new ContentValues();
                            ReviewModelClass.Reviews myReview = myReviewList.get(i);
                            TrailerModelClass.Trailer myTrailer = myTrailerList.get(i);
                            detailValues.put(DetailColumns.KEY, myTrailer.key);
                            detailValues.put(DetailColumns.NAME, myTrailer.name);
                            detailValues.put(DetailColumns.AUTHOR, myReview.author);
                            detailValues.put(DetailColumns.CONTENT, myReview.content);
                            detailValues.put(DetailColumns.ID, myMovie.id);
                            mActivity.getContentResolver().insert(FavouriteProvider.Detail.withId(myMovie.id), detailValues);
                            i++;
                        }

                        while (myReviewList.size() > i && myReviewList.size() != 0) {
                            ContentValues detailValues = new ContentValues();
                            ReviewModelClass.Reviews myReview = myReviewList.get(i);
                            detailValues.put(DetailColumns.KEY, "");
                            detailValues.put(DetailColumns.NAME, "");
                            detailValues.put(DetailColumns.AUTHOR, myReview.author);
                            detailValues.put(DetailColumns.CONTENT, myReview.content);
                            detailValues.put(DetailColumns.ID, myMovie.id);
                            mActivity.getContentResolver().insert(FavouriteProvider.Detail.withId(myMovie.id), detailValues);
                            i++;
                        }

                        while (myTrailerList.size() > i && myTrailerList.size() != 0) {
                            ContentValues detailValues = new ContentValues();
                            TrailerModelClass.Trailer myTrailer = myTrailerList.get(i);
                            detailValues.put(DetailColumns.KEY, myTrailer.key);
                            detailValues.put(DetailColumns.NAME, myTrailer.name);
                            detailValues.put(DetailColumns.AUTHOR, "");
                            detailValues.put(DetailColumns.CONTENT, "");
                            detailValues.put(DetailColumns.ID, myMovie.id);
                            mActivity.getContentResolver().insert(FavouriteProvider.Detail.withId(myMovie.id), detailValues);
                            i++;

                        }


                        // add the image to phone storage

                        String filename = myMovie.poster_path.substring(1);
                        try {
                            saveToInternalStorage(bitmap, filename);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        // display the toast

                        Toast toast = Toast.makeText(mActivity, "Added to Favourites !", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        //Remove from favourite(db)
                        //change image
                        favView.setImageResource(R.drawable.def);
                        // remove the data from the database
                        //delete from movies table
                        mActivity.getContentResolver().delete(FavouriteProvider.MovieLists.URI_MOVIE_LISTS, "ID =" + myMovie.id, null);
                        //delete from details table
                        mActivity.getContentResolver().delete(FavouriteProvider.Detail.withId(myMovie.id), null, null);

                        // remove the image from the phone storage
                        String filename = myMovie.poster_path.substring(1);
                        File file = new File(mActivity.getCacheDir(), filename);
                        // deletes file or directory denoted by abstract pathname
                        file.delete();

                        //display the toast
                        Toast toast = Toast.makeText(mActivity, "Removed from Favourites !", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });


            // Trailers Section
            final ArrayAdapter<String> myTrailersAdapter = new ArrayAdapter<String>(getActivity(), R.layout.trailer_list_item, R.id.trailerTextView);
            ExpandableHeightListView trailerListView = (ExpandableHeightListView) view.findViewById(R.id.trailerListView);
            trailerListView.setAdapter(myTrailersAdapter);
            // this does the magic
            trailerListView.setExpanded(true);


            // On Item Click
            trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    if (myTrailerList != null) {
                        String path = myTrailerList.get(position).getKey();
                        String youtubeUrl = getUrlForKey(path);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl)));
                    }
                }
            });


            String id = myMovie.id;

            // Retrofit Initialisation
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.themoviedb.org")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            // Prepare call
            MovieAPI movieAPI = retrofit.create(MovieAPI.class);
            retrofit.Call<TrailerModelClass> call = movieAPI.loadTrailers("videos", id, MovieGridFragment.appKey);

            //Condition before call
            if (savedInstanceState == null && (sortByValue == 1 || sortByValue == 2)) {
                call.enqueue(new Callback<TrailerModelClass>() {
                    @Override
                    public void onResponse(Response<TrailerModelClass> response, Retrofit retrofit) {

                        TrailerModelClass trailerModelClass = response.body();
                        // check for it
                        myTrailerList = (ArrayList) trailerModelClass.results;
                        setHasOptionsMenu(true);
                        if (myTrailerList != null) {
                            myTrailersAdapter.clear();
                            for (TrailerModelClass.Trailer trailer : myTrailerList) {
                                // Displaying trailers by trailer name
                                myTrailersAdapter.add(trailer.name);
                            }

                        }


                    }

                    @Override
                    public void onFailure(Throwable t) {


                        Log.e("Err fetch trailers ", t.getMessage());
                    }
                });

            }


            // Reviews Section
            myReviewsAdapter = new ReviewsAdapter(getActivity(), new ArrayList<ReviewModelClass.Reviews>());
            ExpandableHeightListView reviewListView = (ExpandableHeightListView) view.findViewById(R.id.reviewListView);
            reviewListView.setAdapter(myReviewsAdapter);
            // this does the magic
            reviewListView.setExpanded(true);
            retrofit.Call<ReviewModelClass> myCall = movieAPI.loadReviews("reviews", id, MovieGridFragment.appKey);


            // Condition before call
            if (savedInstanceState == null && (sortByValue == 1 || sortByValue == 2)) {
                myCall.enqueue(new Callback<ReviewModelClass>() {
                    @Override
                    public void onResponse(Response<ReviewModelClass> response, Retrofit retrofit) {
                        ReviewModelClass reviewModelClass = response.body();
                        myReviewList = reviewModelClass.results;
                        if (myReviewList != null) {

                            myReviewsAdapter.clear();

                            for (ReviewModelClass.Reviews reviews : myReviewList) {

                                myReviewsAdapter.add(reviews);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e("Err fetch reviews", t.getMessage());

                    }
                });
            }


            // Putting to the adapters
            if (savedInstanceState != null || sortByValue == 3) {
                if (myTrailerList != null) {
                    for (TrailerModelClass.Trailer trailer : myTrailerList) {
                        myTrailersAdapter.add(trailer.name);
                    }
                }

                if (myReviewList != null) {
                    for (ReviewModelClass.Reviews reviews : myReviewList) {
                        myReviewsAdapter.add(reviews);
                    }
                }

            }

        }


        return view;
    }

    public void saveToInternalStorage(Bitmap bitmapImage, String filename) throws IOException {
        File file = new File(mActivity.getCacheDir(), filename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file.getPath());
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }

    }

    public void loadImageFromStorage(String filename, ImageView imageView) {

        try {
            File f = new File(mActivity.getCacheDir(), filename);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView.setImageBitmap(b);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getUrlForKey(String path) {


        return "https://www.youtube.com/watch?v=" + path;
    }


}
