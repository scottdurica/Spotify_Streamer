package emroxriprap.com.spotifystreamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import emroxriprap.com.spotifystreamer.R;
import emroxriprap.com.spotifystreamer.models.TopTenTrack;

/**
 * Created by administrator on 6/10/15.
 */
public class TopTenAdapter extends ArrayAdapter<TopTenTrack> {
    Context context;
    private List<TopTenTrack> list;
    public TopTenAdapter(Context context, List<TopTenTrack> list){
        super(context, R.layout.top_ten_list_item,list);
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
                //TODO  Start new activity to play selected track...
            }
        });
        return rowView;
    }
}