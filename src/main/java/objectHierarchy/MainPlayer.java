package objectHierarchy;


public class MainPlayer extends Player{

    private static MainPlayer instance;

    private MainPlayer(float x, float y,int damage,int health) {
        super(x, y, damage, health);

    }

    public static void initialize() {
        if (instance != null) {
            throw new IllegalStateException("MainPlayer already initialized");
        }

        instance = new MainPlayer(
                1,//This is start X value for MainPlayer
                1,//This is start Y value for MainPlayer
                10,//This is start damage for MainPlayer
                10);//This is start health for Mainplayer
    }

    public static MainPlayer getInstance() {
        if (instance == null) {
            throw new IllegalStateException("MainPlayer not initialized. Call MainPlayer.initialize(); first.");
        }
        return instance;
    }

}
