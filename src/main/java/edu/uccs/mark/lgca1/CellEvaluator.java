package edu.uccs.mark.lgca1;

/**
 * This class has the responsibility of traversing the grid and evaluating each cell 1 by 1.
 * It is given a domain of responsibility, and only manages that domain.
 * Multiple Cell Evaluators can perform their duty on different sections of the grid
 * at the same time on different threads.
 * Once the evaluation has finished for a given timestep, then the Cell Evaluator will
 * update all the cells--change the updated incomingState to the existing incomingState, in preparation
 * for the next timestep.
 */
public class CellEvaluator {

    private Grid grid;
    private Collider collider;

    private int startX;
    private int endX;
    private int startY;
    private int endY;

    public CellEvaluator(Grid grid, Collider collider, int startX, int endX, int startY, int endY){
        this.grid = grid;
        this.collider = collider;

        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
    }

    public void evaluate(){
        for(int x = startX; x < endX; x++){
            for(int y = startY; y < endY; y++){
                //get the current incomingState of the surrounding neighbors
                byte inBoundState = grid.inboundState(x,y);
                //use collider to determine the new incomingState and set it
                byte nextState = collider.collide(inBoundState);
                //update the next incomingState
                grid.setStateNext(x,y,nextState);
            }
        }
    }

    public void update(){
        for(int x = startX; x < endX; x++){
            for(int y = startY; y < endY; y++){
                //update each cell's current incomingState to it's calculated new incomingState
                grid.updateState(x,y);
            }
        }
    }

}
