package mvc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A on 2016-02-10.
 */
public abstract class Observable {
    transient static List<Observer> observers = new ArrayList<>();

    public Observable() { }

    public void addView(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeView(Observer observer) {
        observers.remove(observer);
    }

    public void notifyViews() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
}
