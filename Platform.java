import java.lang.Math;
import java.awt.Graphics2D;

public class Platform
{
	public int x = 0; // middle
	public int y = 0; // top
	public int width = 800;
	public int height = 50;

	private Game context;

	public Platform(Game context)
	{
		this.context = context;
	}

	public boolean touching(int x, int y, int r)
	{
		return x + r > this.x - width/2 && x - r < this.x + width/2 && y - r < this.y && y + r > this.y - height;
	}

	public void draw(Graphics2D graphics)
	{
		graphics.drawRect(context.x(x - width/2), context.y(y), width, height);
	}
}
