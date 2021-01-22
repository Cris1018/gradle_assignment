package ghost;

/** ignorant extends the ghost class.
 * in chase mode: if it is 8 grids away from the target waka, the target location is waka, 
 * otherwise the target location is the bottom left corner.
 * in scatter mode, the target location is the bottom left cornor.
 */
public class Ignorant extends Ghost{

    private static final String SPRITE = "src/main/resources/ignorant.png";

    public Ignorant(int x, int y, String sprite){
        super(x, y, sprite);
    }

    public static String getIgnorantSprite(){
        return SPRITE;
    }

    /** 
     * return the coordinates of the bottom left corner (when its distance from waka is less than or equal to 8 units away from waka),
     * or return waka's position when its distance from waka is more than 8 units away from waka.
     * @param waka the target
     * @return target position
     */
    @Override
    public int[] chaseTarget(Waka waka){
        double square = Math.pow(this.x-waka.getX(), 2)+ Math.pow(this.y-waka.getY(), 2);
        if (Math.pow(square, 0.5)>16*8){
            int[] target = new int[]{waka.getX(), waka.getY()};
            return target;
        }
        return this.findCorners()[2];
    }
    
    @Override
    public int[] scatterTarget(){
        return this.findCorners()[2];
    }
}