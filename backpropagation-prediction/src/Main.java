public class Main {

    public static void main(String[] args) {
	// write your code here
        String file = "data/PredictionTurbine.txt";
        int[] nodes = {4,9,1};
        double rateLearning = 0.2;
        double momentum = 0.09;
        int epochs = 15000;
        int dataTrain = 400;
        int dataTest =50;
        double[] parameters = new double[5];

        parameters[0] = rateLearning;
        parameters[1] = momentum;
        parameters[2] = epochs;
        parameters[3] = dataTrain;
        parameters[4] = dataTest;
        NeuronalNetwork net = new NeuronalNetwork(nodes,parameters, file);
        net.neuronalNetwork();
    }
}
