import java.lang.Math;
import java.awt.Graphics2D;

public class Player
{
	public static int radius = 20;

	public int x;
	public int y;
	public float kbx; // knockback
	public float kby;

	public int damage;

	public float moveX; // voluntary movement
	public float moveY;

	public Hitbox hitbox = null;

	public Game context;
	private Controller controller;

	public Player(Game context, Controller controller)
	{
		this.context = context;
		this.controller = controller;

		x = 0;
		y = 400;
		kbx = 0;
		kby = 0;
		damage = 0;
		moveX = 0;
		moveY = 0;
	}

	public void update()
	{
		// act
		if (hitbox == null) // can only act if not attacking
		{
			switch (controller.direction)
			{
				case -1: moveX = -7; break;
				case 0: moveX = 0; break;
				case 1: moveX = 7; break;
			}
			if (controller.jump && context.floor.touching(x, y - 1, radius) && y > context.floor.y)
				moveY = 8;
			switch (controller.attack)
			{
				case 0: break;
				case 1: // body hitbox
					hitbox = new Hitbox(0, 0, true, 0, 0, 30, 5, 10, 12, 10, this);
					break;
				case 2: // projectile hitbox
					hitbox = new Hitbox(x + 25, y, false, 15, 0, 10, 5, 50, 0, 5, this);
					break;
			}
		}
		else // if attacking, then stop actions
		{
			moveX = 0;
		}

		// decrease knockback
		kbx *= .96f;
		kby *= .96f;
		if (kbx*kbx + kby*kby < 0.5f)
		{
			kbx = 0;
			kby = 0;
		}

		// fall
		moveY -= .3f; // only affects jumping

		float vx = kbx + moveX;
		float vy = kby + moveY;

		// check for floor
		Platform f = context.floor;
		if (vy < 0 && f.touching(x + (int)vx, y + (int)vy, radius))
		{
			moveY = 0;
			kby = 0;
			vy = 0;
		}
		
		x += vx;
		y += vy;

		// update hitbox
		if (hitbox != null)
		{
			hitbox.update();
			if (hitbox.state == -1)
				hitbox = null;
		}
	}

	public void draw(Graphics2D graphics)
	{
		graphics.drawOval(context.x(x - radius), context.y(y + radius), 2*radius, 2*radius);
		if (hitbox != null && hitbox.isActive())
			hitbox.draw(graphics);
	}
}

