package it.gdp.p2p.semanticSocialNetwork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    public String nickName;
    String profileKey;
    
    public List<Integer> arrAnswers = new ArrayList<Integer>();
    List<String> frinedList = new ArrayList<String>();
    

    public User() {

    }

    public String getnickName() {
        return nickName;
    }

    public void setnickName(String nickName) {
        this.nickName = nickName;
    }

    public List<Integer> getArrAnswers() {
        return arrAnswers;
    }

    public void setArrAnswers(List<Integer> arrAnswers) {
        this.arrAnswers = arrAnswers;
    }

    public String getProfileKey() {
        return profileKey;
    }

    public void setProfileKey(String profileKey) {
        this.profileKey = profileKey;
    }

    public List<String> getfrinedList() {
        return frinedList;
    }

    public void setFriends(List<String> frinedList) {
        this.frinedList = frinedList;
    }

    public void showUser() {
        System.out.println("Nickname: " + getnickName());
    }

    public void showMe() {
        System.out.println("Nickname: " + getnickName());
        System.out.println("ProfileKey: " + getProfileKey());
    }
    
}
