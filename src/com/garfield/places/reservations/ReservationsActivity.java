package com.garfield.places.reservations;

import java.util.ArrayList;
import java.util.Iterator;

import com.garfield.places.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ReservationsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reservations);
		
		loadReservations();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setContentView(R.layout.activity_reservations);
		
		loadReservations();
	}
	
	private void loadReservations() {
		ListView reservationsListView = (ListView) this.findViewById(R.id.LV_reservations);

		ArrayList<Reservation> reservations = new ArrayList<Reservation>();
		Iterator<Reservation> reservationsFromSql = Reservation
				.findAll(Reservation.class);
		while (reservationsFromSql.hasNext()) {
			reservations.add(reservationsFromSql.next());
		}

		ReservationsAdapter adapter = new ReservationsAdapter(this,
				reservations);
		reservationsListView.setAdapter(adapter);
	}
}
