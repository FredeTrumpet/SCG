package objectHierarchy;

import entity.Point;
import entity.Rectangle;
import entity.RectangleSize;
import entity.Sprite;

public class SlimeEnemy extends Enemy {

    public SlimeEnemy(float x, float y, int damage, int health){
        setBottomLeftCorner(new Point(x, y));
        setSprite(new Sprite(getBottomLeftCorner(),new RectangleSize(64,64),"idk"));
        setHitBox(new Rectangle(getBottomLeftCorner(),new RectangleSize(64,64)));
        setDamage(damage);
        setHealth(health);
    }
}
