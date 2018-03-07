
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;

import java.io.*;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class SON
{
	int k;
	int dimension;
	double lr;
	int epochs;
	int delayTime;
	ArrayList<Neuron> neurons;
	ArrayList<Point> points;
	HashMap<Integer, Integer> classificationMatrix;
	
	public SON(String parametersFilename)
	{
		ReadParameters(parametersFilename);
		neurons = new ArrayList<Neuron>();
		for (int i = 0; i < k; i++)
		{
			Neuron neuron = new Neuron(i, dimension);
			neurons.add(neuron);
		}
	}
	
	public SON(int k_, int dimension_, double lr_, int epochs_, int delayTime_)
	{
		k = k_;
		dimension = dimension_;
		lr = lr_;
		epochs = epochs_;
		delayTime = delayTime_;
		neurons = new ArrayList<Neuron>();
		for (int i = 0; i < k; i++)
		{
			Neuron neuron = new Neuron(i, dimension);
			neurons.add(neuron);
		}
	}
	
	public ArrayList<Neuron> GetNeurons()
	{
		return neurons;
	}
	
	public ArrayList<Point> GetPoints()
	{
		return points;
	}
	
	public HashMap<Integer, Integer> GetClassificationMatrix()
	{
		return classificationMatrix;
	}
	
	public void ReadPoints(String pointsFilename)
	{
		points = new ArrayList<Point>();
		String dataline = null;
        try 
        {
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
	
	public void ReadParameters(String parametersFilename)
	{
		points = new ArrayList<Point>();
		String dataline = null;
		boolean first = true;
        try
        {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(parametersFilename);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((dataline = bufferedReader.readLine()) != null) 
            {
            	if (first)
            	{
            		first = false;
            		continue;
            	}
            	String items[]= dataline.split(",");
            	k = Integer.parseInt(items[0]);
            	dimension = Integer.parseInt(items[1]);
            	lr = Double.parseDouble(items[2]);
            	epochs = Integer.parseInt(items[3]);
            	delayTime = Integer.parseInt(items[4]);
            }

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) 
        {
        	System.out.println("Unable to open file '" +  parametersFilename + "'"); 
        }  
        catch(IOException ex)
        {
            System.out.println("Error reading file '" + parametersFilename + "'");  
        }
	}
	
	public void Train()
	{
		JFrame frame = new JFrame("Clustering...");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 1000);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		for (int epochID = 0; epochID < epochs; epochID++)
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
				neurons.get(minID).UpdateWeights(point, lr);
				classificationMatrix.put(point.GetID(), minID);
			}
			PointsDrawer pointsDrawer = new PointsDrawer(neurons, points, classificationMatrix);
			frame.add(pointsDrawer);
			frame.repaint();
			frame.revalidate();
			try
			{
				TimeUnit.SECONDS.sleep(delayTime);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
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
		String pointsFilename = Paths.get(".").toAbsolutePath().normalize().toString()+"\\data.csv";
		String parametersFilename = Paths.get(".").toAbsolutePath().normalize().toString()+"\\parameters.csv";
		SON son = new SON(parametersFilename);
		son.ReadPoints(pointsFilename);
		son.Train();
		son.Classify();
		//son.PrintClassification();
		
		PointsDrawer pointsDrawer = new PointsDrawer(son.GetNeurons(), son.GetPoints(), son.GetClassificationMatrix());
		JFrame frame = new JFrame("Results");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(pointsDrawer);
		frame.setSize(1000, 1000);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}