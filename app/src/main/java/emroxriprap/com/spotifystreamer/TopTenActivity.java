package emroxriprap.com.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;


public class TopTenActivity extends ActionBarActivity {

    private String spotId=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);
        Intent intent = getIntent();
        String artistName = intent.getStringExtra("artist");
        spotId = intent.getStringExtra("id");
        ActionBar bar = getSupportActionBar();
        bar.setTitle("Top Ten Tracks");
        bar.setSubtitle(artistName);


FetchTopTenTask task = new FetchTopTenTask();
        task.execute( );

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
    public class FetchTopTenTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            spotify.getRelatedArtists("Joe Walsh");

            ArtistsPager results = spotify.searchArtists("Beyonce");
            String s = results.artists.toString();
            Log.e("VALUE IS::::::::::: ",s);
            return null;
        }
    }
}
