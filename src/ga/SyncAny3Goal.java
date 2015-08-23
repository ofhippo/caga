package ga;

import java.util.List;

/**
 * Created by drufener on 8/22/15.
 */
public class SyncAny3Goal implements Goal {
    @Override
    public boolean satisfactory(Individual individual) {
        List<boolean[]> history = individual.getStateHistory();
        //IF A->B->C->D and A==D and A != B (not a fixed point)

        boolean[] stateA = history.get(history.size() - 4);
        boolean[] stateB = history.get(history.size() - 3);
        //boolean[] stateC = history.get(history.size() - 2);
        boolean[] stateD = history.get(history.size() - 1);

        for (int i = 0; i < stateA.length; i++) {
            if (stateA[i] != stateD[i]) {
                return false;
            }
        }

        for (int i = 0; i < stateA.length; i++) {
            if (stateA[i] != stateB[i]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getDuration() {
        return 100;
    }
}
