package ghost;

import processing.core.PApplet;

public class SuperFruit extends Fruit{

    private static final String SPRITE = "src/main/resources/SuperF.jpg";
    
    public SuperFruit(int x, int y, String sprite){
        super(x, y, sprite);
    }

    @Override
    public void draw(PApplet app){
        if (!isEaten) app.image(this.image, this.x+2, this.y);
    }

    public static String superfruitSprite(){
        return SPRITE;
    }
}