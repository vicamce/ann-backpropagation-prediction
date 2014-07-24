/**
 * Created by Amadeus on 24/07/14.
 */
public class Nodes {

    //Parameters
    public double deltaWeight[];
    public double weight[];
    public double deltaThreshold;
    public double threshold;
    public double deltaError;
    public double h;
    public double g;
    public double xhi;

    public Nodes(int numNodes)
    {
        weight = new double[numNodes];
        deltaWeight = weight.clone();
        deltaError = 0;
        h = 0;
        g = 0;
        xhi = 0;
        startWeight();
    }

    public void startWeight()
    {
        //Assigment random each weight for each node
        for(int i = 0; i < weight.length;  i++)
        {
            weight[i] = Math.random();
            deltaWeight[i] = 0;
        }
        threshold = Math.random();
        deltaThreshold = 0;
    }
}
