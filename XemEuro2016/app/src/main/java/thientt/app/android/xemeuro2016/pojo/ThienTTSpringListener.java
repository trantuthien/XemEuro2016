package thientt.app.android.xemeuro2016.pojo;

import android.view.View;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringUtil;

public class ThienTTSpringListener extends SimpleSpringListener {
    private View view;

    public ThienTTSpringListener(View view) {
        this.view = view;
    }

    public ThienTTSpringListener() {
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.75);
        if (view != null) {
            view.setScaleX(mappedValue);
            view.setScaleY(mappedValue);
        }
    }
}