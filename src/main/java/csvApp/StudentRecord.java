package csvApp;

/**
 * @author marilounanderson on 21/10/2025
 */


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StudentRecord {

    private final StringProperty name;
    private final DoubleProperty math;
    private final DoubleProperty science;
    private final DoubleProperty english;
    private final DoubleProperty history;

    public StudentRecord(String name, double math, double science, double english, double history) {
        this.name = new SimpleStringProperty(name);
        this.math = new SimpleDoubleProperty(math);
        this.science = new SimpleDoubleProperty(science);
        this.english = new SimpleDoubleProperty(english);
        this.history = new SimpleDoubleProperty(history);
    }

    // --- Getters and Setters ---

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getMath() {
        return math.get();
    }

    public void setMath(double math) {
        this.math.set(math);
    }

    public double getScience() {
        return science.get();
    }

    public void setScience(double science) {
        this.science.set(science);
    }

    public double getEnglish() {
        return english.get();
    }

    public void setEnglish(double english) {
        this.english.set(english);
    }

    public double getHistory() {
        return history.get();
    }

    public void setHistory(double history) {
        this.history.set(history);
    }

    // --- Property Methods (used by JavaFX binding) ---
    public StringProperty nameProperty() { return name; }
    public DoubleProperty mathProperty() { return math; }
    public DoubleProperty scienceProperty() { return science; }
    public DoubleProperty englishProperty() { return english; }
    public DoubleProperty historyProperty() { return history; }
}
