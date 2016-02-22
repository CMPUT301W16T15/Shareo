package thing;

import java.util.ArrayList;

/**
 * Created by A on 2016-02-10.
 */
public class Game extends Thing {
    public Game(String descrption) {
        super(descrption);
    }

    public Game(String description, Status status) {
        super(description, status);
    }

    public void setKeywords() {
    }

    public ArrayList<String> getKeywords() {
        return null;
    }
}


