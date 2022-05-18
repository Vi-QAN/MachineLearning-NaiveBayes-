import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Entity {
    // contain the number of records shared among Entity objects
    private static int recordCount = 0;

    

    // Entity detail stored in form of key value pair (columnName : value)
    public HashMap<String,String> detail = new HashMap<String,String>();

    // empty constructor
    public Entity(){

    }

    // constructor with Entity detail passed as argument used in GUI when user want to add more Entity detail
    public Entity(HashMap<String,String> detail){
        setDetail(detail);
    }
    
    // method alows to add more information to Entity detail
    public void addDetail(String classifier,String value){
        this.detail.put(classifier, value);
    }


    // method to set Entity detail as a whole
    public void setDetail(HashMap<String,String> detail){
        this.detail = detail;
        increaseRecordCount();
    }

    // method to get Entity detail as a whole
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