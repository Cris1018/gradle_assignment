package ghost;

import processing.core.PApplet;
import processing.core.PImage;

public class Items{
    
    protected int x;
    protected int y;
    protected PImage image;
    protected String sprite;

    public Items(int x, int y, String sprite){
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public void setImage(PApplet app){
        this.image = app.loadImage(this.sprite);
    }

    public void draw(PApplet app){
        app.image(this.image, this.x, this.y);
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }
}