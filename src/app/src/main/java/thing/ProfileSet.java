package thing;

/**
 * Created by Larin on 2016/2/12.
 */
public class ProfileSet {
    private String UserName;
    private String Email;
    private String PhoneNumber;


    public ProfileSet(String UserName,String Email,String PhoneNumber) {
        this.UserName = UserName;
        this.Email = Email;
        this.PhoneNumber = PhoneNumber;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }
}
