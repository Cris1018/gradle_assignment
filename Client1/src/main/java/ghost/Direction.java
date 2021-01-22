package ghost;

/** this class includes all the possible directions of waka and ghosts.
 *
 */
public enum Direction{
    L, R, U, D;

    @Override
    public String toString(){
        if (this == L){
            return "l";
        }
        else if (this == R){
            return "r";
        }
        else if (this == U){
            return "u";
        }
        else{
            return "d";
        }
    }
}