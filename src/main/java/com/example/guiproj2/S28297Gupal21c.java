package com.example.guiproj2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class S28297Gupal21c extends Application {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int SQUARE_SIZE = 50;
    private static final Color SQUARE_COLOR = Color.BLUE;
    private static final int SQUARE_DELAY = 650;
    private static final int MAX_SQUARES = 10;
    private Pane squaresPane;
    private Label percentageLabel;
    private List<Rectangle> squares;
    private Timeline timeline;
    private int squareCounter;
    private int animationCounter;
    private int clickedCounter;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        squaresPane = new Pane();
        squares = new ArrayList<>();
        squareCounter = 0;
        animationCounter = 0;
        clickedCounter = 0;
        root.setStyle("-fx-background-color: yellow;");
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("Falling Squares");
        primaryStage.setScene(scene);
        primaryStage.show();

        root.setCenter(squaresPane);

        percentageLabel = new Label("Clicked: 0%");
        percentageLabel.setPrefWidth(WINDOW_WIDTH);
        percentageLabel.setAlignment(Pos.CENTER);
        root.setBottom(percentageLabel);

        timeline = new Timeline(new KeyFrame(Duration.millis(SQUARE_DELAY), event -> {
            if (squareCounter < MAX_SQUARES) {
                spawnSquare();
                squareCounter++;
            } else {
                timeline.stop();
            }
        }));
        timeline.setCycleCount(MAX_SQUARES);
        timeline.play();
    }

    private void spawnSquare() {
        Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE, SQUARE_COLOR);
        square.setLayoutX(getRandomXPosition());
        square.setLayoutY(0);

        squares.add(square);
        squaresPane.getChildren().add(square);

        square.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Rectangle clickedSquare = (Rectangle) event.getSource();
            squares.remove(clickedSquare);
            squaresPane.getChildren().remove(clickedSquare);
            squareCounter--;
            clickedCounter++;
            updatePercentageLabel();

            if (animationCounter == MAX_SQUARES - 1) {
                showGameOverDialog();
            }
        });

        Duration duration = Duration.seconds(2);
        javafx.animation.TranslateTransition transition = new javafx.animation.TranslateTransition(duration, square);
        transition.setByY(WINDOW_HEIGHT);
        transition.setOnFinished(event -> {
            squares.remove(square);
            squaresPane.getChildren().remove(square);
            animationCounter++;

            if (animationCounter == MAX_SQUARES) {
                showGameOverDialog();
            }
        });
        transition.play();
    }

    private double getRandomXPosition() {
        return Math.random() * (WINDOW_WIDTH - SQUARE_SIZE);
    }

    private void showGameOverDialog() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("Game Over");
            if (((double)clickedCounter/MAX_SQUARES)*100 > 15){
                alert.setContentText("You win!");
            }else {
                alert.setContentText("You loose!");
            }


            ButtonType replayButton = new ButtonType("Replay");
            alert.getButtonTypes().add(replayButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == replayButton) {
                resetGame();
            } else {
                Platform.exit();
            }
        });
    }

    private void resetGame() {
        squareCounter = 0;
        animationCounter = 0;
        clickedCounter = 0;
        squares.clear();
        squaresPane.getChildren().clear();
        timeline.play();
        updatePercentageLabel();
    }

    private void updatePercentageLabel() {
        double percentage = ((double) clickedCounter / MAX_SQUARES) * 100;
        DecimalFormat df = new DecimalFormat("0.00");
        percentageLabel.setText("Clicked: " + df.format(percentage) + "%");
    }

    public static void main(String[] args) {
        launch(args);
    }
}