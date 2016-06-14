package thientt.app.android.xemeuro2016;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import thientt.app.android.xemeuro2016.pojo.ConnectService;
import thientt.app.android.xemeuro2016.pojo.Constant;
import thientt.app.android.xemeuro2016.pojo.Match;
import thientt.app.android.xemeuro2016.pojo.ServerLink;
import thientt.app.android.xemeuro2016.pojo.TLog;

/**
 * Created by thientran on 6/10/16.
 */
public class ThienTTApplication extends Application {
    private ArrayList<ServerLink> serverLinks;
    public String URL = "http://" + Constant.IP + ":3012/";
    private Messenger messenger_full;
    private Handler main_handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int mode = bundle.getInt(Constant.ACTION_SERVICE);

            Message message = Message.obtain();
            switch (mode) {
                case Constant.ACTION_GET_LINK_SERVER:
                    message.setData(bundle);
                    try {
                        messenger_full.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
//                case Constant.ACTION_CREATE_GAME_PLAY:
//                    message.setData(bundle);
//                    try {
//                        messenger_create.send(message);
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                case Constant.ACTION_LIKE_EVENT:
//                    message.setData(bundle);
//                    try {
//                        messenger_like.send(message);
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                    break;
            }
            return true;
        }
    });

    public ArrayList<ServerLink> getServerLinks() {
//        serverLinks = new ArrayList<>();
//        Match match = new Match(100, "9Lw4WIkwj8I", "France vs Romania", "02:00 11/06/2016", "http://translateviet.com/wp-content/uploads/2016/06/France_Romania.jpg");
//        ArrayList<Match> matches = new ArrayList<>();
//        Match match2 = new Match(200, "5UyCodpkhJE", "Albania vs Switzerland", "20:00 11/06/2016", "http://translateviet.com/wp-content/uploads/2016/06/Albania_Switzerland.jpg");
//        matches.add(match2);
//        matches.add(match);
//        ServerLink serverLink = new ServerLink(ServerLink.TYPE_YOUTUBE, "Youtube", matches);
//        serverLinks.add(serverLink);

//        ServerLink serverLink2 = new ServerLink(ServerLink.TYPE_FACEBOOK, "Facebook", matches);
//        serverLinks.add(serverLink2);
//        ServerLink serverLink3 = new ServerLink(ServerLink.TYPE_FACEBOOK, "HayhayTV", matches);
//        serverLinks.add(serverLink3);
//        ServerLink serverLink4 = new ServerLink(ServerLink.TYPE_FACEBOOK, "VietHD", matches);
//        serverLinks.add(serverLink4);
//        ServerLink serverLink5 = new ServerLink(ServerLink.TYPE_FACEBOOK, "FTPPlay", matches);
//        serverLinks.add(serverLink5);
//        TLog.d(null, new Gson().toJson(serverLinks));

        return serverLinks;
    }

    public void getFullLinkServer(Handler handler) {
        messenger_full = new Messenger(handler);
        new ConnectService(this).getLinkServer(main_handler);
    }

    public String getURL() {
        String link = getSharedPreferences(Constant.AUTHORITY, Context.MODE_PRIVATE).getString(Constant.IP, null);
        return link != null ? link : URL;
    }

    public void saveServerLinks(ArrayList<ServerLink> serverLinks) {
        for (int i = 0; i < serverLinks.size(); i++) {
            ArrayList<Match> matches = serverLinks.get(i).getMatches();
            Collections.sort(matches, new Comparator<Match>() {
                public int compare(Match s1, Match s2) {
                    return s2.getMatch_id() - s1.getMatch_id();
                }
            });
            serverLinks.get(i).setMatches(matches);
        }
        this.serverLinks = serverLinks;
    }
}
