package com.garfield.places.places;

import java.util.ArrayList;

import com.garfield.places.R;
import com.garfield.places.R.id;
import com.garfield.places.R.layout;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    public int getCount() 
    {
       return places.size();
    }
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_single_item, null);
		TextView nameTextView = (TextView) rowView
				.findViewById(R.id.TV_home_item_name);
		nameTextView.setText(places.get(position).getName());
		ImageView imageView = (ImageView) rowView
				.findViewById(R.id.IV_home_item);
		byte[] imageAsBytes = Base64.decode(places.get(position)
				.getImageBase64().getBytes(), Base64.DEFAULT);
		imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0,
				imageAsBytes.length));
		TextView idTextView = (TextView) rowView
				.findViewById(R.id.TV_home_item_id);
		idTextView.setText(places.get(position).getId());
		return rowView;
	}
}
