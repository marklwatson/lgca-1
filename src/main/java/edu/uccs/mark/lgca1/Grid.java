package edu.uccs.mark.lgca1;

import static edu.uccs.mark.lgca1.State.NULLSTATE;
import static edu.uccs.mark.lgca1.State.S1MASK;
import static edu.uccs.mark.lgca1.State.SMASKS;

/**
 * Grid of width (x) by height (y) cells arranged in a hexagonal pattern.
 *
 * Hexagonality is realized by shifting odd numbered rows (y) to the right by .5 of the "distance" between them.
 * For instance
 * Row [2]   (0,2)     (1,2)
 * Row [1]        (0,1)     (1,1)
 * Row [0]   (0,0)     (1,0)
 * So that the upper left neighbor of (1,0) is (0,1) which would be (x-1,y+1) since it is on an odd numbered row.
 * But the upper left neighbor of (0,1) is (0,2) which would be (x,y+1) since it is on an even numbered row.
 *
 * At each location there is a 2 element array of bytes.
 * Element 0 is the incomingState of the cell as it is now.  Element 1 is the incomingState the cell will be on the next time stamp
 */
public class Grid {

    private int xWidth = 10;
    private int yHeight = 10;

    private byte[][][] tGrid;

    private static final byte[] NULLCELL = new byte[]{NULLSTATE,NULLSTATE};

    public Grid(int width, int height){
        this.xWidth = width;
        this.yHeight = height;
        buildGrid();
    }

    public int getxWidth() {
        return xWidth;
    }

    public int getyHeight(){
        return yHeight;
    }

    /**
     * Builds completely empty grid based on width and height
     */
    private void buildGrid(){
        tGrid = new byte[xWidth][yHeight][2];
        for(int x=0;x<xWidth;x++){
            for(int y=0;y<yHeight;y++){
                tGrid[x][y][0]=NULLSTATE;
                tGrid[x][y][1]=NULLSTATE;
            }
        }
    }

    /**
     * Returns the specified cell at the coordinates.
     * If the index is out of bounds, it returns a null state.
     * Possibly will re-implement this.
     * @param x
     * @param y
     * @return
     */
    private byte[] cell(int x, int y){
        if(x>=xWidth) return NULLCELL; //right wall
        if(y>=yHeight) return NULLCELL;  //ceiling
        if(x<0) return NULLCELL;  // left wall
        if(y<0) return NULLCELL; // floor
        return tGrid[x][y];
    }

    /**
     * Returns the neighbor to a given cell.
     *
     * This grid is a simple 2D array.
     * However the grid is supposed to represent a hexagonal configuration.
     * The neighbor index is 1-6, starting where 1 -> pi=0, 2 -> pi/3, 3 -> 2pi/3, ..., 6 -> 5pi/3
     *
     * Because of this, retrieving the neighbor is complex (see class level comment)>
     * This method handles the complexity.
     *
     * @param neighborIndex
     * @param x
     * @param y
     * @return
     */
    public byte[] neighbor(int neighborIndex, int x, int y){
        switch(neighborIndex){
            //  1 -> (x+1, y)
            case 1: return cell(x+1, y);
            //  2 -> if y is even (x, y+1)   if y is odd (x+1, y+1)
            case 2: return ( (y & 1) == 0 )?cell(x,y+1):cell(x+1,y+1);
            //  3 -> if y is even (x-1, y+1) if y is odd (x, y+1)
            case 3: return ( (y & 1) == 0 )?cell(x-1,y+1):cell(x, y+1);
            //  4 -> (x-1, y)
            case 4: return cell(x-1,y);
            //  5 -> if y is even (x-1, y-1) if y is odd (x, y-1)
            case 5: return ( (y & 1) == 0 )?cell(x-1,y-1):cell(x,y-1);
            //  6 -> if y is even (x, y-1)   if y is odd (x+1, y-1)
            case 6: return ( (y & 1) == 0 )?cell(x,y-1):cell(x+1,y-1);
            default: return NULLCELL;
        }
    }

    public byte getStateNow(int x, int y){
        return cell(x,y)[0];
    }

    public byte getStateNext(int x, int y){
        return cell(x,y)[1];
    }

    public void setStateNext(int x, int y, byte state){
        cell(x,y)[1]=state;
    }

    public void setStateNow(int x, int y, byte state){
        cell(x,y)[0]=state;
    }

    /**
     * Moves the future state to the current state, and nulls the future state.
     * Use this method before moving on to the next timestep.
     * @param x
     * @param y
     */
    public void updateState(int x, int y){
        byte[] c = cell(x,y);
        c[0] = c[1];
        c[1] = NULLSTATE;
    }

    public byte neighborNow(int neighborIndex, int x, int y){
        return neighbor(neighborIndex, x, y)[0];
    }

    public byte neighborNext(int neighborIndex, int x, int y){
        return neighbor(neighborIndex, x, y)[1];
    }

    /**
     * For the specified cell, determines the future state of the next cell WITHOUT taking into
     * account collisions.
     * In this case, it gets
     *    the 1 occupation number from neighbor 4
     *    the 2 occupation number from neighbor 5
     *    the 3 occupation number from neighbor 6
     *    the 4 occupation number from neighbor 1
     *    the 5 occupation number from neighbor 2
     *    the 6 occupation number from neighbor 3
     *
     *  This is the incoming state that should be passed to the collider to determine the new state.
     *
     * @param x
     * @param y
     * @return
     */
    public byte inboundState(int x, int y){
        byte b = NULLSTATE;
        int s = 0;
        byte nn;
        for(int i = 1;i<=6;i++){
            s = (i>3)?i - 3:i+3;
            nn = neighborNow(i, x, y);
            b |= nn & SMASKS[s-1];
        }
        return b;
    }

}
