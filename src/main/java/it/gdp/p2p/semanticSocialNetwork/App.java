package it.gdp.p2p.semanticSocialNetwork;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class App {

    @Option(name = "-mip", aliases = "--ipmaster", usage = "Ip of master peer", required = true)
    private static String masterIp;

    @Option(name = "-peerid", aliases = "--identifierpeer", usage = "Identifier unique for peer", required = true)
    private static int peerId;

    public static void main(String[] args) throws Exception {
        App app = new App();
        final CmdLineParser parser = new CmdLineParser(app);

        try {
            parser.parseArgument(args);

            TextIO textIO = TextIoFactory.getTextIO();
            TextTerminal terminal = textIO.getTextTerminal();

            SemanticHarmonySocialNetworkImpl peer = new SemanticHarmonySocialNetworkImpl(peerId, masterIp,
                    new MessageListenerImpl(peerId));

            terminal.printf("WELCOME ON BOARD MARINER\n\n");
            terminal.printf("PEER_ID_INFO: [%d] || MASTER_NODE_INFO: [%s]\n", peerId, masterIp);

            makeQuestions(terminal, peer);
            chooseNickname(terminal, textIO, peer);
            while (true) {
                firstMenu(terminal);

                int option = textIO.newIntInputReader().withMaxVal(4).withMinVal(1).read("Option");

                switch (option) {
                    case 1:
                        terminal.printf("** PERSONAL FRIEND LIST  **\n");
                    case 2:
                        terminal.printf("**  PERSONAL INFO  **\n");
                    case 3:
                        terminal.printf("**  NEW EVALUATION TESTS  **\n");
                    case 4:
                        terminal.println("********************");
                        terminal.println("**EXIT FROM SOCIAL**");
                        terminal.println("********************");
                        System.exit(0);
                }
            }

        } catch (CmdLineException e) {
            e.printStackTrace();
        }

    }

    /**
     * First menù of social network
     * 
     * @param terminal
     */
    public static void firstMenu(TextTerminal terminal) {
        terminal.print("\n**************************************\n");
        terminal.printf("**************** MENU ****************");
        terminal.print("\n**************************************\n");
        terminal.printf("** 1 - SHOW YOUR FRIEND LIST        **\n");
        terminal.printf("** 2 - SHOW PERSONAL INFO;          **\n");
        terminal.printf("** 3 - REPEAT EVALUATION TESTS;     **\n");
        terminal.printf("** 4 - EXIT FROM SOCIAL;            **\n");
        terminal.print("**************************************\n");
        terminal.print("**************************************\n");
    }

    public static void makeQuestions(TextTerminal terminal, SemanticHarmonySocialNetworkImpl peer){
        terminal.printf("\nBefore we start, we need to know more about your personality.\n");
        terminal.print("Answer this series of questions with a number between 1 (not at all agree) and 5 (very agree), Have fun!!\n");
        Utils.getAnswers(peer.getUserProfileQuestions(), peer.usr.getArrAnswers());
        Utils.printAnswers(peer.usr.getArrAnswers());
    }

    public static String chooseNickname(TextTerminal terminal, TextIO textIO, SemanticHarmonySocialNetworkImpl peer) {
        String user = peer.createAuserProfileKey(peer.usr.getArrAnswers());
        terminal.print("Set you personal Nickname: ");
        String nickName = textIO.newStringInputReader().read();
        if(peer.checkUnicNickName(nickName)) {
            if(peer.join(user, nickName)) {
                terminal.printf("** NICKNAME OK, USER JOINED! **");
                return nickName;
            }
        }
        else
            terminal.printf("[ERROR] - User already exist\n");
        return chooseNickname(terminal, textIO, peer);
    }
}
