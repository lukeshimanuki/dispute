import java.lang.Math;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

public class Train
{
	private boolean display = false;

	public static void main(String[] args) throws Exception
	{
		new Train().train(null);
//		new Train().train("weights");
	}

	public void train(String weightsFilename) throws Exception
	{
		int popSize = 64;
		int numInput = 96, numHidden = 64, numOutput = 4;
		int numWeights = numInput*numHidden + numHidden*numOutput;

		float[][] weights = new float[popSize][numWeights];

		// if no file given, initialize with random weights
		if (weightsFilename == null)
		{
			for (int i = 0; i < popSize; i++)
				for (int j = 0; j < numWeights; j++)
					weights[i][j] = (float)Math.random() * 2 - 1;
		}
		else // read file
		{
			Scanner weightsFile = new Scanner(new File(weightsFilename));
			for (int i = 0; i < popSize; i++)
				for (int j = 0; j < numWeights; j++)
					weights[i][j] = (float)weightsFile.nextInt() / 256;
		}

		// train :)
		PrintWriter log = new PrintWriter("log", "UTF-8");
		float[] fitness = new float[popSize];
		for (int generation = 1; true; generation++)
		{
			for (int i = 0; i < popSize; i++)
				fitness[i] = 1; // cuz I don't like 0

			// find fitness levels (by playing each set against each other)
			for (int i = 0; i < popSize; i++)
			{
				for (int j = i + 1; j < popSize; j++)
				{
					if (i == j) continue;

					Computer cpu1 = new Computer(weights[i]);
					Computer cpu2 = new Computer(weights[i]);
					Game game = new Game(cpu1, cpu2, display);
					Player p1 = game.players[0], p2 = game.players[1];
					cpu1.set(p1, p2);
					cpu2.set(p2, p1);
					Platform f = game.floor;
					int winner = 0;
					for (int k = 0; k < 60*45; k++) // play game for N frames (60 fps)
					{
						game.update();

						if (display)
						{
							game.repaint();
							Thread.sleep(16);
						}

						if (p1.lives < 0) // p1 died
						{
							winner = 2;
							break;
						}
						if (p2.lives < 0) // p2 died
						{
							winner = 1;
							break;
						}
					}
					if (display) game.frame.dispose();
					// check damage, lives, etc
					switch (winner)
					{
						case 0: break; // if nobody won, not aggressive enough
						case 1: fitness[i] += 1; break;
						case 2: fitness[j] += 1; break;
					}
				}
			}

			// print
			float maxFitness = 0;
			int bestID = 0;
			for (int i = 0; i < popSize; i++)
			{
				if (fitness[i] > maxFitness)
				{
					maxFitness = fitness[i];
					bestID = i;
				}
			}
			for (int i = 0; i < numWeights; i++)
				log.print((int)(weights[bestID][i]*256) + " ");
			log.print("\n");
			log.flush();
			System.out.println(generation + " " + maxFitness);

			// update
			GeneticAlgorithm.updateGenes(popSize, numWeights, weights, fitness, .7f, .001f);
		}
	}
}
