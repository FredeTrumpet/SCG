import render.Renderer;
import render.obj.*;

public class StartGame {
    public static void main(String[] args) {

        initializeAllSingletons();

        loadAllSprites();

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

    public static void update() {
        //all game logic

        //playerUpdate
        //enemyUpdate


    }


    public static void render() {
        //all stuff to render, need to make layer instance



    }

    public static void initializeAllSingletons(){
        Window.initialize(1280, 720, "My Game");
        Renderer.initialize();




    }

    public static void loadAllSprites(){
        //insert all sprites into render


    }
}