package ghost;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GameManager{

    private final static int DelayForBeginning = 600;
    
    private int delayTiming;
    private Waka waka;
    public Waka serverWaka;
    private List<Wall> walls = new ArrayList<>();
    private List<Fruit> fruits = new ArrayList<>();
    private List<SuperFruit> superFruits = new ArrayList<>();
    private List<Items> items = new ArrayList<>();
    private List<Ghost> ghosts = new ArrayList<>();
    private List<Ignorant> ignors = new ArrayList<>();
    private List<Chaser> chasers = new ArrayList<>();
    private List<Whim> whims = new ArrayList<>();
    private List<Ambusher> ambushers = new ArrayList<>();
    private List<Ghost> hitByWaka = new ArrayList<>();
    private String map;
    private int chances;
    private int totalChances;
    private int speed;
    private int[] modes;
    private int cursor;
    private int frightenPeriod;
    private int ghostMoveTiming;
    private int ghostFrightenTiming;
    private Winner winner;
    public Client client;

    public GameManager(String filename){
        client = new Client();
        JSONParser jsonParser = new JSONParser();       
        
        try {
            FileReader reader = new FileReader(/*"config.json"*/ filename);
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObj = (JSONObject) obj;
            
            String map = (String) jsonObj.get("map");
            Object lives = jsonObj.get("lives");
            Object speed = jsonObj.get("speed");
            Object frightenLength = jsonObj.get("frighten");
            Object frightenByDrunkWaka = jsonObj.get("frightenedByDrunkWaka");
            JSONArray arr = (JSONArray) jsonObj.get("modeLengths");

            this.map = map;
            this.chances = ((Long)lives).intValue();
            // this is for restarting the game
            this.totalChances = ((Long)lives).intValue();
            
            this.speed = ((Long)speed).intValue();
            this.frightenPeriod = ((Long)frightenLength).intValue();
            
            int[] modesLengths = new int[arr.size()];
            for (int i=0; i<arr.size(); i++){
                modesLengths[i] = ((Number) arr.get(i)).intValue();
            }

            this.modes = modesLengths;            
        } 
        catch (FileNotFoundException e){

        } 
        catch (IOException e){

        }
        catch (ParseException e) {

        }
    }
    /**
     * read the map file
     * @return the list of string that represents the map
     */
    public List<String> mapToListString(){
        List<String> res = new ArrayList<>();
        try{
            Scanner sc = new Scanner(new File(this.map));
            while (sc.hasNextLine()){
                String line = sc.nextLine();
                res.add(line);
            }
            return res;
        } catch (FileNotFoundException e){
            return null;
        }   
    }

    /** 
     * @return winner of the game
     */
    public Winner getWinner(){
        return this.winner;
    }

    /**
     * @return the number of lives left
     */
    public int getChance(){
        return this.chances;
    }

    public List<Wall> getWall(){
        return this.walls;
    }

    public List<Fruit> getFruits(){
        return this.fruits;
    }

    public List<SuperFruit> getSuperFruits(){
        return this.superFruits;
    }

    public Waka getWaka(){
        return this.waka;
    }

    public List<Chaser> getChasers(){
        return this.chasers;
    }

    public List<Ambusher> getAmbushers(){
        return this.ambushers;
    }

    public List<Ghost> getGhosts(){
        return this.ghosts;
    }

    /**
     * return the list of ghosts that are hit by a waka eating super fruit for this round.
     * @return list of ghosts
     */
    public List<Ghost> getHitbyWaka(){
        return this.hitByWaka;
    }

    public List<Items> getItems(){
        return this.items;
    }

    public int getCursor(){
        return this.cursor;
    }

    public void displayLivesLeft(App app){
        for (int i=0; i<this.chances; i++){
            app.image(app.loadImage(Waka.rightSprite), 28*i+5, 544);
        }
    }

    public void restart(){
        for (Ghost g: this.hitByWaka){
            this.ghosts.add(g);
        }    
        this.hitByWaka.clear();
        this.cursor = 0;
        this.ghostMoveTiming = 0;
        this.ghostFrightenTiming = 0;
        
        this.waka.resetAll();
        for (Ghost g: this.ghosts){
            g.resetAll();
        }
    }
    
    /**
     * when waka wins or ghost wins, the game will restart after 10 seconds
     */
    public void delayForTenAndRestore(){
        this.delayTiming++;
        if (this.delayTiming == DelayForBeginning){
            this.delayTiming = 0;
            this.chances = this.totalChances;
            this.winner = null;
            for (Fruit f: this.fruits){
                f.reset();
            }
            for(SuperFruit sup: this.superFruits){
                sup.reset();
            }
            this.restart();
            return;
        }
        return;
    }

    public void setCursor(){
        cursor++;
    }

    /**
     * set winner for testing purpose
     * @param w set the winner as w. w can only be waka or ghost
     */
    public void setWinner(Winner w){
        this.winner = w;
    }

    /**
     * set up everything in the game
     */
    public void setUp(){
        List<String> mapLs = this.mapToListString();
        // i for y, j for x
        for (int i=0; i<mapLs.size(); i++){
            for (int j=0; j<mapLs.get(0).length(); j++){
                
                char c = mapLs.get(i).charAt(j);
                
                if (c == '1') {
                    Wall horizontal = new Wall(16*j, 16*i, Wall.getHorizontal());
                    this.walls.add(horizontal);
                }
                else if (c == '2'){
                    Wall vertical = new Wall(16*j, 16*i, Wall.getVertical());
                    this.walls.add(vertical);
                }
                else if (c == '3'){
                    Wall upleft = new Wall(16*j, 16*i, Wall.getUpLeft());
                    this.walls.add(upleft);
                }
                else if (c == '4'){
                    Wall upright = new Wall(16*j, 16*i, Wall.getUpRight());
                    this.walls.add(upright);
                }
                else if (c == '5'){
                    Wall downleft = new Wall(16*j, 16*i, Wall.getDownLeft());
                    this.walls.add(downleft);
                }
                else if (c == '6'){
                    Wall downright = new Wall(16*j, 16*i, Wall.getDownRight());
                    this.walls.add(downright);
                }
                else if (c == '7'){
                    Fruit fruit = new Fruit(16*j, 16*i, Fruit.fruitSprite());
                    this.fruits.add(fruit);
                }  
                else if (c == '8')   {
                    SuperFruit supers = new SuperFruit(16*j, 16*i, SuperFruit.superfruitSprite());
                    this.superFruits.add(supers);
                }        
                else if (c == 'p'){
                    this.waka = new Waka(16*j, 16*i, Waka.clientClose);
                    this.serverWaka = new Waka(16*j, 16*i, Waka.close);
                }
                else if (c == 'i'){
                    Ignorant ig = new Ignorant(16*j, 16*i, Ignorant.getIgnorantSprite());
                    this.ignors.add(ig);     
                }
                else if (c == 'c'){
                    Chaser chaser = new Chaser(16*j, 16*i, Chaser.getChaserSprite());
                    this.chasers.add(chaser);
                }
                else if (c == 'w'){
                    Whim w = new Whim(16*j, 16*i, Whim.getWhimSprite());
                    this.whims.add(w);
                }
                else if (c == 'a'){
                    Ambusher amb = new Ambusher(16*j, 16*i, Ambusher.getAmbusherSprite());
                    this.ambushers.add(amb);
                }
            }
        }
    }
    /**
     * put every elements in the game
     */
    public void putEverything(){
        for (Wall w: this.walls){
            this.items.add(w);
        }
        for (Fruit f: this.fruits){
            this.items.add(f);
        }
        for (SuperFruit supers: this.superFruits){
            this.items.add(supers);
        } 
        for (Ambusher a: this.ambushers){
            this.ghosts.add(a);
        }
        for (Chaser c: this.chasers){
            this.ghosts.add(c);
        }
        for (Whim w: this.whims){
            this.ghosts.add(w);
        }
        for (Ignorant ig: this.ignors){
            this.ghosts.add(ig);
        }
    }

    /**
     * this method load ghosts on the game window
     * @param app the app where the game runs
     */
    public void loadGhosts(App app){ 
        if (app == null){
            return;
        }
        
        for (Ghost g: this.ghosts){
            g.setMap(this.mapToListString());
            g.setSpeed(this.speed);
            if (g instanceof Whim){
                g.setSprite(Whim.getWhimSprite());
            }
            else if (g instanceof Ambusher){
                g.setSprite(Ambusher.getAmbusherSprite());
            }
            else if (g instanceof Chaser){
                g.setSprite(Chaser.getChaserSprite());
            }
            else if (g instanceof Ignorant){
                g.setSprite(Ignorant.getIgnorantSprite());
            }
            g.setImage(app);
        }
    }

    public boolean checkGhostEats(){
        for (Ghost g: this.ghosts){
            if (g.eatWaka(this.waka)){
                return true;
            }

            if (g.eatWaka(serverWaka)){
                return true;
            }
        }
        return false;
    }

    public void checkHasWinner(App app){
        if (this.winner == Winner.WAKA){
            app.image(app.loadImage(Waka.win), 48, 144);
        } else if (this.winner == Winner.GHOST) {
            app.image(app.loadImage(Ghost.getWinPrinted()), 16, 144);  
        }
    }

    public boolean wakasWin(){
        for (Fruit f: this.fruits){
            if (!f.checkEaten()){
                return false;
            }
        }

        for (SuperFruit superfruits: this.superFruits){
            if (!superfruits.checkEaten()){
                return false;
            }
        }
        return true;
    }

    /**
     * the total game runs here.
     * @param app the app where the game runs
     */
    public void starting(App app){    
        if (app != null){
            this.waka.clientWakaChange(app);
            if (this.serverWaka.getDirection()!=null){
                this.serverWaka.changeMouth(app);
            }
        }
        
        if (this.waka.getDirection() == null){
            this.client.sendMessage(this.waka.getX()+" "+this.waka.getY());
        } else {
            this.client.sendMessage(this.waka.getX()+" "+this.waka.getY()+" "+this.waka.getDirection().toString());
        }
        

        String fromServer = client.receive();
        String[] arr = fromServer.split(" ");
        this.serverWaka.setX(Integer.parseInt(arr[0]));
        this.serverWaka.setY(Integer.parseInt(arr[1]));
        if (arr.length == 3){
            this.serverWaka.setDirection(Waka.readDirection(arr[2]));
        }
        
        this.waka.setSpeed(this.speed);
        
        this.waka.eating(this.fruits);
        this.waka.eatSuperfruits(this.superFruits);
        this.serverWaka.eating(this.fruits);
        this.serverWaka.eatSuperfruits(this.superFruits);
        
        this.waka.running(this.mapToListString(), this.walls);

        if (this.wakasWin()){
            this.winner = Winner.WAKA;
        }
        
        this.loadGhosts(app);

        // do not go to next if - else block
        if (this.waka.isSuper() || this.serverWaka.isSuper()){
            this.ghostFrightening(app); 
            return;
        } 
        
        if (this.ghostMoveTiming<=60*this.modes[cursor]){
            this.ghostMoveTiming++;    
            
            if (cursor%2 == 0){
                // is scattering
                for (Ghost g: this.ghosts){
                    int[] corner = g.scatterTarget();
                    g.running(corner[0], corner[1]); 
                    
                    if (g.getDebugState()){
                        g.isDebugging(app, corner[0], corner[1]);
                    }
                }
            }
            
            else{
                // is chasing 
                for (Ghost g: this.ghosts){
                    
                    if (g instanceof Whim){
                        Whim w = (Whim) g;
                        if (this.ghosts.contains(this.chasers.get(0))){
                            int[] chaseTarget = w.processTarget(this.waka, this.chasers.get(0));
                            w.running(chaseTarget[0], chaseTarget[1]);
                            if (w.getDebugState()){
                                w.isDebugging(app, chaseTarget[0], chaseTarget[1]);
                            }
                        } else {
                            int[] corner = w.scatterTarget();
                            w.running(corner[0], corner[1]); 
                            if (w.getDebugState()){
                                w.isDebugging(app, corner[0], corner[1]);
                            }
                        }
                    }
                    
                    else{
                        int[] chaseTarget = g.chaseTarget(this.waka);
                        g.running(chaseTarget[0], chaseTarget[1]);
                        if (g.getDebugState()){
                            g.isDebugging(app, chaseTarget[0], chaseTarget[1]);
                        }
                    }
                }
            }
            
            if (this.checkGhostEats()){
                if (this.chances == 0){
                    this.winner = Winner.GHOST;
                }
                else{
                    this.chances--;
                    this.restart();
                }
            }
        }
       
        if (this.ghostMoveTiming == 60*this.modes[cursor]){
            // change mode
            if (cursor == this.modes.length-1){
                cursor = 0;
            } else{
                cursor++; 
            }
            this.ghostMoveTiming = 0;
        }
        return;
    }

    /**
     * this function is called immediately after waka eats a super fruit
     * @param app the app where the game runs
     */
    public void ghostFrightening(App app){
        if (this.ghostFrightenTiming == 60*this.frightenPeriod){
            this.waka.notSuper();;
            ghostFrightenTiming = 0;
        }        
        
        this.ghostFrightenTiming ++;
        // System.out.println("GHOST FRIGHTEN TIMING: "+ this.ghostFrightenTiming);
        for (Ghost g: this.ghosts){
            g.setSprite(Ghost.getFrightenedSprite());
            if (app != null){
                g.setImage(app);
            }
            g.moveRandomly();
            if (g.hitByWaka(this.waka)){
                this.hitByWaka.add(g);
            }
        }

        for (Ghost g: this.hitByWaka){
            this.ghosts.remove(g);
        }
        // if this.ghosts is empty, waka wins
        if (this.ghosts.isEmpty()){
            this.winner = Winner.WAKA;
        }
        return;
    }
}