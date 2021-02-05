package exam.gdp.p2p;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    public String nickname;
    List<String> frinedList = new ArrayList<>();

    public User() {

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<String> getfrinedList() {
        return frinedList;
    }

    public void setFriends(List<String> frinedList) {
        this.frinedList = frinedList;
    }

    public String userProfile() {
        return null;
    }
}
