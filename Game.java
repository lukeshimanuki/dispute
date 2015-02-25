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

	private JFrame frame;

	public Game()
	{
		players = new Player[2];
		Keyboard keyboard1 = new Keyboard(KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_DOWN);
		players[0] = new Player(this, keyboard1);
		Keyboard keyboard2 = new Keyboard(KeyEvent.VK_D, KeyEvent.VK_A, KeyEvent.VK_W, KeyEvent.VK_S);
		players[1] = new Player(this, keyboard2);


		frame = new JFrame();
		frame.getContentPane().add(this);
		frame.setSize(300, 200);
		frame.setVisible(true);

		// set up key listener
		addKeyListener(keyboard1);
		addKeyListener(keyboard2);
		setFocusable(true);
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
				for (Hitbox hitbox : opponent.hitboxes)
				{
					if (hitbox.active == 0) continue;
					if (hitbox.touching(player))
					{
						// damage
						player.damage += 10;
						// knockback (away from hitbox)
						float knockback = 0.1f;
						float dx = player.x - hitbox.x;
						float dy = player.y - hitbox.y;
						float r = (float)Math.sqrt(dx*dx + dy*dy);
						dx /= r;
						dy /= r;
						player.kbx = dx * player.damage * knockback;
						player.kby = dy * player.damage * knockback;
						// turn off hitbox
						hitbox.active = 0;
					}
				}
			}
			System.out.print(player.damage + " ");
		}
		System.out.print("\n");
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

		// draw
		// players
		for (Player player : players)
		{
			player.draw(graphics);
		}
		// floor
		int floorWidth = 800;
		int floorHeight = 50;
		graphics.drawRect(400, 600, 800, 50);
//		graphics.drawRect(width / 2 - floorWidth / 2, height / 2 - floorHeight / 2, floorWidth, floorHeight);
	}
}

