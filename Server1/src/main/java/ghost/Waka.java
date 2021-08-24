package ghost;

import java.util.ArrayList;
import java.util.List;

public class Waka extends Livings{

    private Direction nextMove;
    private int timing;
    private boolean hasSuperfruit;
    // server
    public static final String rightSprite = "src/main/resources/playerRight.png";
    public static final String leftSprite = "src/main/resources/playerLeft.png";
    public static final String upSprite = "src/main/resources/playerUp.png";
    public static final String downSprite = "src/main/resources/playerDown.png";
    public static final String close = "src/main/resources/playerClosed.png";
    // client
    public static final String clientClose = "src/main/resources/clientClose.png";
    public static final String clientLeft = "src/main/resources/clientLeft.png";
    public static final String clientRight = "src/main/resources/clientRight.png";
    public static final String clientUp = "src/main/resources/clientUp.png";
    public static final String clientDown = "src/main/resources/clientDown.png";
    public static final String win = "src/main/resources/YOUWIN.jpg";
    
    public Waka(int x, int y, String sprite){
        super(x, y, sprite);
    }

    public void setNextMove(Direction move){
        this.nextMove = move;
    }
    
    public Direction getNextMove(){
        return this.nextMove;
    }

    public void startSuper(){
        this.hasSuperfruit = true;
    }

    public void notSuper(){
        this.hasSuperfruit = false;
    }

    public boolean isSuper(){
        return this.hasSuperfruit;
    }

    /**
     * if the character represents a wall, then waka cannot walks on it, then return false
     * else if the character represents a fruit, a super fruit, a soda can or air, return true
     * @param c on map
     * @return the boolean value
     */
    public boolean canPass(char c){
        if (c == '1' || c == '2' || c =='3' || c == '4' || c == '5' || c == '6'){
            return false;
        }
        return true;
    }

    public static Direction readDirection(String str){
        if (str.equals("l")){
            return Direction.L;
        }
        else if (str.equals("r")){
            return Direction.R;
        }
        else if (str.equals("u")){
            return Direction.U;
        }
        else{
            return Direction.D;
        }
    }

    public boolean checkWay(List<Character> visited){
        if (!visited.contains('1') && !visited.contains('2') && !visited.contains('3') && !visited.contains('4') && !visited.contains('5') && !visited.contains('6')){
            return true;
        }
        return false;
    }

    public boolean checkWin(List<Fruit> fruits, List<SuperFruit> supers){
        for (Fruit f: fruits){
            if (!f.checkEaten()) return false;
        }
        for (SuperFruit superfruits: supers){
            if (!superfruits.checkEaten()) return false;
        }
        return true;
    }

    public void resetAll(){
        this.notSuper();
        this.setX(this.getInitialX());
        this.setY(this.getInitialY());
        this.setDirection(null);
        this.setNextMove(null);
        this.timing = 0;
        this.setSprite(Waka.close);
    }

    /**
     * waka will eat the fruit when it moves on it
     * @param fruits the fruits in the game
     */
    public void eating(List<Fruit> fruits){
        for (Fruit f: fruits){
            if (f.getX() == x && f.getY() == y){
                if (f.checkEaten() == false){
                    f.eaten();
                }
            }
        }
    }

    /**
     * waka will eat the super fruit when it moves on it
     * then waka will turn the ghosts into frightened mode
     * @param supers the super fruits in the game
     */
    public void eatSuperfruit(List<SuperFruit> supers){
        for (SuperFruit sups: supers){
            if (sups.getX() == x && sups.getY() == y){
                if (sups.checkEaten() == false){
                    sups.eaten();
                    this.hasSuperfruit = true;                    
                }
            }
        }
    }

    /**
     * control waka's sprite switching between open-mouth and close-mouth every 8-frame
     * the open-mouth sprite relate to the direction of waka
     * @param app the app where the game runs
     */
    public void changeMouth(App app){
        if (this.direction == null){
            this.direction = this.getNextMove();
            return;
        }
        
        if (timing<8){
            if (this.direction.equals(Direction.L)){
                this.setSprite(Waka.leftSprite);
            }
            else if (this.direction.equals(/*"r"*/Direction.R)){
                this.setSprite(Waka.rightSprite);
            }
            else if (this.direction.equals(Direction.U)){
                this.setSprite(Waka.upSprite);
            }
            else if (this.direction.equals(Direction.D)){
                this.setSprite(Waka.downSprite);
            }                
            timing++;
        } 
        else {
            this.setSprite(Waka.close);
            timing++; 
            if (timing == 15) {
                timing = 0;
            }
        }
        if (app != null){
           this.setImage(app); 
        } 
    }


    public void moveLeft(List<Wall> walls){
        this.setDirection(Direction.L);
        if (!colLeftWall(walls)){
            this.x-=this.speed; 
        }
    }

    public void moveRight(List<Wall> walls){
        this.setDirection(Direction.R);    
        if (!colRightWall(walls)){
            this.x+=this.speed;
        }
    }

    public void moveUp(List<Wall> walls){
        this.setDirection(Direction.U);
        if (!colUpWall(walls)){
            this.y-=this.speed; 
        }
    }

    public void moveDown(List<Wall> walls){
        this.setDirection(/*"d"*/Direction.D);
        if (!colDownWall(walls)){
            this.y+=this.speed;  
        }
    }

    // this will always be used for the client side
    public void clientWakaChange(App app){
        if (this.direction == null){
            this.direction = this.getNextMove();
            return;
        }
        
        if (timing<8){
            if (this.direction.equals(Direction.L)){
                this.setSprite(Waka.clientLeft);
            }
            else if (this.direction.equals(Direction.R)){
                this.setSprite(Waka.clientRight);
            }
            else if (this.direction.equals(Direction.U)){
                this.setSprite(Waka.clientUp);
            }
            else if (this.direction.equals(Direction.D)){
                this.setSprite(Waka.clientDown);
            }                
            timing++;
        } 
        else {
            this.setSprite(Waka.clientClose);
            timing++; 
            if (timing == 15) {
                timing = 0;
            }
        }
        if (app != null){
           this.setImage(app); 
        } 
    }

    /**
     * search the position where the waka can change its direction
     * this change only applies to
     * 1. turn left or right when it is moving up or down
     * 2. turn up or down when it is moving left or right
     * @param map the game map
     * @return the coordinates of turning point
     */
    public int[] search(List<String> map){ 
        int x = Math.floorDiv(this.x, 16);
        int y = Math.floorDiv(this.y, 16);
        
        int[] res = new int[2];
        List<Character> visited = new ArrayList<>();
        
        if (this.direction.equals(Direction.L) || this.direction.equals(/*"r"*/Direction.R)){
            // move left/right, search up/down
            while (checkWay(visited)){
                if (nextMove.equals(/*"u"*/Direction.U)){
                    char c = map.get(y-1).charAt(x);
                    if (this.canPass(c)){
                        res[0] = x;
                        res[1] = y;
                        return res;
                    }                        
                } else if (nextMove.equals(Direction.D)){
                    char c = map.get(y+1).charAt(x);
                    if (this.canPass(c)){
                        res[0] = x;
                        res[1] = y;
                        return res;
                    }  
                }
                char current = map.get(y).charAt(x);
                visited.add(current);  
                if (this.direction.equals(Direction.L)){
                    x -= 1;
                }
                else if (this.direction.equals(Direction.R)){
                    x += 1;
                }
            }
            return res;                

        } else if (this.direction.equals(Direction.D) || this.direction.equals(/*"u"*/Direction.U)){
            // up/down, search left/right
            while (checkWay(visited)){    
                // left
                if (nextMove.equals(Direction.L)){
                    char c = map.get(y).charAt(x-1);
                    if (canPass(c)){
                        res[0] = x;
                        res[1] = y;
                        return res;
                    }                    
                } else if (nextMove.equals(/*"r"*/Direction.R)){
                    char c = map.get(y).charAt(x+1);
                    if (canPass(c)){
                        res[0] = x;
                        res[1] = y;
                        return res;
                    }  
                }
                char current = map.get(y).charAt(x);
                visited.add(current);
                if (this.direction.equals(Direction.U)) y -= 1;
                else if (this.direction.equals(Direction.D)) y+=1;
            }
            return res;
        }
        return res;
    }

    public void running(List<String> map, List<Wall> wall){
        if (this.direction == null){
            this.direction = this.nextMove;
        } 
        else {
            // left now or right now
            if (this.direction.equals(Direction.L) || this.direction.equals(Direction.R)){
                if (this.nextMove.equals(Direction.L)){
                    this.moveLeft(wall);
                }
                else if (this.nextMove.equals(Direction.R)){
                    this.moveRight(wall);
                }
                else {
                    int[] exit = search(map);
                    if (x == 16*exit[0] && y == 16*exit[1]){
                        if (this.nextMove.equals(Direction.U)){
                            this.moveUp(wall);                                
                        } 
                        else{
                            this.moveDown(wall);  
                        }
                    } 
                    else {
                        if (this.direction.equals(Direction.L)){
                            this.moveLeft(wall);
                        }
                        else{
                            this.moveRight(wall);
                        }
                    }
                }
            }
            // up now or down now
            else if (this.direction.equals(Direction.U) || this.direction.equals(Direction.D)){
                if (this.nextMove.equals(Direction.U)){
                    this.moveUp(wall);
                }
                else if (this.nextMove.equals(Direction.D)){
                    this.moveDown(wall);
                }
                else{
                    int[] exit = search(map);
                    if (x == 16*exit[0] && y == 16*exit[1]){
                        if (this.nextMove.equals(Direction.L)){
                            this.moveLeft(wall);                           
                        } 
                        else{
                            this.moveRight(wall);
                        }
                    } 
                    else {
                        if (this.direction.equals(Direction.U)){
                            this.moveUp(wall);
                        }
                        else{
                            this.moveDown(wall);
                        }
                    }
                }
            }
        }
    }
}