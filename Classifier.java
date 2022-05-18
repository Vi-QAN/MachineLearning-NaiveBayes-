import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;


public class Classifier {
    // classifier list and possible value is stored in form of 
    // classifier : set of possible values
    private HashMap<String,HashSet<String>> values = new HashMap<String,HashSet<String>>();

    // classifier list in data set order
    private List<String> list = new ArrayList<String>();
    
    // contructor
    public Classifier(List<String> rawData){
        setClassifiers(rawData);
    }

    // get classifiers in data set
    public void setClassifiers(List<String> data){
        // get the row that contain classifier/column name
        String classifiersStr = data.get(0);

        // extract each classifier
        this.list = FileHandler.extract(classifiersStr);

        // initialize available values
        for (String classifier : this.list){
            if (!this.values.entrySet().contains(classifier)){
                this.values.put(classifier, new HashSet<String>());
            }
        }
    }

    

    public List<String> seperateCols(String format, HashMap<String,String> selections){
        List<String> cols = new ArrayList<String>();
        selections.entrySet().forEach((e) -> {
            String col = String.format(format,e.getKey(),e.getValue());
            cols.add(col);
        });
        return cols;
    }

    public void addToValueList(String classifier,String value){
        HashSet<String> availableValues = this.values.get(classifier);
        if (!availableValues.contains(value)){
            this.values.get(classifier).add(value);
        }
    }

    // get all available values of each classifier
    public HashMap<String,HashSet<String>> getClassifierVals(){
        return this.values;
    }

    // get list of classifier
    public List<String> getClassifiers(){
        return this.list;
    } 

    @Override
    public String toString() {
        return values.toString();
    }
}