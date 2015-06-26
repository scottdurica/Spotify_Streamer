package emroxriprap.com.spotifystreamer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Scott Durica on 6/1/2015.
 */
public class ArtistEntry implements Parcelable {

    private String spotifyId;
    private String artistName;
    private String imageUrlString;

    public ArtistEntry(){

    }
    //Constructor for loading from Parcel
    public ArtistEntry(Parcel in){
        readFromParcel(in);
    }
    public ArtistEntry(String spotifyId, String artistName, String imageUrlString){
        this.spotifyId = spotifyId;
        this.artistName = artistName;
        this.imageUrlString = imageUrlString;
    }
    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getImageUrlString() {
        return imageUrlString;
    }

    public void setImageUrlString(String imageUrlString) {
        this.imageUrlString = imageUrlString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(spotifyId);
        parcel.writeString(artistName);
        parcel.writeString(imageUrlString);
    }
    private void readFromParcel(Parcel in){
        spotifyId = in.readString();
        artistName = in.readString();
        imageUrlString = in.readString();
    }
    public static final Creator<ArtistEntry> CREATOR = new Parcelable.Creator<ArtistEntry>(){

        @Override
        public ArtistEntry createFromParcel(Parcel source) {
            return new ArtistEntry(source);
        }

        @Override
        public ArtistEntry[] newArray(int size) {
            return new ArtistEntry[size];
        }
    };
}
