package render.obj;

public class Camera {
    private float x, y; // world coordinates of top-left of screen
    private final int screenWidth, screenHeight;

    public Camera(int screenWidth, int screenHeight, int tileSize) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;


    }

    public void follow(float targetX, float targetY) {
        x = targetX - screenWidth / 2f;
        y = targetY - screenHeight / 2f;
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
