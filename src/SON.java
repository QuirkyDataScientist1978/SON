
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.io.*;

public class SON
{
	int k;
	int dimension;
	double lr;
	int epochs;
	ArrayList<Neuron> neurons;
	ArrayList<Point> points;
	HashMap<Integer, Integer> classificationMatrix;
	
	public SON(int k_, int dimension_, double lr_, int epochs_)
	{
		k = k_;
		dimension = dimension_;
		lr = lr_;
		epochs = epochs_;
		neurons = new ArrayList<Neuron>();
		for (int i = 0; i < k; i++)
		{
			Neuron neuron = new Neuron(i, dimension);
			neurons.add(neuron);
		}
	}
	
	public void ReadPoints(String pointsFilename)
	{
		points = new ArrayList<Point>();
		String dataline = null;
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(pointsFilename);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((dataline = bufferedReader.readLine()) != null) 
            {
            	String items[]= dataline.split(",");
            	int id = Integer.parseInt(items[0]);
            	Point point = new Point(id, dimension);
            	for (int i = 1; i < items.length; i++)
            		point.AddValue(i - 1, Double.parseDouble(items[i]));
            	points.add(point);
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) 
        {
        	System.out.println("Unable to open file '" +  pointsFilename + "'"); 
        }  
        catch(IOException ex)
        {
            System.out.println("Error reading file '" + pointsFilename + "'");  
        }	
	}
	
	public void Train()
	{
		for (int epochID = 0; epochID < epochs; epochID++)
		{
			for (int i = 0; i < points.size(); i++)
			{
				Point point = points.get(i);
				double minDist = 1000000;
				int minID = -1;
				for (int j = 0; j < neurons.size(); j++)
				{
					Neuron neuron = neurons.get(j);
					double dist = neuron.ComputeDistance(point);
					if (dist < minDist)
					{
						minDist = dist;
						minID = neuron.GetID();
					}
				}
				neurons.get(minID).UpdateWeights(point, lr);
			}
		}
	}
	
	
	public void Classify()
	{
		classificationMatrix = new HashMap<Integer, Integer>();
		for (int i = 0; i < points.size(); i++)
		{
			Point point = points.get(i);
			double minDist = 1000000;
			int minID = -1;
			for (int j = 0; j < neurons.size(); j++)
			{
				Neuron neuron = neurons.get(j);
				double dist = neuron.ComputeDistance(point);
				if (dist < minDist)
				{
					minDist = dist;
					minID = neuron.GetID();
				}
			}
			classificationMatrix.put(point.GetID(), minID);
		}
	}
	
	public void PrintClassification()
	{
	      Iterator i = classificationMatrix.entrySet().iterator();
	      while(i.hasNext()) 
	      {
	         HashMap.Entry<Integer, Integer> me = (HashMap.Entry<Integer, Integer>) i.next();
	         System.out.print(me.getKey() + ": ");
	         System.out.println(me.getValue());
	      }
	      System.out.println();
	}
	
	public static void main(String args[])
	{
		String pointsFilename = "C:\\Users\\asal\\Desktop\\NeuralNetworks_Ergasia3\\data.csv";
		int k = 5;
		int dimension = 2;
		double lr = 0.01;
		int epochs = 5000;
		SON som = new SON(k, dimension, lr, epochs);
		som.ReadPoints(pointsFilename);
		som.Train();
		som.Classify();
		som.PrintClassification();
	}
}