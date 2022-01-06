package it.gdp.p2p.semanticSocialNetwork;

import java.util.ArrayList;
import java.util.List;

public class Question {

    List<String> questions = new ArrayList<String>();

    public Question() {
        questions.add("[1] - You regularly make new friends.\n");
        questions.add("[2] - You spend a lot of your free time exploring various random topics that pique your interest.\n");
        questions.add("[3] - Even a small mistake can cause you to doubt your overall abilities and knowledge.\n");
        questions.add("[4] - You often make a backup plan for a backup plan.\n");
        questions.add("[5] - Seeing other people cry can easily make you feel like you want to cry too.\n");
        questions.add("[6] - You prefer to completely finish one project before starting another.\n");
        questions.add("[7] - You are very sentimental.\n");
        questions.add("[8] - At social events, you rarely try to introduce yourself to new people and mostly talk to the ones you already know.\n");
        questions.add("[9] - You usually stay calm, even under a lot of pressure\n");
        questions.add("[10] - You are not too interested in discussing various interpretations and analyses of creative works.\n");
    }

    public List<String> returnQuestion() {
        return questions;
    }

}
