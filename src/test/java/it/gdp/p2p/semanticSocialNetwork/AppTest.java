package it.gdp.p2p.semanticSocialNetwork;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.tomp2p.peers.PeerAddress;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    protected static SemanticHarmonySocialNetworkImpl p0, p1, p2, p3;
    List<Integer> answers = new ArrayList<Integer>();
    List<String> question = new ArrayList<String>();
    HashMap<String, String> hm = new HashMap<String, String>();

    public AppTest() throws Exception {

    }

    static class MessageListenerImpl implements MessageListener {

        int peerid;

        public MessageListenerImpl(int peerid) {
            this.peerid = peerid;
        }

        public Object parseMessage(Object obj) {
            return "success";
        }

    }

    @BeforeAll
    static void initializePeers() throws Exception {
        p0 = new SemanticHarmonySocialNetworkImpl(0, "127.0.0.1", new MessageListenerImpl(0));
        p1 = new SemanticHarmonySocialNetworkImpl(1, "127.0.0.1", new MessageListenerImpl(0));
        p2 = new SemanticHarmonySocialNetworkImpl(2, "127.0.0.1", new MessageListenerImpl(1));
        p3 = new SemanticHarmonySocialNetworkImpl(3, "127.0.0.1", new MessageListenerImpl(2));
    }

    @Test
    void checkFillAnserList(){
        answers = Arrays.asList(1, 2 , 3, 4, 5, 1, 2, 3, 4, 5);
        assertTrue(answers.size() == 10);
    }

    @Test
    void checkNewAnswer(){
        answers.add(1);
        assertTrue(answers.size() == 1);
    }

    @Test
    void checkFriendship(){
        User one = new User();
        User two = new User();
        User three = new User();
        User four = new User();

        one.setProfileKey("2311114415LBCSE");
        two.setProfileKey("4513525533KYAVF");
        assertTrue(Utils.checkFriendship(one, two));

        three.setProfileKey("2311114415LBCSE");
        four.setProfileKey("1454155152SFNEU");
        assertFalse(Utils.checkFriendship(three, four));
    }

    @Test
    void checkPeerAddressList(){
        assertTrue(p0.creatPeerAddressList());
        assertTrue(p1.creatPeerAddressList());
        assertTrue(p2.creatPeerAddressList());
        assertTrue(p3.creatPeerAddressList());
    }

    @Test
    void checkSendNotification(){
        HashMap<PeerAddress, String> hashmap = new HashMap<>();
        p0.usr.setArrAnswers(Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1, 5));
        p0.createAuserProfileKey(p0.usr.getArrAnswers());
        assertTrue(p0.notification(hashmap, "testMessage"));
    }

    @Test
    void checkJoinInNetwork(){
        p0.usr.setArrAnswers(Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1, 5));
        p0.createAuserProfileKey(p0.usr.getArrAnswers());
        String usr = p0.createAuserProfileKey(p0.usr.getArrAnswers());
        p0.checkUnicNickname("genTestNickname");
        assertTrue(p0.join(usr, "genTestNickname"));
    }

    @Test
    void checkPeerObj(){
        p0.usr.setArrAnswers(Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1, 5));
        p0.createAuserProfileKey(p0.usr.getArrAnswers());
        String usr = p0.createAuserProfileKey(p0.usr.getArrAnswers());
        p0.checkUnicNickname("genTestNickname");
        p0.join(usr, "genTestNickname");

        p1.usr.setArrAnswers(Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1, 5));
        p1.createAuserProfileKey(p1.usr.getArrAnswers());
        String usrTwo = p1.createAuserProfileKey(p1.usr.getArrAnswers());
        p1.checkUnicNickname("genTest1Nickname");
        p1.join(usrTwo, "genTest1Nickname");

        p2.usr.setArrAnswers(Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1, 5));
        p2.createAuserProfileKey(p2.usr.getArrAnswers());
        String usrThree = p2.createAuserProfileKey(p2.usr.getArrAnswers());
        p2.checkUnicNickname("genTest2Nickname");
        p2.join(usrThree, "genTest2Nickname");

        p3.usr.setArrAnswers(Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1, 5));
        p3.createAuserProfileKey(p3.usr.getArrAnswers());
        String usrFour = p3.createAuserProfileKey(p3.usr.getArrAnswers());
        p3.checkUnicNickname("genTest3Nickname");
        p3.join(usrFour, "genTest3Nickname");

        List<User> list = p0.getObjPeers();
        assertTrue(list.size() == 4);
    }

    @Test
    void checkExitFromNetwork(){
        p0.usr.setArrAnswers(Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1, 5));
        p0.createAuserProfileKey(p0.usr.getArrAnswers());
        String usr = p0.createAuserProfileKey(p0.usr.getArrAnswers());
        p0.checkUnicNickname("genTestNickname");
        p0.join(usr, "genTestNickname");
        assertTrue(p0.exitFromNetwork());
    }

    @Test
    void checkRetriveUserFromSearch() {
        p0.usr.setArrAnswers(Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1, 5));
        p0.createAuserProfileKey(p0.usr.getArrAnswers());
        String usr = p0.createAuserProfileKey(p0.usr.getArrAnswers());
        p0.checkUnicNickname("genTestNickname");
        p0.join(usr, "genTestNickname");

        p1.usr.setArrAnswers(Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1, 5));
        p1.createAuserProfileKey(p0.usr.getArrAnswers());
        String usrTwo = p1.createAuserProfileKey(p1.usr.getArrAnswers());
        p1.checkUnicNickname("genTestXNickname");
        p1.join(usrTwo, "genTestXNickname");

        assertTrue(p0.usr.getnickName() != null);
    }

}
