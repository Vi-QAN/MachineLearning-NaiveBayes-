

public class Classifier {
    private String name;
    private int falseCounter;
    private int trueCounter;

    public Classifier(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getFalseCounter() {
        return this.falseCounter;
    }

    public void increaseFalseCounter() {
        this.falseCounter++;
    }

    public int getTrueCounter() {
        return this.trueCounter;
    }

    public void increaseTrueCounter() {
        this.trueCounter++;
    }
    
}