package ghost;

import processing.core.PApplet;

public abstract class Ghost extends Livings{
    
    protected static final String FRIGHTENEDSPRITE = "src/main/resources/frightened.png";
    protected static final String WIN = "src/main/resources/GAMEOVER.jpg";
    protected boolean canLeft;
    protected boolean canRight;
    protected boolean canUp;
    protected boolean canDown;
    protected boolean frightened;
    protected boolean isDebugging;
    
    public Ghost(int x, int y, String sprite){
        super(x, y, sprite);
    }

    @Override
    public void draw(PApplet app){
        app.image(this.image, this.x-3, this.y-3);
    }

    public abstract int[] chaseTarget(Waka waka);
    
    public abstract int[] scatterTarget();

    public static String getFrightenedSprite(){
        return FRIGHTENEDSPRITE;
    }

    public static String getWinPrinted(){
        return WIN;
    }

    public void changeDebug(){
        this.isDebugging = !this.isDebugging;
    }

    public boolean getDebugState(){
        return this.isDebugging;
    }

    public void isDebugging(PApplet app, int targetX, int targetY){ 
        app.stroke(255, 255, 255);
        app.line(this.x, this.y, targetX, targetY);
    }

    public void resetAll(){
        this.x = this.initialX;
        this.y = this.initialY;
        this.canDown = false;
        this.canUp = false;
        this.canRight = false;
        this.canLeft = false;
        this.isDebugging = false;
    }

    // for test
    public void availableToMove(){
        this.canUp = true;
        this.canDown = true;
        this.canRight = true;
        this.canLeft = true;
    }

    public boolean getCanUp(){
        return this.canUp;
    }

    public boolean getCanLeft(){
        return this.canLeft;
    }

    public boolean getCanRight(){
        return this.canRight;
    }

    public boolean getCanDown(){
        return this.canDown;
    }

    public boolean eatWaka(Waka waka){
        if ((!waka.isSuper()) && Math.abs(x-waka.getX())<5 && Math.abs(y-waka.getY())<5){
            return true;
        }
        return false;
    }
    
    public boolean hitByWaka(Waka waka){
        if ((waka.isSuper()) && Math.abs(x-waka.getX())<5 && Math.abs(y-waka.getY())<5){
            return true;
        }
        return false;
    }

    public void moveRandomly(){
        if (this.x%16 == 0 && this.y%16 == 0){
            
            if (!this.colUpWall(x/16, y/16) && this.direction != Direction.D){
                this.canUp = true;
            } else if (!this.colDownWall(x/16, y/16) &&  this.direction != Direction.U){
                this.canDown = true;
            } else if (!this.colLeftWall(x/16, y/16) && this.direction != Direction.R){
                this.canLeft = true;
            } else if (!this.colRightWall(x/16, y/16) && this.direction != Direction.L){
                this.canRight = true;
            }
        }

        if  (this.canUp){
            this.moveUp();
            return;
        } else if (this.canDown){
            this.moveDown();
            return;
        } else if (this.canRight){
            this.moveRight();
            return;
        } else if (this.canLeft){
            this.moveLeft();
            return;
        }

        if (this.direction == Direction.U){
            this.moveDown();
            return;
        } else if (this.direction == Direction.R){
            this.moveLeft();
            return;
        } else if (this.direction == Direction.D){
            this.moveUp();
            return;
        } else if (this.direction == Direction.L){
            this.moveRight();
            return;
        }
    }

    public double[] findDistance(int[] target){
        // left right down up
        int[] dx = {-16, 16, 0, 0};
        int[] dy = {0, 0, -16, 16};
        double[] ifMove = new double[4];

        // target is in PIXELS (*16) 
        for (int i=0; i<4; i++){
            double d =  Math.pow(this.x+dx[i]-target[0], 2)+ Math.pow(this.y+dy[i]-target[1], 2);
            ifMove[i] = d;
        }
        // {ifLeft, ifRight, ifUp, ifDown};
        return ifMove;
    }

    public int findSmallestIdx(double[] arr){
        int idx = 0;
        for (int i=0; i<4; i++){
            if (arr[i] < arr[idx]) idx = i;
        }
        return idx;
    }

    public void moveLeft(){
        this.x-= this.speed;
        this.direction = Direction.L;
        if (this.x%16 ==0){
            this.canLeft = false;
        }
    }

    public void moveRight(){
        this.x+= this.speed;
        this.direction = Direction.R;
        if (this.x%16 == 0){
            this.canRight = false;
        }
    }
    
    public void moveUp(){
        this.y-= this.speed;
        this.direction = Direction.U;
        if (this.y%16 == 0){
            this.canUp = false;
        }
    }

    public void moveDown(){
        this.y+= this.speed;
        this.direction = Direction.D; 
        if (this.y%16 == 0){
            this.canDown= false;
        }
    }

    //leftBound, rightBound, upper Bound, lowerBound
    public boolean colLeftWall(int xIdx, int yIdx){ 
        char c = this.map.get(yIdx).charAt(xIdx-1);
        if (c == '1' || c =='2' || c=='3' || c=='4' || c=='5' || c=='6'){
            return true;
        }
        return false;            
    }

    public boolean colRightWall(int xIdx, int yIdx){
        char c = this.map.get(yIdx).charAt(xIdx+1);
        if (c == '1' || c =='2' || c=='3' || c=='4' || c=='5' || c=='6'){
            return true;
        }
        return false;            
    }

    public boolean colUpWall(int xIdx, int yIdx){
        char c = this.map.get(yIdx-1).charAt(xIdx);
        if (c == '1' || c =='2' || c=='3' || c=='4' || c=='5' || c=='6'){
            return true;
        }
        return false;            
    }

    public boolean colDownWall(int xIdx, int yIdx){
        char c = this.map.get(yIdx+1).charAt(xIdx);
        if (c=='1' || c =='2' || c=='3' || c=='4' || c=='5' || c=='6'){
            return true;
        }       
        return false;            
    }

    /**
     * if on this direction the ghost will run into a wall, set the distance as the largest value
     * @param targetX x position of the target
     * @param targetY y position of the target
     * @return the direction the ghost will move to
     */
    public Direction nextDirection(int targetX, int targetY){
        // l r u d
        double[] orders = this.findDistance(new int[]{targetX, targetY}); 
        // col to wall - Change to Double.MAX_Value
      
        if (this.colLeftWall(x/16, y/16) || this.checkLeftBound(x/16)){
            orders[0] = Double.MAX_VALUE;
        }
        if (this.colRightWall(x/16, y/16) || this.checkRightBound(x/16)){
            orders[1] = Double.MAX_VALUE;
        }
        if (this.colUpWall(x/16, y/16) || this.checkUpperBound(y/16)){
            orders[2] = Double.MAX_VALUE;
        }
        if (this.colDownWall(x/16, y/16) || this.checklowerBound(y/16)){
            orders[3] = Double.MAX_VALUE;
        }

        if (this.direction!=null){
            if (this.direction == Direction.U){
                orders[3] = Double.MAX_VALUE;
            }
            else if (this.direction == Direction.D){
                orders[2] = Double.MAX_VALUE;
            }
            else if (this.direction == Direction.R){
                orders[0] = Double.MAX_VALUE;
            }
            else if (this.direction == Direction.L){
                orders[1] = Double.MAX_VALUE;
            }
        }

        if (orders[0] == orders[1] && orders[1] == orders[2] && orders[2] == orders[3] && orders[0] ==  Double.MAX_VALUE){
            if (this.direction == Direction.U){
                return Direction.D;
            }
            else if (this.direction == Direction.D){
                return Direction.U;
            }
            else if (this.direction == Direction.R){
                return Direction.L;
            }
            else if (this.direction == Direction.L){
                return Direction.R;
            }
            
        }
        
        int idx = this.findSmallestIdx(orders);
        
        if (idx == 0){
            return Direction.L;
        }
        else if (idx == 1){
            return Direction.R;
        }
        else if (idx == 2){
            return Direction.U;
        }
        else{
            return Direction.D;
        }
    }

    public void running(int targetX, int targetY){
        // one direction/ move every time
        // 16 times
        this.direction= nextDirection(targetX, targetY);
        if (this.x%16 == 0 && this.y%16 == 0){
            
            if (this.direction == Direction.L){
                this.canLeft = true;
            } else if (this.direction == Direction.R){
                this.canRight = true;
            } else if (this.direction == Direction.U){
                this.canUp = true;
            } else if (this.direction == Direction.D){
                this.canDown = true;
            }
        }

        if (this.canLeft){
            this.moveLeft();
        } else if (this.canRight){
            this.moveRight();
        } else if (this.canUp){
            this.moveUp();
        } else if (this.canDown){
            this.moveDown();
        }
    }
}