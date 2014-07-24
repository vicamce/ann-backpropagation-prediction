import javax.xml.soap.Node;

/**
 * Created by Amadeus on 24/07/14.
 */
public class NeuronalNetwork {

    //Parameters
    private double rateLearnig;
    private double momentum;
    private int epochs;
    private int numLayers;
    private Layers[] layer;
    private double[] predictions;
    public DataPrediction dataPrediction;

    public NeuronalNetwork(int[] numNodes,double[]parameters,String file)
    {
        rateLearnig = parameters[0];
        momentum = parameters[1];
        epochs = (int)parameters[2];
        numLayers = numNodes.length;
        layer = new Layers[numLayers];
        layer[0] = new Layers(numNodes[0],numNodes[0]);

        //Assign nodes each layer and number inputs
        for(int indexLayer=1; indexLayer < numLayers; indexLayer++)
        {
            layer[indexLayer] = new Layers(numNodes[indexLayer],numNodes[indexLayer - 1]);
        }

        dataPrediction =new DataPrediction(parameters,file);
    }

    public void neuronalNetwork()
    {
        try
        {
            //execute network for a number the epoch
            dataPrediction.readDates();
            dataPrediction.loadDataOutputsTrain();
            if(dataPrediction.inputTraining.length != 0)
            {
                for (int nEpoch = 0;nEpoch < epochs; nEpoch++)
                {
                    processTrainNetwork();
                }
                dataPrediction.loadDataOutputsTest();
                processTestNetwork();
            }
        }
        catch (Exception e)
        {

        }
    }

    private void processTrainNetwork() {

        //Execute process for training

        for(int pPattern = 0; pPattern<dataPrediction.inputTraining.length;pPattern++)//inputs.length
        {
            //selected a pattern
            int pattern = (int)(Math.random() * dataPrediction.inputTraining.length);

            //Assign the inputs of the first layer
            for(int i  = 0; i < layer[0].nodes.length; i++)
            {
                layer[0].input[i] = dataPrediction.inputTraining[pattern][i];
            }

            //Activation feed forward all network
            feedForward();
            //Execute backPropagation
            backPropagation(pattern);
        }
    }

    private void processTestNetwork()
    {
        //Execute process for training
        predictions = new double[dataPrediction.inputTest.length];

        for(int pPattern = 0; pPattern<dataPrediction.inputTest.length;pPattern++)
        {
             //Assign the inputs of the first layer
            for(int i  = 0; i < layer[0].nodes.length; i++)
            {
                layer[0].input[i] = dataPrediction.inputTest[pPattern][i];
            }

            //Activation feed forward all network
            feedForward();
            for(int pNode = 0; pNode < layer[numLayers - 1].nodes.length; pNode++)
            {
                predictions[pPattern] = layer[numLayers - 1].nodes[pNode].xhi;
            }
        }
        dataPrediction.writeDataPrediction(predictions);
        dataPrediction.calculateError();

    }


    private void feedForward()
    {
        //Activate feed forward
        for(int pNode = 0; pNode < layer[0].nodes.length;  pNode++ )
        {
            //the output of the first layer is the same an inputs
            layer[0].nodes[pNode].xhi = layer[0].input[pNode];
        }
        //Assign the inputs of the second layer, which are outputs of the nodes of the first layer
        layer[1].input = layer[0].getOutputsLayer();

        //calculated xhi from the other layers
        for(int pNode = 1; pNode <= numLayers - 1;    pNode++)
        {
            layer[pNode].feedForwardActivation();
            if(pNode != numLayers-1)
            {
                //get the outputs of last layer and assign this which inputs of next layer
                layer[pNode + 1].input = layer[pNode].getOutputsLayer();
            }

        }
    }

    private void backPropagation(int pattern) {
        int indexNode,indexLayer,indexNextNode,indexInput, indexWeight;
        double sum;
        int indexFinalLayer = numLayers - 1;

        //Calculate of delta the last layer // Formulate 4.9
        for(indexNode = 0; indexNode < layer[indexFinalLayer].nodes.length; indexNode++)
        {
            layer[indexFinalLayer].nodes[indexNode].deltaError = (dataPrediction.expectedOutputs[pattern][0] - layer[indexFinalLayer].nodes[indexNode].xhi) * (layer[indexFinalLayer].nodes[indexNode].xhi * (1 - layer[indexFinalLayer].nodes[indexNode].xhi));
        }

        //Calculate of deltas of the hidden layer, without the first layer // Formulate 4.10
        for (indexLayer = numLayers - 2; indexLayer > 0; indexLayer--) {
            for (indexNode = 0; indexNode < layer[indexLayer].nodes.length; indexNode++) {
                sum = 0;
                for (indexNextNode = 0; indexNextNode < layer[indexLayer + 1].nodes.length; indexNextNode++) {
                    sum = sum + layer[indexLayer + 1].nodes[indexNextNode].deltaError * layer[indexLayer + 1].nodes[indexNextNode].weight[indexNode];
                }

                layer[indexLayer].nodes[indexNode].deltaError = (layer[indexLayer].nodes[indexNode].xhi * (1 - layer[indexLayer].nodes[indexNode].xhi)) * sum;
            }
        }


        //Calculate of deltas in all layers . Formulate 4.8
        for (indexLayer = numLayers - 1; indexLayer > 0; indexLayer--) {
            for (indexNode = 0; indexNode < layer[indexLayer].nodes.length; indexNode++) {
                for (indexInput = 0; indexInput < layer[indexLayer].input.length; indexInput++) {
                    layer[indexLayer].nodes[indexNode].deltaWeight[indexInput] = (rateLearnig) * layer[indexLayer].nodes[indexNode].deltaError * layer[indexLayer].input[indexInput]; // (momentum * layer[indexLayer].nodes[indexNode].deltaWeight[indexInput]);
                }
                layer[indexLayer].nodes[indexNode].deltaThreshold = rateLearnig * layer[indexLayer].nodes[indexNode].deltaError;  ///(momentum * layer[indexLayer].nodes[indexNode].deltaThreshold );
            }
        }

        //Update all weight in each node // Formulate 4.7
        for (indexLayer = numLayers - 1; indexLayer > 0; indexLayer--) {
            for (indexNode = 0; indexNode < layer[indexLayer].nodes.length; indexNode++) {
                for (indexWeight = 0; indexWeight < layer[indexLayer - 1].nodes.length; indexWeight++) {
                    layer[indexLayer].nodes[indexNode].weight[indexWeight] = layer[indexLayer].nodes[indexNode].weight[indexWeight] + layer[indexLayer].nodes[indexNode].deltaWeight[indexWeight];
                }
                layer[indexLayer].nodes[indexNode].threshold = layer[indexLayer].nodes[indexNode].threshold + layer[indexLayer].nodes[indexNode].deltaThreshold;
            }
        }
    }
}
