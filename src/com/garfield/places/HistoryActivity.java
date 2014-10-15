package com.garfield.places;

import java.util.ArrayList;
import java.util.Iterator;

import com.garfield.places.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class HistoryActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		
		ListView history = (ListView)this.findViewById(R.id.LV_history);
		
		ArrayList<Reservation> reservations = new ArrayList<Reservation>();
		Iterator<Reservation> reservationsFromSql = Reservation.findAll(Reservation.class);
		while (reservationsFromSql.hasNext()) {
			reservations.add(reservationsFromSql.next());
		}
		
		ReservationsAdapter adapter = new ReservationsAdapter(this, reservations);
		history.setAdapter(adapter);
	}
}
