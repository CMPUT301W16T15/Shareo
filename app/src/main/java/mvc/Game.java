package mvc;

import java.util.ArrayList;

/**
 * Created by A on 2016-02-10.
 */
public class Game extends Thing {
    public Game(String gameName, String description, String owner) {
        super(gameName, description, owner);
    }

    public Game(String gameName, String description, String owner, Status status) {
        super(gameName, description, owner, status);
    }

    public void setKeywords() {
    }

    public ArrayList<String> getKeywords() {
        return null;
    }
}


