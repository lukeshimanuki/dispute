import java.lang.Math;
import java.awt.Graphics2D;

public class Platform
{
	public int x = 0; // middle
	public int y = 0; // top
	public int width = 1000;
	// flat, so no height

	private Game context;
	private Sprite sprite;

	public Platform(Sprite sprite, Game context)
	{
		this.sprite = sprite;
		this.context = context;
	}

	// above before, but moving would go through it
	public boolean through(int x, int y, int r, float vy)
	{
		return x + r > this.x - width/2 && x - r < this.x + width/2 && y - r >= this.y && y - r + vy < this.y;
	}
	// above by one pixel
	public boolean touching(int x, int y, int r)
	{
		return through(x, y, r, -1);
	}

	public void draw(Graphics2D graphics)
	{
		sprite.draw(graphics, context.x(x), context.y(y));
	}
}
