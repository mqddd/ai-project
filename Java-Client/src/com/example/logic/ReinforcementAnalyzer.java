package com.example.logic;

import com.example.BaseAgent;
import com.example.Utils;
import com.example.model.Tile;
import com.example.model.TileType;

import java.util.HashMap;

public class ReinforcementAnalyzer {

    private final Utils utils;
    private final static int EPISODES = 1000;
    private final static double LEARNING_RATE = 0.1;
    private final static double DISCOUNT_FACTOR = 0.95;
    private final static int NUMBER_OF_ACTIONS = 5;
    private int[][][] q_table;
    private HashMap<BaseAgent.Action, Integer> actionValueMap;
    private String[][] grid;

    public ReinforcementAnalyzer(Utils utils) {
        this.utils = utils;
        this.grid = utils.gridDeepCopy(this.utils.getAgent().getGrid());
        this.initActionValueMap();
        this.initQTable();
    }

    public BaseAgent.Action train() {

    }

    private void initActionValueMap() {
        this.actionValueMap = new HashMap<>();
        for (int i = 0; i < BaseAgent.Action.values().length; i++) {
            BaseAgent.Action action = BaseAgent.Action.values()[i];
            if (action.equals(BaseAgent.Action.Up))
                this.actionValueMap.put(action, 0);
            else if (action.equals(BaseAgent.Action.Down))
                this.actionValueMap.put(action, 1);
            else if (action.equals(BaseAgent.Action.Right))
                this.actionValueMap.put(action, 2);
            else if (action.equals(BaseAgent.Action.Left))
                this.actionValueMap.put(action, 3);
            else if (action.equals(BaseAgent.Action.Teleport))
                this.actionValueMap.put(action, 4);
        }
    }

    private void initQTable() {
        int height = this.utils.getAgent().getGridHeight();
        int width = this.utils.getAgent().getGridWidth();
        this.q_table = new int[height][width][NUMBER_OF_ACTIONS];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < NUMBER_OF_ACTIONS; k++) {
                    TileType type = this.utils.getTileType(i, j, this.grid);
                    Tile tile = new Tile(i, j, type);
                    if (this.isActionPossible(k, this.grid, tile)) {
                        this.q_table[i][j][k] = 0;
                    } else
                        this.q_table[i][j][k] = Integer.MIN_VALUE;
                }
            }
        }
    }

    private boolean isActionPossible(int actionValue, String[][] grid, Tile tile) {
        if (actionValue == 0) {
            return this.utils.isUpActionValid(tile, grid);
        } else if (actionValue == 1) {
            return this.utils.isDownActionValid(tile, grid);
        } else if (actionValue == 2) {
            return this.utils.isRightActionValid(tile, grid);
        } else if (actionValue == 3) {
            return this.utils.isLeftActionValid(tile, grid);
        } else if (actionValue == 4) {
            return this.utils.isTeleportActionValid(tile, grid);
        } else throw new IllegalStateException("action value is invalid!");
    }

    // validation of action is checked before
    private String[][] updateGridByUpAction(String[][] grid, Tile source) {
        
    }

}
