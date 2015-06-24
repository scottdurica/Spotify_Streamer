package emroxriprap.com.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import emroxriprap.com.spotifystreamer.fragments.PlayerFragment;

public class PlayerActivity extends FragmentActivity {

PlayerFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_player);
        if (savedInstanceState != null){
            fragment = (PlayerFragment)getSupportFragmentManager().getFragment(savedInstanceState,"player");
        }else{
            fragment = new PlayerFragment();
            Bundle arguments = getIntent().getExtras();
            fragment.setArguments(arguments);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.player_container,fragment).commit();

    }

    @Override
    public void onBackPressed(){
        PlayerFragment.mHandler.removeCallbacksAndMessages(null);
        PlayerFragment.mMediaPlayer.stop();
        PlayerFragment.mMediaPlayer.release();
        PlayerFragment.mMediaPlayer = null;
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState,"player",fragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
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


}
