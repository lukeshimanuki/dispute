import java.lang.Math;
import java.awt.Graphics2D;

public class Player
{
	public int x;
	public int y;
	public float kbx; // knockback
	public float kby;
	public boolean direction;

	public int damage;

	public float moveX; // voluntary movement
	public float moveY;

	public Hitbox hitboxes[];

	private Game context;
	private Controller controller;

	public Player(Game context, Controller controller)
	{
		this.context = context;
		this.controller = controller;

		x = 800;
		y = 0;
		kbx = 0;
		kby = 0;
		direction = false;
		damage = 0;
		moveX = 0;
		moveY = 0;
		hitboxes = new Hitbox[1];
		hitboxes[0] = new Hitbox(x, y, 40);
	}

	public void update()
	{
		// act
		switch (controller.direction)
		{
			case -1: moveX = -7; break;
			case 0: moveX = 0; break;
			case 1: moveX = 7; break;
		}
		if (controller.jump && x > 400 && x < 1200 && y + 40 < 610 && y + 40 > 590)
			moveY = -8;
		switch (controller.attack)
		{
			case 0: break;
			case 1: // body hitbox
				hitboxes[0].active = 20;
				break;
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
		moveY += .3f; // only affects jumping

		float vx = kbx + moveX;
		float vy = kby + moveY;

		// check for floor
		if (x > 400 && x < 1200 && vy > 0 && y + vy + 40 > 600 && y + vy - 40 < 650)
		{
			moveY = 0;
			kby = 0;
			vy = 0;
		}
		
		x += vx;
		y += vy;

		// update hitboxes
		hitboxes[0].set(x, y);
		for (Hitbox hitbox : hitboxes)
			if (hitbox.active > 0)
				hitbox.active--;
	}

	public void draw(Graphics2D graphics)
	{
		graphics.drawOval(x, y, 40, 40);
		for (Hitbox hitbox : hitboxes)
			if (hitbox.active > 0)
				hitbox.draw(graphics);
	}
}

