package ca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by drufener on 8/16/15.
 */
public class CellularAutomaton {
    public static final int RULE = 102;
    public static final int WORLD_SIZE = 149;
    public static final boolean RANDOM_INITIAL_CONDITION = false;
    private static final boolean DEBUG = false;

    public final HashMap<List<Boolean>, Boolean> rule;
    final int neighborhoodRadius;
    int time;
    public boolean[] currentState;
    public List<boolean[]> stateHistory;
    public boolean[] initialState;

    public static void main(String[] args) {
        boolean[] initialState = RANDOM_INITIAL_CONDITION ? getUnbiasedRandomState() : getOneBlackCellInitialState();
        CellularAutomaton ca = new CellularAutomaton(getElementaryRuleFromWolframNumber(RULE));
        ca.reset(initialState);
        ca.run(1000);
    }

    public static boolean[] getOneBlackCellInitialState() {
        boolean[] initialState = new boolean[WORLD_SIZE];
        for (int i = 0; i < WORLD_SIZE; i++) {
            initialState[i] = i == WORLD_SIZE / 2;
        }
        return initialState;
    }

    public static boolean[] getUnbiasedRandomState() {
        boolean[] initialState = new boolean[WORLD_SIZE];
        for (int i = 0; i < WORLD_SIZE; i++) {
            initialState[i] = Math.random() < 0.5;
        }
        return initialState;
    }


    public static boolean[] getUniformRandomState() {
        boolean[] initialState = new boolean[WORLD_SIZE];

        Random rand = new Random();
        int numBlack = rand.nextInt(WORLD_SIZE);
        for (int i = 0; i < WORLD_SIZE - numBlack; i++) {
            initialState[i] = true;
        }

        for (int i = WORLD_SIZE - numBlack; i < WORLD_SIZE; i++) {
            initialState[i] = false;
        }

        shuffleArray(initialState);

        return initialState;
    }

    private static void shuffleArray(boolean[] array)//http://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
    {
        int index;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            if (index != i)
            {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
    }

    public static HashMap<List<Boolean>, Boolean> getElementaryRuleFromWolframNumber(int n) {
        HashMap<List<Boolean>, Boolean> results = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            List<Boolean> key = new ArrayList<>();
            for(int j = 0; j < 3; j++) {
                key.add(((int) Math.pow(2, j) & i) == Math.pow(2, j));
            }
            results.put(key, (((int) Math.pow(2, i)) & n) == (int) Math.pow(2, i));
        }
        return results;
    }

    public static HashMap<List<Boolean>, Boolean> getRandomRule(int neighborhoodSize) {
        HashMap<List<Boolean>, Boolean> results = new HashMap<>();
        Random rand = new Random();
        for (int i = 0; i < Math.pow(2, neighborhoodSize); i++) {
            List<Boolean> key = new ArrayList<>();
            for(int j = 0; j < neighborhoodSize; j++) {
                key.add(((int) Math.pow(2, j) & i) == Math.pow(2, j));
            }
            results.put(key, rand.nextBoolean());
        }
        return results;
    }

    public CellularAutomaton(HashMap<List<Boolean>, Boolean> rule) {
        this.rule = rule;
        this.neighborhoodRadius = (((List<Boolean>) rule.keySet().toArray()[0]).size() - 1) / 2;
    }

    public void reset(boolean[] initialState) {
        this.time = 0;
        this.stateHistory = new ArrayList<>();
        this.initialState = initialState;
        this.currentState = initialState;
    }

    public void run(int duration) {
        run(duration, false);
    }

    public void run(int duration, boolean print) {
        for (int i = 0; i < duration; i++) {
            stateHistory.add(this.currentState);
            if (i > 0 && i % 10 == 0) {
                boolean matchesLast = true;
                for (int j = 0; j < stateHistory.get(0).length; j++) {
                    if (stateHistory.get(i)[j] != stateHistory.get(i - 1)[j]) {
                        matchesLast = false;
                        break;
                    }
                }
                if (matchesLast) {
                    if (print || DEBUG) printCurrentState();
                    return;
                }
            }
            if (print || DEBUG) printCurrentState();
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
        List<Boolean> neighbors = new ArrayList<>();
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
