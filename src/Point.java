import java.util.ArrayList;

class Point
{
	int id;
	int dimension;
	ArrayList<Double> inputVector;
	
	public Point(int id_, int dimension_)
	{
		id = id_;
		dimension = dimension_;
		inputVector = new ArrayList<Double>();
		for (int i = 0; i < dimension; i++)
			inputVector.add(0.0);
	}
	
	public void AddValue(int index, double value)
	{
		if ((index >= 0) && (index < inputVector.size()))
			inputVector.set(index, value);
	}
	
	public int GetID()
	{
		return id;
	}
	
	public int GetDimension()
	{
		return dimension;
	}
	
	public ArrayList<Double> GetInputVector()
	{
		return inputVector;
	}

}