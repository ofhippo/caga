package ga;

import java.util.List;

/**
 * Created by drufener on 8/22/15.
 */
public class SyncAny2Goal implements Goal {
    @Override
    public boolean satisfactory(Individual individual) {
        List<boolean[]> history = individual.getStateHistory();
        boolean[] stateA = history.get(history.size() - 3);
        boolean[] stateB = history.get(history.size() - 2);
        boolean[] stateC = history.get(history.size() - 1);

        for (int i = 0; i < stateA.length; i++) {
            if (stateA[i] != stateC[i]) {
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
