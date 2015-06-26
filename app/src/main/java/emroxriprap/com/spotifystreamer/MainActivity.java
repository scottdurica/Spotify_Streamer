package emroxriprap.com.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import emroxriprap.com.spotifystreamer.fragments.ArtistSearchFragment;
import emroxriprap.com.spotifystreamer.fragments.TopTenFragment;
import emroxriprap.com.spotifystreamer.models.ArtistEntry;
import emroxriprap.com.spotifystreamer.models.TopTenTrack;


public class MainActivity extends ActionBarActivity implements ArtistSearchFragment.CallbackToActivity
        ,TopTenFragment.CallbackToActivity{

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    public static boolean mTwoPane;
    private static final String TOP_TEN_FRAGMENT_TAG = "ttft";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.search_results_container) != null){
            //must be a tablet
            mTwoPane = true;
        }else{
            //its a phone
            mTwoPane = false;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("key", ArtistSearchFragment.mArtistsEntries);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArtistSearchFragment.mArtistsEntries = savedInstanceState.getParcelableArrayList("key");


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


    @Override
    public void onItemSelected(ArtistEntry artistEntry, int position) {

        if (isConnected()) {
            String spotId = artistEntry.getSpotifyId();
            if (mTwoPane) {
                //tablet...load fragment
                TopTenFragment fragment = new TopTenFragment();
                Bundle arguments = new Bundle();
                arguments.putString(TopTenFragment.FRAG_TAG, spotId);
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.search_results_container, fragment).commit();
            } else {
                //must be phone.. start new activity
                Intent intent = new Intent(this, TopTenActivity.class);
                intent.putExtra(TopTenFragment.FRAG_TAG, spotId);
                startActivity(intent);
            }

        }else{
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isConnected() {
        final ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean  connected = (activeNetwork != null && activeNetwork.isConnected()) ? true : false;
        return connected;
    }
    @Override
    public void onItemSelected(ArrayList<TopTenTrack> list ,int position) {

        if (isConnected()) {
            //This is called from the TopTenFragment class- when on tablet
            TopTenTrack t = list.get(position);
            Intent intent = new Intent(this, PlayerActivity.class);
            Bundle arguments = new Bundle();
            arguments.putParcelableArrayList(TopTenActivity.DATA_LIST, list);
            arguments.putInt(TopTenActivity.SELECTED_POSITION, position);
            intent.putExtras(arguments);
            startActivity(intent);
        }else{
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
        }
    }


}
