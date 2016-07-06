package com.example.rishabh.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements MovieGridFragment.Callback {

    private static final String DETAILFRAGMENTTAG = "DFTAG";

    boolean mTwoPane;
    public static String key = "movieValues";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Before onCreate is completed , any instance on getActivity() is intolerable make sure that during
        // its creation getACtivity() is never called


        // Checking for the two pane UI
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                fragmentTransaction.replace(R.id.movie_detail_container, movieDetailFragment, DETAILFRAGMENTTAG);
                fragmentTransaction.commit();


            }
        }else {
            mTwoPane = false;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Movie movie) {

        // when an item is selected, we want to perform the requisite operation
        if (mTwoPane) {

            // Pass on the info to the detailedfragment , since it is a two pane layout
            // Communication with the detail fragment using bundle

            Bundle args = new Bundle();
            args.putParcelable(key, movie);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENTTAG)
                    .commit();

        } else {
            // Make an intent for one pane layout
            // Start the activity using that intent normally
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(key, movie);
            startActivity(intent);
        }
    }

    @Override
    public boolean getMTwoPane() {
        return this.mTwoPane;
    }
}
