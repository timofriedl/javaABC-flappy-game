import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FlappyGame extends JFrame implements GameObject, KeyListener {
    private final Display display;
    private Player player;
    private final double gravity;
    private boolean gameOver;
    private double speed;
    private double progress;
    private final List<Obstacle> obstacles;
    private int score;
    private boolean reset;

    public FlappyGame() {
        super("Flappy Game");
        setSize(500, 500);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addKeyListener(this);

        display = new Display(this);
        obstacles = new CopyOnWriteArrayList<>();
        reset();
        gravity = 0.4;
        gameOver = true;

        setVisible(true);
        startLoop();
    }

    private void reset() {
        player = new Player(this, 150.0, 200.0, 15.0);
        obstacles.clear();
        speed = 2.0;
        progress = 200.0;
        score = 0;
        reset = true;
    }

    private void startLoop() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            tick();
            display.repaint();
        }, 0L, 1000L / 60L, TimeUnit.MILLISECONDS);
    }

    public void end() {
        gameOver = true;
        reset = false;
        new Thread(() -> {
            JOptionPane.showMessageDialog(this, "You Lost!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            reset();
        }).start();
    }

    private void addRandomObstacle() {
        double gapHeight = 150.0;
        double gapY = Math.random() * (display.getHeight() - gapHeight);
        obstacles.add(new Obstacle(this, display.getWidth(), gapHeight, gapY, 40.0));
    }

    public void incrementScore() {
        score++;
    }

    @Override
    public void tick() {
        if (gameOver)
            return;

        speed += 0.001;
        progress += speed;

        if (progress >= 200.0) {
            addRandomObstacle();
            progress = 0.0;
        }

        obstacles.forEach(Obstacle::tick);
        player.tick();
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, display.getWidth(), display.getHeight());

        obstacles.forEach(o -> o.render(g));
        player.render(g);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Monospace", Font.BOLD, 48));
        g.drawString(Integer.toString(score), 20, 50);
    }

    public static void main(String[] args) {
        new FlappyGame();
    }

    public double getGravity() {
        return gravity;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // ignore
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (reset) {
                gameOver = false;
            }
            player.hop();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // ignore
    }

    public Display getDisplay() {
        return display;
    }

    public double getSpeed() {
        return speed;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public Player getPlayer() {
        return player;
    }
}