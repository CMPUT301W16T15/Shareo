package mvc;

import java.util.List;

/**
 * Created by A on 2016-02-10.
 */
public abstract class MVCModel {
    List<MVCView> views;

    public void addView(MVCView view) {
        views.add(view);
    }

    public void removeView(MVCView view) {
        views.remove(view);
    }

    public void notifyViews() {
        for (MVCView view : views) {
            view.updateView(this);
        }
    }
}
