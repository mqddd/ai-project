package com.example.logic;

import com.example.Utils;

public class AdversarialAnalyzer {

    private Utils utils;
    private int bestScore = Integer.MIN_VALUE;

    public void getNextMove() {
//        int score = alphaBetaSearch();
//        if (score > bestScore)it statu


    }

    private int alphaBetaSearch(State state) {
        return maxValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private int maxValue(State state, int alpha, int beta) {
        if (terminalTest(state)) return utility(state);
        int v = Integer.MIN_VALUE;

        return 0;
    }

    private int minValue(State state, int alpha, int beta) {


        return 0;
    }

    private boolean terminalTest(State state) {


        return true;
    }

    private int utility(State state) {


        return 0;
    }


}
