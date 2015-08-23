package ga;

import ca.CellularAutomaton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by drufener on 8/22/15.
 */
public class CellularAutomatonIndividual implements Individual{
    private static final int NUM_MUTATIONS_IN_OFFSPRING = 2;
    CellularAutomaton cellularAutomaton;
    float fitness;
    Goal goal;

    public CellularAutomatonIndividual(CellularAutomaton cellularAutomaton, Goal goal) {
        this.cellularAutomaton = cellularAutomaton;
        this.goal = goal;
    }

    @Override
    public void assignFitness(List<boolean[]> initialConditions, Goal goal) {
        float numCorrect = 0;
        for (boolean[] initialCondition : initialConditions) {
            cellularAutomaton.reset(initialCondition);

            cellularAutomaton.run(goal.getDuration());

            if (goal.satisfactory(this)) {
                numCorrect++;
            }
        }

       this.fitness = numCorrect / initialConditions.size();
    }

    @Override
    public Individual breedWith(Individual mate) {
        CellularAutomatonIndividual mateIndividual = (CellularAutomatonIndividual) mate;

        int ruleSize = getRule().size();

        HashMap<List<Boolean>, Boolean> newRule = new HashMap<>();

        Random rand = new Random();
        int numGenesFromSelf = rand.nextInt(ruleSize);

        Set<List<Boolean>> keys = getRule().keySet();

        int j = 0;
        List<Integer> mutationPoints = new ArrayList<>();

        for (int i = 0; i < NUM_MUTATIONS_IN_OFFSPRING; i++) {
            mutationPoints.add(rand.nextInt(keys.toArray().length));
        }

        for (List<Boolean> key : keys) {
            boolean updateState;
            if (j < numGenesFromSelf) {
                updateState = this.getRule().get(key);
            } else {
                updateState = mateIndividual.getRule().get(key);
            }
            if (mutationPoints.contains(j)) {
                newRule.put(key, !updateState);
            } else {
                newRule.put(key, updateState);
            }

            j++;
        }

        CellularAutomaton offspringCA = new CellularAutomaton(newRule);
        return new CellularAutomatonIndividual(offspringCA, goal);
    }

    @Override
    public float getFitness() {
        return fitness;
    }

    @Override
    public boolean[] getInitialCondition() {
        return cellularAutomaton.initialState;
    }

    @Override
    public boolean[] getCurrentState() {
        return cellularAutomaton.currentState;
    }

    @Override
    public List<boolean[]> getStateHistory() {
        return cellularAutomaton.stateHistory;
    }

    public HashMap<List<Boolean>, Boolean> getRule() {
        return cellularAutomaton.rule;
    }
}
