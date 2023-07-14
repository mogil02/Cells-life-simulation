import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;

public class Window extends JFrame implements Runnable {

    private final int w = 1600;
    private final int h = 900;
    private Logic logic;
    private final int FRAMES_TOTAL = 100000;
    private final int SKIP_FRAMES = 1;

    private final Color BG = new Color(15, 70, 80, 255);
    private BufferedImage buf = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    private BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    private BufferedImage graph = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    private BufferedImage sprites[] = new BufferedImage[2];
    private final AffineTransform IDENTITY = new AffineTransform();
    private final int FOOD_RADIUS = 5;
    private final int CELL_RADIUS = 20;
    private int frame = 0;

    public Window() {
        super("Симуляция поведения клеток");
        ImageIcon imageIcon = new ImageIcon("source/m0.png");
        setIconImage(imageIcon.getImage());
        for (int i = 0; i < sprites.length; i++) {
            try {
                sprites[i] = ImageIO.read(new File("source/m" + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.setSize(w + 16, h + 38);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(50, 50);
        this.add(new JLabel(new ImageIcon(img)));
        logic = new Logic(w, h);
    }

    @Override
    public void run() {
        while(frame < FRAMES_TOTAL) {
            this.repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        try {
            drawScene(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < SKIP_FRAMES; i++)
            logic.behavior();
        Graphics2D g2 = buf.createGraphics();
        g2.drawImage(img, null, 0, 0);
        g2.drawImage(graph, null, 0, 0);
        ((Graphics2D)g).drawImage(buf, null, 8, 30);
    }

    private void drawScene(BufferedImage image) throws IOException {
        Graphics2D g2 = image.createGraphics();
        g2.setColor(BG);
        g2.fillRect(0, 0, w, h);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Food a : logic.get_Afood()) {
            g2.setColor(Food.COLOR);
            g2.fillOval((int) a.x - FOOD_RADIUS, (int) a.y - FOOD_RADIUS, FOOD_RADIUS * 2, FOOD_RADIUS * 2);
        }
        float cellScale = CELL_RADIUS * 0.01f;
        for (Cell a : logic.get_Acell()) {
            float sw = sprites[a.type].getWidth() * 0.5f * cellScale;
            float sh = sprites[a.type].getHeight() * 0.5f * cellScale;
            AffineTransform trans = new AffineTransform();
            trans.setTransform(IDENTITY);
            trans.translate(a.x - sw, a.y - sh);
            trans.rotate(a.rotation + Math.PI / 2, sw, sh);
            trans.scale(cellScale, cellScale);
            g2.drawImage(sprites[a.type], trans, this);
        }
    }
}