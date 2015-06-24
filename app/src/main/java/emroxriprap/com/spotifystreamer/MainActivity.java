package emroxriprap.com.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import emroxriprap.com.spotifystreamer.fragments.ArtistSearchFragment;
import emroxriprap.com.spotifystreamer.fragments.TopTenFragment;
import emroxriprap.com.spotifystreamer.models.ArtistEntry;
import emroxriprap.com.spotifystreamer.models.TopTenTrack;


public class MainActivity extends ActionBarActivity implements ArtistSearchFragment.CallbackToActivity
        ,TopTenFragment.CallbackToActivity{

//    private final String LOG_TAG = MainActivity.class.getSimpleName();

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

        String spotId = artistEntry.getSpotifyId();
        if (mTwoPane){
            //tablet...load fragment
            TopTenFragment fragment = new TopTenFragment();
            Bundle arguments = new Bundle();
            arguments.putString(TopTenFragment.FRAG_TAG,spotId);
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.search_results_container,fragment).commit();
        }else{
            //must be phone.. start new activity
            Intent intent = new Intent(this,TopTenActivity.class);
            intent.putExtra(TopTenFragment.FRAG_TAG,spotId);
            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(ArrayList<TopTenTrack> list ,int position) {
        //This is called from the TopTenFragment class- when on tablet
        TopTenTrack t = list.get(position);
        Intent intent = new Intent(this,PlayerActivity.class);
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(TopTenActivity.DATA_LIST,list);
        arguments.putInt(TopTenActivity.SELECTED_POSITION,position);
        intent.putExtras(arguments);
        startActivity(intent);
    }
}
