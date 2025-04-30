package src;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class GameCanvas extends StackPane {
    private Canvas canvas;
    private GraphicsContext gc;

    private double ballX = 400;
    private double ballY = 100;
    private double ballSpeed = 3;

    private boolean ballMoving = false;
    private boolean batSwinging = false;

    public GameCanvas() {
        canvas = new Canvas(600, 400);
        gc = canvas.getGraphicsContext2D();

        Button batButton = new Button("Bat");
        batButton.setOnAction(e -> swingBat());

        VBox layout = new VBox(10, canvas, batButton);
        this.getChildren().add(layout);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
            }
        };
        timer.start();
    }

    private void update() {
        if (ballMoving) {
            ballX -= ballSpeed;
            if (ballX < 120 && batSwinging) {
                ballSpeed = 0;
                ballX = 120;
            } else if (ballX < 0) {
                resetBall();
            }
        }
    }

    private void draw() {
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw pitch line
        gc.setStroke(Color.DARKGREEN);
        gc.strokeLine(100, 0, 100, 400);

        // Draw batsman
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(80, 300, 20, -60); // body
        if (batSwinging) {
            gc.setStroke(Color.BROWN);
            gc.strokeLine(100, 250, 140, 220); // bat swinging
        } else {
            gc.setStroke(Color.BROWN);
            gc.strokeLine(100, 250, 100, 200); // bat resting
        }

        // Draw ball
        gc.setFill(Color.RED);
        gc.fillOval(ballX, ballY, 15, 15);
    }

    private void swingBat() {
        batSwinging = true;
        ballMoving = true;

        // reset swing after a short time
        new Thread(() -> {
            try {
                Thread.sleep(400);
            } catch (InterruptedException ignored) {}
            batSwinging = false;
        }).start();
    }

    private void resetBall() {
        ballX = 400;
        ballY = 100;
        ballSpeed = 3;
        ballMoving = false;
    }
}
