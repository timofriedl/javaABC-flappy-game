import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Player implements GameObject {
    private final FlappyGame game;
    private final double x;
    private double y;
    private double speedY;
    private final double radius;
    private Ellipse2D.Double shape;

    public Player(FlappyGame game, double x, double y, double radius) {
        this.game = game;
        this.x = x;
        this.y = y;
        speedY = 0.0;
        this.radius = radius;
        shape = makeShape();
    }

    private Ellipse2D.Double makeShape() {
        return new Ellipse2D.Double(x - radius, y - radius, 2.0 * radius, 2.0 * radius);
    }

    public void hop() {
        speedY = -7.0;
    }

    @Override
    public void tick() {
        speedY += game.getGravity();
        y += speedY;

        shape = makeShape();

        if (y - radius < 0.0) {
            speedY = 0.0;
            y = radius;
        } else if (y + radius > game.getDisplay().getHeight()) {
            game.end();
            return;
        }

        for (Obstacle obstacle : game.getObstacles()) {
            if (obstacle.collisionWith(this)) {
                game.end();
                return;
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.YELLOW);
        g.fill(shape);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2F));
        g.draw(shape);
    }

    public Ellipse2D.Double getShape() {
        return shape;
    }

    public double getX() {
        return x;
    }

    public double getRadius() {
        return radius;
    }
}
