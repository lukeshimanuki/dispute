import java.lang.Math;
import java.awt.Graphics2D;

public class Hitbox
{
	public int x;
	public int y;
	public int vx;
	public int vy;
	public int r;
	public boolean relative;

	private int ax; // changes based on whether relative is set
	private int ay;

	private int startup;
	private int active;
	private int endlag;
	public int state = 0;

	public int damage;

	private Player player;
	private Game context;

	public Hitbox(int x, int y, boolean relative, int vx, int vy, int r, int startup, int active, int endlag, int damage, Player player)
	{
		this.x = x;
		this.y = y;
		this.relative = relative;
		this.vx = vx;
		this.vy = vy;
		this.r = r;

		this.startup = startup;
		this.active = active;
		this.endlag = endlag;

		this.damage = damage;

		this.player = player;
		this.context = player.context;
	}

	public void update()
	{
		if (state != -1) // if started
			state++;
		if (state > startup + active + endlag) // if ended
			state = -1;

		if (isActive())
		{
			x += vx;
			y += vy;

			ax = x;
			ay = y;
			if (relative)
			{
				ax += player.x;
				ay += player.y;
			}
		}
	}
	public boolean isActive()
	{
		if (state > startup && state < startup + active)
			return true;
		else
			return false;
	}

	public void activate()
	{
		if (state == -1)
			state = 0;
	}
	public void deactivate()
	{
		state = -1;
	}

	public boolean touching(Player p)
	{
		return Math.pow(ax - p.x, 2) + Math.pow(ay - p.y, 2) < Math.pow(r + Player.radius, 2);
	}

	public float trajectoryX(Player p)
	{
		float dx = p.x - ax;
		float dy = p.y - ay;
		float r = (float)Math.sqrt(dx*dx + dy*dy);
		return dx / r;
	}
	public float trajectoryY(Player p)
	{
		float dx = p.x - ax;
		float dy = p.y - ay;
		float r = (float)Math.sqrt(dx*dx + dy*dy);
		return dy / r;
	}

	public void draw(Graphics2D graphics)
	{
		graphics.fillOval(context.x(ax - r), context.y(ay + r), 2*r, 2*r);
	}
}

