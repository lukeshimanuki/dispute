import java.lang.Math;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

public class Train
{
	public static void main(String[] args) throws Exception
	{
//		new Train().train(null);
		new Train().train("weights");
	}

	public void train(String weightsFilename) throws Exception
	{
		int popSize = 64;
		int numInput = 8, numHidden = 32, numOutput = 8;
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
				fitness[i] = 0;

			// find fitness levels (by playing each set against each other)
			for (int i = 0; i < popSize; i++)
			{
				for (int j = 0; j < popSize; j++)
				{
					if (i == j) continue;

					Game game = new Game(new Computer(weights[i]), new Computer(weights[j]), true);
					for (int k = 0; k < 15*90; k++) // play game for N frames
					{
						game.update();
						game.repaint();
						Thread.sleep(1);
					}
					game.frame.dispose();
					// check damage, lives, etc
					Player p1 = game.players[0], p2 = game.players[1];
					float score1 = p1.lives * 120 - p1.damage;
					float score2 = p2.lives * 120 - p2.damage;
					if (score1 <= 0) score1 = 1;
					if (score2 <= 0) score2 = 1;
					
					// update fitness levels
					fitness[i] += score1 / score2;
					fitness[j] += score2 / score1;
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
			GeneticAlgorithm.updateGenes(popSize, numWeights, weights, fitness, .8f, .01f);
		}
	}
}
