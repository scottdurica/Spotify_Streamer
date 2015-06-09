package emroxriprap.com.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity {
    //this is a test to see how branches work.
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    MyCustomAdapter adapter;
    SearchView searchView;
    ArrayList<ArtistEntry> artistEntries = new ArrayList<ArtistEntry>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = (SearchView)findViewById(R.id.sv_artist_search);
        searchView.setIconifiedByDefault(false);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pollSpotifyForArtists(query);
//                Log.d(LOG_TAG, "Artist name sent to request is " + searchView.getQuery().toString());
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
    protected void onResume() {
        super.onResume();
        adapter = new MyCustomAdapter(this,artistEntries);
        ListView listView = (ListView)findViewById(R.id.lv_search_results);
        listView.setEmptyView(findViewById(R.id.empty_list_view));
        listView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("key", artistEntries);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        artistEntries = savedInstanceState.getParcelableArrayList("key");


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


    private void pollSpotifyForArtists(String artistName) {
        //                FetchArtistTask task = new FetchArtistTask();
//                task.execute(searchView.getQuery().toString());
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        Map map = new HashMap();
        map.put("type","artist");
        spotify.searchArtists(artistName,map,new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager p, Response response) {
                final List<ArtistEntry> artistsList = new ArrayList<ArtistEntry>();
                Pager<Artist> list = p.artists;
                List<Artist> artists = list.items;
                for (Artist a: artists) {
                    String thumbImageUrl = null;
//                          Log.d(LOG_TAG,"Artist ID is "+ a.id);
                    List<Image> images = a.images;
                    if (images != null) {
                        for (Image i : images) {
                            if (i.height > 100) {
                                thumbImageUrl = i.url;
                            }
                        }
                    }
//                Log.d(LOG_TAG,"Thumbnail Image URL is "+thumbImageUrl);
                    ArtistEntry artistEntry = new ArtistEntry(a.id, a.name, thumbImageUrl);
                    artistsList.add(artistEntry);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (artistsList == null || artistsList.size()==0){
                            //No results found.  Tell user to change search

                            Toast.makeText(getApplication(), "No matching artists found. Please refine your search.", Toast.LENGTH_LONG).show();


                        }else{

                            adapter.clear();
                            for (ArtistEntry ae: artistsList){
                                adapter.add(ae);
                            }



                        }
                    }
                });



            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

//    public class FetchArtistTask extends AsyncTask<String,Void,List<ArtistEntry>>{
//
//        private final String LOG_TAG = FetchArtistTask.class.getSimpleName();
//
//        @Override
//        protected void onPostExecute(List<ArtistEntry> artistEntries) {
//            //update the adapter
//            if (artistEntries == null || artistEntries.size()==0){
//                //No results found.  Tell user to change search
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplication(), "No matching artists found. Please refine your search.", Toast.LENGTH_LONG).show();
//                    }
//                });
//
//            }else{
//                adapter.clear();
//                for (ArtistEntry ae: artistEntries){
//                    adapter.add(ae);
//                }
//            }
//
////            Log.e(LOG_TAG,"List size is: " + artistEntries.size());
//        }
//
//        private List<ArtistEntry> getArtistDataFromJson(String artistJsonString)throws
//                    JSONException {
//            final String SPOT_ARTIST_NAME = "name";
//            final String SPOT_ARTISTS = "artists";
//            final String SPOT_ITEMS = "items";
//            final String SPOT_IMAGES = "images";
//            final String SPOT_ID = "id";
//            final String SPOT_URL = "url";
//            final String SPOT_HEIGHT = "height";
//            final String SPOT_WIDTH = "width";
//            final String SPOT_TOTAL = "total";
//
//            JSONObject artistJson = new JSONObject(artistJsonString);
//            JSONObject artists = artistJson.getJSONObject(SPOT_ARTISTS);
//            JSONArray artistArray = artists.getJSONArray(SPOT_ITEMS);
//            int listCount = artists.getInt(SPOT_TOTAL);
//            artistEntries = new ArrayList<ArtistEntry>();
//
//            for (int i=0; i<artistArray.length();i++){
//                JSONObject artistObject = artistArray.getJSONObject(i);
//
//                String id = artistObject.getString(SPOT_ID);
//                String name = artistObject.getString(SPOT_ARTIST_NAME);
//                String imageUrl = null;
//                JSONArray imageArray = artistObject.getJSONArray(SPOT_IMAGES);
//                if (imageArray != null && imageArray.length()>0){
//                    //get the smallest available image
//
//                    JSONObject thumbObject = imageArray.getJSONObject(imageArray.length()-1);
//                    imageUrl = thumbObject.getString(SPOT_URL);
//                }
//
//                ArtistEntry ae = new ArtistEntry(id,name,imageUrl);
//                artistEntries.add(ae);
////                Log.d(LOG_TAG,"IMAGEURL val is "+ ae.getImageUrlString());
////                Log.e(LOG_TAG,"Artist ID is "+ id );
////                Log.e(LOG_TAG,"Artist NAME is "+ name );
////                Log.e(LOG_TAG,"Artist IMAGEURL is "+ imageUrl );
//
//            }
//            return artistEntries;
//        }
//        @Override
//        protected List<ArtistEntry> doInBackground(String... params) {
//
//            List<ArtistEntry>artistsList = new ArrayList<ArtistEntry>();
//            SpotifyApi api = new SpotifyApi();
//            SpotifyService spotify = api.getService();
//            Map map = new HashMap();
//            map.put("type","artist");
//            ArtistsPager p = spotify.searchArtists(params[0],map);
//            Pager<Artist>list = p.artists;
//            List<Artist> artists = list.items;
//            for (Artist a: artists){
//                String thumbImageUrl = null;
////                Log.d(LOG_TAG,"Artist ID is "+ a.id);
//                List<Image>images = a.images;
//                if (images !=null){
//                    for (Image i:images){
//                        if (i.height>100){
//                            thumbImageUrl = i.url;
//                        }
//                    }
//                }
////                Log.d(LOG_TAG,"Thumbnail Image URL is "+thumbImageUrl);
//                ArtistEntry artistEntry = new ArtistEntry(a.id,a.name,thumbImageUrl);
//                artistsList.add(artistEntry);
//            }
////            HttpURLConnection urlConnection = null;
////            BufferedReader reader = null;
////
////            //will contain the raw JSON response
////            String artistJsonString = null;
//
////            try{
////                //Construct the URL for the spotify query
////                //possible params are available at
////                //https://developer.spotify.com/web-api/search-item/
////                //url that I THINK returns what I need.
////                //https://api.spotify.com/v1/search?q=joe%20walsh&type=artist
////                final String ARTIST_BASE_URL ="https://api.spotify.com/v1/search?";
////                final String QUERY_PARAM = "q";
////                final String TYPE_PARAM = "type";
////                final String TYPE = "artist";
//////                Log.e(LOG_TAG,"PARAM[0]: "+ params[0]);
////
////                Uri builtUri = Uri.parse(ARTIST_BASE_URL).buildUpon()
////                        .appendQueryParameter(QUERY_PARAM,params[0])
////                        .appendQueryParameter(TYPE_PARAM,TYPE).build();
////                URL url = new URL(builtUri.toString());
////                Log.d(LOG_TAG,"BUILT URL: " + builtUri.toString());
////
////                // Create the request to OpenWeatherMap, and open the connection
////                urlConnection = (HttpURLConnection) url.openConnection();
////                urlConnection.setRequestMethod("GET");
////                urlConnection.connect();
////
////                // Read the input stream into a String
////                InputStream inputStream = urlConnection.getInputStream();
////                StringBuffer buffer = new StringBuffer();
////                if (inputStream == null) {
////                    // Nothing to do.
////                    return null;
////                }
////                reader = new BufferedReader(new InputStreamReader(inputStream));
////
////                String line;
////                while ((line = reader.readLine()) != null) {
////                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
////                    // But it does make debugging a *lot* easier if you print out the completed
////                    // buffer for debugging.
////                    buffer.append(line + "\n");
////                }
////
////                if (buffer.length() == 0) {
////                    // Stream was empty.  No point in parsing.
////                    return null;
////                }
////                artistJsonString = buffer.toString();
////                Log.d(LOG_TAG, "Forecast JSON Sting: " + artistJsonString);
////            }catch (IOException e){
////                Log.e(LOG_TAG, "Error ", e);
////                return null;
////            }finally {
////                if (urlConnection != null) {
////                    urlConnection.disconnect();
////                }
////                if (reader != null) {
////                    try {
////                        reader.close();
////                    } catch (final IOException e) {
////                        Log.e(LOG_TAG, "Error closing stream", e);
////                    }
////                }
////            }
////            try{
////                return getArtistDataFromJson(artistJsonString);
////            }catch (JSONException e){
////                Log.e(LOG_TAG,e.getMessage(),e);
////                e.printStackTrace();
////            }
////            return null;
//            return artistsList;
//        }
//    }
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
            final ArtistEntry ae = list.get(position);
            artistName.setText(ae.getArtistName());
            if (ae.getImageUrlString()!=null){
                Picasso.with(context).load(ae.getImageUrlString()).into(imageView);
            }
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),TopTenActivity.class);
                    intent.putExtra("id",ae.getSpotifyId());
                    intent.putExtra("artist",ae.getArtistName());
                    startActivity(intent);
                }
            });
            return rowView;
        }
    }
}
