package thientt.app.android.xemeuro2016.pojo;

/**
 * Created by thientran on 6/10/16.
 */
public class Match {
    private int match_id;
    private String link;
    private String title;
    private String time;

    private String thumbnail;

    public Match() {
    }

    public Match(int match_id, String link, String title, String time) {

        this.match_id = match_id;
        this.link = link;
        this.title = title;
        this.time = time;
    }

    public Match(int match_id, String link, String title, String time, String thumbnail) {
        this.match_id = match_id;
        this.link = link;
        this.title = title;
        this.time = time;
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
