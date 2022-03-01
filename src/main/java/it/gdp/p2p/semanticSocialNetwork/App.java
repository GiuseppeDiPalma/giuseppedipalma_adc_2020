package it.gdp.p2p.semanticSocialNetwork;

import java.util.ArrayList;
import java.util.List;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class App {

    protected static SemanticHarmonySocialNetworkImpl peer;
    static List<String> friends = new ArrayList<String>();

    @Option(name = "-mip", aliases = "--ipmaster", usage = "Ip of master peer", required = true)
    private static String master;

    @Option(name = "-peerid", aliases = "--identifierpeer", usage = "Identifier unique for peer", required = true)
    private static int id;

    public static void main(String[] args) throws Exception {
        App app = new App();
        final CmdLineParser parser = new CmdLineParser(app);

        try {
            parser.parseArgument(args);
            TextIO textIO = TextIoFactory.getTextIO();
            TextTerminal terminal = textIO.getTextTerminal();
            
            peer = new SemanticHarmonySocialNetworkImpl(id, master, new MessageListenerImpl(id));

            terminal.printf("WELCOME ON BOARD MARINER\n\n");
            terminal.printf("PEER_ID_INFO: [%d] || MASTER_NODE_INFO: [%s]\n", id, master);
        
            makeQuestions(terminal, peer);
            nameAndJoin(terminal, textIO, peer);
            
            while (true) {
                firstMenu(terminal);

                int option = textIO.newIntInputReader().withMaxVal(4).withMinVal(1).read("Option");

                switch (option) {
                    case 1:
                        friends = peer.getFriends();
                        if(friends.size() != 0) {
                            terminal.printf("**  FRIEND LIST  **\n");
                            Utils.getFriends(peer.usr.getFriendList());
                            terminal.printf("*******************\n");
                        }
                        else {
                            terminal.printf("** NO FRIENDS YET **\n");
                        }
                        break;
                    case 2:
                        terminal.printf("**  PEOPLE SEARCH  **\n");
                        String nicknameToFind = textIO.newStringInputReader().withMaxLength(20).read("Nickname");
                        User userFind = peer.getUser(nicknameToFind);
                        if(userFind != null){
                            if(userFind.getnickName().equals(peer.usr.getnickName())) {
                                userFind.showMe();
                                terminal.printf("**************\n");
                            }
                            else {
                                userFind.showUser();
                                terminal.printf("**************\n");
                            }
                        } else {
                            terminal.printf("** NO USER FOUND **\n");
                        }
                        break;
                    case 3:
                        //return all users in the network
                        terminal.printf("**  ALL USERS **\n");
                        List<User> allUsers = peer.getAllUsers();
                        for(User user : allUsers) {
                            user.showUser();
                        }
                        terminal.printf("****************\n");
                        break;
                    case 4:
                        terminal.println("************************");
                        terminal.println("**  EXIT FROM SOCIAL  **");
                        terminal.println("************************");
                        peer.exitFromNetwork();
                        System.exit(0);
                        break;
                }
            }
        } catch (CmdLineException e) {
            e.printStackTrace();
        }
    }

    /**
     * First menu of social network
     * 
     * @param terminal
     */
    public static void firstMenu(TextTerminal terminal) {
        terminal.printf("\n\n**************** MENU ****************\n");
        terminal.printf("** 1 - SHOW FRIEND LIST;            **\n");
        terminal.printf("** 2 - PEOPLE SEARCH;               **\n");
        terminal.printf("** 3 - SHOW ALL USER IN SOCIAL      **\n");
        terminal.printf("** 4 - EXIT FROM SOCIAL;            **\n");
        terminal.print("**************************************\n");
    }

    public static void makeQuestions(TextTerminal terminal, SemanticHarmonySocialNetworkImpl peer){
        terminal.printf("\nBefore we start, we need to know more about your personality.\n");
        terminal.print("Answer this series of questions with a number between 1 (not at all agree) and 5 (very agree), Have fun!!\n");
        Utils.pressToContinue();
        Utils.getAnswers(peer.getUserProfileQuestions(), peer.usr.getArrAnswers());
    }

    public static void nameAndJoin(TextTerminal terminal, TextIO textIO, SemanticHarmonySocialNetworkImpl peer) {
        String user = peer.createAuserProfileKey(peer.usr.getArrAnswers());

        boolean flag = false;
        do{
            terminal.print("Set you personal Nickname: ");
            String nickName = textIO.newStringInputReader().read();
            if(peer.checkUnicNickname(nickName)) {
                if(peer.join(user, nickName)) {
                    terminal.println("** USER JOINED **");
                    flag = true;
                }
            }
            else {
                terminal.printf("[ERROR] Nickname already in use");
            }
        }while(flag != true);
    }

    public static void showUser(TextTerminal terminal, String userInfo) {
        terminal.printf("\n%s\n", userInfo);
    }
    
}
