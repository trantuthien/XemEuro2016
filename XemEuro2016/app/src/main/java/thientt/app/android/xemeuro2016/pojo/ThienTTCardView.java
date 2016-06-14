package thientt.app.android.xemeuro2016.pojo;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import thientt.app.android.xemeuro2016.R;

public class ThienTTCardView extends FrameLayout {

    @Bind(R.id.textView)
    public TextView textView;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    ArrayList<Match> data;
    OnItemClickListener mItemClickListener;
    OnScrollListener onScrollListener;
    LinearLayoutManager layoutManager;
    RecyclerImageAdapter adapter;

    public ThienTTCardView(Context context) {
        super(context);
        initView();
    }

    public ThienTTCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ThienTTCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.parent_item_layout, null);
        ButterKnife.bind(this, view);
        addView(view);
    }

    public void initRecyclerView(ServerLink object) {
        this.data = object.getMatches();
        adapter = new RecyclerImageAdapter();
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new HidingScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (onScrollListener != null) {
                    onScrollListener.onScrolled(recyclerView, dx, dy, layoutManager);
                    onScrollListener.currentPosition(layoutManager.findFirstVisibleItemPosition());
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public interface OnScrollListener {
        void onScrolled(RecyclerView recyclerView, int dx, int dy, LinearLayoutManager layoutManager);

        void currentPosition(int childPosition);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class RecyclerImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public RecyclerImageAdapter() {
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
            View sView = mInflater.inflate(R.layout.child_item_layout, parent, false);
            return new ViewHolder(sView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            ViewHolder holder1 = (ViewHolder) viewHolder;
            holder1.setInfo(position);
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
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.imageView)
        ImageView imageView;

        @Bind(R.id.title)
        TextView title;

        @Bind(R.id.time)
        TextView time;

        private Spring spring_imageview;

        public ViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);

            spring_imageview = SpringSystem.create().createSpring();
            spring_imageview.addListener(new ThienTTSpringListener(imageView));
            imageView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            spring_imageview.setEndValue(1);
                            break;
                        case MotionEvent.ACTION_UP:
                            spring_imageview.setEndValue(0);
                            if (mItemClickListener != null) {
                                mItemClickListener.onItemClick(v, getAdapterPosition());
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            spring_imageview.setEndValue(0);
                            break;
                    }
                    return true;
                }
            });
        }

        public void setInfo(int position) {
            Glide.with(getContext()).load(data.get(position).getThumbnail()).into(imageView);
            time.setText(data.get(position).getTime() + "");
            title.setText(data.get(position).getTitle() + "");
        }
    }

    public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {

        private static final int HIDE_THRESHOLD = 20;
        private int mScrolledDistance = 0;
        private boolean mControlsVisible = true;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            if (firstVisibleItem == 0) {
                if (!mControlsVisible) {
                    mControlsVisible = true;
                }
            } else {
                if (mScrolledDistance > HIDE_THRESHOLD && mControlsVisible) {
                    mControlsVisible = false;
                    mScrolledDistance = 0;
                } else if (mScrolledDistance < -HIDE_THRESHOLD && !mControlsVisible) {
                    mControlsVisible = true;
                    mScrolledDistance = 0;
                }
            }
            if ((mControlsVisible && dy > 0) || (!mControlsVisible && dy < 0)) {
                mScrolledDistance += dy;
            }
        }

    }
}