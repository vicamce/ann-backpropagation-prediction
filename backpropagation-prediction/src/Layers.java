import function.Function;

/**
 * Created by Amadeus on 24/07/14.
 */
public class Layers {

    //Parameters
    public double[] input;
    public Nodes[] nodes;
    private double net;
    Function function;

    public Layers (int numNodes, int numInputs) {

        function = new SigmoidFunction();
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
            nodes[pNode].g =function.calculate(net);
            nodes[pNode].xhi = nodes[pNode].g;
        }
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
