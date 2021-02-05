package exam.gdp.p2p;

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

            User user = new User();

            terminal.printf("Staring peer id: %d on master node: %s\n", peerId, masterIp);

            while (true) {
                firstMenu(terminal);

                int option = textIO.newIntInputReader().withMaxVal(3).withMinVal(1).read("Option");

                switch (option) {
                    case 1:
                        terminal.printf("REGISTER NEW USER\n");
                        String nickname = textIO.newStringInputReader().withMinLength(1).read("Choose a nickname: ");
                        terminal.printf("Hi, " + nickname);
                        terminal.printf(
                                "Now, with the following questions, we can help you find friends similar to you.");

                    case 2:
                        terminal.printf("SIGN IN\n");

                    case 3:
                        terminal.printf("BYE BYE\n");
                        System.exit(0);
                }
            }

        } catch (CmdLineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * First menù of social network
     * 
     * @param terminal
     */
    public static void firstMenu(TextTerminal terminal) {
        terminal.printf("1 - REGISTER;\n");
        terminal.printf("2 - LOGIN;\n");
        terminal.printf("3 - EXIT;\n");
    }
}
