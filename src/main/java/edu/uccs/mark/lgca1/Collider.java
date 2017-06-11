package edu.uccs.mark.lgca1;

import java.util.ArrayList;
import java.util.Random;

import static edu.uccs.mark.lgca1.State.ALL_POSSIBLE_STATES;
import static edu.uccs.mark.lgca1.State.SMASKS;

/**
 * This class manages all collisions.
 * A collision is determined based on the incoming incomingState--i.e. all of the particles coming into
 * the cell from its neighbors.
 *
 * Physically, all particles that come in will collide with each other.
 * Their new trajectories will follow conservation of momentum.
 * Here the mass and velocity are assumed to be unity.
 *
 * Most collisions will result in a singe possible trajectory.
 * Some of them will have multiple trajectory possibilities, and one will be chosen at random.
 */
public class Collider {

    /**
     * Holds a single entry of an incoming incomingState and it's possible trajectories
     */
    private static class StateMapEntry {
        byte incomingState;
        byte[] resultingState;
    }

    /**
     * Holds a mapping of all possible states with their possible collisions.
     */
    private StateMapEntry[] collisions = new StateMapEntry[64];

    /**
     * Random number generator
     */
    private Random rand = new Random();

    /**
     * Uses the list of all possible states to populate the collision map
     * comparing each one with the entire list using conservation of momentum.
     */
    public void preCalculateCollisions(){
        for(int i = 0; i < ALL_POSSIBLE_STATES.length; i++){
            collisions[i] = new StateMapEntry();
            collisions[i].incomingState = ALL_POSSIBLE_STATES[i];
            collisions[i].resultingState = calculateAllPossibleOutboundStates(ALL_POSSIBLE_STATES[i], ALL_POSSIBLE_STATES);
            //collisions[i].print();
        }
    }

    /**
     * Determines all possible outbound states given an inbound state using conservation of momentum.
     *
     * @param state
     * @param allPossible
     * @return
     */
    private byte[] calculateAllPossibleOutboundStates(byte state, byte[] allPossible){
        int stateNumParticles = calcNumParticlesInState(state);
        int[] stateMomentum = calculateMomentum(state);
        ArrayList<Byte> possibleStates = new ArrayList<>();
        for(byte poss:allPossible){
            int possNumPart = calcNumParticlesInState(poss);
            if(possNumPart==stateNumParticles){
                int[] possMomentum = calculateMomentum(poss);
                if(compareMomenta(possMomentum, stateMomentum)){
                    possibleStates.add(poss);
                }
            }
        }
        byte[] ret = new byte[possibleStates.size()];
        int i = 0;
        for(Byte b:possibleStates){
            ret[i++] = b.byteValue();
        }
        return ret;
    }

    /**
     * Quick utility method to test equality of momentum vectors.
     * @param a
     * @param b
     * @return
     */
    private boolean compareMomenta(int[] a, int[] b){
        return ((a[0] == b[0]) &&
                (a[1]) == b[1]) ;
    }

    private static final int xm[] = new int[]{100, 50, -50, -100, -50, 50};
    private static final int ym[] = new int[]{0, 87, 87, 0, -87, -87};

    /**
     * Calculates the total momentum of the given state in order to compare it to possible states.
     *
     * This method has 2 implementations.
     *
     * The purest one uses cos() and sin().  It is much more general and can be used for more than a
     * hexagonal grid (more than 6 directions) provided the grid is still 2D.
     * However, it is slower, and it returns floating point numbers which are difficult to compare
     * with other calculations.
     *
     * Therefore, the actual implementation uses the results of sin() and cos() for a hexagonal grid,
     * which in this case is npi/3 where n = 0-5, and multiplies the results by 100;
     * The precalculation means it is only useful for a hexagonal 2D grid, but it is much faster
     * and it doesn't have floating point comparison problems.
     *
     * @param state
     * @return
     */
    private int[] calculateMomentum(byte state){
        //pull out each particle.

        //each particle has an x and y momentum based on its direction
        //1  x = 1, y = 0
        //2  x = cos(pi/3), y = sin(pi/3)
        //3  x = cos(2pi/3), y = sin(2pi/3)
        //4  x = -1, y = 0
        //5  x = cos(4pi/3), y = sin(4pi/3)
        //6  x = cos(5pi/3), y = sin(5pi/3)


//        double x = 0.0;
//        double y = 0.0;
//
//        double arg = 0.0;
//        for(int n = 0; n < SMASKS.length; n++){
//            if((incomingState & SMASKS[n]) == SMASKS[n]){
//                arg = (n*Math.PI)/3;
//                x += BigDecimal.valueOf(Math.cos(arg)).setScale(5, BigDecimal.ROUND_DOWN).doubleValue();
//                y += BigDecimal.valueOf(Math.sin(arg)).setScale(5, BigDecimal.ROUND_DOWN).doubleValue();
//            }
//        }
//        return new double[]{x, y};

        //each particle has an x and y momentum based on its direction
        //1  x =  100, y =  0000
        //2  x =  050, y =  087
        //3  x = -050, y =  087
        //4  x = -100, y =  000
        //5  x = -050, y = -087
        //6  x =  050, y = -087

        int x = 0;
        int y = 0;

        for(int n = 0; n < SMASKS.length; n++){
            if((state & SMASKS[n]) == SMASKS[n]){
                x += xm[n];
                y += ym[n];
            }
        }
        return new int[]{x, y};
    }

    /**
     * Given a state, determines the number of particles using bitwise operators.
     * @param state
     * @return
     */
    private int calcNumParticlesInState(byte state){
        //use bitwise operators to determine the number of particles in the incomingState
        int count = 0;
        for(int n = 0; n < SMASKS.length; n++){
            if((state & SMASKS[n]) == SMASKS[n]){
                count++;
            }
        }
        return count;
    }

    /**
     * Returns a randomly chosen state from a set of possible states based on the inBoundState.
     * This is the implementation of the Collision Operator which is based on the conservation of momentum.
     *
     * @param inBoundState
     * @return
     */
    public byte collide(byte inBoundState){
        byte[] poss = collisions[(int)inBoundState].resultingState;
        if(poss.length==1){
            return poss[0];
        }
        else{
            int ndx = rand.nextInt(poss.length - 1);
            return poss[ndx];
        }
    }

    public byte[] getCollisionPosibilities(byte inBoundState){
        return collisions[(int)inBoundState].resultingState;
    }


}
