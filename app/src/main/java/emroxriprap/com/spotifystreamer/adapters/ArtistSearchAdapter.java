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
import emroxriprap.com.spotifystreamer.models.ArtistEntry;

/**
 * Created by administrator on 6/10/15.
 */
public class ArtistSearchAdapter extends ArrayAdapter<ArtistEntry> {
    Context context;
    private List<ArtistEntry> list;
    public ArtistSearchAdapter(Context context, List<ArtistEntry> list){
        super(context, R.layout.artist_list_item,list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.artist_list_item,parent,false);
            holder = new ViewHolder();
            holder.artistName = (TextView)convertView.findViewById(R.id.tv_artist_name);
            holder.imageView = (ImageView)convertView.findViewById(R.id.iv_small);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        final ArtistEntry ae = list.get(position);
        holder.artistName.setText(ae.getArtistName());
        if (ae.getImageUrlString()!=null){
            Picasso.with(context).load(ae.getImageUrlString()).into(holder.imageView);
        }

        return convertView;
    }
    class ViewHolder {
        TextView artistName;
        ImageView imageView;
    }
}
