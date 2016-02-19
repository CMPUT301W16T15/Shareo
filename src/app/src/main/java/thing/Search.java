package thing;

import java.util.List;

/**
 * Created by anonymous on 2/12/2016.
 */
public class Search {
    public static final int  THINGSEARCH = 0;
    public static final int BIDSEARCH = 1;
    List<Thing> currentThings;
    List<Bid> currentBids;

    public Search(List<Thing> listToSearch) {}
    public Search() {}

    public void setCurrentBids(List<Bid> bidList) {}
    public void setCurrentThings(List<Thing> currentThings) {}

    public List<Bid> getCurrentBids() throws InvalidSearchException {return currentBids;}
    public List<Thing> getCurrentThings() throws InvalidSearchException { return currentThings;}

    public List<Thing> filterByKeyword(String keyword) throws InvalidSearchException { return null; }
    public List<Thing> filterByDescription(String description) throws InvalidSearchException { return null; }

    public List<Thing> filterByNothing() throws InvalidSearchException { return null; }
    public List<Bid> bidFilterByNothing() throws InvalidSearchException{ return null; }

    public List<Bid> bidFilterByKeyword(String keyword) throws InvalidSearchException { return null; }
    public List<Bid> bidFilterByDescription(String description) throws InvalidSearchException { return null; }
    //Currently these next two aren't implemented yet (since we dont have gameName or gameType defined).
    public List<Thing> filterByGameName(String gameName) throws InvalidSearchException  { return null; }
    public List<Thing> filterByGameType(String gameType) throws InvalidSearchException { return null; }

    public List<Bid> bidFilterByGameType(String gameType) throws InvalidSearchException { return null; }
    public List<Bid> bidFilterByGameName(String gameType) throws InvalidSearchException  { return null; }

}
