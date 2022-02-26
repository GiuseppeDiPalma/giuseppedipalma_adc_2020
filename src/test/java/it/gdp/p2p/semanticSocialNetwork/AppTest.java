package it.gdp.p2p.semanticSocialNetwork;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
    void testCaseFillAanserList(){
        answers = Arrays.asList(1, 2 , 3, 4, 5, 1, 2, 3, 4, 5);
        assertTrue(answers.size() == 10);
    }

    @Test
    void testCaseNewAnswer(){
        answers.add(1);
        assertTrue(answers.size() == 1);
    }
}
