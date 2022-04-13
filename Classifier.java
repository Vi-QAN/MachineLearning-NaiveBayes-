import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;


public class Classifier {
    // classifier list and possible value is stored in form of 
    // classifier : set of possible values
    public static HashMap<String,HashSet<String>> values = new HashMap<String,HashSet<String>>();

    // classifier list in data set order
    public static List<String> list = new ArrayList<String>();
    
    @Override
    public String toString() {
        
        return values.toString();
    }

    // seperate columns to 
    public List<String> seperateCols(String format){
        List<String> cols = new ArrayList<String>();
        Classifier.values.entrySet().forEach((entry) -> {
            entry.getValue().forEach((value) -> {
                String columnName = "";
                columnName = String.format(format, entry.getKey(), value);
                cols.add(columnName);
            });
        });
        return cols;
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
        HashSet<String> availableValues = Classifier.values.get(classifier);
        if (!availableValues.contains(value)){
            Classifier.values.get(classifier).add(value);
        }
    }


}