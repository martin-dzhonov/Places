package com.garfield.places.reservations;

import java.util.Date;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.garfield.places.R;
import com.garfield.places.places.PlaceDetailsActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReservationActivity extends Activity {
	Context context = this;
	private String placeId;
	private Bundle info;
	
	private EditText namEditText;
	private TimePicker timePicker;
	private NumberPicker numberPicker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reservation);
		Intent intent = getIntent();
		info = intent.getExtras();
		placeId = info.getString(PlaceDetailsActivity.PLACE_ID);
		
		initNumberPicker();
		
		final Button placeButton = (Button) findViewById(R.id.Btn_save_reservation);
		placeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				saveReservationToSql();
				
				Intent intent = new Intent(context, ReservationsActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void saveReservationToSql() {
		ProgressDialog progressDialog = ProgressDialog.show(ReservationActivity.this, "",
				"Saving. Please wait...", true);
		
		namEditText = (EditText) findViewById(R.id.ET_reservation_name);
		timePicker = (TimePicker) findViewById(R.id.TP_reservation);
		numberPicker = (NumberPicker) findViewById(R.id.NP_reservation);
		
		String reservationName = namEditText.getText().toString();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, timePicker.getCurrentHour());
		cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
		
		int numberOfPeople = numberPicker.getValue();
		
		String placeName = info.getString(PlaceDetailsActivity.PLACE_NAME_KEY);
		String description = info.getString(PlaceDetailsActivity.DESCRIPTION_KEY);
		String image = info.getString(PlaceDetailsActivity.IMAGE_KEY);
		String placeWebsite = info.getString(PlaceDetailsActivity.PLACE_WEBSITE_KEY);
		String placePhoneNubmer = info.getString(PlaceDetailsActivity.PLACE_PHONE_NUMBER_KEY);
		String placeOpenTime = info.getString(PlaceDetailsActivity.PLACE_OPEN_TIME_KEY);
		
		Reservation reservation = new Reservation(reservationName, cal.getTime(), numberOfPeople, placeId, 
				placeName, description, image, placeWebsite, placePhoneNubmer, placeOpenTime);	
		
		reservation.save();
		
		progressDialog.dismiss();
		Toast.makeText(context, "Reservation saved.", Toast.LENGTH_SHORT).show();
	}
	
	private void initNumberPicker() {
		NumberPicker np = (NumberPicker) findViewById(R.id.NP_reservation);
		np.setMaxValue(50);
		np.setMinValue(2);
		np.setValue(1);
	}
}
