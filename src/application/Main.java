package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Main extends Application {

	public static final int HEIGHT = 400;
	public static final int WIDTH = 400;
	public static final int CUBESIZE = 10;
	// public static final int HEIGHT = 400;
	// public static final int HEIGHT = 400;
	// public static final int HEIGHT = 400;

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

	@Override
	public void start(Stage primaryStage) {
		try {
			root = new BorderPane();
			mainScene = new Scene(root, HEIGHT, WIDTH);
			highscores = new File("C:\\\\Users\\\\justi\\\\eclipse-workspace\\\\ICS3U-Final-Project");
			// HomeScene = new Scene(grid,400,400);
			rect = new Rectangle(4 * CUBESIZE, 0, CUBESIZE, CUBESIZE);
			recList.add(0, rect);
			root.getChildren().addAll(rect);


			food = new Rectangle(WIDTH / 2, HEIGHT / 2, 10, 10);
			root.getChildren().add(food);

			commence = false;

			needFood = true;



			bindPlayerControls();

			start = System.nanoTime();
			if(gameOver==false)
			primaryStage.setScene(mainScene);

			primaryStage.show();

			new AnimationTimer() {
				@Override
				public void handle(long now) {

					if (commence == true) {
						
						if(killedYourself()) {
							gameOver=true;
						}

						if (isEaten()) {
							addLength();
							needFood = true;
						}

						if (needFood == true) {
							addFood();
						}
						

						double t = (double) (now - start / 1000000000);
						if (t % 10 == 0) {
							move();
						}
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
				yVelocity = -velocity;
				xVelocity = 0;
				upOrDown = 1;
				leftOrRight = 0;
				commence = true;
				break;

			case DOWN:
				yVelocity = velocity;
				xVelocity = 0;
				upOrDown = 2;
				leftOrRight = 0;
				commence = true;
				break;

			case LEFT:
				yVelocity = 0;
				xVelocity = -velocity;
				upOrDown = 0;
				leftOrRight = 1;
				commence = true;
				break;

			case RIGHT:
				yVelocity = 0;
				xVelocity = velocity;
				upOrDown = 0;
				leftOrRight = 2;
				commence = true;
				break;
			case P:
				commence = false;
				addLength();
				break;
			}

		});

	}

	public static void setTrailer() {

	}

	public static void addFood() {
		food.setX(((int) (Math.random() * ((WIDTH / 10) - 10)) * CUBESIZE) * 1.0);
		food.setY(((int) (Math.random() * ((WIDTH / 10) - 10)) * CUBESIZE) * 1.0);

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
		rect.setX(WIDTH-CUBESIZE);
	}
	if (rect.getY() > HEIGHT) {
		rect.setY(0);
	}
	if (rect.getY() < 0) {
		rect.setY(HEIGHT-CUBESIZE);
	}

	xTotal += xVelocity;
	yTotal += yVelocity;
	rect.setX(rect.getX() + xVelocity);
	rect.setY(rect.getY() + yVelocity);

	}

	public static void addLength() {
		Rectangle body = new Rectangle(10, 10);
		recList.add(recList.size(), body);
		root.getChildren().add(body);
	}

	public static boolean isEaten() {
		if (rect.getX() == food.getX() && rect.getY() == food.getY()) {
			return true;
		} else
			return false;

	}
	
	public static boolean killedYourself() {
		boolean yes = false;
		
		for(int i=0; i<recList.size(); i++) {
			
		
		if (rect.getX()==recList.get(i).getX()&&rect.getY()==recList.get(i).getY()&&i>1) {
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
