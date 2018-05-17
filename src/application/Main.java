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
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Main extends Application {

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
	public static Rectangle rect2;
	public static Rectangle rect3;
	public static Rectangle rect4;
	public static int xVelocity = 0;
	public static int yVelocity = 0;
	public static double prevX = 0;
	public static double prevY = 0;
	public static File highscores;
	public static int counter = 0;
	public static int xTotal = 0, yTotal = 0;
	public static int leftOrRight = 0, upOrDown = 0;
	public static int velocity = 1;

	@Override
	public void start(Stage primaryStage) {
		try {
			root = new BorderPane();
			mainScene = new Scene(root, 400, 400);
			highscores = new File("C:\\\\Users\\\\justi\\\\eclipse-workspace\\\\ICS3U-Final-Project");
			// HomeScene = new Scene(grid,400,400);
			rect = new Rectangle(40, 10, 10, 10);
			rect2 = new Rectangle();
			rect3 = new Rectangle();
			rect4 = new Rectangle();

			recList.add(rect);
			recList.add(rect2);
			recList.add(rect3);
			recList.add(rect4);
			root.getChildren().addAll(rect, rect2, rect3, rect4);

			bindPlayerControls();
			// addLength();

			primaryStage.setScene(mainScene);

			primaryStage.show();

			new AnimationTimer() {
				@Override
				public void handle(long now) {
					setTrailer();
					
					
					
					if(rect.getX()>400) {
						rect.setX(0);
					}
					if(rect.getX()<0) {
						rect.setX(400);
					}
					if(rect.getY()>400) {
						rect.setY(0);
					}
					if(rect.getY()<0) {
						rect.setY(390);
					}
					
					xTotal+=xVelocity;
					yTotal+=yVelocity;
					rect.setX(rect.getX()+xVelocity);
					rect.setY(rect.getY()+yVelocity);
					
//					for(int i = 0; i<recList.size(); i++) {
//						if(leftOrRight == 1) {
//							if(recList.get(i).getX()!=xTotal) {
//								recList.get(i).setX(recList.get(i).getX()+xVelocity);
//							}
//						}
//					}
					
					
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
				break;

			case DOWN:
				yVelocity = velocity;
				xVelocity = 0;
				upOrDown = 2;
				leftOrRight = 0; 
				break;

			case LEFT:
				yVelocity = 0;
				xVelocity = -velocity;
				upOrDown = 0;
				leftOrRight = 1; 
				break;

			case RIGHT:
				yVelocity = 0;
				xVelocity = velocity;
				upOrDown = 0;
				leftOrRight = 2; 
				break;
			case T:
				counter++;
			}

		});

	}

	public static void setTrailer() {
		for (int i = 0; i < recList.size()-1; i++) {
			PrevX.add(recList.get(i).getX());
			PrevY.add(recList.get(i).getY());
		}
		for (int i = 0; i < recList.size() - 1; i++) {
			recList.get(i + 1).setX(PrevX.get(i));
			recList.get(i + 1).setY(PrevY.get(i));
		}
		for (int i = 0; i < recList.size() - 1; i++) {
			PrevX.remove(i);
			PrevY.remove(i);
		}
		
	}

	public static void addLength(int x, int y) {

		Rectangle body = new Rectangle(x, y , 10, 10);
		recList.add(body);
		root.getChildren().add(body);
	}

	// public static int getHighScore() {
	// Scanner sc = new Scanner(highscores);
	// int scoreA = 0;
	// int scoreB = 0;
	// int highScore = 0;
	// while(sc.hasNext()) {
	// scoreA = sc.nextInt();
	// highScore = Math.max(scoreA, scoreB);
	// scoreB = scoreA;
	// }
	//
	// return highScore;
	// }

	public static void main(String[] args) {
		launch(args);
	}
}
