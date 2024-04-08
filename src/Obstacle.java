import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Obstacle implements GameObject {
    private final FlappyGame game;
    private double x;
    private final double gapHeight;
    private final double gapY;
    private final double width;

    private Rectangle2D.Double upperRectangle;
    private Rectangle2D.Double lowerRectangle;

    private boolean passed;

    public Obstacle(FlappyGame game, double x, double gapHeight, double gapY, double width) {
        this.game = game;
        this.x = x;
        this.gapHeight = gapHeight;
        this.gapY = gapY;
        this.width = width;
        upperRectangle = makeUpperRectangle();
        lowerRectangle = makeLowerRectangle();
    }

    private Rectangle2D.Double makeUpperRectangle() {
        return new Rectangle2D.Double(x, -5.0, width, gapY + 5.0);
    }

    private Rectangle2D.Double makeLowerRectangle() {
        return new Rectangle2D.Double(x, gapY + gapHeight, width, game.getDisplay().getHeight() - gapHeight - gapY + 5.0);
    }

    @Override
    public void tick() {
        x -= game.getSpeed();

        if (!passed && game.getPlayer().getX() - game.getPlayer().getRadius() > x + width) {
            game.incrementScore();
            passed = true;
        }

        if (x + width < 0.0) {
            game.getObstacles().remove(this);
        }

        upperRectangle = makeUpperRectangle();
        lowerRectangle = makeLowerRectangle();
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(new Color(80, 200, 0));
        g.fill(upperRectangle);
        g.fill(lowerRectangle);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2F));
        g.draw(upperRectangle);
        g.draw(lowerRectangle);
    }

    public boolean collisionWith(Player player) {
        return player.getShape().intersects(upperRectangle)
                || player.getShape().intersects(lowerRectangle);
    }
}
