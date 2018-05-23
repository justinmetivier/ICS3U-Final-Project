package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Main extends Application {

	public static final int WIDTH = 1200;
	public static final int HEIGHT = 600;
	public static final int CUBESIZE = 20;

	public static Scene mainScene;
	public static GridPane grid;
	public static Scene HomeScene;
	public static boolean gameOver = false;
	public static int HighScore = 0;
	public static Pane root;
	public static Line l;
	public static ArrayList<Rectangle> recList = new ArrayList<Rectangle>();
	public static ArrayList<Double> PrevX = new ArrayList<Double>();
	public static ArrayList<Double> PrevY = new ArrayList<Double>();
	public static Rectangle rect;
	public static int xVelocity = 0;
	public static int yVelocity = 0;

	public static File highscores;
	public static int counter = 0;
	public static int xTotal = 0, yTotal = 0;
	public static int leftOrRight = 0, upOrDown = 0;
	public static int velocity = CUBESIZE;
	public static long start;
	public static boolean commence;
	public static Rectangle food;
	public static boolean needFood;
	public static boolean h, v;
	public static int score;
	public static Text scoreBox;

	@Override
	public void start(Stage primaryStage) {
		try {
			root = new BorderPane();
			mainScene = new Scene(root, WIDTH, HEIGHT);
			highscores = new File("C:\\\\Users\\\\justi\\\\eclipse-workspace\\\\ICS3U-Final-Project");
			// HomeScene = new Scene(grid,400,400);

			rect = new Rectangle(4 * CUBESIZE, 0, CUBESIZE, CUBESIZE);
			recList.add(0, rect);
			root.getChildren().addAll(rect);

			food = new Rectangle(CUBESIZE, CUBESIZE);
			root.getChildren().add(food);
			scoreBox = new Text();
			root.getChildren().add(scoreBox);
			scoreBox.setX(0);
			scoreBox.setY(0);

			commence = false;
			needFood = true;
			h = false;
			v = false;
			score = 0;

			bindPlayerControls();

			start = System.nanoTime();
			primaryStage.setScene(mainScene);

			primaryStage.show();

			new AnimationTimer() {
				@Override
				public void handle(long now) {

					if (commence == true) {

						gameLive();
						double t = (double) (now - start / 1000000000);
						if (t % 5 == 0)
							move();

					}
				}

			}.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void bindPlayerControls() {
		mainScene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			// setTrailer();

			switch (event.getCode()) {
			case UP:
				if (v == false) {
					yVelocity = -velocity;
					xVelocity = 0;
					upOrDown = 1;
					commence = true;
					h = false;
					v = true;
				}
				break;

			case DOWN:
				if (v == false) {
					yVelocity = velocity;
					xVelocity = 0;
					upOrDown = 2;
					commence = true;
					h = false;
					v = true;
				}
				break;

			case LEFT:
				if (h == false) {
					yVelocity = 0;
					xVelocity = -velocity;
					upOrDown = 0;
					commence = true;
					h = true;
					v = false;
				}
				break;

			case RIGHT:
				if (h == false) {
					yVelocity = 0;
					xVelocity = velocity;
					upOrDown = 0;
					commence = true;
					h = true;
					v = false;
				}
				break;
			case P:
				commence = false;
				addLength();
				break;
			}

		});

	}

	public static void gameLive() {
		scoreBox.setText(score + "");

		if (killedYourself()) {
			gameOver = true;
			System.out.println("GameOver");
		}

		if (isEaten()) {
			addLength();
			changeColour();
			needFood = true;
			score++;
		}

		if (needFood == true) {
			addFood();
		}

	}

	public static void addFood() {
		food.setX(((int) (Math.random() * ((WIDTH / CUBESIZE) - CUBESIZE)) * CUBESIZE) * 1.0);
		food.setY(((int) (Math.random() * ((HEIGHT / CUBESIZE) - CUBESIZE)) * CUBESIZE) * 1.0);

		needFood = false;

	}

	public void move() {
		for (int i = 0; i < recList.size() - 1; i++) {
			PrevX.add(i, recList.get(i).getX());
			PrevY.add(i, recList.get(i).getY());
		}
		for (int i = 0; i < recList.size() - 1; i++) {
			recList.get(i + 1).setX(PrevX.get(i));
			recList.get(i + 1).setY(PrevY.get(i));
		}

		if (rect.getX() > WIDTH) {
			rect.setX(0);
		}
		if (rect.getX() < 0) {
			rect.setX(WIDTH - CUBESIZE);
		}
		if (rect.getY() > HEIGHT) {
			rect.setY(0);
		}
		if (rect.getY() < 0) {
			rect.setY(HEIGHT - CUBESIZE);
		}

		xTotal += xVelocity;
		yTotal += yVelocity;
		rect.setX(rect.getX() + xVelocity);
		rect.setY(rect.getY() + yVelocity);

	}

	public static void addLength() {
		Rectangle body = new Rectangle(CUBESIZE, CUBESIZE);
		recList.add(recList.size(), body);
		root.getChildren().add(body);
	}

	public static boolean isEaten() {
		if (rect.getX() == food.getX() && rect.getY() == food.getY()) {
			return true;
		} else
			return false;

	}

	public static void changeColour() {
		int colour = (int) (Math.random() * 7);

		switch (colour) {
		case 0:
			for (int i = 0; i < recList.size(); i++) {
				recList.get(i).setFill(Color.RED);
			}

			break;
		case 1:
			for (int i = 0; i < recList.size(); i++) {
				recList.get(i).setFill(Color.TURQUOISE);
			}

			break;
		case 2:
			for (int i = 0; i < recList.size(); i++) {
				recList.get(i).setFill(Color.YELLOW);
			}

			break;
		case 3:
			for (int i = 0; i < recList.size(); i++) {
				recList.get(i).setFill(Color.BLUE);
			}

			break;
		case 4:
			for (int i = 0; i < recList.size(); i++) {
				recList.get(i).setFill(Color.GREEN);
			}

			break;
		case 5:
			for (int i = 0; i < recList.size(); i++) {
				recList.get(i).setFill(Color.PURPLE);
			}

			break;
		case 6:
			for (int i = 0; i < recList.size(); i++) {
				recList.get(i).setFill(Color.PINK);
			}

			break;
		}

	}

	public static boolean killedYourself() {
		boolean yes = false;

		for (int i = 0; i < recList.size(); i++) {

			if (rect.getX() == recList.get(i).getX() && rect.getY() == recList.get(i).getY() && i > 1) {
				yes = true;
			} else {
				yes = false;
			}
		}
		return yes;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
