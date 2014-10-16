package com.garfield.places.reservations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.garfield.places.R;
import com.garfield.places.R.id;
import com.garfield.places.R.layout;

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

public class ReservationsAdapter extends ArrayAdapter<Reservation> {
	private ArrayList<Reservation> reservations;
	private Context context;

	public ReservationsAdapter(Context context,
			ArrayList<Reservation> reservations) {
		super(context, R.layout.reservation_layout, reservations);
		this.context = context;
		this.reservations = reservations;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.reservation_layout, parent,
					false);

			holder = new ViewHolder();
			holder.placeNameView = (TextView) convertView
					.findViewById(R.id.placeName);
			
			holder.dateView = (TextView) convertView.findViewById(R.id.date);
			
			holder.peopleNumberView = (TextView) convertView
					.findViewById(R.id.peopleNumber);
			
			holder.reservationNameView = (TextView) convertView
					.findViewById(R.id.reservationName);
			
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.restourantImage);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Reservation reservation = this.reservations.get(position);

		if (reservation != null) {
			holder.reservationNameView.setText(reservation.getName());
			holder.placeNameView.setText(reservation.getPlaceName());

			Date date = reservation.getDate();
			holder.dateView.setText(getDateAsString(date));

			int peopleNumber = reservation.getNumberOfPeople();
			holder.peopleNumberView.setText(String.valueOf(peopleNumber));

			byte[] imageAsBytes = Base64.decode(reservation.getImage()
					.getBytes(), Base64.DEFAULT);
			holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(
					imageAsBytes, 0, imageAsBytes.length));
		}

		return convertView;
	}

	private String getDateAsString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm dd.MM", Locale.US);
		return format.format(date);
	}

	private class ViewHolder {
		public TextView placeNameView;
		public TextView dateView;
		public TextView peopleNumberView;
		public TextView reservationNameView;
		public ImageView imageView;
	}
}
