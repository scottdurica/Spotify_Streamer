package emroxriprap.com.spotifystreamer;

/**
 * Created by Scott Durica on 6/1/2015.
 */
public class ArtistEntry {

    private String spotifyId;
    private String artistName;
    private String imageUrlString;

    public ArtistEntry(){

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
}
