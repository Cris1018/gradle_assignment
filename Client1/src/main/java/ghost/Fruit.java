package ghost;

import processing.core.PApplet;

/** fruits are eaten by waka
 * when there are not fruits and super fruits left
 * waka wins
 */
public class Fruit extends Items{

    private static final String SPRITE = "src/main/resources/fruit.png";
    protected boolean isEaten;
    
    public Fruit(int x, int y, String sprite){
        super(x, y, sprite);
    }

    /**
     * only draw the fruits when waka has not eaten them
     */
    @Override
    public void draw(PApplet app){
        if (!isEaten) app.image(this.image, this.x, this.y);
    }

    public void eaten(){
        this.isEaten = true;
    }

    public boolean checkEaten(){
        return this.isEaten;
    }

    public void reset(){
        this.isEaten = false;
    }

    public static String fruitSprite(){
        return SPRITE;
    }
}