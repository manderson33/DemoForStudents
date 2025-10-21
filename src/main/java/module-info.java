module com.example.demoforstudents {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demoforstudents to javafx.fxml;
    exports com.example.demoforstudents;

    exports quizApp;
    opens quizApp to javafx.fxml;
}