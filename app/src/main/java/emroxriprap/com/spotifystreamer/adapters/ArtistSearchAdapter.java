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
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.artist_list_item,parent,false);
        TextView artistName = (TextView)rowView.findViewById(R.id.tv_artist_name);
        ImageView imageView = (ImageView)rowView.findViewById(R.id.iv_small);
        final ArtistEntry ae = list.get(position);
        artistName.setText(ae.getArtistName());
        if (ae.getImageUrlString()!=null){
            Picasso.with(context).load(ae.getImageUrlString()).into(imageView);
        }

        return rowView;
    }
}
