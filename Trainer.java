import java.util.ArrayList;
import java.util.List;

public class Trainer {
    public static List<Classifier> classifierList = new ArrayList<Classifier>();
    public static List<StudentDetail> studentDetail = new ArrayList<StudentDetail>();


    public Trainer(){
        AutoLoad load = new AutoLoad();
        load.start();
        try {
            load.join();
        } catch (InterruptedException e){
            System.err.println("Loading error");
            System.err.println(e.getMessage());
        }

        AutoExtract process = new AutoExtract(load.getResult(),this);
        process.start();
        try {
            process.join();
        } catch (InterruptedException e){
            System.err.println("Data process error");
        }

    }
    public List<String> getClassifiers(List<String> data){
        String classifiersStr = data.get(0);
        return FileHandler.extract(classifiersStr);
    }
    
    public List<List<String>> getData(List<List<String>> data){
        List<List<String>> result = new ArrayList<List<String>>(); 
        for (int i = 2; i < data.size();i++){
            String detail = data.get(i).get(0);
            result.add(FileHandler.extract(detail));
        }
        return result;
    }


    private class AutoLoad extends Thread {
        private final String filePath = "MLdata.csv";
        private List<List<String>> result = new ArrayList<List<String>>();

        @Override
        public void run(){
            result = FileHandler.readFile(filePath);
        }

        public List<List<String>> getResult(){
            return this.result;
        }
    }

    private class AutoExtract extends Thread {
        private List<List<String>> rawData;
        private List<String> classifiers = new ArrayList<String>();
        private List<List<String>> studentDetail = new ArrayList<List<String>>();
        private Trainer trainer;

        public AutoExtract(List<List<String>> rawData,Trainer trainer){
            this.rawData = rawData;
            this.trainer = trainer;
        }

        @Override
        public void run(){
            classifiers = this.trainer.getClassifiers(rawData.get(1));
            studentDetail = this.trainer.getData(rawData);
        }

        public List<String> getClassifiers(){
            return this.classifiers;
        }

        public List<List<String>> getStudentDetail(){
            return this.studentDetail;
        }
    }

    private class AutoConvert extends Thread {
        private List<List<String>> data;
        private List<StudentDetail> studentDetail = new ArrayList<>();    
        
    }

    public static void main(String[] args){
        Trainer t = new Trainer();
        List<List<String>> rawData = FileHandler.readFile("MLdata.csv");
        List<String> classifiersStr = t.getClassifiers(rawData.get(1));
        List<Classifier> classifiersObj = new ArrayList<Classifier>();
        for (String each : classifiersStr){
            System.out.println(each);
        }
        List<List<String>> data = t.getData(rawData);
        for (List<String> line : data){
            for (String detail : line){
                System.out.print(detail + " ");
            }
            System.out.println();
        }
    }
}
