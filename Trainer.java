import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

public class Trainer {
    private String dataSet = "MLData.csv";
    private static Table table;

    public Trainer(String dataSet){
        setDataSet(dataSet);
        initTrainer();
        table = new Table();
    }

    // method allows user to load in their favourite data set
    public void setDataSet(String dataSet){
        this.dataSet = dataSet;
    }

    // read in data set then get classifiers, and data in each row
    public void initTrainer(){
        List<List<String>> rawData = FileHandler.readFile(this.dataSet);
        Classifier.list = getClassifiers(rawData.get(1));
        Student.students = getData(rawData);
    }

    public double calculateProbability(){
        return 0;
    }
    
    // get classifiers in data set
    public List<String> getClassifiers(List<String> data){
        String classifiersStr = data.get(0);
        List<String> list = FileHandler.extract(classifiersStr);
        for (String classifier : list){
            if (!Classifier.values.entrySet().contains(classifier)){
                Classifier.values.put(classifier, new HashSet<String>());
            }
        }
        return list;
    }
    
    // get data in each row then add to Students, add classifier's possible value list
    public List<Student> getData(List<List<String>> data){
        List<Student> result = new ArrayList<Student>(); 
        Classifier classifier = new Classifier();
        for (int i = 2; i < data.size();i++){
            String detail = data.get(i).get(0);
            List<String> studentDetail = FileHandler.extract(detail);
            Student student = new Student();
            for (int j = 0; j < Classifier.list.size();j++){
                String currentClass = Classifier.list.get(j);
                String detailValue = studentDetail.get(j);
                classifier.addToValueList(currentClass, detailValue);
                student.addDetail(currentClass, detailValue);
            }
            result.add(student);
            Student.increaseRecordCount();
            
        }
        return result;
    }


    public static void main(String[] args){
        Trainer t = new Trainer("MLdata.csv");

        // Classifier.list.forEach((c) ->{
        //     System.out.println(c);
        // });
        // Classifier.values.forEach((c,val) -> {
        //     System.out.println(c + " " + val.toString());
        // });
        //System.out.println(Trainer.students.size());
        //System.out.println(Student.getRecordCount());

        Table table = new Table();
        table.summarize();
        table.printSummary();


        table.conditionalSummarize();
        table.printConditionalSummary();
    }
}
