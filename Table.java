import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class Table {
    // classifiers will be splited in to many columns according to its possible values
    // column that will be in summary and conditional table
    private static List<String> columns = new ArrayList<String>();

    // formate of the column (classifierName - value)
    public static final String colFormat = "%s - %s";

    // general summary of data set 
    public static HashMap<String,Integer> summary = new HashMap<String,Integer>();

    // summary in target filtered such as "Become an Entrepreneur - Yes"
    public static HashMap<String,HashMap<String,Integer>> conditionalSummary = new HashMap<>();

    public Table(){
        if (columns.isEmpty()){
            setColumns();
        }
    }

    // format columns of summary tables
    public HashMap<String,Integer> formatTable(){
        HashMap<String,Integer> format = new HashMap<>();
        for (int i = 0; i < columns.size(); i++){
            format.put(columns.get(i),0);
        }
        return format;
    }

    // summarize for general summary table
    public void summarize(){
        summary = formatTable();
        Student.students.forEach((s) -> {
            List<String> detail = studentToCol(s, colFormat);
            detail.forEach((d) -> {
                summary.merge(d, 1, (oldVal,newVal) -> oldVal + newVal);
            });
        });
    }

    // summarize for target filtered summary table 
    public void conditionalSummarize(){
        List<String> classList = Classifier.list;
        String predictCol = classList.get(classList.size() - 1);
        List<String> predictConditions = new ArrayList<String>();
        for (String col : columns){
            if (col.startsWith(predictCol)){
                predictConditions.add(col);
            }
        }

        for (String condition : predictConditions){
            System.out.println(condition);
        }

        for (String condition : predictConditions){
            conditionalSummary.put(condition,formatTable());
        }

        Student.students.forEach((s) -> {
            List<String> details = studentToCol(s, colFormat);
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
        
    }


    public void setColumns() {
        Classifier cl = new Classifier();
        columns = cl.seperateCols(colFormat);
    }

    public List<String> getColumns() {
        return this.columns;
    }

    public List<String> studentToCol(Student student, String colFormat){
        List<String> cols = new ArrayList<String>();
        student.detail.forEach((key,val) -> {
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

    public void printConditionalSummary(){
        conditionalSummary.forEach((key,cols) -> {
            System.out.println(key);
            printSummary(cols);
        });
    }

    private void printSummary(HashMap<String,Integer> cols){
        cols.forEach((col,val) -> {
            System.out.printf("\t%s : %d\n",col,val);
        });
    }

    public void printSummary(){
        printSummary(summary);
    }





    
}
