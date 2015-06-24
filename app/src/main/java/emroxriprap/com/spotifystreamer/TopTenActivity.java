package emroxriprap.com.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import emroxriprap.com.spotifystreamer.adapters.TopTenAdapter;
import emroxriprap.com.spotifystreamer.fragments.TopTenFragment;
import emroxriprap.com.spotifystreamer.models.TopTenTrack;


public class TopTenActivity extends ActionBarActivity implements TopTenFragment.CallbackToActivity{

    private String spotId=null;
    private TopTenAdapter adapter;
    ArrayList<TopTenTrack> dataList = new ArrayList<TopTenTrack>();
    TopTenFragment fragment;
    public static final String DATA_LIST = "data_list";
    public static final String SELECTED_POSITION = "selected_position";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);

        if (savedInstanceState != null){
            fragment = (TopTenFragment)getSupportFragmentManager().getFragment(savedInstanceState,"top_ten_list");
        }else {
            fragment = new TopTenFragment();
            Bundle arguments = getIntent().getExtras();

            fragment.setArguments(arguments);
        }
        ActionBar bar = getSupportActionBar();
        bar.setTitle("Top Ten Tracks");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.search_results_container,fragment).commit();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        getSupportFragmentManager().putFragment(savedInstanceState,"top_ten_list",fragment);
        super.onSaveInstanceState(savedInstanceState);
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
    @Override
    public void onItemSelected(ArrayList<TopTenTrack> list ,int position) {
        //This is called from the TopTenFragment class- when on phone

        Intent intent = new Intent(this,PlayerActivity.class);
        TopTenTrack t = list.get(position);
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(DATA_LIST,list);
        arguments.putInt(SELECTED_POSITION,position);
        intent.putExtras(arguments);
        startActivity(intent);


    }
}
