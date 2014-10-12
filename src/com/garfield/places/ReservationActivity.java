package com.garfield.places;

import com.garfield.places.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;

public class ReservationActivity extends Activity {
	Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reservation);
		NumberPicker np = (NumberPicker) findViewById(R.id.NP_reservation);
		np.setMaxValue(50);
		np.setMinValue(2);
		np.setValue(1);
		final Button placeButton = (Button) findViewById(R.id.Btn_save_reservation);
		placeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, HistoryActivity.class);
				startActivity(intent);
			}
		});
	}
}
