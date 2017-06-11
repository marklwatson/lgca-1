package edu.uccs.mark.lgca1;

/**
 * Constants for different useful states.
 */
public class State {

    public static byte[] ALL_POSSIBLE_STATES = new byte[64];

    static{
        for(int i = 0; i < ALL_POSSIBLE_STATES.length; i++){
            ALL_POSSIBLE_STATES[i] = (byte)i;
        }

    }

    public static final byte NULLSTATE = (byte)0;  //00 000000

    public static final byte S1MASK = (byte)1;   //00 00 0001
    public static final byte S2MASK = (byte)2;   //00 00 0010
    public static final byte S3MASK = (byte)4;   //00 00 0100
    public static final byte S4MASK = (byte)8;   //00 00 1000
    public static final byte S5MASK = (byte)16;  //00 01 0000
    public static final byte S6MASK = (byte)32;  //00 10 0000

    public static final byte[] SMASKS = new byte[]{S1MASK,S2MASK,S3MASK,S4MASK,S5MASK,S6MASK};


}
