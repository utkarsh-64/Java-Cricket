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
