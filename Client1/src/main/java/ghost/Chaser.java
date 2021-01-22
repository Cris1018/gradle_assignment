package ghost;

/** Chaser extends the ghost class.
 * chase mode - waka's position.
 * scatter mode - top left corner.
 */
public class Chaser extends Ghost{

    private static final String SPRITE = "src/main/resources/chaser.png";

    public Chaser(int x, int y, String sprite){
        super(x, y, sprite);
    }

    public static String getChaserSprite(){
        return SPRITE;
    }

    /** method to find the chase mode target.
     * @param waka the target
     * @return waka's position
     */
    @Override
    public int[] chaseTarget(Waka waka){
        int[] target = new int[]{waka.getX(), waka.getY()};
        return target;
    }

    /** method to find the scatter mode target.
     * @return the top left corner's x and y position 
     */
    @Override
    public int[] scatterTarget(){
        // top left corner
        return this.findCorners()[0];
    }
}