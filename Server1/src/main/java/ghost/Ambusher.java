package ghost;

/** Ambusher extends the ghost class.
 * chase mode - 4 grid spaces ahead of waka based on waka's current direction.
 * if waka's current direction is null, it will be 4 grids left.
 * scatter mode - top right corner.
 */
public class Ambusher extends Ghost{

    private static final String SPRITE = "src/main/resources/ambusher.png";

    public Ambusher(int x, int y, String sprite){
        super(x, y, sprite);
    }

    public static String getAmbusherSprite(){
        return SPRITE;
    }
    
    /**
     * if waka has a direction, the target is 4 grids spaces ahead of waka based on waka's current direction.
     * if waka's direction is null, the target is 4 grids left to the waka.
     * @param waka the target
     * @return x and y position of target
     */
    @Override
    public int[] chaseTarget(Waka waka){
        int[] target= new int[2];
        
        this.findBounds();
        
        if (waka.getDirection() == null){
            target[0] = Math.max(waka.getX()-64, this.leftBound);
            target[1] = Math.max(waka.getY(), this.upperBound);
            return target;
        }

        if (waka.getDirection().equals(Direction.L)){ 
            target[0] = Math.max(waka.getX()-64, this.leftBound);
            target[1] = waka.getY();
            return target;
        }
        else if (waka.getDirection().equals(Direction.R)){
            target[0] = Math.min(waka.getX()+64, this.rightBound);
            target[1] = waka.getY();
            return target;
        }
        else if (waka.getDirection().equals(Direction.U)){
            target[0] = waka.getX();
            target[1] = Math.max(waka.getY()-64, this.upperBound);
            return target;
        }
        else {
            target[0] = waka.getX();
            target[1] = Math.min(waka.getY()+64, this.lowerBound);
            return target;
        }
    }
    
    /**
     * @return x and y position of top right corner
     */
    @Override
    public int[] scatterTarget(){
        // top right
        return this.findCorners()[1];
    }
}