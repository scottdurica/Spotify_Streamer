package emroxriprap.com.spotifystreamer;

/**
 * Created by Scott Durica on 6/2/2015.
 */
public class TopTenTrack {

    private String albumName;
    private String songName;
    private String smImgUrl;
    private String lgImgUrl;

    public TopTenTrack() {
    }

    public TopTenTrack(String albumName, String songName, String smImgUrl, String lgImgUrl) {
        this.albumName = albumName;
        this.songName = songName;
        this.smImgUrl = smImgUrl;
        this.lgImgUrl = lgImgUrl;
    }

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
}
