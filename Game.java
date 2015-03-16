import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.lang.Math;

public class Game extends JPanel
{
	public Player[] players;

	public Platform floor;

	public JFrame frame;

	public int centerX = 0;
	public int centerY = 0;

	public int x(int gameX)
	{
		return centerX + gameX;
	}
	public int y(int gameY)
	{
		return centerY - gameY;
	}

	public Sprite spritePlayer1;
	public Sprite spritePlayer2;
	public Sprite spriteA1;
	public Sprite spriteA2;
	public Sprite spriteA3;
	public Sprite spriteA4;
	public Sprite spriteStage;
	public Sprite spriteBackground;

	public Game()
	{
		// load images
		spritePlayer1 = new Sprite("player1.png", 40, 40, 20, 20);
		spritePlayer2 = new Sprite("player2.png", 40, 40, 20, 20);
		spriteStage = new Sprite("stage.png", 800, 120, 400, 0);
		spriteBackground = new Sprite("background.png", 1920, 1080, 960, 540);
		spriteA1 = new Sprite("a1.png", 120, 120, 60, 60);
		spriteA2 = new Sprite("a2.png", 30, 30, 15, 15);
		spriteA3 = new Sprite("a3.png", 30, 30, 15, 15);
		spriteA4 = new Sprite("a4.png", 30, 30, 15, 15);

		// set up players
		players = new Player[2];
		Keyboard keyboard1 = new Keyboard(KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_PERIOD, KeyEvent.VK_COMMA, KeyEvent.VK_SPACE, KeyEvent.VK_M);
		Player k = new Player(this, keyboard1, spritePlayer1, -300, 100);
		Keyboard keyboard2 = new Keyboard(KeyEvent.VK_D, KeyEvent.VK_A, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_E, KeyEvent.VK_Q, KeyEvent.VK_Z, KeyEvent.VK_X);
		Player k2 = new Player(this, keyboard2, spritePlayer2, 300, 100);
		Computer cpu = new Computer();
		Player c = new Player(this, cpu, spritePlayer1, -300, 100);
		Computer cpu2 = new Computer();
		Player c2 = new Player(this, cpu2, spritePlayer2, 300, 100);

		players[0] = c;
		players[1] = c2;
		cpu.set(players[0],players[1]);
		cpu2.set(players[1], players[0]);

		floor = new Platform(spriteStage, this);

		frame = new JFrame();
		frame.getContentPane().add(this);
		frame.setSize(300, 200);
		frame.setVisible(true);

		// set up key listener
		addKeyListener(keyboard1);
		addKeyListener(keyboard2);
		setFocusable(true);

		// get origin
		centerX = frame.getSize().width / 2;
		centerY = frame.getSize().height / 2;
	}

	public Game(Controller c1, Controller c2, boolean useGraphics) // for training
	{
		if (useGraphics)
		{
			// load images
			spritePlayer1 = new Sprite("player1.png", 40, 40, 20, 20);
			spritePlayer2 = new Sprite("player2.png", 40, 40, 20, 20);
			spriteStage = new Sprite("stage.png", 800, 120, 400, 0);
			spriteBackground = new Sprite("background.png", 1920, 1080, 960, 540);
			spriteA1 = new Sprite("a1.png", 120, 120, 60, 60);
			spriteA2 = new Sprite("a2.png", 30, 30, 15, 15);
			spriteA3 = new Sprite("a3.png", 30, 30, 15, 15);
			spriteA4 = new Sprite("a4.png", 30, 30, 15, 15);
		}
		// set up players
		players = new Player[2];
		players[0] = new Player(this, c1, spritePlayer1, -300, 100);
		players[1] = new Player(this, c2, spritePlayer2, 300, 100);

		floor = new Platform(spriteStage, this);

		// set up graphics
		if (useGraphics)
		{
			frame = new JFrame();
			frame.getContentPane().add(this);
			frame.setSize(300, 200);
			frame.setVisible(true);

			setFocusable(true);

			// get origin
			centerX = frame.getSize().width / 2;
			centerY = frame.getSize().height / 2;
		}
	}

	public void update()
	{
		// update
		for (Player player : players)
		{
			player.update();
		}

		// collisions
		for (Player player : players)
		{
			for (Player opponent : players)
			{ // check player with each of opponent's hitboxes
				if (player == opponent) continue;
				Hitbox hitbox = opponent.hitbox;
				if (hitbox == null) continue;
				if (hitbox.isActive() == false) continue;
				if (hitbox.touching(player))
				{
					// damage
					player.damage += hitbox.damage;
					// knockback (away from hitbox)
					player.kbx = hitbox.trajectoryX(player) * player.damage * hitbox.kb;
					player.kby = hitbox.trajectoryY(player) * player.damage * hitbox.kb;
					// turn off hitbox
					hitbox.deactivate();
				}
			}
		}
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D graphics = (Graphics2D) g;
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
		
		int width = frame.getSize().width;
		int height = frame.getSize().height;

		// update origin
		centerX = frame.getSize().width / 2;
		centerY = frame.getSize().height / 2;

		// draw
		// background
		spriteBackground.draw(graphics, centerX, centerY);
		// players
		for (Player player : players)
		{
			player.draw(graphics);
		}
		// floor
		floor.draw(graphics);
	}
}

