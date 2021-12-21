package it.gdp.p2p.semanticSocialNetwork;

import java.util.ArrayList;
import java.util.List;

public class Question {

    List<String> questions = new ArrayList<String>();

    public Question() {
        questions.add("I see myself as someone who...\n" + "\tHa pochi interessi artistici\n");
        questions.add("I see myself as someone who...\n" + "\tTende ad essere pigro\n");
        questions.add("I see myself as someone who...\n" + "\tÈ estroverso e socievole\n");
        questions.add("I see myself as someone who...\n" + "\tHa una fervida immaginazione\n");
        questions.add("I see myself as someone who...\n" + "\tTende a trovare difetti negli altri\n");
        questions.add("I see myself as someone who...\n" + "\tSi innervosisce facilmente\n");
        questions.add("I see myself as someone who...\n" + "\tÈ rilassato e gestisce bene lo stress\n");
        questions.add("I see myself as someone who...\n" + "\tÈ solitamente fiducioso\n");
        questions.add("I see myself as someone who...\n" + "\tÈ riservato\n");
        questions.add("I see myself as someone who...\n" + "\tHa pochi interessi artistici\n");
    }

    public List<String> returnQuestion() {
        return questions;
    }

}
