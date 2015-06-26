package emroxriprap.com.spotifystreamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import emroxriprap.com.spotifystreamer.R;
import emroxriprap.com.spotifystreamer.models.TopTenTrack;

/**
 * Created by administrator on 6/10/15.
 */
public class TopTenAdapter extends ArrayAdapter<TopTenTrack> {
    Context context;
    public static ArrayList<TopTenTrack> mList;
    public TopTenAdapter(Context context, ArrayList<TopTenTrack> list){
        super(context, R.layout.top_ten_list_item,list);
        this.context = context;
        this.mList = list;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(R.layout.top_ten_list_item,parent,false);
            holder = new ViewHolder();
            holder.albumName = (TextView)convertView.findViewById(R.id.tv_ttl_album_name);
            holder.songName = (TextView)convertView.findViewById(R.id.tv_ttl_song_name);
            holder.imageView = (ImageView)convertView.findViewById(R.id.iv_ttl_thumb_img);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final TopTenTrack track = mList.get(position);
        holder.songName.setText(track.getSongName());
        holder.albumName.setText(track.getAlbumName());


        if (track.getSmImgUrl()!=null){
            Picasso.with(context).load(track.getSmImgUrl()).into(holder.imageView);
        }


        return convertView;
    }
    class ViewHolder {
        TextView songName;
        TextView albumName;
        ImageView imageView;
    }
}