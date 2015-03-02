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

	private boolean freefall = false;

	public Hitbox hitbox = null;

	public Game context;
	private Controller controller;
	private Sprite sprite;

	public Player(Game context, Controller controller, Sprite sprite)
	{
		this.context = context;
		this.controller = controller;
		this.sprite = sprite;

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
		if (context.floor.touching(x, y, radius))
			freefall = false;
		if (!freefall) // can't do anything but move in free fall
		{
			if (hitbox == null || hitbox.state > hitbox.endlag) // can't move (on ground) or jump while attacking
			{
				// jump
				if (controller.jump && context.floor.touching(x, y, radius))
					moveY = 16;
			}
			if (hitbox == null) // can't attack while any hitbox is out
			{
				// attack
				switch (controller.attack)
				{
					case 0: break;
					case 1: // body hitbox
						hitbox = new Hitbox(0, 0, true, 0, 0, false, 30, 5, 10, 20, 10, context.spriteA1, this);
						break;
					case 2: // right projectile hitbox
						hitbox = new Hitbox(25, 0, false, 10, 0, false, 15, 5, 50, 20, 5, context.spriteA2, this);
						break;
					case 3: // left projectile hitbox
						hitbox = new Hitbox(25, 0, false, -10, 0, false, 15, 5, 50, 20, 5, context.spriteA3, this);
						break;
					case 4: // bomb hitbox
						hitbox = new Hitbox(0, 25, false, 0, 10, true, 15, 5, 70, 20, 15, context.spriteA4, this);
						break;
					case 5: // recovery
						moveY = 24;
						freefall = true;
						break;
				}
			}
			else // if attacking, stop moving (on the ground)
				moveX = 0;
		}
		// move (if either in air or not attacking)
		if (hitbox == null || hitbox.state > hitbox.endlag || !context.floor.touching(x, y, radius))
		{
			switch (controller.direction)
			{
				case -1: moveX = -7; break;
				case 0: moveX = 0; break;
				case 1: moveX = 7; break;
			}
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
		moveY -= .65f; // only affects jumping

		float vx = kbx + moveX;
		float vy = kby + moveY;

		// check for floor
		Platform f = context.floor;
		if (vy < 0 && f.through(x, y, radius, vy))
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

		// process ko
		if (y < -800)
		{
			x = 0;
			y = 400;
			moveY = 0;
			moveX = 0;
			kby = 0;
			kbx = 0;
			damage = 0;
		}
	}

	public void draw(Graphics2D graphics)
	{
		sprite.draw(graphics, context.x(x), context.y(y));
		if (hitbox != null && hitbox.isActive())
			hitbox.draw(graphics);
	}
}

