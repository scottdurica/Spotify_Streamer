package emroxriprap.com.spotifystreamer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import emroxriprap.com.spotifystreamer.R;
import emroxriprap.com.spotifystreamer.adapters.ArtistSearchAdapter;
import emroxriprap.com.spotifystreamer.models.ArtistEntry;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ArtistSearchFragment extends Fragment

{

    private ArtistSearchAdapter mAdapter;
    private SearchView searchView;
    public static ArrayList<ArtistEntry> mArtistsEntries = new ArrayList<ArtistEntry>();
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_POSITION = "selected_position_in_list";
    private ListView listView;
    public interface CallbackToActivity {
        public void onItemSelected(ArtistEntry artistEntry,int position);
//        public void onItemSelected(ArrayList<TopTenTrack>list, int position);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //uncomment to access the menu for this fragment
//        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION){
            savedInstanceState.putInt(SELECTED_POSITION, mPosition);
        }
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public void onResume() {
        listView.smoothScrollToPosition(mPosition);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_artist_search,container,false);
        mAdapter = new ArtistSearchAdapter(getActivity(),mArtistsEntries);

        listView = (ListView)rootView.findViewById(R.id.lv_search_results);
        listView.setEmptyView(rootView.findViewById(R.id.empty_list_view));
        listView.setAdapter(mAdapter);

        searchView = (SearchView)rootView.findViewById(R.id.sv_artist_search);
        searchView.setIconifiedByDefault(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((CallbackToActivity)getActivity()).onItemSelected(mArtistsEntries.get(position),position);
                mPosition = position;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pollSpotifyForArtists(query);
                InputMethodManager inputMethodManager = (InputMethodManager)getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(),0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //this will fire on every change to searchView
                return false;
            }
        });
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_POSITION)){
            mPosition = savedInstanceState.getInt(SELECTED_POSITION);

        }

        return rootView;
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
                    List<Image> images = a.images;
                    if (images != null) {
                        for (Image i : images) {
                            if (i.height > 100) {
                                thumbImageUrl = i.url;
                            }
                        }
                    }
                    ArtistEntry artistEntry = new ArtistEntry(a.id, a.name, thumbImageUrl);
                    artistsList.add(artistEntry);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (artistsList == null || artistsList.size() == 0) {
                            //No results found.  Tell user to change search

                            Toast.makeText(getActivity(), "No matching artists found. Please refine your search.", Toast.LENGTH_LONG).show();


                        } else {

                            mAdapter.clear();
                            for (ArtistEntry ae : artistsList) {
                                mAdapter.add(ae);
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
}
