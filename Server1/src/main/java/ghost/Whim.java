package ghost;

/** Whim extends the ghost class.
 * when in chase mode, its target is to double the vector from chaser to 2 grid spaces ahead of waka.
 * when in scatter mode, its target is the bottom right corner of the map.
 */
public class Whim extends Ghost{

    private static final String SPRITE = "src/main/resources/whim.png";

    public Whim(int x, int y, String sprite){
        super(x, y, sprite);
    }

    public static String getWhimSprite(){
        return SPRITE;
    }

    @Override
    public int[] scatterTarget(){
        return this.findCorners()[3];
    }

    @Override
    public int[] chaseTarget(Waka waka){
        return this.findCorners()[3];
    }

    public int[] chaseTarget(Waka waka, Chaser chaser){
        
        if (waka.getDirection() == null){
            return this.findCorners()[3];
        }

        if (chaser == null){
            return this.findCorners()[3];
        }
        // leftBound, rightBound, upperBound, lowerBound;
        int[] target = new int[2];
        
        if (waka.getDirection().equals(Direction.L)){
            target[0] = 2*(waka.getX()-32) - chaser.getX();
            target[1] = 2*waka.getY() - chaser.getY();
            return target;
        }
        else if (waka.getDirection().equals(Direction.R)){
            target[0] = 2*(waka.getX()+32) - chaser.getX();
            target[1] = 2*waka.getY() - chaser.getY();
            return target;
        }
        else if (waka.getDirection().equals(Direction.U)){
            target[0] = 2*waka.getX() - chaser.getX();
            target[1] = 2*(waka.getY()-32) - chaser.getY();
            return target;
        }
        else {
            target[0] = 2*waka.getX() - chaser.getX();
            target[1] = 2*(waka.getY()+32) - chaser.getY();
            return target;
        }
    }

    public int[] processTarget(Waka waka, Chaser chaser){
    
        int[] target = this.chaseTarget(waka, chaser);
        this.findBounds();
        // target[0] - xVal, target[1] - yVal
        
        if (this.xInBound(target[0]) && this.yInBound(target[1])){
            return target;
        }
        else if (this.xInBound(target[0]) && !this.yInBound(target[1])){
            int num = target[1];
            num = Math.max(num, this.upperBound);
            num = Math.min(num, this.lowerBound);
            target[1] = num;
            return target;
        }
        else if (!this.xInBound(target[0]) && this.yInBound(target[1])){
            int num = target[0];
            num = Math.max(num, this.leftBound);
            num = Math.min(num, this.rightBound);
            target[0] = num;
            return target;
        }
        else {
            // if both x and y are out of bound, just let the corner that is closest to the target be the target
            // 0 -topleft, 1 - topright 2 - bottomleft 3 - bottomright
            int xTarget = target[0];
            int yTarget = target[1];
            if (xTarget<this.leftBound && yTarget<this.upperBound){
                return this.findCorners()[0];
            }
            else if (xTarget>this.rightBound && yTarget<this.upperBound){
                return this.findCorners()[1];
            }
            else if (xTarget<this.leftBound && yTarget>this.lowerBound){
                return this.findCorners()[2];
            }
            else {
                return this.findCorners()[3];
            }
        }   
    }

    public boolean xInBound(int xVal){
        // this.findBounds();
        if (xVal>= this.leftBound && xVal<= this.rightBound){
            return true;
        }
        return false;
    }

    public boolean yInBound(int yVal){
        // this.findBounds();
        if (yVal>= this.upperBound && yVal<= this.lowerBound){
            return true;
        }
        return false;
    }
}