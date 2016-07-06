package com.example.rishabh.popularmovies;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

/**
 * Created by Rishabh on 6/21/16.
 */
public class ReviewsAdapter extends ArrayAdapter<ReviewModelClass.Reviews> {


    Context myContext;
    ArrayList<ReviewModelClass.Reviews> myReviews;
    private final SparseBooleanArray mCollapsedStatus;


    public ReviewsAdapter(Context context, ArrayList<ReviewModelClass.Reviews> reviews) {
        super(context, 0, reviews);
        myContext = context;
        myReviews = reviews;
        mCollapsedStatus = new SparseBooleanArray();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) myContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.review_list_item, parent, false);
        } else {
            rowView = convertView;
        }

        TextView nameTextView = (TextView) rowView.findViewById(R.id.nameTextView);
        nameTextView.setText(myReviews.get(position).author + " says:");
        ExpandableTextView expandableTextView = (ExpandableTextView) rowView.findViewById(R.id.reviewTextView);

        // Different style for
        expandableTextView.setText(myReviews.get(position).content, mCollapsedStatus, position);


        return rowView;
    }
}
