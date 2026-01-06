import objectHierarchy.MainPlayer;
import org.lwjgl.opengl.GL11;
import render.Renderer;
import render.obj.Camera;
import render.obj.Layer;
import render.obj.Window;

public class StartGame {
    public static void main(String[] args) {

        initializeAllSingletons();

        loadAllSprites();

        setUpGameObjsForGame();

        gameLoop();

        Window.getInstance().destroy();
        Renderer.getInstance().destroy();
    }


    public static void gameLoop() {

        Window window = Window.getInstance();

        while (!window.shouldClose()) {
            update();

            render();

            window.update();


        }
    }

    public static void setUpGameObjsForGame(){
        Layer.getInstance().addGameObjToLayers(MainPlayer.getInstance());


    }

    public static void update() {
        //all game logic
        Camera.getInstance().follow(MainPlayer.getInstance());
        //playerUpdate
        //enemyUpdate


    }


    public static void render() {
        //all stuff to render, need to make layer instance
        GL11.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        Layer.getInstance().renderGameObjs();


    }

    public static void initializeAllSingletons(){
        Window.initialize();
        Renderer.initialize();
        Layer.initialize();
        Camera.initialize();
        MainPlayer.initialize();




    }

    public static void loadAllSprites(){
        //insert all sprites into render
        Renderer.getInstance().addTextures("testTexture","src/main/resources/slime/SLIMEMOVERIGHT1.png");


    }
}