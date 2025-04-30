package src;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.sql.*;

public class Main extends Application {
    private Game game;
    private String playerName;

    // UI Elements
    private Label botScoreLabel = new Label();
    private Label playerStatusLabel = new Label("Press 'Bat' to play!");
    private Label ballsLabel = new Label();
    private Label scoreLabel = new Label("Score: 0");
    private Label resultLabel = new Label();

    private Button batButton = new Button("Bat");
    private Button playAgainButton = new Button("Play Again");
    private Button searchButton = new Button("Search Player");
    private Button deleteButton = new Button("Delete Player");
    private TextField searchField = new TextField();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Entry form components
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");
        TextField oversField = new TextField();
        oversField.setPromptText("Enter number of overs");
        Button startButton = new Button("Start Game");

        // Start scene layout
        VBox startLayout = new VBox(10,
                new Label("Welcome to the Cricket Game!"),
                nameField,
                oversField,
                startButton,
                new Separator(),
                searchField,
                new HBox(10, searchButton, deleteButton)
        );
        startLayout.setStyle("-fx-padding: 20;");
        Scene startScene = new Scene(startLayout, 400, 300);

        // Game scene layout
        VBox gameLayout = new VBox(15,
                botScoreLabel,
                ballsLabel,
                scoreLabel,
                playerStatusLabel,
                batButton,
                resultLabel,
                playAgainButton
        );
        gameLayout.setStyle("-fx-padding: 20;");
        Scene gameScene = new Scene(gameLayout, 400, 300);

        // Bat button handler
        batButton.setOnAction(e -> handleBatAction());

        // Start button handler
        startButton.setOnAction(e -> {
            try {
                playerName = nameField.getText().trim();
                int overs = Integer.parseInt(oversField.getText().trim());

                validateInputs(playerName, overs);
                //checkPlayerExistence(playerName);

                initializeGame(overs);
                updateUIForGameStart(primaryStage, gameScene, overs);
                DatabaseManager.createPlayer(playerName);
            } catch (InvalidTeamNameException | InvalidOversException ex) {
                showAlert(Alert.AlertType.ERROR, "Input Error", ex.getMessage());
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Something went wrong: " + ex.getMessage());
            }
        });
