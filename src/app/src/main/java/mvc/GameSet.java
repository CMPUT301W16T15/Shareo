package mvc;

/**
 * Created by Larin on 2016/2/11.
 */
public class GameSet {
    private String GameName;
    private String KeyWord;
    private String Description;
    private double PricePerDay;

    public GameSet(String GameName,double PricePerDay,String KeyWord,String Description){
        this.GameName = GameName;
        this.KeyWord = KeyWord;;
        this.Description = Description;
        this.PricePerDay = PricePerDay;
    }

    public String getGameName() {
        return GameName;
    }

    public void setGameName(String GameName) {
        this.GameName = GameName;
    }

    public String getKeyWord() {
        return KeyWord;
    }

    public void setKeyWord(String keyWord) {
        this.KeyWord = KeyWord;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = Description;
    }

    public double getPricePerDay() {
        return PricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.PricePerDay = PricePerDay;
    }
}
