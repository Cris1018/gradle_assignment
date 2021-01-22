package ghost;

public class Wall extends Items{

    private static final String HORIZONTAL = "src/main/resources/horizontal.png";
    private static final String VERTICAL = "src/main/resources/vertical.png";
    private static final String UPLEFT = "src/main/resources/upLeft.png";
    private static final String UPRIGHT = "src/main/resources/upRight.png";
    private static final String DOWNLEFT = "src/main/resources/downLeft.png";
    private static final String DOWNRIGHT = "src/main/resources/downRight.png";

    public Wall(int x, int y, String sprite){
        super(x, y, sprite);
    }

    public static String getHorizontal(){
        return HORIZONTAL;
    }

    public static String getVertical(){
        return VERTICAL;
    }

    public static String getUpLeft(){
        return UPLEFT;
    }

    public static String getUpRight(){
        return UPRIGHT;
    }

    public static String getDownLeft(){
        return DOWNLEFT;
    }

    public static String getDownRight(){
        return DOWNRIGHT;
    }
}