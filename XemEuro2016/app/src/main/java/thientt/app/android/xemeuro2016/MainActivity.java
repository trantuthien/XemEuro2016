package thientt.app.android.xemeuro2016;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import thientt.app.android.xemeuro2016.pojo.Constant;
import thientt.app.android.xemeuro2016.pojo.DeveloperKey;
import thientt.app.android.xemeuro2016.pojo.Match;
import thientt.app.android.xemeuro2016.pojo.ServerLink;
import thientt.app.android.xemeuro2016.pojo.TLog;
import thientt.app.android.xemeuro2016.pojo.ThienTTCardView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends AppCompatActivity implements YouTubePlayer.OnFullscreenListener, SwipeRefreshLayout.OnRefreshListener {
    private boolean isFullscreen;
    private VideoFragment videoFragment;
    private static final int ANIMATION_DURATION_MILLIS = 300;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private static final int LANDSCAPE_VIDEO_PADDING_DP = 5;

    private View videoBox;
    private View closeButton;


    //ThienTT
    private ThienTTApplication application;
    private ArrayList<ServerLink> serverLinks;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerAdapter adapter;
    private boolean is_refesh = true;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (bundle != null) {
                swipeRefreshLayout.setRefreshing(false);
                is_refesh = true;
                int mode = bundle.getInt(Constant.ACTION_SERVICE);
                if (mode == Constant.ACTION_GET_LINK_SERVER) {
                    String data = bundle.getString(Constant.RESULT_HANDLER);
                    if (data != null) {
                        //TLog.d(null, data);
                        try {
                            JSONObject object = new JSONObject(data);
                            if (object.getBoolean("success")) {
                                String game_data = object.getString("message");
                                if (game_data != null)
                                    serverLinks = new Gson().fromJson(game_data, new TypeToken<ArrayList<ServerLink>>() {
                                    }.getType());
                                if (serverLinks != null) {
                                    application.saveServerLinks(serverLinks);
                                    createAdapterForRecyclerView();
                                }
                            } else {
                                //Toast.makeText(PlashScreenActivity.this, "Something Wrong, try again", Toast.LENGTH_LONG).show();
                                Constant.showSnackBar(swipeRefreshLayout, getString(R.string.wrong));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
            return true;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        application = (ThienTTApplication) getApplication();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

//        createToolbar();
        showDataOnRecyclerView();

       // TLog.d(this, new Gson().toJson(serverLinks));
        videoFragment =
                (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment_container);
        videoBox = findViewById(R.id.video_box);
        closeButton = findViewById(R.id.close_button);
        videoBox.setVisibility(View.INVISIBLE);
        layout();
        checkYouTubeApi();
        if (serverLinks != null && serverLinks.size() > 0)
            openYoutubeLink(serverLinks.get(0).getMatches().get(0));
    }

    private void showDataOnRecyclerView() {
        serverLinks = application.getServerLinks();
        if (serverLinks != null && serverLinks.size() > 0) {
            createAdapterForRecyclerView();
        }
    }

    private void createAdapterForRecyclerView() {
        if (layoutManager == null || swipeRefreshLayout == null) {
            adapter = new RecyclerAdapter(serverLinks);
            recyclerView.setAdapter(adapter);
            layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
            swipeRefreshLayout.setOnRefreshListener(MainActivity.this);
        } else {
            serverLinks = application.getServerLinks();
            adapter = new RecyclerAdapter(serverLinks);
            recyclerView.setAdapter(adapter);
            openYoutubeLink(serverLinks.get(0).getMatches().get(0));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            recreate();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        layout();
    }

    public void onClickClose(@SuppressWarnings("unused") View view) {

        videoFragment.pause();
        ViewPropertyAnimator animator = videoBox.animate()
                .translationYBy(videoBox.getHeight())
                .setDuration(ANIMATION_DURATION_MILLIS);
        runOnAnimationEnd(animator, new Runnable() {
            @Override
            public void run() {
                videoBox.setVisibility(View.INVISIBLE);
            }
        });
    }

    @TargetApi(16)
    private void runOnAnimationEnd(ViewPropertyAnimator animator, final Runnable runnable) {
        if (Build.VERSION.SDK_INT >= 16) {
            animator.withEndAction(runnable);
        } else {
            animator.setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    runnable.run();
                }
            });
        }
    }

    private void checkYouTubeApi() {
        YouTubeInitializationResult errorReason =
                YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else if (errorReason != YouTubeInitializationResult.SUCCESS) {
            String errorMessage =
                    String.format(getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    private void openYoutubeLink(Match match) {
        VideoFragment videoFragment =
                (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment_container);
        videoFragment.setVideoId(match.getLink());

        // The videoBox is INVISIBLE if no video was previously selected, so we need to show it now.
        if (videoBox.getVisibility() != View.VISIBLE) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                // Initially translate off the screen so that it can be animated in from below.
                videoBox.setTranslationY(videoBox.getHeight());
            }
            videoBox.setVisibility(View.VISIBLE);
        }

        // If the fragment is off the screen, we animate it in.
        if (videoBox.getTranslationY() > 0) {
            videoBox.animate().translationY(0).setDuration(ANIMATION_DURATION_MILLIS);
        }
    }

    @Override
    public void onFullscreen(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        layout();
    }

    private void layout() {
        boolean isPortrait =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

//         recyclerView.setVisibility(isFullscreen ? View.GONE : View.VISIBLE);

        closeButton.setVisibility(isPortrait ? View.VISIBLE : View.GONE);
        //getSupportActionBar().show();
        if (isFullscreen) {
            //  getSupportActionBar().hide();
            videoBox.setTranslationY(0); // Reset any translation that was applied in portrait.
            setLayoutSize(videoFragment.getView(), MATCH_PARENT, MATCH_PARENT);
            setLayoutSizeAndGravity(videoBox, MATCH_PARENT, MATCH_PARENT, Gravity.TOP | Gravity.LEFT);
        } else if (isPortrait) {
//            setLayoutSize(recyclerView, MATCH_PARENT, MATCH_PARENT);
            setLayoutSize(videoFragment.getView(), MATCH_PARENT, WRAP_CONTENT);
            setLayoutSizeAndGravity(videoBox, MATCH_PARENT, WRAP_CONTENT, Gravity.BOTTOM);
        } else {
            videoBox.setTranslationY(0); // Reset any translation that was applied in portrait.
            int screenWidth = dpToPx(getResources().getConfiguration().screenWidthDp);
//             setLayoutSize(recyclerView, screenWidth / 4, MATCH_PARENT);
            int videoWidth = screenWidth - screenWidth / 4 - dpToPx(LANDSCAPE_VIDEO_PADDING_DP);
            setLayoutSize(videoFragment.getView(), videoWidth, WRAP_CONTENT);
            setLayoutSizeAndGravity(videoBox, videoWidth, WRAP_CONTENT,
                    Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    private static void setLayoutSize(View view, int width, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    private static void setLayoutSizeAndGravity(View view, int width, int height, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        params.gravity = gravity;
        view.setLayoutParams(params);
    }

    @Override
    public void onRefresh() {
        //ThienTT
        if (is_refesh) {
            is_refesh = false;
            application.getFullLinkServer(handler);
        } else
            swipeRefreshLayout.setRefreshing(false);
    }

    public static final class VideoFragment extends YouTubePlayerFragment
            implements YouTubePlayer.OnInitializedListener {

        private YouTubePlayer player;
        private String videoId;

        public static VideoFragment newInstance() {
            return new VideoFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            initialize(DeveloperKey.DEVELOPER_KEY, this);
        }

        @Override
        public void onDestroy() {
            if (player != null) {
                player.release();
            }
            super.onDestroy();
        }

        public void setVideoId(String videoId) {
            if (videoId != null && !videoId.equals(this.videoId)) {
                this.videoId = videoId;
                if (player != null) {
                    player.cueVideo(videoId);
                }
            }
        }

        public void pause() {
            if (player != null) {
                player.pause();
            }
        }

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean restored) {
            this.player = player;
            player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
            player.setOnFullscreenListener((MainActivity) getActivity());
            if (!restored && videoId != null) {
                player.cueVideo(videoId);
            }
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
            this.player = null;
        }

    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<ServerLink> data;

        public RecyclerAdapter(ArrayList<ServerLink> data) {
            this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            final ThienTTCardView cardView = new ThienTTCardView(parent.getContext());
            FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layout.setMargins(15, 10, 10, 15);
            cardView.setLayoutParams(layout);
            return new ViewHolder(cardView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.getDataToHolder(position);
        }

        public int getBasicItemCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position)) {
                return 0;
            }
            return 1;
        }

        @Override
        public int getItemCount() {
            return getBasicItemCount();
        }


        private boolean isPositionHeader(int position) {
            return position == 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ThienTTCardView view;
            public int position;

            public ViewHolder(final ThienTTCardView view) {
                super(view);
                this.view = view;
            }

            public void getDataToHolder(final int position) {
                view.textView.setText(data.get(position).getName());

                if (data.get(position).getMatches().size() > 0)
                    view.initRecyclerView(data.get(position));
                this.position = position;
                view.setOnScrollListener(new ThienTTCardView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy, LinearLayoutManager layoutManager) {
                    }

                    @Override
                    public void currentPosition(int childPosition) {
                        //TLog.d(null, position + " " + childPosition);
                        //data.get(position).setPosition(childPosition);
                    }
                });
                view.setOnItemClickListener(new ThienTTCardView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position_item) {
                       // TLog.d(null, "nhan vao: " + position + "-" + position_item);
                        //if (data.get(position).getMode() == ServerLink.TYPE_YOUTUBE) {
                            openYoutubeLink(data.get(position).getMatches().get(position_item));
//                        } else {
//                            Toast.makeText(MainActivity.this, getString(R.string.update), Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
            }

        }
    }

    public boolean doubleBackToExitPressedOnce;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.againexit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
