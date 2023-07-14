import java.awt.*;

public class Food {

    public int type;
    public float x;
    public float y;
    public boolean toBeDeleted;

    public static Color COLOR = new Color(255, 0, 0);

    public Food(float x, float y) {
        this.x = x;
        this.y = y;
        this.type = (int)(Math.random() * 6);
        this.toBeDeleted = false;
    }
}