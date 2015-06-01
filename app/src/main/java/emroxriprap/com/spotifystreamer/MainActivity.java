package emroxriprap.com.spotifystreamer;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;


public class MainActivity extends ActionBarActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    MyCustomAdapter adapter;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<ArtistEntry> dataList = new ArrayList<ArtistEntry>();
        adapter = new MyCustomAdapter(this,dataList);
        ListView listView = (ListView)findViewById(R.id.lv_search_results);
        listView.setAdapter(adapter);

        searchView = (SearchView)findViewById(R.id.sv_artist_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FetchArtistTask task = new FetchArtistTask();
                task.execute(searchView.getQuery().toString());
//                Log.d(LOG_TAG,"Artist name sent to request is "+ searchView.getQuery().toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //this will fire on every change to searchView
                return false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchArtistTask extends AsyncTask<String,Void,List<ArtistEntry>>{

        private final String LOG_TAG = FetchArtistTask.class.getSimpleName();

        @Override
        protected void onPostExecute(List<ArtistEntry> artistEntries) {
            //update the adapter
            adapter.clear();
            for (ArtistEntry ae: artistEntries){
                adapter.add(ae);
            }
            Log.e(LOG_TAG,"List size is: " + artistEntries.size());
        }

        private List<ArtistEntry> getArtistDataFromJson(String artistJsonString)throws
                    JSONException {
            final String SPOT_ARTIST_NAME = "name";
            final String SPOT_ARTISTS = "artists";
            final String SPOT_ITEMS = "items";
            final String SPOT_IMAGES = "images";
            final String SPOT_ID = "id";
            final String SPOT_URL = "url";
            final String SPOT_HEIGHT = "height";
            final String SPOT_WIDTH = "width";
            final String SPOT_TOTAL = "total";

            JSONObject artistJson = new JSONObject(artistJsonString);
            JSONObject artists = artistJson.getJSONObject(SPOT_ARTISTS);
            JSONArray artistArray = artists.getJSONArray(SPOT_ITEMS);
            int listCount = artists.getInt(SPOT_TOTAL);
            List<ArtistEntry> artistEntries = new ArrayList<ArtistEntry>();

            for (int i=0; i<artistArray.length();i++){
                JSONObject artistObject = artistArray.getJSONObject(i);

                String id = artistObject.getString(SPOT_ID);
                String name = artistObject.getString(SPOT_ARTIST_NAME);
                String imageUrl = null;
                JSONArray imageArray = artistObject.getJSONArray(SPOT_IMAGES);
                if (imageArray != null && imageArray.length()>0){
                    //get the smallest available image

                    JSONObject thumbObject = imageArray.getJSONObject(imageArray.length()-1);
                    imageUrl = thumbObject.getString(SPOT_URL);
                }

                ArtistEntry ae = new ArtistEntry(id,name,imageUrl);
                artistEntries.add(ae);
//                Log.d(LOG_TAG,"IMAGEURL val is "+ ae.getImageUrlString());

//                Log.e(LOG_TAG,"Artist ID is "+ id );
//                Log.e(LOG_TAG,"Artist NAME is "+ name );
//                Log.e(LOG_TAG,"Artist IMAGEURL is "+ imageUrl );

            }
            return artistEntries;
        }
        @Override
        protected List<ArtistEntry> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //will contain the raw JSON response
            String artistJsonString = null;

            try{
                //Construct the URL for the spotify query
                //possible params are available at
                //https://developer.spotify.com/web-api/search-item/
                //url that I THINK returns what I need.
                //https://api.spotify.com/v1/search?q=joe%20walsh&type=artist
                final String ARTIST_BASE_URL ="https://api.spotify.com/v1/search?";
                final String QUERY_PARAM = "q";
                final String TYPE_PARAM = "type";
                final String TYPE = "artist";
//                Log.e(LOG_TAG,"PARAM[0]: "+ params[0]);

                Uri builtUri = Uri.parse(ARTIST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM,params[0])
                        .appendQueryParameter(TYPE_PARAM,TYPE).build();
                URL url = new URL(builtUri.toString());
                Log.d(LOG_TAG,"BUILT URL: " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                artistJsonString = buffer.toString();
                Log.d(LOG_TAG, "Forecast JSON Sting: " + artistJsonString);
            }catch (IOException e){
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try{
                return getArtistDataFromJson(artistJsonString);
            }catch (JSONException e){
                Log.e(LOG_TAG,e.getMessage(),e);
                e.printStackTrace();
            }
            return null;
        }
    }
    public class MyCustomAdapter extends ArrayAdapter<ArtistEntry>{
        Context context;
        private List<ArtistEntry> list;
        public MyCustomAdapter(Context context, List<ArtistEntry> list){
            super(context,R.layout.artist_list_item,list);
            this.context = context;
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.artist_list_item,parent,false);
            TextView artistName = (TextView)rowView.findViewById(R.id.tv_artist_name);
            ImageView imageView = (ImageView)rowView.findViewById(R.id.iv_small);
            ArtistEntry ae = list.get(position);
            artistName.setText(ae.getArtistName());
//            String imgUrl = ae.getImageUrlString();
//           Log.e(LOG_TAG,"IMAGEURL val is "+ imgUrl);
            if (ae.getImageUrlString()!=null){
                Picasso.with(context).load(ae.getImageUrlString()).into(imageView);
            }

            return rowView;
        }
    }
}
