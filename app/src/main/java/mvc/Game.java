package mvc;

import java.util.ArrayList;

/**
 * Created by A on 2016-02-10.
 */
public class Game extends Thing {
    public Game(String descrption, String name) {
        super(descrption, name);
    }

    public Game(String description, String name, Status status) {
        super(description, name, status);
    }

    public void setKeywords() {
    }

    public ArrayList<String> getKeywords() {
        return null;
    }
}


