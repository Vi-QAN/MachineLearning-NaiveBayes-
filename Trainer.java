import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;

public class Trainer {
    // data set currently used
    private String dataSet;

    // table use to generally summarize and conditionally summarize data set
    private Table table;

    // general summary of data set 
    private HashMap<String,Integer> summary = new HashMap<String,Integer>();

    // summary in target filtered such as "Become an Entrepreneur - Yes"
    private HashMap<String,HashMap<String,Integer>> conditionalSummary = new HashMap<>();

    // Classifier to perform actions related to classifier
    private Classifier classifier;

    // contain Entity detail in stored in form of Entity class shared among Entity objects
    private List<Entity> entities;

    public Trainer(String dataSet){
        setDataSet(dataSet);
        initTrainer();
    }

    // method allows user to load in their favourite data set
    public void setDataSet(String dataSet){
        this.dataSet = dataSet;
    }

    // read in data set then get classifiers, and data in each row
    public void initTrainer(){
        // read data set
        List<List<String>> rawData = FileHandler.readFile(this.dataSet);

        // extract classifiers
        this.classifier = new Classifier(rawData.get(1));

        // load data from data set
        this.entities = getData(rawData);

        // divide data set into 2 parts training and testing
        int midInd = separateIndex(this.entities.size());
        
        // get training data
        List<Entity> trainingEntities = new ArrayList<>();
        for (int i = 0; i < midInd; i++){
            trainingEntities.add(entities.get(i));
        }

        // get testing data
        List<Entity> testingEntities = new ArrayList<>();
        for (int i = midInd; i < entities.size(); i++){
            testingEntities.add(entities.get(i));
        }

        // summarize training data
        this.summary = Table.summarize(trainingEntities, this.classifier);

        // // summarize testing data
        // HashMap<String,Integer> testSummary = Table.summarize(testingEntities, this.classifier);

        // summarize training data
        this.conditionalSummary = Table.conditionalSummarize(trainingEntities, this.classifier);
    }

    public double calculateProbability(){
        return 0;
    }

    // print out general summary table
    public void printSummary(){
        Table.printSummary(this.summary);    
    }

    // print out conditional summary table
    public void printConditionalSummary(){
        Table.printConditionalSummary(this.conditionalSummary);
    }
    
    
    // get data in each row then add to Entitys, add classifier's possible value list
    public List<Entity> getData(List<List<String>> data){
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




    public static void main(String[] args){
        Trainer t = new Trainer("MLdata.csv");
        System.out.println("General Summary");
        t.printSummary();

        System.out.println("Conditional Summary");
        t.printConditionalSummary();
       
        // int noOfEntity = Entity.getRecordCount();
        // System.out.println(noOfEntity);
        // int index = t.separateIndex(noOfEntity);
        
        // for (int i = 0; i < index;i++){
        //     System.out.printf("\nindex: %d \nDetail: %s",i,Entity.entities.get(i));
        // }
        
    }
}
