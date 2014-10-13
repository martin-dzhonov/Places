package com.garfield.places;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.garfield.places.R;

import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends Activity {

	Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);

		Intent intent = getIntent();

		new LoadPlaceDetails().execute(intent
				.getStringExtra(HomeActivity.EXTRA_PLACE_ID));

		final Button button = (Button) findViewById(R.id.Btn_make_reservation);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, ReservationActivity.class);
				startActivity(intent);
			}
		});
	}

	private class LoadPlaceDetails extends AsyncTask<String, Void, JSONObject> {
		private ProgressDialog progressDialog;
		private TextView nameTextView;
		private TextView descriptionTextView;
		private TextView capacityTextView;
		private ImageView imageView;
		private String name;

		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(DetailsActivity.this, "",
					"Fetching data. Please wait...", true);
			nameTextView = (TextView) findViewById(R.id.TV_details_name);
			descriptionTextView = (TextView) findViewById(R.id.TV_details_description);
			capacityTextView = (TextView) findViewById(R.id.TV_details_capacity);
			imageView = (ImageView) findViewById(R.id.IV_details_image);
		}

		@Override
		protected JSONObject doInBackground(String... placeId) {
			JSONObject placeJsonObject = null;
			try {

				HttpClient hc = new DefaultHttpClient();
				String path = "https://api.everlive.com/v1/BPHTkWwyt41jYxjq/Places/6956bbf0-52ce-11e4-ac6c-25b0bb1c1807";
				HttpGet get = new HttpGet(path);

				HttpResponse rp = hc.execute(get);

				if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String result = EntityUtils.toString(rp.getEntity());
					JSONObject root = new JSONObject(result);
					placeJsonObject = root.getJSONObject("Result");				
				}
			} catch (Exception e) {
				Log.e("HomeActivity", "Error loading JSON", e);
			}
			return placeJsonObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			progressDialog.dismiss();
			try {
				String name = result.getString("name");
				String description = result.getString("description");
				String capacity = result.getString("capacity");
				String imageData = result.getString("image");
				JSONObject location = result.getJSONObject("location");
				Toast.makeText(context, location.toString(), Toast.LENGTH_LONG).show();
				nameTextView.setText(name);
				descriptionTextView.setText(description);
				capacityTextView.setText(capacity);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
