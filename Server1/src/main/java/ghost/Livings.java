package ghost;

import java.util.List;
import processing.core.PApplet;
import processing.core.PImage;

/** this is the parent class of waka and ghosts
 * 
 */
public class Livings{

    protected int x;
    protected int y;
    protected int initialX;
    protected int initialY;
    protected PImage image;
    protected String sprite;
    protected Direction direction;
    protected int speed;
    protected List<String> map;
    protected int upperBound;
    protected int lowerBound;
    protected int leftBound;
    protected int rightBound;

    public Livings(int x, int y, String sprite){
        this.initialX = x;
        this.initialY = y;
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public void setSprite(String spr){
        this.sprite = spr;
    }

    public String getSprite(){
        return this.sprite;
    }

    public void setImage(PApplet app){
        this.image = app.loadImage(this.sprite);
    }

    public void draw(PApplet app){
        app.image(this.image, this.x-3, this.y-3);
    }

    public void setMap(List<String> map){
        this.map = map;
    }

    public int getInitialX(){
        return this.initialX;
    }

    public int getInitialY(){
        return this.initialY;
    }

    public void setX(int toX){
        this.x = toX;
    }

    public void setY(int toY){
        this.y = toY;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setDirection(Direction direction){
        this.direction = direction;
    }

    public Direction getDirection(){
        return this.direction;
    }
    
    public boolean colLeftWall(List<Wall> walls){
        for (Wall w: walls){
            if (this.x-16 == w.getX() && this.y == w.getY()){
                return true;
            }
        }
        return false;
    }

    public boolean colRightWall(List<Wall> walls){
        for (Wall w: walls){
            if (this.x+16 == w.getX() && this.y == w.getY()){
                return true;
            }
        }
        return false;
    }

    public boolean colUpWall(List<Wall> walls){
        for (Wall w: walls){
            if (this.x == w.getX() && this.y-16 == w.getY()){
                return true;
            }
        }
        return false;
    }

    public boolean colDownWall(List<Wall> walls){
        for (Wall w: walls){
            if (this.x == w.getX() && this.y+16 == w.getY()){
                return true;
            }
        }
        return false;
    }

    // public void moveLeft(List<Wall> walls){
    //     this.setDirection(Direction.L);
    //     if (!colLeftWall(walls)){
    //         this.x-=this.speed; 
    //     }
    // }

    // public void moveRight(List<Wall> walls){
    //     this.setDirection(Direction.R);    
    //     if (!colRightWall(walls)){
    //         this.x+=this.speed;
    //     }
    // }

    // public void moveUp(List<Wall> walls){
    //     this.setDirection(Direction.U);
    //     if (!colUpWall(walls)){
    //         this.y-=this.speed; 
    //     }
    // }

    // public void moveDown(List<Wall> walls){
    //     this.setDirection(/*"d"*/Direction.D);
    //     if (!colDownWall(walls)){
    //         this.y+=this.speed;  
    //     }
    // }

    /**
     * read and parse the map
     * @return a 2-dimension array containing the coordinates of corners
     */
    public int[][] findCorners(){        
        int[] boundary = new int[4];
        // {up, down, left, right}
        for (int i=0; i<this.map.size(); i++){
            if (!this.map.get(i).equals("0000000000000000000000000000")){
                boundary[0] = i;
                String upBoundary = this.map.get(i);
                for (int j=0; j<upBoundary.length();j++){
                    if (upBoundary.charAt(j) != '0'){
                        boundary[2] = j;
                        break;
                    }
                }
                break;
            }
        }
        for (int i=this.map.size()-1; i>0; i--){
            if (!this.map.get(i).equals("0000000000000000000000000000")){
                boundary[1] = i;
                String downBoundary = this.map.get(i);
                for (int j=downBoundary.length()-1; j>=0; j--){
                    if (downBoundary.charAt(j) != '0'){
                        boundary[3] = j;
                        break;
                    }    
                }
                break;
            }
        }
        int[][] coords= new int[4][2];
        // 0 -topleft, 1 - topright 2 - bottomleft 3 - bottomright
        coords[0][0] = 16*boundary[2];
        coords[0][1] = 16*boundary[0];
        coords[1][0] = 16*boundary[3];
        coords[1][1] = 16*boundary[0];
        coords[2][0] = 16*boundary[2];
        coords[2][1] = 16*boundary[1];
        coords[3][0] = 16*boundary[3];
        coords[3][1] = 16*boundary[1];
        return coords;
    }

    /**
     * set the corner for the ghosts and waka
     */
    public void findBounds(){
        int[][] corners = this.findCorners();
        this.leftBound = corners[0][0];
        this.upperBound = corners[0][1];
        this.lowerBound = corners[3][1];
        this.rightBound = corners[3][0];
    }

    /**
     * getter method
     * @return the lefe bound
     */
    public int getLeftBound(){
        return this.leftBound;
    }

    /**
     * getter method
     * @return the right bound
     */
    public int getRightBound(){
        return this.rightBound;
    }

    /**
     * @return the upper bound
     */
    public int getUpperBound(){
        return this.upperBound;
    }

    /**
     * @return the lower bound
     */
    public int getLowerBound(){
        return this.lowerBound;
    }

    /**
     * @param xIdx the x position of waka or ghosts 
     * @return boolean value if out of left bound or not
     */
    public boolean checkLeftBound(int xIdx){
        this.findBounds();
        if (xIdx<=this.leftBound/16) return true;
        return false;
    }

    /**
     * @param xIdx the x position of waka or ghosts 
     * @return boolean value if out of right bound or not
     */
    public boolean checkRightBound(int xIdx){
        this.findBounds();
        if (xIdx>=this.rightBound/16) return true;
        return false;
    }

    /**
     * @param yIdx the y position of waka or ghosts 
     * @return boolean value if out of up bound or not
     */
    public boolean checkUpperBound(int yIdx){
        this.findBounds();
        // System.out.println(this.upperBound);
        if (yIdx<=this.upperBound/16) return true;
        return false;
    }

    /**
     * @param yIdx the y position of waka or ghosts 
     * @return boolean value if out of low bound or not
     */
    public boolean checklowerBound(int yIdx){
        this.findBounds();
        // System.out.println(this.lowerBound);
        if (yIdx>=this.lowerBound/16) return true;
        return false;
    }
}