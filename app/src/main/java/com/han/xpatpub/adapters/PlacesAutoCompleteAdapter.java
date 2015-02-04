package com.han.xpatpub.adapters;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.han.xpatpub.R;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
	
	private ArrayList<String> resultList;
	private int NUM_RESULT = 3;
	
	private final static String TAG = "PlacesAutoCompleteAdapter";
	
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";     
    private static String api_key;

	public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		api_key = context.getResources().getString(R.string.google_map_api_key_autocomplete);
	}

	@Override
	public int getCount() {
		if (resultList.size() > NUM_RESULT)
			return NUM_RESULT;
		
		else 
			return resultList.size(); 
	}

	@Override
	public String getItem(int index) {
		return resultList.get(index);
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					// Retrieve the autocomplete results.

					resultList = autocomplete(constraint.toString());
					
					// Assign the data to the FilterResults
					filterResults.values = resultList;
					filterResults.count = resultList.size();
				}
				
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
					
				} else {
					notifyDataSetInvalidated();
				}
			}
		};
		
		return filter;
	}

	public ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE
                    + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?sensor=true" + "&key=" + api_key 
                    + "&input=" + URLEncoder.encode(input, "UTF-8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            
//          Log.d(TAG, "jsonResults = " + jsonResults);
            
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error processing Places API URL", e);
            return resultList;
            
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to Places API", e);
            // TODO try to connect with Google Play Services again
            return resultList;
            
        } finally {
            if (conn != null)
                conn.disconnect();
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj1 = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj1.getJSONArray("predictions");
            
            // Extract a name of the street from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
            	JSONObject jsonObj2 = predsJsonArray.getJSONObject(i);
            	resultList.add(jsonObj2.getString("description"));
//            	Log.d(TAG, predsJsonArray.getJSONObject(i).toString());
//            	JSONArray termsJsonArray = jsonObj2.getJSONArray("terms");
//            	for (int j = 0; j < i; j++)	TODO check if there are repeated values
//            	resultList.add(termsJsonArray.getJSONObject(0).getString("value"));
            }
            
        } catch (JSONException e) {
            Log.e(TAG, "Cannot process JSON results", e);
        }

        return resultList;
	}	
	
	/** Creates list for circle scroll. */
	public LinkedList<Integer> makeFifo(int number) {
        LinkedList<Integer> fifo = new LinkedList<Integer>();

        for (int i = 1; i <= number; i++)
            fifo.add(Integer.valueOf(i));
        
        return fifo;
	}
}
