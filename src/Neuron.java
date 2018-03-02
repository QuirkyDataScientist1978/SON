import java.util.ArrayList;
import java.util.Random;

class Neuron
{
	int id;
	ArrayList<Double> weights;
	
	public Neuron(int id_, int weight_dimension)
	{
		weights = new ArrayList<Double>();
		id = id_;
		Random rand = new Random();
		for (int i = 0; i < weight_dimension; i++)
		{
			double initialWeightValue = rand.nextDouble();
			weights.add(initialWeightValue);
		}
	}
	
	int GetID()
	{
		return id;
	}
	
	public ArrayList<Double> GetWeights()
	{
		return weights;
	}
	
	public double ComputeDistance(Point point)
	{
		ArrayList<Double> inputVector = point.GetInputVector();
		int dimension = point.GetDimension();
		if (dimension != weights.size())
			return -1.0;
		else
		{
			double dist = 0.0;
			for (int i = 0; i < weights.size(); i++)
				dist += Math.pow(weights.get(i) - inputVector.get(i), 2);
			dist = Math.sqrt(dist);
			return dist;
		}
	}
	
	public void UpdateWeights(Point point, double lr)
	{
		ArrayList<Double> inputVector = point.GetInputVector();
		int dimension = point.GetDimension();
		if (dimension == weights.size())
		{
			for (int i = 0; i < weights.size(); i++)
			{
				double weightOld = weights.get(i);
				double weightNew = weightOld + lr * (inputVector.get(i) - weightOld);
				weights.set(i, weightNew);
			}
		}
	}
}
