import java.lang.Math;

public class NeuralNetwork
{
	public static void updateNeurons(int numInput, int numHidden, int numOutput, float[] input, float[] output, float[] weights)
	{
		int numInputWeights = numInput * numHidden;
		int numOutputWeights = numHidden * numOutput;
		int numWeights = numInputWeights + numOutputWeights;

		float[] hidden = new float[numHidden];

		for (int i = 0; i < numHidden; i++)
		{
			float sum = 0;
			for (int j = 0; j < numInput; j++)
				sum += input[j] * weights[i*numInput + j];
			hidden[i] = 1 / (1 + (float)Math.pow(Math.E, -sum));
		}
		
		for (int i = 0; i < numOutput; i++)
		{
			float sum = 0;
			for (int j = 0; j < numHidden; j++)
				sum += hidden[j] * weights[numInputWeights + i*numHidden + j];
			output[i] = 1 / (1 + (float)Math.pow(Math.E, -sum));
		}
	}
}
