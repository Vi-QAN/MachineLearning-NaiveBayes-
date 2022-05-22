import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Calculator {
    // general summary of data set 
    private HashMap<String,Integer> summary;

    // summary in target filtered such as "Become an Entrepreneur - Yes"
    private HashMap<String,HashMap<String,Integer>> conditionalSummary = new HashMap<>();

    // Classifier to perform actions related to classifier
    private Classifier classifier;
    
    // total of records
    private int totalRecords;

    // constructor
    public Calculator(HashMap<String,Integer> summary,HashMap<String,HashMap<String,Integer>> conditionalSummary, Classifier classifier, int totalRecords ) {
        this.summary = summary;
        this.conditionalSummary = conditionalSummary;
        this.classifier = classifier;
        this.totalRecords = totalRecords;
    }

    // calculate probability
    public double calculateProb(int numerator, int denominator){
        return  ((double)numerator / denominator); // cast 1 number to double to get correct result
    }

    // Compute the probability of given list of classifier
    // ex: Become an Entrepreneur - Yes, Urban or rural address - Urban
    // return a hash map of that list including the calculated probabilies
    
    // public  listOfProbs (List<String> list,int denominator){
    //     // initialize result
    //     HashMap<String,Double> result = new HashMap<>();
        
    //     // calculate probability of each classifier
    //     for (int i = 0; i < list.size();i++){
            
    //     }

    //     return result;
    // }

    // Compute the ‘Prior’ probabilities for each of the class of prediction
    private HashMap<String,Double> priorProb(List<String> separatedCols){
        // initialize result
        HashMap<String,Double> result = new HashMap<>();

        // calculate each probability
        for (String col : separatedCols){
            // get frequency
            int frequency = this.summary.get(col);

            // calculate probability
            double prob = calculateProb(frequency, totalRecords);
            
            result.put(col, prob);
        }

        return result;
    }
    // Compute the probability of evidence that goes in the denominator.
    private double evidenceProb(List<String> attrList){
        // initialize result
        double result = 1;

        // iterate through 
        for (int i = 0; i < attrList.size();i++){
            // attribute
            String attribute = attrList.get(i);

            // get frequency of each attribute
            int frequency = summary.get(attribute);

            // calculate probability
            double prob = calculateProb(frequency,totalRecords);

            result *= prob;
        }

        return result;
    }

    // Compute the probability of likelihood of evidences that goes in the numerator.
    private double likelihoodProb(List<String> otherAttr, String predictAttr){
        // initial result
        double result = 1;

        // retrieve frequency table
        HashMap<String,Integer> frequencyTable = this.conditionalSummary.get(predictAttr);

        // get predict attribute frequency
        int predictFrequency = frequencyTable.get(predictAttr);

        // get other attribute frequencies and calculate probability
        for (int i = 0; i < otherAttr.size(); i++){
            String attr = otherAttr.get(i);
            int attrFrequency = frequencyTable.get(attr);
            double prob = calculateProb(attrFrequency,predictFrequency);
            result *= prob;
        }
        return result;
    }

    // Substitute all the 3 equations into the Naive Bayes formula, to get the probability that it is a banana.
    public HashMap<String,Double> predictionProb(List<String> otherAttr, String prediction){
        // result of predictions
        HashMap<String,Double> result = new HashMap<>();

        // calculate prior probability
        // get values of prediction
        HashSet<String> values = this.classifier.getClassifierVals(prediction);   

        // separate into columns
        List<String> predictAttr = Table.seperateCol(Table.colFormat, prediction, values);

        // result of prior probability
        HashMap<String,Double> priorResult = priorProb(predictAttr);

        // result of evidence probability
        double calculatedEvidence = evidenceProb(otherAttr);
        

        // result of likelihood probability 
        for (int i = 0; i < predictAttr.size();i++){
            String temp = predictAttr.get(i);

            double calculatedLikelihood = likelihoodProb(otherAttr,temp);

            double prob = (calculatedLikelihood * priorResult.get(temp)) / calculatedEvidence;
            result.put(temp,prob);
        }
        
        return result;
    } 
}
