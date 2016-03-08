package mvc;

import java.util.ArrayList;

/**
 * Created by A on 2016-02-10.
 */
public class Game extends Thing {

    public Game(String gameName, String description) {
        super(gameName, description);
    }

    public Game(String gameName, String description, Status status) {
        super(gameName, description, status);
    }

    public void setKeywords() {
    }

    public ArrayList<String> getKeywords() {
        return null;
    }
}


