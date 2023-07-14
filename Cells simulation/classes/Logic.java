import java.util.ArrayList;

public class Logic {

    private final int width;
    private final int height;

    private ArrayList<Cell> cell;
    private ArrayList<Food> food;

    private final int FOOD_RADIUS = 5;
    private final int CELL_RADIUS = 20;
    private int frame = 0;

    public Logic(int width, int height) {
        this.width = width;
        this.height = height;
        for(int i = 0; i < 1; i++) {
            Cell a = new Cell(0, (float)(Math.random() * (this.width - 100) + 50), (float)(Math.random() * (this.height - 100) + 50));
            cell.add(a);
        }
    }

    public void behavior() {
        for (Cell a : cell) {
            a.x += a.sx;
            a.y += a.sy;
            a.sx *= a.slip;
            a.sy *= a.slip;
            if(a.x < 0) {
                a.sx += 5;
            }
            else if(a.x > width) {
                a.sx -= 5;
            }
            if(a.y < 0) {
                a.sy += 5;
            }
            else if(a.y > height) {
                a.sy -= 5;
            }
            double targetAngle = Math.atan2(a.ty, a.tx);
            float rotationForMotion;
            if (targetAngle < 0) targetAngle = targetAngle + (float)(Math.PI * 2.0);
            if ((Math.abs(a.rotation - targetAngle) < a.rotationSpeed) || (Math.abs(a.rotation - targetAngle) > Math.PI * 2 - a.rotationSpeed)) {
                a.rotation = (float)targetAngle;
            }
            else if (((a.rotation < targetAngle) && (a.rotation + 3.1415f > targetAngle)) || ((a.rotation > targetAngle) && (a.rotation - 3.1415f > targetAngle))) {
                a.rotation += a.rotationSpeed;
            }
            else {
                a.rotation -= a.rotationSpeed;
            }
            if(a.rotation < 0) a.rotation += (float)(Math.PI * 2.0);
            else if(a.rotation > Math.PI * 2.0) a.rotation -= (float)(Math.PI * 2.0);
            rotationForMotion = a.rotation;
            if(a.tx * a.tx + a.ty * a.ty > 1) {
                a.sx += (float)Math.cos(rotationForMotion) * a.speed;
                a.sy += (float)Math.sin(rotationForMotion) * a.speed;
            }
            if(a.age > 50) {
                if (a.type == 0) {
                    Food closestFood = null;
                    float minFoodDist = (width * width) + (height * height);
                    for (Food f : food) {
                        if (f.toBeDeleted) continue;
                        float dist2 = (a.x - f.x) * (a.x - f.x) + (a.y - f.y) * (a.y - f.y);
                        if(dist2 < a.sightDistance * a.sightDistance) {
                            if (dist2 < minFoodDist) {
                                minFoodDist = dist2;
                                closestFood = f;
                            }
                        }
                    }
                    if (closestFood != null) {
                        a.tx = closestFood.x - a.x;
                        a.ty = closestFood.y - a.y;
                        if (minFoodDist < FOOD_RADIUS * FOOD_RADIUS + CELL_RADIUS * CELL_RADIUS) {
                            closestFood.toBeDeleted = true;
                            a.food++;
                        }
                    }
                    else {
                        if(Math.random() < a.directionChangeRate) {
                            double randomAngle = Math.random() * Math.PI * 2;
                            a.tx = (float)Math.cos(randomAngle) * 2;
                            a.ty = (float)Math.sin(randomAngle) * 2;
                        }
                    }
                } else if (a.type == 1) {
                    Cell closestFood = null;
                    float minFoodDist = (width * width) + (height * height);
                    for (Cell b : cell) {
                        if (b.toBeDeleted) continue;
                        if (b.type != 0) continue;
                        if (b.food > 3) continue;
                        float dist2 = (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
                        if(dist2 < a.sightDistance * a.sightDistance) {
                            if (dist2 < minFoodDist) {
                                minFoodDist = dist2;
                                closestFood = b;
                            }
                        }
                    }
                    if (closestFood != null) {
                        a.tx = closestFood.x - a.x;
                        a.ty = closestFood.y - a.y;
                        if (minFoodDist < CELL_RADIUS * CELL_RADIUS + CELL_RADIUS * CELL_RADIUS) {
                            closestFood.toBeDeleted = true;
                            a.food += closestFood.food * 0.1f;
                        }
                    }
                    else {
                        if(Math.random() < a.directionChangeRate) {
                            double randomAngle = Math.random() * Math.PI * 2;
                            a.tx = (float)Math.cos(randomAngle) * 2;
                            a.ty = (float)Math.sin(randomAngle) * 2;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < cell.size(); i++) {
            Cell a = cell.get(i);
            if(a.food >= 6) {
                a.food -= 3;
                int type = a.type;
                if(Math.random() < 0.2) {
                    type = (int)(Math.random() * 2);
                }
                Cell b = new Cell(type, a.x + (float)Math.random() * 10 - 5, a.y + (float)Math.random() * 10 - 5);
                    b.speed = a.speed;
                    b.slip = a.slip;
                cell.add(b);
            }
            if(a.food <= 0) {
                a.toBeDeleted = true;
            }
            else {
                if(a.age % 300 == 299) {
                    a.food -= 0.2f;
                }
                a.age++;
            }
            if(a.toBeDeleted) {
                cell.remove(i);

                i--;
            }
        }
        for (int i = 0; i < food.size(); i++) {
            if(food.get(i).toBeDeleted) {
                food.remove(i);
                i--;
            }
        }
        if(frame % 1000 == 0) {
            Food a = new Food((float)(Math.random() * (width - 100) + 50), (float)(Math.random() * (height - 100) + 50));
            food.add(a);
        }
        frame++;
    }
    
    public ArrayList<Cell> get_Acell() {
        return this.cell;
    }

    public ArrayList<Food> get_Afood() {
        return this.food;
    }
}