package quizApp;

/**
 * @author marilounanderson on 21/10/2025
 */


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller for the Quiz Application.
 * Handles user interactions, quiz logic, CSV loading, and timer updates.
 */
public class QuizController {

    @FXML private Label questionLabel;
    @FXML private Label timerLabel;
    @FXML private Label scoreLabel;
    @FXML private Button loadButton;
    @FXML private Button startButton;
    @FXML private Button nextButton;
    @FXML private Button optionAButton;
    @FXML private Button optionBButton;
    @FXML private Button optionCButton;
    @FXML private Button optionDButton;

    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private boolean answered = false;

    private Timeline timer;
    private int timeRemaining = 60; // total quiz time (in seconds)

    /**
     * Called automatically after FXML is loaded.
     * Disables quiz controls until CSV is loaded.
     */
    @FXML
    private void initialize() {
        disableAnswerButtons(true);
        nextButton.setDisable(true);
        startButton.setDisable(true);
        timerLabel.setText("00:00");
    }

    /**
     * Allows user to choose and load a CSV file with quiz questions.
     * CSV format: Question,OptionA,OptionB,OptionC,OptionD,CorrectAnswer
     */
    @FXML
    private void handleLoadCSV(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Quiz CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(loadButton.getScene().getWindow());

        if (file != null) {
            loadQuestionsFromCSV(file);
        }
    }

    /**
     * Reads and parses questions from the CSV file.
     */
    private void loadQuestionsFromCSV(File file) {
        questions.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 6) {
                    questions.add(new Question(
                            tokens[0],
                            tokens[1],
                            tokens[2],
                            tokens[3],
                            tokens[4],
                            tokens[5]
                    ));
                }
            }

            if (!questions.isEmpty()) {
                startButton.setDisable(false);
                questionLabel.setText("File loaded successfully. Click Start Quiz to begin!");
            } else {
                questionLabel.setText("CSV file loaded but no valid questions found.");
            }

        } catch (IOException e) {
            questionLabel.setText("Error loading CSV file.");
            e.printStackTrace();
        }
    }

    /**
     * Starts the quiz and initializes the timer.
     */
    @FXML
    private void handleStartQuiz(ActionEvent event) {
        score = 0;
        currentQuestionIndex = 0;
        scoreLabel.setText("0");
        timeRemaining = 60; // reset timer

        startTimer();
        showQuestion();
        disableAnswerButtons(false);
        startButton.setDisable(true);
        nextButton.setDisable(true);
    }

    /**
     * Displays one question at a time with its options.
     */
    private void showQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            questionLabel.setText((currentQuestionIndex + 1) + ". " + q.getQuestionText());
            optionAButton.setText(q.getOptionA());
            optionBButton.setText(q.getOptionB());
            optionCButton.setText(q.getOptionC());
            optionDButton.setText(q.getOptionD());
            answered = false;
            nextButton.setDisable(true);
        } else {
            endQuiz();
        }
    }

    /**
     * Handles answer button click and checks if the answer is correct.
     */
    @FXML
    private void handleAnswerSelected(ActionEvent event) {
        if (answered) return;

        Button selectedButton = (Button) event.getSource();
        String selectedAnswer = selectedButton.getText();
        String correctAnswer = questions.get(currentQuestionIndex).getCorrectAnswer();

        if (selectedAnswer.equalsIgnoreCase(correctAnswer)) {
            score++;
            scoreLabel.setText(String.valueOf(score));
            selectedButton.setStyle("-fx-background-color: #81C784;"); // green
        } else {
            selectedButton.setStyle("-fx-background-color: #E57373;"); // red
        }

        // Highlight correct answer
        highlightCorrectAnswer(correctAnswer);
        answered = true;
        disableAnswerButtons(true);
        nextButton.setDisable(false);
    }

    /**
     * Loads the next question in the quiz.
     */
    @FXML
    private void handleNextQuestion(ActionEvent event) {
        resetButtonStyles();
        currentQuestionIndex++;
        disableAnswerButtons(false);
        showQuestion();
    }

    /**
     * Starts a countdown timer using Timeline.
     */
    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeRemaining--;
            timerLabel.setText(formatTime(timeRemaining));

            if (timeRemaining <= 0) {
                timer.stop();
                endQuiz();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    /**
     * Ends the quiz and displays final score.
     */
    private void endQuiz() {
        disableAnswerButtons(true);
        nextButton.setDisable(true);
        startButton.setDisable(false);
        if (timer != null) timer.stop();

        questionLabel.setText("Quiz Over! Your score: " + score + "/" + questions.size());
    }

    // ===== Utility Methods =====

    private void disableAnswerButtons(boolean disable) {
        optionAButton.setDisable(disable);
        optionBButton.setDisable(disable);
        optionCButton.setDisable(disable);
        optionDButton.setDisable(disable);
    }

    private void highlightCorrectAnswer(String correctAnswer) {
        for (Button b : Arrays.asList(optionAButton, optionBButton, optionCButton, optionDButton)) {
            if (b.getText().equalsIgnoreCase(correctAnswer)) {
                b.setStyle("-fx-background-color: #64B5F6;"); // blue
            }
        }
    }

    private void resetButtonStyles() {
        for (Button b : Arrays.asList(optionAButton, optionBButton, optionCButton, optionDButton)) {
            b.setStyle("");
        }
    }

    private String formatTime(int seconds) {
        int mins = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }
}
