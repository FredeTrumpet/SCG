package render.obj;

import entity.Point;
import entity.RectangleSize;
import objectHierarchy.GameObj;

public final class Camera {

    private static Camera instance;

    private float x, y; // world coordinates of top-left of screen
    private final int screenWidth;
    private final int screenHeight;

    // Private constructor
    private Camera(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public static void initialize() {
        if (instance != null) {
            throw new IllegalStateException("Camera already initialized");
        }
        instance = new Camera((int)Window.getInstance().getSize().width(), (int)Window.getInstance().getSize().height());
    }

    public static Camera getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Camera not initialized. Call Camera.init() first.");
        }
        return instance;
    }

    public void follow(GameObj theThingCameraFollow) {
        x = theThingCameraFollow.getBottomLeftCorner().x() - screenWidth / 2f;
        y = theThingCameraFollow.getBottomLeftCorner().y() - screenHeight / 2f;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
