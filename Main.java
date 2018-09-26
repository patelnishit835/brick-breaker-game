import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Main {

	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		
		Gameplay game = new Gameplay();
		
		frame.setBounds(10,10,700,600);
		frame.setTitle("Angry Ball");
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(game);

	}

}

class Gameplay extends JPanel implements KeyListener,ActionListener{
	
	private boolean play = false;
	private int score = 0;
	
	private int totalBricks = 21;
	private Timer timer;
	private int delay = 8;
	
	private int sliderX = 310;
	
	private int ballX =120;
	private int ballY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	private MapGen map;
	
	public Gameplay() {
		
		map = new MapGen(3,7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay,this);
		timer.start();
		
	}
	
	public void paint(Graphics g) {
		//FOR THE BACKGROUND
		g.setColor(Color.BLACK);
		g.fillRect(1, 1, 692, 592);
		
		//FOR THE BRICKS
		map.draw((Graphics2D)g);
		
		
		//FOR THE BORDERS
		//NOTE :We will create only 3 borders on the right,left and the top side of the frame.
		//There won't be any border on the bottom of the frame.
		
		g.setColor(Color.red);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		//FOR SCORES
		g.setColor(Color.green);
		g.setFont(new Font("algerian",Font.BOLD,25));
		g.drawString(""+score, 590, 30);
		
		//FOR THE SLIDER
		g.setColor(Color.orange);
		g.fillRect(sliderX, 550, 100, 8);
		
		//FOR THE SLIDER
		g.setColor(Color.red);
		g.fillOval(ballX, ballY, 20, 20);
		
		if(ballY > 570)
		{
			play = false;
			g.setColor(Color.GREEN);
			g.setFont(new Font("algerian",Font.BOLD,30));
			g.drawString("GAME OVER!!", 190, 300);
			
			g.setFont(new Font("algerian",Font.BOLD,20));
			g.drawString("Press Enter to restart the GAME", 230, 350);
		}
		
		if(totalBricks == 0) {
			play = false;
			g.setFont(new Font("algerian",Font.BOLD,20));
			g.drawString("CONGRATS!!! YOU WON!!", 230, 350);
		}
		
		g.dispose();
	}

	public void actionPerformed(ActionEvent e) {
		timer.start();
		
		if(play) {
			
			if(new Rectangle(ballX,ballY,20,20).intersects(new Rectangle(sliderX,550,100,8))) {
				ballYdir = -ballYdir;
			}
			
			Label: for(int i=0;i<map.map.length;i++) {
				for(int j=0;j<map.map[0].length;j++) {
					if(map.map[i][j] > 0)
					{
						int X = j * map.width + 80;
						int Y = i * map.height + 50;
						int height = map.height;
						int width = map.width;
						
						Rectangle brickRect = new Rectangle(X,Y,width,height);
						Rectangle ballRect = new Rectangle(ballX,ballY,20,20);
						
						if(ballRect.intersects(brickRect)) 
						{
							map.setValBrick(0, i, j);
							totalBricks--;
							score += 50;
							
							if(ballX + 19 <= brickRect.x || ballX + 1 >= brickRect.x + brickRect.width)
							{
								ballXdir = -ballXdir;
							}
							else
							{
								ballYdir = -ballYdir;
							}
							break Label;
						}
					}
				}
			}
			
			ballX += ballXdir;
			ballY += ballYdir;
			
			if(ballX < 0) {
				ballXdir = -ballXdir;
			}
			if(ballY < 0) {
				ballYdir = -ballYdir;
			}
			if(ballX > 670) {
				ballXdir = -ballXdir;
			}
		}
		repaint();
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(sliderX >= 600) {
				sliderX = 600;
			}
			else {
				moveRight();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(sliderX < 10) {
				sliderX = 10;
			}
			else {
				moveLeft();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			if(!play)
			{
				play = true;
				ballX = 120;
				ballY = 350;
				ballXdir = -1;
				ballYdir = -2;
				sliderX = 310;
				score = 0;
				totalBricks = 21;
				map = new MapGen(3,7);
				
				repaint();
			}
		}
	}
	
	public void moveRight() {
		play = true;
		sliderX+=20;
	}
	
	public void moveLeft() {
		play = true;
		sliderX-=20;
	}

	public void keyReleased(KeyEvent arg0) {}

	public void keyTyped(KeyEvent arg0) {}
	

}

class MapGen {

	public int map[][];
	public int width;
	public int height;
	
	public MapGen(int row,int col) {
		map = new int[row][col];
		
		for(int i=0;i<map.length;i++) {
			for(int j=0;j<map[0].length;j++) {
				map[i][j] = 1;
			}
		}
		
		height = 150/row;
		width = 540/col;
		}
	
	public void draw(Graphics2D g) {
		for(int i=0;i<map.length;i++) {
			for(int j=0;j<map[0].length;j++) {
				if(map[i][j] > 0) {
					g.setColor(Color.white);
					g.fillRect(j * width + 80,i * height + 50, width, height);
					
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.BLACK);
					g.drawRect(j * width + 80,i * height + 50, width, height);
					
				}
			}
		}
	}
	
	public void setValBrick(int val,int row,int col) {
		map[row][col] = val;
	}
	

}