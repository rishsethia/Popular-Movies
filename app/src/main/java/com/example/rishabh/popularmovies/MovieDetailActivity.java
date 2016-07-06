package com.example.rishabh.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Before onCreate is completed , any instance on getActivity() is intolerable make sure that during
        // its creation getACtivity() is never called

        // Create a detail fragment and add it to the activity
        // using fragment transaction
        // Making the method consistent
        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            String key = MainActivity.key;
            arguments.putParcelable(key, getIntent().getParcelableExtra(key));

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    // Same to same copied from main activity
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

}
