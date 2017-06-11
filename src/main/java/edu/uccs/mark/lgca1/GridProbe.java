package edu.uccs.mark.lgca1;

import static edu.uccs.mark.lgca1.State.SMASKS;

/**
 * Prints out the contents of the grid to the System Console.
 */
public class GridProbe {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    /**
     * Prints the numbers representing the states
     * @param g
     */
    public void probeGrid(Grid g){
        int x0 = 0; int xmax = g.getxWidth();
        int y0 = 0; int ymax = g.getyHeight();

        GridProbe.StatePrinter sp = new GridProbe.StatePrinter();
        for(int y = ymax - 1; y >= y0; y--){
            if(y%2==1){
                sp.space();
                sp.space();
            }
            sp.bar();
            for(int x= x0; x < xmax; x++){
                sp.addState(g.getStateNow(x,y));
                sp.bar();
            }
            sp.print();
        }
    }

    /**
     * Prints the possible collisions for a given state using ascii art
     * @param state
     * @param possibleCollisions
     */
    public void printCollisionPossibilities(byte state, byte[] possibleCollisions){
        GridProbe.StatePrinter sp = new GridProbe.StatePrinter();
        sp.bar();
        sp.addState(state);
        sp.bar();

        sp.message("Possibilities");
        for(int j=0;j<possibleCollisions.length;j++){
            sp.bar();
            sp.addState(possibleCollisions[j]);
        }
        sp.bar();
        sp.print();
        System.out.println("");
    }

    private static class StatePrinter{
        StringBuilder lineOne = new StringBuilder();
        StringBuilder lineTwo = new StringBuilder();

        public void space(){
            lineOne.append("  ");
            lineTwo.append("  ");
        }

        public void bar(){
            lineOne.append(ANSI_YELLOW).append("  |  ").append(ANSI_WHITE);
            lineTwo.append(ANSI_YELLOW).append("  |  ").append(ANSI_WHITE);
        }

        public void message(String msg){
            lineOne.append(" ").append(msg).append(" ");
            lineTwo.append("  ");
            for(int i = 0; i < msg.length(); i++){
                lineTwo.append(' ');
            }
        }

        public void addState(byte state){
            //    _\/_    [1] 1234
            //     /\     [2] 1234
            //  4 by 2 characters
            // if 1 then 1,4 = _
            // if 2 then 1,3 = /
            // if 3 then 1,2 = \
            // if 4 then 1,1 = _
            // if 5 then 2,2 = /
            // if 6 then 2,3 = \
            // 2,1 and 2.4 are always space
            char[] one = new char[4];
            one[0] = one[1] = one[2] = one[3] = ' ';
            char[] two = new char[4];
            two[0] = two[1] = two[2] = two[3] = ' ';
            for(int n = 1; n <= SMASKS.length; n++){
                if((state & SMASKS[n-1]) == SMASKS[n-1]){
                    switch(n){
                        case 1: one[4-1] = '_'; break;
                        case 2: one[3-1] = '/'; break;
                        case 3: one[2-1] = '\\'; break;
                        case 4: one[1-1] = '_'; break;
                        case 5: two[2-1] = '/'; break;
                        case 6: two[3-1] = '\\'; break;
                    }
                }
            }

            lineOne.append(new String(one));
            lineTwo.append(new String(two));
        }

        public void print(){
            System.out.println(lineOne.toString());
            System.out.println(lineTwo.toString());
            lineOne = new StringBuilder();
            lineTwo = new StringBuilder();
        }
    }





}
