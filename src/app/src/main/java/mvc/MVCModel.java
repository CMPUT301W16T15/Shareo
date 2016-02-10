package mvc;

/**
 * Created by A on 2016-02-10.
 */
public abstract class MVCModel {
    public void addView(MVCView view) {}
    public void removeView(MVCView view) {}
    public void notifyViews() {}
}
