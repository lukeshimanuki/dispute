import java.lang.Math;

public class GeneticAlgorithm
{
	public static void updateGenes(int popSize, int geneSize, float[][] genes, float[] fitness, float cross, float mutate)
	{
		// calculate total fitness
		float total = 0;
		for (int i = 0; i < popSize; i++)
		{
			total += fitness[i];
		}
		if (total == 0) return;

		// generate new gene pool
		float[][] newGenes = new float[popSize][geneSize];
		for (int i = 0; i < popSize; i++)
		{
			// pick parents
			int p1 = 0, p2 = 0;
			float num = (float)Math.random() * total; // pick random number less than total
			for (int j = 0; j < popSize; j++)
			{ // cycle through each to see which one corresponds
				if (num <= fitness[j])
				{
					p1 = j;
					break;
				}
				num -= fitness[j];
			}
			num = (float)Math.random() * total;
			for (int j = 0; j < popSize; j++)
			{
				if (num <= fitness[j])
				{
					p2 = j;
					break;
				}
				num -= fitness[j];
			}

			// combine (p1 is base, with chance of cross from p2)
			// base
			for (int j = 0; j < geneSize; j++)
				newGenes[i][j] = genes[p1][j];
			// cross
			if (Math.random() < cross)
			{
				int pivot = (int)(Math.random() * geneSize);
				if (pivot == geneSize) pivot = 0; // in case random = 1.000
				for (int j = pivot; j < geneSize; j++)
					newGenes[i][j] = genes[p2][j];
			}
			// mutate (add random value)
			for (int j = 0; j < geneSize; j++)
			{
				if (Math.random() < mutate)
				{
					newGenes[i][j] += Math.random();
					// make sure it isn't over 1 or under -1
					while (newGenes[i][j] > 1) newGenes[i][j] -= 1;
					while (newGenes[i][j] < -1) newGenes[i][j] += 1;
				}
			}
		}

		// copy new genes to array
		for (int i = 0; i < popSize; i++)
			for (int j = 0; j < geneSize; j++)
				genes[i][j] = newGenes[i][j];
	}
}
