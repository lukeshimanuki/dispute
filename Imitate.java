import java.lang.Math;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

public class Imitate
{
	public static void main(String[] args) throws Exception
	{
		// settings
		String weightsFile = null;
		String inputFile = "input";
		String outputFile = "output";
		
		int numInput = 18;
		int numHidden = 32;
		int numOutput = 8;
		int numWeights = numInput*numHidden + numHidden*numOutput;
		int popSize = 1024;
		int numSamples = 2048;

		// initialization and read from files
		float[][] inputSamples = new float[numSamples][numInput];
		Scanner inScanner = new Scanner(new File(inputFile));
		for (int i = 0; i < numSamples; i++)
			for (int j = 0; j < numInput; j++)
				inputSamples[i][j] = (float)inScanner.nextInt() / 256;
		float[][] outputSamples = new float[numSamples][numOutput];
		Scanner outScanner = new Scanner(new File(outputFile));
		for (int i = 0; i < numSamples; i++)
			for (int j = 0; j < numOutput; j++)
				outputSamples[i][j] = (float)outScanner.nextInt() / 256;
		float[][] weights = null;
		if (weightsFile != null)
		{
			Scanner weightsScanner = new Scanner(new File(weightsFile));
			for (int i = 0; i < popSize; i++)
				for (int j = 0; j < numWeights; j++)
					weights[i][j] = (float)weightsScanner.nextInt() / 256;
		}

		// imitate!
		imitate(numInput, numHidden, numOutput, popSize, numSamples, inputSamples, outputSamples, weights);
	}

	public static void imitate(int numInput, int numHidden, int numOutput, int popSize, int numSamples, float[][] inputSamples, float[][] outputSamples, float[][] weights) throws Exception
	{
		int numWeights = numInput*numHidden + numHidden*numOutput;
		if (weights == null) // randomize if not already initialized
		{
			weights = new float[popSize][numWeights];
			for (int i = 0; i < popSize; i++)
				for (int j = 0; j < numWeights; j++)
					weights[i][j] = 2*(float)Math.random() - 1;
		}

		float[] output = new float[numOutput];
		float[] fitness = new float[popSize];
		PrintWriter best = new PrintWriter("best", "UTF-8");
		int generation = 1;
		while (true)
		{
			// calculate fitness
			float minError = 99999999;
			int bestID = 0;
			float totalError = 0;
			for (int i = 0; i < popSize; i++) // set of weights
			{
				// add up error for each sample
				float error = 1;
				for (int j = 0; j < numSamples; j++) // sample
				{
					NeuralNetwork.updateNeurons(numInput, numHidden, numOutput, inputSamples[j], output, weights[i]);
					for (int k = 0; k < numOutput; k++)
						error += Math.abs(output[k] - outputSamples[j][k]);
				}
				if (error < minError)
				{
					minError = error;
					bestID = i;
				}
				totalError += error;

				// fitness is inverse error
				fitness[i] = 1 / error;
			}

			// save best set of weights
			for (int i = 0; i < numWeights; i++)
				best.print((int)(weights[bestID][i]*256) + " ");
			best.print("\n");
			best.flush();

			// print
			System.out.println(generation + " " + minError + " " + (totalError/popSize));
			generation++;

			// find next gen
			GeneticAlgorithm.updateGenes(popSize, numWeights, weights, fitness, .7f, .001f);
		}
	}
}

