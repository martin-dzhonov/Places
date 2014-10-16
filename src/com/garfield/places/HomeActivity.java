package com.garfield.places;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.garfield.places.R;
import com.garfield.places.places.Place;
import com.garfield.places.places.PlaceDetailsActivity;
import com.garfield.places.places.PlacesAdapter;
import com.garfield.places.places.SuggestPlaceActivity;
import com.garfield.places.reservations.ReservationsActivity;
import com.orm.SugarApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HomeActivity extends Activity {
	public final static String EXTRA_PLACE_NAME = "com.example.myfirstapp.EXTRA_PLACE_NAME";
	public final static String EXTRA_PLACE_ID = "com.example.myfirstapp.EXTRA_PLACE_ID";
	
	private Context context = this;
	
	GridView MyGrid;
	ArrayList<Place> places;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		MyGrid = (GridView) findViewById(R.id.gridView1);
		
		new PopulatePlacesTask().execute();
		
		MyGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id)
			{
				Intent intent = new Intent(context, PlaceDetailsActivity.class);
				String placeId = places.get(position).getId();
				intent.putExtra(EXTRA_PLACE_ID, placeId);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// action with ID action_refresh was selected
		case R.id.action_suggest_place:
			Intent suggestPlaceIntent = new Intent(context, SuggestPlaceActivity.class);
			startActivity(suggestPlaceIntent);
			break;
		// action with ID action_settings was selected
		case R.id.action_reservations:
			Intent reservationsIntent = new Intent(context, ReservationsActivity.class);
			startActivity(reservationsIntent);
			break;
		case R.id.action_refresh:
			new PopulatePlacesTask().execute();
			break;
		default:
			break;
		}

		return true;
	}

	private class PopulatePlacesTask extends
			AsyncTask<Void, Void, ArrayList<Place>> {
		private ProgressDialog progressDialog;
		
		protected void onPreExecute() {
			places = new ArrayList<Place>();
			progressDialog = ProgressDialog.show(HomeActivity.this, "",
					"Fetching data. Please wait...", true);
		}

		@Override
		protected ArrayList<Place> doInBackground(Void... arg0) {			
			try {
				HttpClient hc = new DefaultHttpClient();
				HttpGet get = new HttpGet(
						"https://api.everlive.com/v1/BPHTkWwyt41jYxjq/Places");

				HttpResponse rp = hc.execute(get);

				if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String result = EntityUtils.toString(rp.getEntity());
					JSONObject root = new JSONObject(result);
					JSONArray placesJsonArray = root.getJSONArray("Result");
					for (int i = 0; i < placesJsonArray.length(); i++) {
						JSONObject placeJson = placesJsonArray.getJSONObject(i);
						String id = placeJson.getString("Id");
						String name = placeJson.getString("name");
						String imageData = placeJson.getString("image");
						Place place = new Place(name, id, imageData);
						places.add(place);
					}
				}
			} catch (Exception e) {
				Toast.makeText(context, "Error connecting  to database",
						Toast.LENGTH_LONG).show();
			}
			return places;
		}

		@Override
		protected void onPostExecute(ArrayList<Place> result) {
			progressDialog.dismiss();
			MyGrid.setAdapter(new PlacesAdapter(HomeActivity.this.getApplicationContext(), places));
		}
	}
}
