package ga;

import ca.CellularAutomaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by drufener on 8/22/15.
 */
public class CellularAutomataEvolver {

    static final int POP_SIZE = 100;
    static final int NUM_ELITE_SURVIVORS = 20;
    static final int RUNS_PER_GENERATION = 100;
    static final boolean TRANSITION_IC_SAMPLE_DIST = false;
    static final Goal goal = new MajorityGoal();//Sync2Goal SyncAny2Goal SyncAny3 SyncAny3Goal MajorityGoal
    static int genNum = 0;

    static CellularAutomatonIndividual[] population = new CellularAutomatonIndividual[POP_SIZE];

    public static void main(String[] args) throws Exception {
        //create initial population
        for (int i = 0; i < POP_SIZE; i++) {
            CellularAutomaton ca = new CellularAutomaton(CellularAutomaton.getRandomRule(7));
            population[i] = new CellularAutomatonIndividual(ca, goal);
        }

        while (true) {
            //create RUNS_PER_GENERATION initial conditions
            List<boolean[]> initialConditions = new ArrayList<>();
            for (int i = 0; i < RUNS_PER_GENERATION; i++) {
                   if (TRANSITION_IC_SAMPLE_DIST) {
                       if (i < genNum) {
                           initialConditions.add(CellularAutomaton.getUnbiasedRandomState());//hard
                       } else {
                           initialConditions.add(CellularAutomaton.getUniformRandomState());//easy
                       }
                   } else {
                       initialConditions.add(CellularAutomaton.getUniformRandomState());
                   }
            }

            //caclulate fitness for each individual
            for (CellularAutomatonIndividual cai : population) {
                cai.assignFitness(initialConditions, goal);
            }

            //sort by finess
            Arrays.sort(population, new Comparator<CellularAutomatonIndividual>() {
                public int compare(CellularAutomatonIndividual o1, CellularAutomatonIndividual o2) {
                    return Math.round(100 * o2.fitness) - Math.round(100 * o1.fitness);
                }
            });

            report();

            //take NUM_ELITE_SURVIVORS strongest parents
            CellularAutomatonIndividual[] survivors = Arrays.copyOfRange(population, 0, NUM_ELITE_SURVIVORS);

            CellularAutomatonIndividual[] nextGeneration = new CellularAutomatonIndividual[POP_SIZE];
            //create 80 children by breeding parents (single point crossover from parents, with replacement)
            //  and double point mutation
            int numChildren = POP_SIZE - NUM_ELITE_SURVIVORS;
            Random rand = new Random();
            for (int i = 0; i < numChildren; i++) {
                CellularAutomatonIndividual mate1 = survivors[rand.nextInt(survivors.length)];
                CellularAutomatonIndividual mate2 = survivors[rand.nextInt(survivors.length)];
                nextGeneration[i] = (CellularAutomatonIndividual) mate1.breedWith(mate2);
            }

            for (int i = 0; i < survivors.length; i++) {
                nextGeneration[i + numChildren] = survivors[i];
            }

            population = nextGeneration;
            genNum++;

            if (population.length != POP_SIZE) {
                throw new Exception("Population size changed!");
            }
        }
    }

    public static void report() {
        CellularAutomatonIndividual fittest = population[0];
        System.out.println("");
        System.out.println("----------------- Gen " + genNum + " -----------------------");
        System.out.println("");
        final int numRuns = 3;
        for (int i = 0; i < numRuns; i++) {
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("Gen: " + genNum);
            System.out.println("Fittest Fitness: " + fittest.fitness);
            System.out.println("Example " + (i + 1) + " of " + numRuns + " Uniform Runs");
            fittest.cellularAutomaton.reset(CellularAutomaton.getUniformRandomState());
            fittest.cellularAutomaton.run(300, true);
            System.out.println("");
            System.out.println("");
            System.out.println("");
        }

        for (int i = 0; i < numRuns; i++) {
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("Gen: " + genNum);
            System.out.println("Fittest Fitness: " + fittest.fitness);
            System.out.println("Example " + (i + 1) + " of " + numRuns + " Unbiased Runs");
            fittest.cellularAutomaton.reset(CellularAutomaton.getUnbiasedRandomState());
            fittest.cellularAutomaton.run(300, true);
            System.out.println("");
            System.out.println("");
            System.out.println("");
        }
    }


}
