package com.garfield.places;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.garfield.places.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReservationActivity extends Activity {
	Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reservation);
		
		Intent intent = getIntent();
		final String placeId = intent.getStringExtra(DetailsActivity.PLACE_ID);
		
		initNumberPicker();
		
		final Button placeButton = (Button) findViewById(R.id.Btn_save_reservation);
		placeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new EverlivePut().execute(placeId);
				Intent intent = new Intent(context, HistoryActivity.class);
				startActivity(intent);
			}
		});
	}
	private class EverlivePut extends AsyncTask<String, Void, Void> {
		private ProgressDialog progressDialog;
		private EditText namEditText;
		private TimePicker timePicker;
		private NumberPicker numberPicker;

		protected void onPreExecute() {
			namEditText = (EditText) findViewById(R.id.ET_reservation_name);
			timePicker = (TimePicker) findViewById(R.id.TP_reservation);
			numberPicker = (NumberPicker) findViewById(R.id.NP_reservation);
			progressDialog = ProgressDialog.show(ReservationActivity.this, "",
					"Saving. Please wait...", true);
		}

		@Override
		protected Void doInBackground(String... placeId) {
			try {
				
				JSONObject obj = new JSONObject();
				//obj.put("Name", namEditText.getText().toString());
				//obj.put("People", numberPicker.getValue());
				//obj.put("PlaceId", placeId[0]);
				//String time = timePicker.getCurrentHour() + ":" +timePicker.getCurrentMinute();
				//obj.put("Time", time);
				
				obj.put("capacity", numberPicker.getValue());
				
				HttpClient httpclient = new DefaultHttpClient();

				String path = "https://api.everlive.com/v1/BPHTkWwyt41jYxjq/Places/" + placeId[0];
				// url with the post data
				HttpPut httpPut = new HttpPut(path);

				// passes the results to a string builder/entity
				StringEntity se = new StringEntity(obj.toString());

				// sets the post request as the resulting string
				httpPut.setEntity(se);
				// sets a request header so the page receving the request
				// will know what to do with it

				// 7. Set some headers to inform server about the type of the
				// content
			

				// 8. Execute POST request to the given URL
				HttpResponse httpResponse = httpclient.execute(httpPut);

			} catch (Exception e) {
				Toast.makeText(context, "Error connecting  to database",
						Toast.LENGTH_LONG).show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			Toast.makeText(context, "Success.", Toast.LENGTH_SHORT).show();
		}
	}
	private void initNumberPicker() {
		NumberPicker np = (NumberPicker) findViewById(R.id.NP_reservation);
		np.setMaxValue(50);
		np.setMinValue(2);
		np.setValue(1);
	}
}
