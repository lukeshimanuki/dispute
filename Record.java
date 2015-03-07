import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.io.File;
import java.io.PrintWriter;

public class Record
{
	public static void main(String[] args) throws Exception
	{
		PrintWriter in = new PrintWriter("input", "UTF-8");
		PrintWriter out = new PrintWriter("output", "UTF-8");

		Game game = new Game();
		while (true)
		{
			// play
			game.update();
			game.repaint();
			Thread.sleep(16); // 60 fps
			
			// record behavior
			Player p = game.players[0];
			Player o = game.players[1];
			Platform f = p.context.floor;
			float[] input = {
				// for player
				(float)(p.x - (f.x - f.width/2)) / f.width, // dist from left
				(float)(p.x - (f.x + f.width/2)) / f.width, // dist from right
				(float)(p.y - f.y) / 500, // height
				p.kbx / 20, // knockback
				p.kby / 20,
				p.moveX / 20, // movement
				p.moveY / 20, // jumping / falling
				(float)p.damage / 100, // damage
				// for opponent
				(float)(o.x - (f.x - f.width/2)) / f.width, // dist from left
				(float)(o.x - (f.x + f.width/2)) / f.width, // dist from right
				(float)(o.y - f.y) / 500, // height
				o.kbx / 20, // knockback
				o.kby / 20,
				o.moveX / 20, // movement
				o.moveY / 20, // jumping / falling
				(float)o.damage / 100, // damage
				// relative
				(float)(p.x - o.x) / f.width, // horizontal distance from opponent
				(float)(p.y - o.y) / 500 // vertical distance from opponent
			};
			Controller c = p.controller;
			if (!c.jump && c.attack == 0 && c.direction == 0) continue; // don't record if not acting
			float[] output = new float[8];
			if (c.direction == 1) output[0] = 1;
			else if (c.direction == -1) output[1] = 1;
			if (c.jump) output[2] = 1;
			output[2 + c.attack] = 1;

			// write to file
			for (int i = 0; i < input.length; i++)
				in.print((int)(input[i] * 256) + " ");
			in.print("\n");
			in.flush();
			for (int i = 0; i < output.length; i++)
				out.print((int)(output[i] * 256) + " ");
			out.print("\n");
			out.flush();
		}
	}
}

