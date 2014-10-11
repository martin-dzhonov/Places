package com.gerfield.places;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;

public class AddPlaceActivity extends Activity {
	Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_place);
		NumberPicker np = (NumberPicker) findViewById(R.id.NP_add_place);
		np.setMaxValue(1000);
		np.setMinValue(1);
		np.setValue(30);
		final Button placeButton = (Button) findViewById(R.id.Btn_save_place);
		placeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, HomeActivity.class);
				startActivity(intent);
			}
		});
	}
}
