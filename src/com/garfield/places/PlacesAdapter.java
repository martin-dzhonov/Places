package com.garfield.places;

import java.util.ArrayList;

import com.garfield.places.R;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PlacesAdapter extends ArrayAdapter<Place> {
	private ArrayList<Place> places;
	private final Activity context;

	public PlacesAdapter(Activity context, ArrayList<Place> places) {
		super(context, R.layout.list_single_item, places);
		this.places = places;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_single_item, null, true);
		TextView nameTextView = (TextView) rowView.findViewById(R.id.TV_home_item_name);
		nameTextView.setText(places.get(position).getName());
		TextView idTextView = (TextView) rowView.findViewById(R.id.TV_home_item_id);
		idTextView.setText(places.get(position).getId());
		return rowView;
	}
}
