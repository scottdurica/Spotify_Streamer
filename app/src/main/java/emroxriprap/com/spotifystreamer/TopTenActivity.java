package emroxriprap.com.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;


public class TopTenActivity extends ActionBarActivity {

    private String spotId=null;
    private MyCustomAdapter adapter;
    ArrayList<TopTenTrack> dataList = new ArrayList<TopTenTrack>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);
        Intent intent = getIntent();
        String artistName = intent.getStringExtra("artist");
        spotId = intent.getStringExtra("id");
//        Log.d("SPOTID value is: ",spotId);
        ActionBar bar = getSupportActionBar();
        bar.setTitle("Top Ten Tracks");
        bar.setSubtitle(artistName);
        FetchTopTenTask task = new FetchTopTenTask();
        task.execute(spotId);
//        ArrayList<TopTenTrack> dataList = new ArrayList<TopTenTrack>();
//        adapter = new MyCustomAdapter(this,dataList);
//        ListView listView = (ListView)findViewById(R.id.lv_top_tracks);
//        listView.setEmptyView(findViewById(R.id.empty_list_view));
//        listView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter = new MyCustomAdapter(this,dataList);
        ListView listView = (ListView)findViewById(R.id.lv_top_tracks);
        listView.setEmptyView(findViewById(R.id.empty_list_view));
        listView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("key", dataList);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        dataList = savedInstanceState.getParcelableArrayList("key");


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_ten, menu);
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
    public class FetchTopTenTask extends AsyncTask<String,Void,List<TopTenTrack>>{
        private final String LOG_TAG = FetchTopTenTask.class.getSimpleName();

        @Override
        protected void onPostExecute(List<TopTenTrack> topTenTracks) {
            //update the adapter
            if (topTenTracks != null){
                adapter.clear();
                for (TopTenTrack track: topTenTracks){
                    adapter.add(track);
                }
            }
        }

        @Override
        protected List<TopTenTrack> doInBackground(String... params) {
            Tracks tracks = null;
            List<TopTenTrack>trackList = new ArrayList<TopTenTrack>();
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            Map<String,Object> map = new HashMap<>();
            map.put("country",getApplicationContext().getResources().getConfiguration().locale.getCountry());
            try {
                tracks = spotify.getArtistTopTrack(spotId,map);
            }catch (RetrofitError e){
                Log.e(LOG_TAG,"Error: " + e.getMessage());
            }
            List<Track> list  = tracks.tracks;
            if (list.size() == 0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplication(), "No top tracks found for selected artist.", Toast.LENGTH_LONG).show();
                    }
                });
                return null;
            }

            for (Track t: list){
                String smImgUrl = null;
                String lgImgUrl = null;
                String trackName = t.name;
                AlbumSimple albumSimple = t.album;
                String albumName = albumSimple.name;
                List<Image>albumImages = albumSimple.images;
//                Log.d(LOG_TAG,"Track Name: "+ trackName);
//                Log.d(LOG_TAG,"Album Name: "+ albumName);
                if (albumImages !=null){
                    for (Image i: albumImages){
                        int smallestOver600 = 0;
                        int smallestOver100 = 0;

                        if (i.height>600){
                            lgImgUrl = i.url;
                        }
                        if (i.height>100){
                            smImgUrl = i.url;
                        }
//                        String imgUrl = i.url.toString();
                    }
                }
//                Log.d(LOG_TAG,"Large Image URL: "+ lgImgUrl);
//                Log.d(LOG_TAG,"Thumbnail Image URL: "+ smImgUrl);
                TopTenTrack track = new TopTenTrack(albumName,trackName,smImgUrl,lgImgUrl);
                trackList.add(track);

            }
            return trackList;
        }
    }
    public class MyCustomAdapter extends ArrayAdapter<TopTenTrack> {
        Context context;
        private List<TopTenTrack> list;
        public MyCustomAdapter(Context context, List<TopTenTrack> list){
            super(context,R.layout.top_ten_list_item,list);
            this.context = context;
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.top_ten_list_item,parent,false);
            TextView songName = (TextView)rowView.findViewById(R.id.tv_ttl_song_name);
            TextView albumName = (TextView)rowView.findViewById(R.id.tv_ttl_album_name);
            ImageView imageView = (ImageView)rowView.findViewById(R.id.iv_ttl_thumb_img);
            final TopTenTrack track = list.get(position);
            songName.setText(track.getSongName());
            albumName.setText(track.getAlbumName());


            if (track.getSmImgUrl()!=null){
                Picasso.with(context).load(track.getSmImgUrl()).into(imageView);
            }

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //start new activity to play selected track...
                }
            });
            return rowView;
        }
    }
}
