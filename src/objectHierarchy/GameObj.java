package objectHierarchy;

import entity.Point;
import entity.Rectangle;
import entity.RectangleSize;
import entity.Sprite;

abstract public class GameObj {
    private Point bottomLeftCorner;
    private Rectangle hitBox;
    private Sprite sprite;

    public Point getBottomLeftCorner() {
        return bottomLeftCorner;
    }

    public void setBottomLeftCorner(Point bottomLeftCorner) {
        this.bottomLeftCorner = bottomLeftCorner;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

}
