import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Student {
    // contain the number of records shared among student objects
    private static int recordCount = 0;

    // contain student detail in stored in form of Student class shared among student objects
    public static List<Student> students = new ArrayList<Student>();

    // student detail stored in form of key value pair (columnName : value)
    public HashMap<String,String> detail = new HashMap<String,String>();

    // empty constructor
    public Student(){

    }

    // constructor with student detail passed as argument used in GUI when user want to add more student detail
    public Student(HashMap<String,String> detail){
        setDetail(detail);
    }
    
    // method alows to add more information to student detail
    public void addDetail(String classifier,String value){
        this.detail.put(classifier, value);
    }


    // method to set student detail as a whole
    public void setDetail(HashMap<String,String> detail){
        this.detail = detail;
        increaseRecordCount();
    }

    // method to get student detail as a whole
    public HashMap<String,String> getDetail(){
        return this.detail;
    }

    // get number of record
    public static int getRecordCount(){
        return recordCount;
    }

    // increase number of records everytime a new one added
    public static void increaseRecordCount(){
        recordCount++;
    }
    
    @Override
    public String toString(){
        
        return detail.toString();
    }
}