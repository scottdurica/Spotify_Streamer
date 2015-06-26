package emroxriprap.com.spotifystreamer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import emroxriprap.com.spotifystreamer.R;
import emroxriprap.com.spotifystreamer.adapters.TopTenAdapter;
import emroxriprap.com.spotifystreamer.models.TopTenTrack;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TopTenFragment extends Fragment {
//    final String LOG_TAG = "TopTenFragment";
    private String mSpotifyId;
    public static final String FRAG_TAG = "tag";
    public static ArrayList<TopTenTrack> mTopTracks = new ArrayList<TopTenTrack>();
    public static TopTenAdapter mTopTenAdapter;

    public interface CallbackToActivity {
        public void onItemSelected(ArrayList<TopTenTrack>list, int position);
    }
    public TopTenFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments.getString(FRAG_TAG) != null){
            mSpotifyId = arguments.getString(FRAG_TAG);
        }else
        if (getActivity().getIntent().getStringExtra(FRAG_TAG) != null){
            mSpotifyId = getActivity().getIntent().getStringExtra(FRAG_TAG);
        }
        View rootView = inflater.inflate(R.layout.fragment_top_ten, container, false);
        ListView listView = (ListView)rootView.findViewById(R.id.lv_top_tracks);
        listView.setEmptyView(rootView.findViewById(R.id.empty_list_view));
        mTopTenAdapter = new TopTenAdapter(getActivity(),mTopTracks);
        listView.setAdapter(mTopTenAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //tablet- calls back to MainActivity since this fragment was added
                //to the mainactivity screen instead of being loaded by the TopTenActivity

                //Phone- calls back to onItemSelected() of the TopTenActivity class
                ((CallbackToActivity)getActivity()).onItemSelected(mTopTracks, position);

            }
        });
        if (mSpotifyId !=null){
            //If internet connection is available...
//            if (isConnected()){
                pollSpotifyForTopTracks(mSpotifyId);
//            }else{
//                Toast.makeText(getActivity(), R.string.no_connection, Toast.LENGTH_SHORT).show();
//            }
        }
        return rootView;
    }

//    public boolean isConnected() {
//        final ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
//        boolean  connected = (activeNetwork != null && activeNetwork.isConnected()) ? true : false;
//        return connected;
//    }
    private void pollSpotifyForTopTracks(String spotifyId){
       final ArrayList<TopTenTrack> trackList = new ArrayList<TopTenTrack>();
       SpotifyApi api = new SpotifyApi();
       SpotifyService spotify = api.getService();
       Map<String,Object> map = new HashMap<>();
       map.put("country",getActivity().getApplicationContext().getResources().getConfiguration().locale.getCountry());

       spotify.getArtistTopTrack(spotifyId, map, new Callback<Tracks>() {
           @Override
           public void success(Tracks tracks, Response response) {
               List<Track> list = tracks.tracks;
               if (list.size() == 0) {
                   getActivity().runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           Toast.makeText(getActivity(), "No top tracks found for selected artist.", Toast.LENGTH_LONG).show();
                           mTopTenAdapter.clear();
                       }
                   });
                   return;
               }
               for (Track t : list) {
                   String smImgUrl = null;
                   String lgImgUrl = null;
                   String trackName = t.name;
                   String previewUrl = t.preview_url;
                   AlbumSimple albumSimple = t.album;
                   String albumName = albumSimple.name;

                   List<ArtistSimple>s = t.artists;
                   ArtistSimple artist = s.get(0);
                   String artistName = artist.name;
                   List<Image> albumImages = albumSimple.images;
                   if (albumImages != null) {
                       for (Image i : albumImages) {
                           if (i.height > 600) {
                               lgImgUrl = i.url;
                           }
                           if (i.height > 100) {
                               smImgUrl = i.url;
                           }
                       }
                   }
                   TopTenTrack track = new TopTenTrack(albumName, trackName, smImgUrl, lgImgUrl,previewUrl,artistName);
                   trackList.add(track);
               }
               getActivity().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {

                         mTopTenAdapter.clear();
                         for (TopTenTrack t : trackList) {
                             mTopTenAdapter.add(t);

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
