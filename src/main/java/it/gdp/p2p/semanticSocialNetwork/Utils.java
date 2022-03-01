package it.gdp.p2p.semanticSocialNetwork;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.security.*;

public class Utils {

    final static int MASTER_PORT_BASE = 6000;
    static TextIO textIO = TextIoFactory.getTextIO();
    static TextTerminal terminal = textIO.getTextTerminal();

    public static void getAnswers(List<String> questionsList, List<Integer> answersList) {
        for (int i = 0; i < questionsList.size(); i++) {
            terminal.printf(questionsList.get(i));
            answersList.add(textIO.newIntInputReader().withMaxVal(5).withMinVal(1).read());
        }
    }

    public static void printAnswers(List<Integer> answersList) {
        System.out.print("\nAnswer Key: [");

        for (int i = 0; i < answersList.size(); i++)
            System.out.print(answersList.get(i));
        
        System.out.println("]\n");        
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
    
    public static void getFriends (List<String> friends) {
        for (int i = 0; i < friends.size(); i++) {
            terminal.printf("Friend %d: %s\n", i + 1, friends.get(i));
        }
    }

    public static String genProfileKey(List<Integer> answer) throws NoSuchAlgorithmException {
        
        List<String> str = answer.stream().map(Object::toString).collect(Collectors.toUnmodifiableList());
        String profileKey = str.stream().collect(Collectors.joining(""));

        return profileKey + generateRandomString();
        //return shuffle(finalString);
    }

    public static String generateRandomString() {
 
        int leftLimit = 65; // letter 'A'
        int rightLimit = 90; // letter 'Z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) 
              (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
    
        return generatedString;
    }

    public static String shuffle(final String str) {
        List<Character> chars = str.chars().mapToObj(e->(char)e).collect(Collectors.toList());
        Collections.shuffle(chars);
        return chars.stream().map(e->e.toString()).collect(Collectors.joining());
    }

    public static boolean checkFriendship(User one, User two) {
        int LCSDistance = editDistanceWith2Ops(one.getProfileKey(), two.getProfileKey());
        System.out.println(LCSDistance);
        return true;
    }

    static int editDistanceWith2Ops(String X, String Y) {
        // Find LCS
        int m = X.length(), n = Y.length();
        int L[][] = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0 || j == 0) {
                    L[i][j] = 0;
                } else if (X.charAt(i - 1) == Y.charAt(j - 1)) {
                    L[i][j] = L[i - 1][j - 1] + 1;
                } else {
                    L[i][j] = Math.max(L[i - 1][j], L[i][j - 1]);
                }
            }
        }
        int lcs = L[m][n];
 
        // Edit distance is delete operations +
        // insert operations.
        return (m - lcs) + (n - lcs);
    }
}
