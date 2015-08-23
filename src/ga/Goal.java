package ga;

/**
 * Created by drufener on 8/22/15.
 */
public interface Goal {
    boolean satisfactory(Individual individual);

    int getDuration();
}
