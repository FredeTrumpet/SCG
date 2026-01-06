package objectHierarchy;

public class Combat extends GameObj {
    private int damage;
    private int health;

    public int getDamage() {
        return damage;
    }

    public void setDamage (int damage) {
        this.damage = damage;
    }

    public int getHealth (){
        return health;
    }

    public void setHealth (int health){
        this.health = health;
    }
}
