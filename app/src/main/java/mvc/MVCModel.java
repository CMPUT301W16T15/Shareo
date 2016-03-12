package mvc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A on 2016-02-10.
 */
public abstract class MVCModel {
    transient static List<MVCView> views = new ArrayList<>();

    public MVCModel() { }

    public void addView(MVCView view) {
        if (!views.contains(view)) {
            views.add(view);
        }
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
