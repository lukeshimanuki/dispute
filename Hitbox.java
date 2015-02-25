import java.lang.Math;
import java.awt.Graphics2D;

public class Hitbox
{
	public int x;
	public int y;
	public int r;

	public int active;

	public Hitbox(int x, int y, int r)
	{
		this.x = x;
		this.y = y;
		this.r = r;

		active = 0;
	}

	public void set(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public boolean touching(Player player)
	{
		return Math.pow(x - player.x, 2) + Math.pow(y - player.y, 2) < Math.pow(r + 40, 2); // 40 is radius of player
	}

	public void draw(Graphics2D graphics)
	{
		graphics.fillOval(x, y, r, r);
	}
}

