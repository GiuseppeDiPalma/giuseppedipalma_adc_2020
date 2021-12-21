package it.gdp.p2p.semanticSocialNetwork;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

public class MessageListenerImpl implements MessageListener {

    int peerid;

    public MessageListenerImpl(int peerid) {
        this.peerid = peerid;

    }

    public Object parseMessage(Object obj) {

        TextIO textIO = TextIoFactory.getTextIO();
        TextTerminal terminal = textIO.getTextTerminal();
        String message = (String) obj;
        // Simple check with message
        terminal.printf("\n" + peerid + "] (Direct Message Received) " + message + "\n");
        return "success";
    }
}
