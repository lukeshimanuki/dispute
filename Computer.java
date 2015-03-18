import java.lang.Math;
import java.util.Scanner;
import java.io.File;

public class Computer extends Controller
{
	private Player player, opponent;
	private Game game;

	private float[] weights;

	private int numInput = 96, numHidden = 64, numOutput = 4;

	// initialize with random from -1 to 1
	public Computer()
	{
		weights = new float[numInput*numHidden + numHidden*numOutput];
		for (int i = 0; i < weights.length; i++)
			this.weights[i] = 2*(float)Math.random() - 1;
	}

	// store reference to array
	public Computer(float[] weights)
	{
		this.weights = weights;
	}

	// read weights (file contains integers from -255 to 255; convert to [-1, 1])
	public Computer(String weightsFilename)
	{
		this.weights = new float[numInput*numHidden + numHidden*numOutput];

		Scanner file = null;
		try
		{
			file = new Scanner(new File(weightsFilename));
		}
		catch (Exception e)
		{
		}

		for (int i = 0; i < numInput*numHidden + numHidden*numOutput; i++)
			this.weights[i] = (float)file.nextInt() / 255;
	}

	public void set(Player player, Player opponent)
	{
		this.player = player;
		this.opponent = opponent;

		game = player.context;
		
		
		player.moveX = 1; // so it never stands still
	}

	// keep away and shoot
	private void camp()
	{
		Platform floor = game.floor;
		// attempt to keep a long distance from opponent
		if (player.x > opponent.x) // p is to the right
		{
			if (player.x > opponent.x + 500) // too far
				direction = -1; // approach
			else if (player.x < opponent.x + 400) // too close
				direction = 1; // back off
			else // just right
				direction = player.moveX > 0 ? -1 : 1; // dash dance
		}
		else // p is to the left
		{
			if (player.x < opponent.x - 400) // too far
				direction = 1; // approach
			else if (player.x > opponent.x - 300) // too close
				direction = -1; // back off
			else // just right
				direction = player.moveX > 0 ? 1 : -1; // dash dance
		}

		// jumping
		Hitbox h = opponent.hitbox;
		if (h != null && Math.abs(h.vx) > 5 && Math.abs(h.y - player.y) < 60 && Math.abs(h.x + 10*h.vx - player.x) < 60) // projectile is approaching
			jump = true; // jump to avoid
		else
			jump = false;
		
		// attack with projectile in direction of opponent
		attack = player.x > opponent.x ? 3 : 2;
	}

	// stay close, but safe
	private void pressure()
	{
		Platform floor = game.floor;
		// attempt to keep a short distance from opponent
		if (player.x > opponent.x) // p is to the right
		{
			if (player.x > opponent.x + 200) // too far
				direction = -1; // approach
			else if (player.x < opponent.x + 100) // too close
				direction = 1; // back off
			else // just right
				direction = player.moveX > 0 ? -1 : 1; // dash dance
		}
		else // p is to the left
		{
			if (player.x < opponent.x - 200) // too far
				direction = 1; // approach
			else if (player.x > opponent.x - 100) // too close
				direction = -1; // back off
			else // just right
				direction = player.moveX > 0 ? 1 : -1; // dash dance
		}

		// jumping
		Hitbox h = opponent.hitbox;
		if (h != null && Math.abs(h.vx) > 5 && Math.abs(h.y - player.y) < 60 && Math.abs(h.x + 10*h.vx - player.x) < 60) // projectile is approaching
			jump = true; // jump to avoid
		else
			jump = false;
		
		// if expected to hit, attack with projectile in direction of opponent
		if (Math.abs((player.y + 8*player.moveY + 8*player.kby) - (opponent.y + 30*opponent.moveY + 20*opponent.kby)) < 40)
			attack = player.x > opponent.x ? 3 : 2;
		
		// if under opponent, but horizonatally near, shoot up
		if (Math.abs(player.x - opponent.x) < 60 && player.y - opponent.y < -40)
			attack = 4;

		// if in close range, use full body attack
		if (Math.pow(player.x - opponent.x, 2) + Math.pow(player.y - opponent.y, 2) < 5000)
			attack = 1;

	}

	private void ground()
	{
		Platform floor = game.floor;
		// approach
		if (player.x > opponent.x) // p is to the right
		{
			direction = -1; // move left
		}
		else // p is to the left
		{
			direction = 1; // move right
		}

		// if in range, attack
		if (Math.abs(player.x - opponent.x) < 90)
			attack = 1;
	}

	private void air()
	{
		Platform floor = game.floor;
		// approach
		if (player.x > opponent.x) // p is to the right
		{
			direction = -1; // move left
		}
		else // p is to the left
		{
			direction = 1; // move right
		}

		// if can leap to opponent, leap
		if (Math.abs(player.x - opponent.x) < 600)
			jump = true;

		// if falling and close to opponent, attack
		if (player.y < 60 && player.moveY < 0 && Math.abs(player.x - opponent.x) < 120)
			attack = 1;
	}

	public void update()
	{
		Platform floor = game.floor;

		// by default, don't attack, and don't jump
		attack = 0;
		jump = false;

		// use neural network to decide how to act
		float[] output = new float[4]; // {camp, pressure, ground, air}

		float[] input = getInput();
		NeuralNetwork.updateNeurons(input.length, 64, output.length, input, output, weights);

		// figure out which one is highest
		int decision = 0;
		for (int i = 1; i < 4; i++)
			if (output[i] > output[decision])
				decision = i;

		switch (decision)
		{
			case 0: camp(); break;
			case 1: pressure(); break;
			case 2: ground(); break;
			case 3: air(); break;
		}

		// overrides:
		// if sliding off edge, don't attack (cuz the endlag is bad), and jump
		if (player.x > floor.x + floor.width*3/8 && player.kbx > 5 || player.x < floor.x - floor.width*3/8 && player.kbx < - 5)
		{
			attack = 0;
			jump = true;
		}

		// if in danger, just get back to the stage
		// vertical
		if (player.y < floor.y) // too low
			attack = 5; // recover
		
		// horizontal
		if (player.x > floor.x + floor.width*3/8) // too far to the right
			direction = -1; // go left
		else if (player.x < floor.x - floor.width*3/8) // too far to the left
			direction = 1; // go right
	}

	private float[] getInput()
	{
		// get values
		boolean[] playerState = getState(player); // stuff just for player
		boolean[] opponentState = getState(opponent); // stuff just for opponent
		boolean[] relativeState = getRelativeState(player, opponent); // stuff between p and o

		// create array to contain all 3 (and change to float)
		float[] input = new float[playerState.length + opponentState.length + relativeState.length];

		// set array
		for (int i = 0; i < playerState.length; i++)
			input[i] = playerState[i] ? 1 : 0;
		for (int i = 0; i < opponentState.length; i++)
			input[i + playerState.length] = opponentState[i] ? 1 : 0;
		for (int i = 0; i < relativeState.length; i++)
			input[i + playerState.length + opponentState.length] = relativeState[i] ? 1 : 0;

		return input;
	}
	
	private boolean[] getState(Player player)
	{
		Player p = player;
		Platform f = player.context.floor;
		boolean[] states = {
			// for player
			// position {off, left, midleft, mid, midright, right, off}
			p.x < f.x - f.width/2,
			p.x >= f.x - f.width/2 && p.x < f.x - f.width*3/8,
			p.x >= f.x - f.width*3/8 && p.x < f.x - f.width/8,
			p.x >= f.x - f.width/8 && p.x < f.x + f.width/8,
			p.x >= f.x + f.width/8 && p.x < f.x + f.width*3/8,
			p.x >= f.x + f.width/2,
			// height {below, ground, above, far above}
			p.y - p.radius < f.y,
			p.y - p.radius == f.y,
			p.y - p.radius > f.y && p.y - p.radius < f.y + 600,
			p.y - p.radius >= f.y + 600,
			// states {freefall, endlag, has hitbox out}
			p.freefall,
			p.hitbox != null && p.hitbox.state <= p.hitbox.endlag,
			p.hitbox != null,
			// damage
			p.damage == 0,
			p.damage > 0 && p.damage <= 20,
			p.damage > 20 && p.damage <= 50,
			p.damage > 50 && p.damage <= 100,
			p.damage > 100,
			// knockback
			p.kbx < -16,
			p.kbx > -16 && p.kbx <= -10,
			p.kbx > -10 && p.kbx <= -5,
			p.kbx > -5 && p.kbx <= -3,
			p.kbx > -3 && p.kbx < 0,
			p.kbx == 0,
			p.kbx > 0 && p.kbx <= 2,
			p.kbx > 3 && p.kbx <= 5,
			p.kbx > 5 && p.kbx <= 10,
			p.kbx > 10 && p.kbx <= 16,
			p.kbx > 16,
			p.kby < -16,
			p.kby > -16 && p.kby <= -10,
			p.kby > -10 && p.kby <= -5,
			p.kby > -5 && p.kby <= -3,
			p.kby > -3 && p.kby < 0,
			p.kby == 0,
			p.kby > 0 && p.kby <= 2,
			p.kby > 3 && p.kby <= 5,
			p.kby > 5 && p.kby <= 10,
			p.kby > 10 && p.kby <= 16,
			p.kby > 16,
			// movement
			p.moveX < 0,
			p.moveX == 0,
			p.moveX > 0,
			p.moveY < 0,
			p.moveY == 0,
			p.moveY > 0
		};
		return states;
	}

	private boolean[] getRelativeState(Player player, Player opponent)
	{
		Player p = player, o = opponent;
		boolean[] state = {
			// distance
			Math.abs(p.x - o.x) > 300,
			Math.abs(p.y - o.y) > 100,
			// who is winning
			p.damage > o.damage,
			// is opponent approaching
			Math.abs(p.x - (o.x + 10*o.moveX)) < Math.abs(p.x - o.x)
		};
		return state;
	}
}

