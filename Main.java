import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main
{
	public static void main(String[] args) throws InterruptedException
	{
		Game game = new Game();
		while (true)
		{
			game.update();
			game.repaint();
			Thread.sleep(16); // 60 fps
		}
	}
}

