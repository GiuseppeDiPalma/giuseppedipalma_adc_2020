package it.gdp.p2p.semanticSocialNetwork;

import java.util.List;
import java.util.Random;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

public class Utils {

    final static int MASTER_PORT_BASE = 6000;
    static TextIO textIO = TextIoFactory.getTextIO();
    static TextTerminal terminal = textIO.getTextTerminal();

    /**
     * Print all questions to the user and add the answer in a list.
     * @param questionsList
     * @param answersList
     */
    public static void getAnswers(List<String> questionsList, List<Integer> answersList) {
        for (int i = 0; i < questionsList.size(); i++) {
            terminal.printf(questionsList.get(i));
            answersList.add(textIO.newIntInputReader().withMaxVal(5).withMinVal(1).read());
        }
    }

    /**
     * Print the answer to the questions
     */
    public static void printAnswers(List<Integer> answersList) {
        System.out.print("\nAnswer Key: [");

        for (int i = 0; i < answersList.size(); i++)
            System.out.print(answersList.get(i));
        
        System.out.println("]\n");        
    }
    
    /**
     * Print all frinds of the User.
     */
    public static void getFriends(List<String> friends) {
        for (int i = 0; i < friends.size(); i++) {
            terminal.printf("Friend %d: %s\n", i + 1, friends.get(i));
        }
    }

    /**
     * Merge list of answer with a random string of length 5
     * @return String
     */
    public static String genProfileKey(List<Integer> answer){
        String profileKey = "";
        for (int i = 0; i < answer.size(); i++) {
             profileKey += answer.get(i);
        } 

        return profileKey + generateRandomString();
    }

    /**
     * Generate a random string of length 5;
     * String have random characters from 'A' to 'Z'
     * @return String
     */
    public static String generateRandomString() {
        int leftLimit = 65; // letter 'A'
        int rightLimit = 90; // letter 'Z'
        int targetStringLength = 5;
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

    /**
     * Check if two users are friends.
     * If LCSDisance <= 5 then they are friends.
     * Else they are not friends.
     */
    public static boolean checkFriendship(User one, User two) {
        int LCSDistance = LCSDistance(one.getProfileKey(), two.getProfileKey());
        //System.out.println(one.getProfileKey()+" <> "+ two.getProfileKey() + " LCSDistance " + LCSDistance);
        if (LCSDistance <= 5) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns the length of the longest common subsequence between two strings.
     * @param X String 1
     * @param Y String 2
     * @return LCSDistance
     */
    static int LCSDistance(String X, String Y) {
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
        return lcs;
    }

    /**
     * Trivial method for pess to continue
     */
    public static void pressToContinue(){
        System.out.println("\nPress enter to continue...");
        try{
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
