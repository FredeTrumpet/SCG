package render.obj;

import objectHierarchy.GameObj;
import render.Renderer;

import java.util.ArrayList;

public class Layer {

    private final ArrayList<GameObj> renderObj = new ArrayList<>();

    private Layer() {

    }

    private static class Holder {
        private static Layer INSTANCE;
    }

    public static synchronized void initialize() {
        if (Holder.INSTANCE == null) {
            Holder.INSTANCE = new Layer();
        } else {
            throw new IllegalStateException("Layer has already been initialized");
        }
    }

    public static Layer getInstance() {
        if (Holder.INSTANCE == null) {
            throw new IllegalStateException("Layer is not initialized. Call initialize() first.");
        }
        return Holder.INSTANCE;
    }


    public void updateLayerPossition(GameObj obj) {

        renderObj.add(obj);
    }

    public void addGameObjToLayers(GameObj obj) {
        renderObj.add(obj);
        updateLayerPossition(obj);
    }

    public void renderGameObjs() {
        Renderer renderer = Renderer.getInstance();
        if (!renderObj.isEmpty()) {
            for (GameObj gameObj : renderObj) {
                renderer.renderGameObj(gameObj);
            }
        }
    }
}
