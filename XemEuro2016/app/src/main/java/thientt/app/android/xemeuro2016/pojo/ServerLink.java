package thientt.app.android.xemeuro2016.pojo;

import java.util.ArrayList;

/**
 * Created by thientran on 6/10/16.
 */
public class ServerLink {
    public static final int TYPE_YOUTUBE = 0;
    public static final int TYPE_FACEBOOK = 1;
    private int mode;
    private String name;
    private ArrayList<Match> matches;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }

    public ServerLink() {

    }

    public ServerLink(int mode, String name, ArrayList<Match> matches) {

        this.mode = mode;
        this.name = name;
        this.matches = matches;
    }
}
