package com.garfield.places.places;

import java.util.ArrayList;

import com.garfield.places.R;

import android.app.Service;
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
	private Context context;

	public PlacesAdapter(Context context, ArrayList<Place> places) {
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
//		LayoutInflater inflater = (LayoutInflater) context
//			.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
//		View view = inflater.inflate(R.layout.list_single_item, parent, false);
//		
//		TextView nameTextView = (TextView) view.findViewById(R.id.TV_home_item_name);			
//		TextView idTextView = (TextView) view.findViewById(R.id.TV_home_item_id);		
//		ImageView imageView = (ImageView) view.findViewById(R.id.IV_home_item);
//		
//		Place place = places.get(position);
//		
//		nameTextView.setText(place.getName());
//		
//		byte[] imageAsBytes = Base64.decode(place
//				.getImageBase64().getBytes(), Base64.DEFAULT);
//
//		imageView.setImageBitmap(BitmapFactory.decodeByteArray(
//				imageAsBytes, 0, imageAsBytes.length));
//		
//		idTextView.setText(place.getId());
//		
//		return view;
		
		ViewHolder holder;
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_single_item, parent,
					false);

			holder = new ViewHolder();
			
			holder.nameTextView = (TextView) convertView.findViewById(R.id.TV_home_item_name);			
			holder.idTextView = (TextView) convertView.findViewById(R.id.TV_home_item_id);		
			holder.imageView = (ImageView) convertView.findViewById(R.id.IV_home_item);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Place place = places.get(position);

		if (place != null) {
			holder.nameTextView.setText(place.getName());
			
			byte[] imageAsBytes = Base64.decode(place
					.getImageBase64().getBytes(), Base64.DEFAULT);

			holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(
					imageAsBytes, 0, imageAsBytes.length));
			
			holder.idTextView.setText(place.getId());
		}

		return convertView;
	}
	
	private class ViewHolder {
		public TextView nameTextView;
		public TextView idTextView;
		public ImageView imageView;
	}
}
