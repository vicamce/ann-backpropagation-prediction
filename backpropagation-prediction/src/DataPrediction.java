import java.io.*;
import java.util.StringTokenizer;

/**
 * Created by Amadeus on 24/07/14.
 */
public class DataPrediction {

    public double[][] inputTraining;
    public double[][] inputTest;
    public double[][] expectedOutputs;
    private double[]  predictions;
    private String file;
    private int train;
    private int test;

    public DataPrediction(double[] parameters, String name)
    {
        inputTraining = new double[(int)parameters[3]+1][5];
        inputTest = new double[(int)parameters[4]][5];
        file = name;
        train = (int)parameters[3];
        test = (int)parameters[4];
    }

    public void readDates()
    {
        String line = "";
        double[] numColumns;
        try{
            File fileText = new File(file);
            FileReader fileReader = new FileReader(fileText);
            BufferedReader bufferReader = new BufferedReader(fileReader);
            int numTrain = 0;
            int numTest = 0;

            while((line = bufferReader.readLine()) != null)
            {
                numColumns = tokenData(line);
                if(numTrain <= train){
                    for (int pData = 0; pData < numColumns.length; pData++)
                    {
                        inputTraining[numTrain][pData] = numColumns[pData];
                    }
                    numTrain += 1;
                }
                else
                {
                    if(numTrain > train)
                    {
                        for(int pData = 0; pData < numColumns.length; pData++)
                        {
                            inputTest[numTest][pData]=numColumns[pData];
                        }
                        numTest += 1;
                    }
                }
            }

        }
        catch (Exception e)
        {
            System.out.println("Read File: " + e.getMessage());
        }
    }

    private double[] tokenData(String line) {

        //Separate each columns of the line
        StringTokenizer token = new StringTokenizer(line, ",");
        int numTokens = token.countTokens();
        double[] arrayToken = new double[numTokens];
        for (int pToken = 0; pToken < numTokens; pToken++)
        {
            arrayToken[pToken] = Double.parseDouble(token.nextToken());
        }

        return(arrayToken);
    }

    public void writeDataPrediction(double[] numPredictions)
    {
        //write in file text, the prediction of set test
        predictions = numPredictions;
        File file = new File("data/ResultDataPrediction");
        try
        {
            FileWriter fileWrite = new FileWriter(file);
            BufferedWriter bufferWrite = new BufferedWriter(fileWrite);
            PrintWriter printWrite = new PrintWriter(bufferWrite);
            for(int pData = 0; pData < predictions.length; pData++)
            {
                printWrite.write(String.valueOf(predictions[pData]) + '\n');
            }
            printWrite.close();
            bufferWrite.close();

        }
        catch (Exception e)
        {
            System.out.println("Write File: " + e.getMessage());
        }
    }

    public void loadDataOutputsTrain()
    {
        //Contiene the data real each pattern
        expectedOutputs = new double[inputTraining.length][1];

        //Se asigna los patrones de entrada  y las salidas esperadas  a la red
        for(int pPattern = 0; pPattern < inputTraining.length; pPattern++)
        {
            expectedOutputs[pPattern][0]=inputTraining[pPattern][4];
        }

    }

    public void loadDataOutputsTest()
    {
        //Contains the data real each pattern
        expectedOutputs = new double[inputTest.length][1];
        for(int pPattern = 0; pPattern < inputTest.length; pPattern++)
        {
            expectedOutputs[pPattern][0]=inputTest[pPattern][4];
        }

    }

    public void calculateError()
    {
        double errorPartial = 0;
        double errorTotal = 0;
        double sumReal = 0;
        double sumErrorPartial = 0;

        for(int pData = 0; pData < test; pData++)
        {
            errorPartial = expectedOutputs[pData][0] - predictions[pData];
            sumErrorPartial +=Math.abs(errorPartial);
            sumReal += expectedOutputs[pData][0];
        }
        errorTotal = (sumErrorPartial / sumReal) * 100;
        System.out.println("The Error total of prediction is: " + errorTotal + "%");
    }

}
