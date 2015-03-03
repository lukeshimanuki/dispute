import java.lang.Math;
import java.util.Scanner;
import java.io.File;

public class Computer extends Controller
{
	private float[] weights;

	private int numInput = 8;
	private int numHidden = 32;
	private int numOutput = 8;

	private Player player, opponent;

	public Computer(String weightsFilename)
	{
		int numWeights = numInput*numHidden + numHidden*numOutput;
		weights = new float[numWeights];
		if (weightsFilename == null)
		{ // get random weights
			for (int i = 0; i < numWeights; i++)
				weights[i] = 2 * (float)Math.random() - 1;
		}
		else //read from file
		{
			Scanner weightsFile = null;
			try
			{
				weightsFile = new Scanner(new File(weightsFilename));
			}
			catch (Exception e) {}
			for (int i = 0; i < numWeights; i++)
				weights[i] = (float)weightsFile.nextInt() / 256;
		}
	}

	public Computer(float[] weights) // mostly for training purposes
	{
		this.weights = weights;
	}

	public void set(Player player, Player opponent)
	{
		this.player = player;
		this.opponent = opponent;
	}

	@Override
	public void update()
	{
		Platform f = player.context.floor;
		Player p = player;
		Player o = opponent;
		float[] input = {
			// for player
			(float)(p.x - (f.x - f.width/2)) / f.width, // dist from left
			(float)(p.x - (f.x + f.width/2)) / f.width, // dist from right
			(float)(p.y - f.y) / 500, // height
			(float)p.damage / 100, // damage
			// for opponent
			(float)(o.x - (f.x - f.width/2)) / f.width, // dist from left
			(float)(o.x - (f.x + f.width/2)) / f.width, // dist from right
			(float)(o.y - f.y) / 500, // height
			(float)o.damage / 100 // damage
			};
		float[] output = new float[numOutput]; // {right, left, jump, a1, a2, a3, a4, a5}
		NeuralNetwork.updateNeurons(numInput, numHidden, numOutput, input, output, weights);
		
		// process right/left
		if (Math.abs(output[0] - output[1]) < 0.1f) // close
			direction = 0; // don't move
		else if (output[0] > output[1]) // right > left
			direction = 1; // move right
		else // right < left
			direction = -1; // move left

		// process jump
		if (output[2] > 0.5f)
			jump = true;
		else
			jump = false;

		// process attack
		float max = 0;
		for (int i = 1; i <= 5; i++)
		{
			if (output[i + 2] > max)
			{
				max = output[i + 2];
				attack = i;
			}
		}
		if (max < 0.5f)
			attack = 0;
	}
}

