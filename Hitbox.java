import java.lang.Math;
import java.awt.Graphics2D;

public class Hitbox
{
	public int x;
	public int y;
	public float vx;
	public float vy;
	public boolean fall;
	public int r;
	public boolean relative;

	private int ax; // changes based on whether relative is set
	private int ay;

	private int startup;
	private int active;
	public int endlag; // how long player waits after starting attack
	public int state = 0;

	public int damage;

	private Player player;
	private Game context;
	private Sprite sprite;

	public Hitbox(int x, int y, boolean relative, float vx, float vy, boolean fall, int r, int startup, int active, int endlag, int damage, Sprite sprite, Player player)
	{
		this.x = x;
		this.y = y;
		this.relative = relative;
		this.vx = vx;
		this.vy = vy;
		this.fall = fall;
		this.r = r;

		this.startup = startup;
		this.active = active;
		this.endlag = endlag;

		this.damage = damage;

		this.player = player;
		this.context = player.context;
		this.sprite = sprite;
	}

	public void update()
	{
		if (state != -1) // if started
			state++;
		if (state > startup + active) // if ended
			state = -1;

		if (state == startup) // just became active
		{ // so set its initial position if not relative
			if (!relative)
			{
				x += player.x;
				y += player.y;
			}
		}

		if (isActive())
		{
			if (fall)
				vy -= .3f;

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
		return state > startup && state < startup + active;
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
		if (r == 0) return 0; // default is up
		return dx / r;
	}
	public float trajectoryY(Player p)
	{
		float dx = p.x - ax;
		float dy = p.y - ay;
		float r = (float)Math.sqrt(dx*dx + dy*dy);
		if (r == 0) return 1; // default is up
		return dy / r;
	}

	public void draw(Graphics2D graphics)
	{
		sprite.draw(graphics, context.x(ax), context.y(ay));
	}
}

