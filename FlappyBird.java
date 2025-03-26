import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class FlappyBird {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}

class GamePanel extends JPanel implements ActionListener {
    // Game variables
    int birdY = 250;
    int birdVelocity = 0;
    int gravity = 1;
    int score = 0;
    int groundX = 0;
    
    List<Integer> pipeXs = new ArrayList<>();
    List<Integer> pipeHeights = new ArrayList<>();
    Random random = new Random();
    
    javax.swing.Timer gameTimer;
    javax.swing.Timer pipeTimer;
    
    public GamePanel() {
        // Initialize pipes
        pipeXs.add(400);
        pipeHeights.add(random.nextInt(200) + 100);
        
        gameTimer = new javax.swing.Timer(20, this);
        gameTimer.start();
        
        // Add pipe every 1.5 seconds
        pipeTimer = new javax.swing.Timer(1500, e -> {
            pipeXs.add(400);
            pipeHeights.add(random.nextInt(200) + 100);
        });
        pipeTimer.start();
        
        // Add key listener for bird control
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    birdVelocity = -15; // Jump
                }
            }
        });
        this.setFocusable(true);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw background
        g.setColor(new Color(135, 206, 235)); // Sky blue
        g.fillRect(0, 0, 400, 600);
        
        // Draw pipes
        g.setColor(new Color(34, 139, 34)); // Forest green
        for (int i = 0; i < pipeXs.size(); i++) {
            int pipeHeight = pipeHeights.get(i);
            g.fillRect(pipeXs.get(i), 0, 50, pipeHeight);
            g.fillRect(pipeXs.get(i), pipeHeight + 150, 50, 600 - pipeHeight - 150);
        }
        
        // Draw ground
        g.setColor(new Color(222, 184, 135)); // Burlywood
        g.fillRect(0, 550, 400, 50);
        g.setColor(new Color(139, 69, 19)); // Saddle brown
        g.fillRect(groundX, 550, 400, 10);
        g.fillRect(groundX + 400, 550, 400, 10);
        
        // Draw bird
        g.setColor(Color.orange);
        g.fillRect(100, birdY, 30, 30);
        g.setColor(Color.red);
        g.fillRect(125, birdY + 10, 10, 10); // Beak
        
        // Draw score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("" + score, 180, 100);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Game logic
        birdVelocity += gravity;
        birdY += birdVelocity;
        groundX -= 5;
        
        if (groundX <= -400) {
            groundX = 0;
        }
        
        // Move pipes and check for score
        for (int i = 0; i < pipeXs.size(); i++) {
            pipeXs.set(i, pipeXs.get(i) - 5);
            
            // Score when bird passes pipe
            if (pipeXs.get(i) == 100 - 50 - 5) {
                score++;
            }
            
            // Remove pipes that are off screen
            if (pipeXs.get(i) < -50) {
                pipeXs.remove(i);
                pipeHeights.remove(i);
                i--;
            }
        }
        
        // Collision detection
        if (birdY > 520 || birdY < 0) {
            gameOver();
        }
        
        for (int i = 0; i < pipeXs.size(); i++) {
            int pipeHeight = pipeHeights.get(i);
            if (100 + 30 > pipeXs.get(i) && 100 < pipeXs.get(i) + 50) {
                if (birdY < pipeHeight || birdY + 30 > pipeHeight + 150) {
                    gameOver();
                }
            }
        }
        
        repaint();
    }
    
    void gameOver() {
        gameTimer.stop();
        pipeTimer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Score: " + score);
        // Reset game
        birdY = 250;
        birdVelocity = 0;
        pipeXs.clear();
        pipeHeights.clear();
        pipeXs.add(400);
        pipeHeights.add(random.nextInt(200) + 100);
        score = 0;
        gameTimer.start();
        pipeTimer.start();
    }
}