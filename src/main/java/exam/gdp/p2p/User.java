package exam.gdp.p2p;

import java.util.List;
import java.util.Vector;

import net.tomp2p.peers.PeerAddress;

public class User {

    private String nickname;
    public List<User> friendList;

    private PeerAddress peer;
    public Vector resultVector;

    public User(PeerAddress peer, String nickname, List<User> friendList, Vector resultVector) {

        this.setPeer(peer);
        this.setNickname(nickname);
        this.friendList = friendList;
        this.resultVector = resultVector;

    }

    public PeerAddress getPeer() {
        return peer;
    }

    public void setPeer(PeerAddress peer) {
        this.peer = peer;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
