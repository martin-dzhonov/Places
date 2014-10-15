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

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class HomeActivity extends ListActivity {
	public final static String EXTRA_PLACE_NAME = "com.example.myfirstapp.EXTRA_PLACE_NAME";
	public final static String EXTRA_PLACE_ID = "com.example.myfirstapp.EXTRA_PLACE_ID";
	private Context context = this;
	private ArrayList<Place> places = new ArrayList<Place>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new PopulatePlacesTask().execute();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home, menu);
		
		if (AccountGeneral.ACCOUNT_TYPE_CUSTOMER == AccountGeneral.USER_ACCOUNT_TYPE) {
			menu.findItem(R.id.action_create_place).setVisible(false);
		}
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// action with ID action_refresh was selected
		case R.id.action_create_place:
			Intent addPlaceIntent = new Intent(context, AddPlaceActivity.class);
			startActivity(addPlaceIntent);
			break;
		// action with ID action_settings was selected
		case R.id.action_history:
			Intent historyIntent = new Intent(context, HistoryActivity.class);
			startActivity(historyIntent);
			break;
		case R.id.action_refresh:
			new PopulatePlacesTask().execute();
			break;
		default:
			break;
		}

		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(context, DetailsActivity.class);
		String message = places.get(position).getId();
		intent.putExtra(EXTRA_PLACE_ID, message);
		startActivity(intent);
	}

	private class PopulatePlacesTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progressDialog;

		protected void onPreExecute() {
			places = new ArrayList<Place>();
			progressDialog = ProgressDialog.show(HomeActivity.this, "",
					"Fetching data. Please wait...", true);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
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
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			setListAdapter(new PlacesAdapter(HomeActivity.this, places));
		}
	}
}
