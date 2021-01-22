package ghost;

import processing.core.PApplet;

/**
 * This is the app class. It will show the game window.
 */
public class App extends PApplet {
    
    public static final int WIDTH = 448;
    public static final int HEIGHT = 576;
    public GameManager manager;

    public App() {
        //Set up the objects
        this.manager = new GameManager("config.json");
    }

    /**
     * set up the game here
     */
    public void setup() {
        frameRate(60);
        // Load images
        this.manager.setUp();
        this.manager.putEverything();
        this.manager.server.start(8888);
        
        for (Items i: this.manager.getItems()){
            i.setImage(this);
        }            

        if (this.manager.getGhosts().size() > 0){
            for (Ghost g: this.manager.getGhosts()){
                g.setImage(this);
            }              
        }
          
        this.manager.getWaka().setImage(this); 
        this.manager.clientWaka.setImage(this);
    }

    public void settings() {
        size(WIDTH, HEIGHT);   
    }

    /**
     * draw the game window here
     */
    public void draw() { 
        background(0, 0, 0);       

        if (this.manager.getWinner() == null) {
            this.manager.displayLivesLeft(this);
            for (Items i: this.manager.getItems()){
                i.draw(this);
            }
            for (Ghost g: this.manager.getGhosts()){
                g.draw(this);
            }
            this.manager.getWaka().draw(this);  
            this.manager.clientWaka.draw(this);
            this.manager.starting(this);            
        } else {
            this.manager.checkHasWinner(this);
            this.manager.delayForTenAndRestore();
        }
    }

    /**
     * Accept use's pressing the keys and controlling waka moving or ghost debuging
     */
    public void keyPressed() { 
        if (this.keyCode == 37) {
            this.manager.getWaka().setNextMove(Direction.L);
        } else if (this.keyCode == 38) {
            this.manager.getWaka().setNextMove(Direction.U);
        } else if (this.keyCode == 39) {
            this.manager.getWaka().setNextMove(Direction.R);
        } else if (this.keyCode == 40) {
            this.manager.getWaka().setNextMove(Direction.D);
        }

        if (this.keyCode == 32){    
            for (Ghost g: this.manager.getGhosts()){
                g.changeDebug();
            }                
        }
    }

    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }
}