import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Table {

    // formate of the column (classifierName - value)
    public static final String colFormat = "%s - %s";

    
    // each entity will have column names and corresponding values
    // seperate entity attributes to columns 
    public static List<String> seperateCols(String format,Classifier classifier){
        // init cols to store those values in form of name - value 
        List<String> cols = new ArrayList<String>();

        // get column name and possible values
        HashMap<String,HashSet<String>> values = classifier.getClassifierVals();
        
        // iterate through column names
        values.entrySet().forEach((entry) -> {
            // iterate through column values
            entry.getValue().forEach((value) -> {
                String columnName = "";
                columnName = String.format(format, entry.getKey(), value);
                cols.add(columnName);
            });
        });
        return cols;
    }

    // initial format 'column - value : frequency' of summary tables
    // with value's frequency counter initialize to 0
    public static HashMap<String,Integer> formatTable(List<String> columns){
        // initialize format
        HashMap<String,Integer> format = new HashMap<>();

        // populate table with initial counter
        for (int i = 0; i < columns.size(); i++){
            format.put(columns.get(i),0);
        }
        return format;
    }

    // summarize for general summary table
    public static HashMap<String,Integer> summarize(List<Entity> entities,Classifier classifier){
        // separate an entity to many columns
        List<String> columns = seperateCols(Table.colFormat,classifier);

        // init summary table using HashMap
        HashMap<String,Integer> summary = formatTable(columns);

        // populate summary table by iterating through each entity
        entities.forEach((e) -> {
            // get attributes and corresponding values of each entity
            List<String> detail = entityToCol(e, colFormat);

            // increase the counters of each attribute's value
            detail.forEach((d) -> {
                summary.merge(d, 1, (oldVal,newVal) -> oldVal + newVal);
            });
        });
        return summary;
    }

    // summarize for target filtered summary table 
    public static HashMap<String,HashMap<String,Integer>> conditionalSummarize(List<Entity> entities,Classifier classifier){
        // separate an entity to many columns
        List<String> columns = seperateCols(Table.colFormat,classifier);
        
        // init conditional summary table 
        HashMap<String,HashMap<String,Integer>> conditionalSummary = new HashMap<>();

        // get the column want to predict (always positioned at the end)
        List<String> classifierList = classifier.getClassifiers();
        String predictCol = classifierList.get(classifierList.size() - 1);

        // 
        List<String> predictConditions = new ArrayList<String>();

        // 
        for (String col : columns){
            if (col.startsWith(predictCol)){
                predictConditions.add(col);
            }
        }

        for (String condition : predictConditions){
            System.out.println(condition);
        }

        for (String condition : predictConditions){
            conditionalSummary.put(condition,formatTable(columns));
        }

        entities.forEach((s) -> {
            List<String> details = entityToCol(s, colFormat);
            String targetDetail = "";
            for (int i = 0; i < details.size();i++){
                String currentDetail = details.get(i);
                if (currentDetail.startsWith(predictCol)){
                    targetDetail = currentDetail;
                }
            }
            HashMap<String,Integer> detailCols = conditionalSummary.get(targetDetail);
            for (int i = 0; i < details.size(); i++){
                detailCols.merge(details.get(i), 1, (cur,increment) -> cur + increment);
            }
        });
        return conditionalSummary;
    }


    public static List<String> entityToCol(Entity entity, String colFormat){
        List<String> cols = new ArrayList<String>();
        entity.detail.forEach((key,val) -> {
            String col = String.format(colFormat, key,val);
            cols.add(col);
        });
        return cols;
    }

    public String findTargetCol(List<String> cols, String colName){
        String value = "";
        for (int i = 0; i < cols.size();i++){
            String temp = cols.get(i);
            if (temp.startsWith(colName)){
                value = temp;
            }
        }
        return value;
    } 

    public static void printConditionalSummary(HashMap<String,HashMap<String,Integer>> conditionalSummary){
        conditionalSummary.forEach((key,cols) -> {
            System.out.println(key);
            printSummary(cols);
        });
    }

    public static void printSummary(HashMap<String,Integer> cols){
        cols.forEach((col,val) -> {
            System.out.printf("\t%s : %d\n",col,val);
        });
    }


    
}
