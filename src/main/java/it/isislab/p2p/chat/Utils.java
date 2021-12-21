package it.isislab.p2p.chat;

import java.util.List;
import java.util.Scanner;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

public class Utils {

    final static int MASTER_PORT_BASE = 6000;
    static Scanner input = new Scanner(System.in);
    static TextIO textIO = TextIoFactory.getTextIO();
    static TextTerminal terminal = textIO.getTextTerminal();

    public static void getAnswers(List<String> questionsList, List<Integer> answersList) {
        for (int i = 0; i < questionsList.size(); i++) {
            terminal.printf(questionsList.get(i));
            answersList.add(textIO.newIntInputReader().withMaxVal(1).withMinVal(0).read());
        }
    }

    public static void printAnswers(List<Integer> answersList) {
        for (int i = 0; i < answersList.size(); i++)
            terminal.println(answersList.get(i).toString(i));
    }

    // function to calculate Hamming distance
    static int hammingDist(String str1, String str2) {
        int i = 0, count = 0;
        while (i < str1.length()) {
            if (str1.charAt(i) != str2.charAt(i))
                count++;
            i++;
        }
        return count;
    }
}
