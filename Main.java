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

        // Search button handler
        searchButton.setOnAction(e -> {
            try {
                String name = searchField.getText().trim();
                if (name.isEmpty()) throw new Exception("Please enter a name");

                String stats = DatabaseManager.getPlayerStats(name);
                showAlert(Alert.AlertType.INFORMATION, "Player Stats", stats);
            } catch (PlayerNotFoundException | DatabaseException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });

        // Delete button handler
        deleteButton.setOnAction(e -> {
            try {
                String name = searchField.getText().trim();
                if (name.isEmpty()) throw new Exception("Please enter a name");

                DatabaseManager.deletePlayer(name);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Player deleted successfully!");
            } catch (PlayerNotFoundException | DatabaseException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });

        // Play again button handler
        playAgainButton.setOnAction(e -> {
            nameField.clear();
            oversField.clear();
            primaryStage.setScene(startScene);
        });

        // Result label listener
        resultLabel.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.isEmpty()) {
                playAgainButton.setVisible(true);
            }
        });

        primaryStage.setTitle("Cricket Game Simulator");
        primaryStage.setScene(startScene);
        primaryStage.show();
    }

    private void handleBatAction() {
        try {
            if (game == null || game.isGameOver()) return;

            String outcome = game.getPlayer().bat();
            updateGameUI(outcome);

            if (game.isGameOver()) {
                batButton.setDisable(true);
                resultLabel.setText(game.getResult(playerName));
            }
        } catch (InvalidBattingActionException ex) {
            showAlert(Alert.AlertType.ERROR, "Batting Error", ex.getMessage());
        }
    }

    private void validateInputs(String name, int overs) throws InvalidTeamNameException, InvalidOversException {
        if (name.isEmpty()) throw new InvalidTeamNameException("Player name cannot be empty!");
        if (overs <= 0) throw new InvalidOversException("Overs must be greater than zero!");
    }

    private void initializeGame(int overs) {
        game = new Game(overs);
    }

    private void updateUIForGameStart(Stage stage, Scene gameScene, int overs) {
        botScoreLabel.setText(game.getBot().getSummary(overs));
        ballsLabel.setText("Balls Faced: 0");
        scoreLabel.setText("Score: 0");
        playerStatusLabel.setText("Game started! Press 'Bat' to play.");
        resultLabel.setText("");
        batButton.setDisable(false);
        playAgainButton.setVisible(false);
        stage.setScene(gameScene);
    }

    private void updateGameUI(String outcome) {
        playerStatusLabel.setText(outcome);
        ballsLabel.setText("Balls Faced: " + game.getPlayer().getBallsFaced());
        scoreLabel.setText("Score: " + game.getPlayer().getScore());
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
