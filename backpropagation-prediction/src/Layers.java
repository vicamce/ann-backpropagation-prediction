import javax.xml.soap.Node;

/**
 * Created by Amadeus on 24/07/14.
 */
public class Layers {

    //Parameters
    public double[] input;
    public Nodes[] nodes;
    private double net;

    public Layers (int numNodes, int numInputs) {
        nodes = new Nodes[numNodes];
        //assign all inputs each node in the layer
        for (int i = 0; i < numNodes; i++) {
            nodes[i] = new Nodes(numInputs);
        }

        input = new double[numInputs];
        net = 0;
    }

    public void feedForwardActivation()
    {
        //Activation for each node  feed forward
        for(int pNode = 0; pNode < nodes.length; pNode++)
        {
            net = 0;
            for(int pWeight = 0;pWeight < nodes[pNode].weight.length; pWeight++)
            {
                net = net + input[pWeight] * nodes[pNode].weight[pWeight];
            }
            nodes[pNode].h = net - nodes[pNode].threshold;
            nodes[pNode].g = functionSigmoid(net);
            nodes[pNode].xhi = nodes[pNode].g;
        }
    }

    private double functionSigmoid(double net) {

        //function sigmoid for perform of the feed forward
        return 1/(1 + Math.exp(-net));
    }

    public double[] getOutputsLayer()
    {
        //get all outputs of the layer,which are de xhi each node
        double[] outputsLayer = new double[nodes.length];
        for(int pNode = 0; pNode < nodes.length; pNode++)
        {
            outputsLayer[pNode] = nodes[pNode].xhi;
        }
        return (outputsLayer);

    }
}
