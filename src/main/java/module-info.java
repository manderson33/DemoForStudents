module com.example.demoforstudents {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demoforstudents to javafx.fxml;
    exports com.example.demoforstudents;

   opens csvApp to javafx.fxml;
   exports csvApp;
}