package csvApp;

/**
 * @author marilounanderson on 21/10/2025
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainController {

    //UI Components
    @FXML private TableView<StudentRecord> tableView;
    @FXML private TableColumn<StudentRecord, String> nameColumn;
    @FXML private TableColumn<StudentRecord, Double> mathColumn;
    @FXML private TableColumn<StudentRecord, Double> scienceColumn;
    @FXML private TableColumn<StudentRecord, Double> englishColumn;
    @FXML private TableColumn<StudentRecord, Double> historyColumn;
    @FXML private Button loadButton;

    private ObservableList<StudentRecord> studentData = FXCollections.observableArrayList();


    /**
     * Called automatically by JavaFX after the FXML file is loaded.
     * This method initializes the TableView by linking each column
     * to the corresponding property in the StudentRecord class.
     * It also binds the ObservableList (studentData) to the table.
     */
    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        mathColumn.setCellValueFactory(cellData -> cellData.getValue().mathProperty().asObject());
        scienceColumn.setCellValueFactory(cellData -> cellData.getValue().scienceProperty().asObject());
        englishColumn.setCellValueFactory(cellData -> cellData.getValue().englishProperty().asObject());
        historyColumn.setCellValueFactory(cellData -> cellData.getValue().historyProperty().asObject());
        tableView.setItems(studentData);
    }

    /**
     * Triggered when the "Load CSV" button is clicked.
     * Opens a FileChooser dialog allowing the user to select a CSV file.
     * Once a valid file is chosen, it calls loadCSV() to read and display the data.
     *
     * @param event the button click event
     */
    @FXML
    private void handleLoad(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(loadButton.getScene().getWindow());

        if (file != null) {
            loadCSV(file);
        }
    }

    /**
     * Reads a CSV file line by line and converts each row into a StudentRecord object.
     * - Clears existing data from the ObservableList.
     * - Skips the header row (first line).
     * - Splits each subsequent line by commas into tokens.
     * - Parses numeric values and adds each record to the studentData list.
     *
     * @param file the CSV file to load
     */
    private void loadCSV(File file) {
        studentData.clear();  // Clear old data before loading new data

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Read and skip the first line (header row)

            // Read each remaining line and convert to a StudentRecord
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(","); // Split by comma into 5 tokens

                // Expecting exactly 5 values per line: name + 4 grades
                if (tokens.length == 5) {
                    studentData.add(new StudentRecord(
                            tokens[0],                              // Name
                            Double.parseDouble(tokens[1]),          // Math
                            Double.parseDouble(tokens[2]),          // Science
                            Double.parseDouble(tokens[3]),          // English
                            Double.parseDouble(tokens[4])           // History
                    ));
                }
            }

        } catch (IOException e) {
            // Print error details to console if file reading fails
            e.printStackTrace();
        }
    }
}

