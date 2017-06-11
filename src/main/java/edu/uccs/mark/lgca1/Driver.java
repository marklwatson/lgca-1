package edu.uccs.mark.lgca1;

import java.util.ArrayList;

import static edu.uccs.mark.lgca1.State.ALL_POSSIBLE_STATES;
import static edu.uccs.mark.lgca1.State.S1MASK;
import static edu.uccs.mark.lgca1.State.S4MASK;

/**
 * Created by Mark on 6/10/2017.
 */
public class Driver {

    public static void main(String[] args){
        Driver d = new Driver();
        d.runSimulation1();
    }

    public void runSimulation1(){
        //Set up 10*10 grid and initialize collider
        Grid g = new Grid(10, 10);
        Collider c = new Collider();
        c.preCalculateCollisions();
        GridProbe gp = new GridProbe();

        //Print out all determined collision possibilities
//        for(byte b:ALL_POSSIBLE_STATES){
//            gp.printCollisionPossibilities(b, c.getCollisionPosibilities(b));
//        }

        //Set up particles on the left flowing to the right
        for(int y = 0; y < 10; y++){
            g.setStateNow(0, y, S1MASK);
        }
        //Set up particles on the right flowing to the left
        for(int y = 0; y < 10; y++){
            g.setStateNow(8, y, S4MASK);
        }

        //Print out starting grid
        System.out.println("Start Grid");
        gp.probeGrid(g);

        //Set up a single evaluator traversing the whole grid
        CellEvaluator ce = new CellEvaluator(g, c, 0, 10, 0, 10);

        //Evaluate the grid for t = 1 to t = 9
        for(int t = 1; t < 10; t++){
            ce.evaluate();
            ce.update();

            //Print out the results of the timestep
            System.out.println("\nGrid at time " + t);
            gp.probeGrid(g);
        }

        //Print out the final grid
        System.out.println("\nFinal Grid");
        gp.probeGrid(g);
    }

}
