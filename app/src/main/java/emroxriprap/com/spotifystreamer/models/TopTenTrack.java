package emroxriprap.com.spotifystreamer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Scott Durica on 6/2/2015.
 */
public class TopTenTrack implements Parcelable{

    private String albumName;
    private String songName;
    private String smImgUrl;
    private String lgImgUrl;
    private String previewUrl;
    private String artistName;

    public TopTenTrack() {
    }

    //Parcelable constructor
    public TopTenTrack(Parcel in){
        readFromParcel(in);
    }

    public TopTenTrack(String albumName, String songName, String smImgUrl, String lgImgUrl, String previewUrl,String artistName) {
        this.albumName = albumName;
        this.songName = songName;
        this.smImgUrl = smImgUrl;
        this.lgImgUrl = lgImgUrl;
        this.previewUrl = previewUrl;
        this.artistName = artistName;
    }

    public String getPreviewUrl() {return previewUrl;}

    public void setPreviewUrl(String previewUrl) {this.previewUrl = previewUrl;}

    public String getArtistName() {return artistName;}

    public void setArtistName(String artistName) {this.artistName = artistName;}

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSmImgUrl() {
        return smImgUrl;
    }

    public void setSmImgUrl(String smImgUrl) {
        this.smImgUrl = smImgUrl;
    }

    public String getLgImgUrl() {
        return lgImgUrl;
    }

    public void setLgImgUrl(String lgImgUrl) {
        this.lgImgUrl = lgImgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(albumName);
        dest.writeString(songName);
        dest.writeString(smImgUrl);
        dest.writeString(lgImgUrl);
        dest.writeString(previewUrl);
        dest.writeString(artistName);
    }
    private void readFromParcel(Parcel in){
        albumName = in.readString();
        songName = in.readString();
        smImgUrl = in.readString();
        lgImgUrl = in.readString();
        previewUrl = in.readString();
        artistName = in.readString();
    }
    public static final Creator<TopTenTrack> CREATOR = new Parcelable.Creator<TopTenTrack>(){

        @Override
        public TopTenTrack createFromParcel(Parcel source) {
            return new TopTenTrack(source);
        }

        @Override
        public TopTenTrack[] newArray(int size) {
            return new TopTenTrack[size];
        }
    };
}
