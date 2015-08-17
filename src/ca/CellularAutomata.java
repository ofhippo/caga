package ca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by drufener on 8/16/15.
 */
public class CellularAutomata {
    public static final int RULE = 102;
    public static final int WORLD_SIZE = 100;
    public static final int DURATION = 1000;
    public static final boolean RANDOM_INITIAL_CONDITION = false;

    final boolean[] initialState;
    final HashMap<List<Boolean>, Boolean> rule;
    final int neighborhoodRadius;
    int time;
    boolean[] currentState;

    public static void main(String[] args) {
        boolean[] initialState = new boolean[WORLD_SIZE];
        for (int i = 0; i < WORLD_SIZE; i++) {
            if (RANDOM_INITIAL_CONDITION) {
                initialState[i] = Math.random() < 0.5;
            } else {
                initialState[i] = i == WORLD_SIZE / 2;
            }
        }
        CellularAutomata ca = new CellularAutomata(getElementaryRuleFromWolframNumber(RULE), initialState);
        ca.run(DURATION);
    }

    static HashMap<List<Boolean>, Boolean> getElementaryRuleFromWolframNumber(int n) {
        HashMap<List<Boolean>, Boolean> results = new HashMap<List<Boolean>, Boolean>();
        for (int i = 0; i < 8; i++) {
            List<Boolean> key = new ArrayList<Boolean>();
            for(int j = 0; j < 3; j++) {
                key.add(((int) Math.pow(2, j) & i) == Math.pow(2, j));
            }
            results.put(key, (((int) Math.pow(2, i)) & n) == (int) Math.pow(2, i));
        }
        return results;
    }

    public CellularAutomata(HashMap<List<Boolean>, Boolean> rule, boolean[] initialState) {
        this.rule = rule;
        this.initialState = initialState;
        this.currentState = initialState;
        this.time = 0;
        this.neighborhoodRadius = (((List<Boolean>) rule.keySet().toArray()[0]).size() - 1) / 2;
    }

    public void run(int duration) {
        for(int i = 0; i < duration; i++) {
            printCurrentState();
            this.currentState = applyRule(this.currentState);
        }
    }

    public boolean[] applyRule(boolean[] state) {
        boolean[] resultState = state.clone();
        for (int i = 0; i < state.length; i++) {
            resultState[i] = rule.get(getNeighborhood(state, i));
        }
        return resultState;
    }

    List<Boolean> getNeighborhood(boolean[] state, int index) {
        List<Boolean> neighbors = new ArrayList<Boolean>();
        for (int i = 1; i <= neighborhoodRadius; i++) {
            neighbors.add(state[((index - i) + state.length) % state.length]);
        }
        neighbors.add(state[index]);
        for (int j = 1; j <= neighborhoodRadius; j++) {
            neighbors.add(state[((index + j) + state.length) % state.length]);
        }
        return neighbors;
    }

    void printCurrentState() {
        System.out.println(booleanArrayToString(this.currentState));
    }

    static String booleanArrayToString(boolean[] a) {
        String s = "";
        for (boolean b : a) {
            s += b ? "#" : " ";
        }
        return s;
    }
}
