package ga;

import java.util.List;

/**
 * Created by drufener on 8/22/15.
 */
public interface Individual {
    void assignFitness(List<boolean[]> initialConditions, Goal goal);
    float getFitness();

    boolean[] getInitialCondition();

    boolean[] getCurrentState();

    List<boolean[]> getStateHistory();

    Individual breedWith(Individual mate);
}
