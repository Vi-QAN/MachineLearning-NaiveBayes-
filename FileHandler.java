import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;

public class FileHandler {
    public static List<List<String>> readFile(String filePath){
        List<List<String>> data = new ArrayList<List<String>>();
        try {
            File file = new File(filePath);
            Scanner s = new Scanner(file).useDelimiter(",");
            while (s.hasNextLine()){
                List<String> lineData = Arrays.asList(s.nextLine());//splitting lines
                data.add(lineData);
        }
          
        } catch (Exception e) { 
            System.out.println("Error in csv file reading");
        }
        return data;
    } 
    
    public static List<String> extract(String line){
        List<String> classifiers = new ArrayList<String>();
        String classifier = "";
        for (int i = 0;i < line.length();i++){
            char c = line.charAt(i);
            if (c != ',' ){
            classifier += c;
            }
            else {
                classifiers.add(classifier);
                classifier = "";
            }
        }
        classifiers.add(classifier);
        
        return classifiers;
    }
}
