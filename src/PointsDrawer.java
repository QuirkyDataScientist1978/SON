
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JPanel;

class PointsDrawer extends JPanel
{
	ArrayList<Neuron> neurons;
	ArrayList<Point> points;
	HashMap<Integer, Integer> classificationMatrix;
	
	public PointsDrawer(ArrayList<Neuron> neurons_, ArrayList<Point> points_, HashMap<Integer, Integer> classificationMatrix_)
	{
		neurons = new ArrayList<Neuron>();
		neurons = neurons_;
		
		points = new ArrayList<Point>();
		points = points_;
		
		classificationMatrix = new HashMap<Integer, Integer>();
		classificationMatrix = classificationMatrix_;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2dObject = (Graphics2D) g;
		ArrayList<Color> colors = new ArrayList<Color>();
		for (int i = 0; i < neurons.size(); i++)
		{
			ArrayList<Double> weights = neurons.get(i).GetWeights();
			double x = weights.get(0) * 500;
			double y = weights.get(1) * 500;
			Rectangle2D.Double rect = new Rectangle2D.Double(x, y, 20, 20);
			
			Random rand = new Random();
			float red = rand.nextFloat();
			float green = rand.nextFloat();
			float blue = rand.nextFloat();
			Color color = new Color(red, green, blue);
			colors.add(color);
			
			g2dObject.setColor(color);
			g2dObject.draw(rect);
			g2dObject.fill(rect);
		}
		
		for (int i = 0; i < points.size(); i++)
		{
			int pointID = points.get(i).GetID();
			ArrayList<Double> inputVector = points.get(i).GetInputVector();
			double x = inputVector.get(0) * 500;
			double y = inputVector.get(1) * 500;
			int clusterID = classificationMatrix.get(pointID);
			Rectangle2D.Double rect = new Rectangle2D.Double(x, y, 5, 5);
			g2dObject.setColor(colors.get(clusterID));
			g2dObject.draw(rect);
			g2dObject.fill(rect);
		}
	}
}