package it.gdp.p2p.semanticSocialNetwork;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
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
        
        // System.out.println("Strings: " + str + " -  md5: "+ getMd5(profileKey));
        
        return profileKey;
    }

    public static String getMd5(String input)
    {
        try {
  
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
  
            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());
  
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
  
            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } 
  
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
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


    public static void cls(){
        try {
    
         if (System.getProperty("os.name").contains("Windows"))
             new ProcessBuilder("cmd", "/c", 
                      "cls").inheritIO().start().waitFor();
         else
             Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {}
    }
}
