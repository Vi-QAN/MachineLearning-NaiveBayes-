import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.HashSet;

public class Trainer {
    // data set currently used
    private String dataSet;

    // general summary of data set 
    private HashMap<String,Integer> summary = new HashMap<String,Integer>();

    // summary in target filtered such as "Become an Entrepreneur - Yes"
    private HashMap<String,HashMap<String,Integer>> conditionalSummary = new HashMap<>();

    // Classifier to perform actions related to classifier
    private Classifier classifier;

    // contain Entity detail in stored in form of Entity class shared among Entity objects
    private List<Entity> entities;

    // training data set
    private List<Entity> trainingEntities = new ArrayList<>();

    // testing data set
    private List<Entity> testingEntities = new ArrayList<>();

    // probability calculator
    private Calculator calculator;

    // accuracy score
    private float accuracy;

    // constructor
    public Trainer(String dataSet){
        setDataSet(dataSet);
        initTrainer();
    }

    // read in data set then get classifiers, and data in each row
    private void initTrainer(){
        // read data set
        List<List<String>> rawData = FileHandler.readFile(this.dataSet);

        // extract classifiers
        this.classifier = new Classifier(rawData.get(1));

        // load data from data set
        this.entities = getData(rawData);

        // divide data set into 2 parts training and testing
        int midInd = separateIndex(this.entities.size());
        
        // get training data
        this.trainingEntities = this.entities.subList(0, midInd);

        // get testing data
        this.testingEntities = this.entities.subList(midInd, this.entities.size());
    }

    // train model using training data set
    // outcomes are general summary and conditional summary table
    public void train(){
        // summarize training data
        this.summary = Table.summarize(this.trainingEntities, this.classifier);

        // // summarize testing data
        // HashMap<String,Integer> testSummary = Table.summarize(testingEntities, this.classifier);

        // summarize training data
        this.conditionalSummary = Table.conditionalSummarize(this.trainingEntities, this.classifier);

        System.out.println("General Summary");
        printSummary();

        System.out.println("Conditional Summary");
        printConditionalSummary();

        // init calculator
        this.calculator = new Calculator(this.summary,this.conditionalSummary,this.classifier,this.trainingEntities.size());
    }

    public void test(){
        // counter
        int counter = 0;

        // get testing size
        int testingSize = this.testingEntities.size();  

        // prediction classifier
        String predictClassifier = this.classifier.getPredictClassifier();
        
        // iterate through entities
        for (Entity entity : this.testingEntities) {
            // separate entity into attributes stored in list of strings 
            List<String> attributes = Table.entityToCol(entity, Table.colFormat);

            // prediction attribute
            String prediction = "";

            // other attributes
            List<String> other = new ArrayList<>();

            // iterate through attribute list
            for (int i = 0; i < attributes.size();i++){
                String attr = attributes.get(i);
                if (attr.contains(predictClassifier)){
                    prediction = attr;
                }
                else {
                    other.add(attr);
                }
            }

            HashMap<String,Double> result = this.calculator.predictionProb(other, predictClassifier);
            

            // generate prediction
            String generetedPrediction = generatePrediction(result);

            // compare with reality
            if (generetedPrediction.equals(prediction)){
                counter++;
            }
            
        }
        System.out.printf("Counter: %d testing: %d",counter,testingSize);
        this.accuracy = ((float)counter / testingSize);
    }

    // compare and give prediction
    private String generatePrediction(HashMap<String,Double> result){
        String prediction = "";
        double temp = Double.MIN_VALUE;

        for (Entry<String,Double> e : result.entrySet()){
            double value = e.getValue();
            if (value > temp){
                temp = value;
                prediction = e.getKey();
            }
        }
        return prediction;
    }
    
    // get data in each row then add to Entitys, add classifier's possible value list
    private List<Entity> getData(List<List<String>> data){
        // result after loading all data
        List<Entity> result = new ArrayList<Entity>(); 

        // list of classifiers
        List<String> classifierList = this.classifier.getClassifiers();

        // iterate through data set to get values
        for (int i = 2; i < data.size();i++){
            String detail = data.get(i).get(0);
            List<String> entityDetail = FileHandler.extract(detail);
            Entity entity = new Entity();
            for (int j = 0; j < classifierList.size();j++){
                String currentClass = classifierList.get(j);
                String detailValue = entityDetail.get(j);
                this.classifier.addToValueList(currentClass, detailValue);
                entity.addDetail(currentClass, detailValue);
            }
            result.add(entity);
            Entity.increaseRecordCount();
            
        }
        return result;
    }

    // seperate index 70% for training and 30% for testing
    private int separateIndex(int total_size){
        int index = (int)(total_size * 0.7);
        return index;
    }
    // method allows user to load in their favourite data set
    public void setDataSet(String dataSet){
        this.dataSet = dataSet;
    }

    public float getAccuracy(){
        return this.accuracy;
    }

    // print out general summary table
    public void printSummary(){
        Table.printSummary(this.summary);    
    }

    // print out conditional summary table
    public void printConditionalSummary(){
        Table.printConditionalSummary(this.conditionalSummary);
    }

    // get classifier list
    public List<String> getClassifierList(){
        return this.classifier.getClassifiers();
    }

    // get prediction classifier
    public String getPredictClassifer(){
        return this.classifier.getPredictClassifier();
    }

    // get classifer values
    public HashMap<String,HashSet<String>> getClassifierValues(){
        return this.classifier.getAllClassifierVals();
    }

    // add entity to list interact with GUI
    public void addEntity(Entity e){
        // add entity to lsit
        this.entities.add(e);
    }

    public static void main(String[] args){
        Trainer t = new Trainer("MLdata.csv");
        
        t.train();
        t.test();

        System.out.println(t.getAccuracy());
       
        // int noOfEntity = Entity.getRecordCount();
        // System.out.println(noOfEntity);
        // int index = t.separateIndex(noOfEntity);
        
        // for (int i = 0; i < index;i++){
        //     System.out.printf("\nindex: %d \nDetail: %s",i,Entity.entities.get(i));
        // }
        
    }
}
