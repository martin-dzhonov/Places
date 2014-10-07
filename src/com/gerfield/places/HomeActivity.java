package com.gerfield.places;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.goebl.david.Webb;
import com.google.gson.Gson;

import android.R.string;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class HomeActivity extends ListActivity {
	public final static String EXTRA_PLACE_NAME = "com.example.myfirstapp.EXTRA_PLACE_NAME";
	private Context context = this;
	private ListView list;
	private ArrayList<Place> places = new ArrayList<Place>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		new MyTask().execute();
		
	}

	private class MyTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progressDialog;

		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(HomeActivity.this, "",
					"Loading. Please wait...", true);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {

				HttpClient hc = new DefaultHttpClient();
				HttpGet get = new HttpGet("https://api.everlive.com/v1/BPHTkWwyt41jYxjq/Places");

				HttpResponse rp = hc.execute(get);

				if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String result = EntityUtils.toString(rp.getEntity());
					JSONObject root = new JSONObject(result);
					JSONArray sessions = root.getJSONArray("Result");
					for (int i = 0; i < sessions.length(); i++) {
						JSONObject session = sessions.getJSONObject(i);
						Place place = new Place();
						place.setName(session.getString("name"));
						places.add(place);
					}
				}
			} catch (Exception e) {
				Log.e("HomeActivity", "Error loading JSON", e);
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
