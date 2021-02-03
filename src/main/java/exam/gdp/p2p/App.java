package exam.gdp.p2p;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;


public class App {

    //@Option(name = "-mip", aliases = "--ipmaster", usage = "Ip of master peer", required = true)
    private static String masterIp;

    //@Option(name = "-peerid", aliases = "--identifierpeer", usage = "Identifier unique for peer", required = true)
    private static int peerId;

    public static void main(String[] args) {
        App app = new App();
        final CmdLineParser parser = new CmdLineParser(app);

        try {

            parser.parseArgument(args);

            TextIO textIO = TextIoFactory.getTextIO();
            TextTerminal terminal = textIO.getTextTerminal();

            terminal.printf("questo è solo un test");

        } catch (CmdLineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
