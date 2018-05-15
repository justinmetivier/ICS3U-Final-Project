package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

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
	public static Rectangle rect;
	public static Rectangle rect2;
	public static int xVelocity = 0;
	public static int yVelocity = 0;
	public static double prevX = 0;
	public static double prevY = 0;
	public static File highscores;

	@Override
	public void start(Stage primaryStage) {
		try {
			root = new BorderPane();
			mainScene = new Scene(root, 400, 400);
			highscores = new File("C:\\\\Users\\\\justi\\\\eclipse-workspace\\\\ICS3U-Final-Project");
			// HomeScene = new Scene(grid,400,400);
			rect = new Rectangle(10, 10, 10, 10);
			rect2 = new Rectangle(0, 10, 10, 10);
			recList.add(rect);
			recList.add(rect2);
			root.getChildren().addAll(rect, rect2);

			bindPlayerControls();
			addLength();

			primaryStage.setScene(mainScene);

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void bindPlayerControls() {
		mainScene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			setTrailer();

			switch (event.getCode()) {
			case UP:
				yVelocity = -10;
				xVelocity = 0;
				break;

			case DOWN:
				yVelocity = 10;
				xVelocity = 0;
				break;

			case LEFT:
				yVelocity = 0;
				xVelocity = -10;
				break;

			case RIGHT:
				yVelocity = 0;
				xVelocity = 10;
				System.out.println("RIGHT");
				break;
			}

			rect.setX(rect.getX() + xVelocity);
			rect.setY(rect.getY() + yVelocity);

		});

	}

	public static void setTrailer() {
		int length = recList.size();
		for (int i = 0; i < length - 1; i++) {
			prevX = recList.get(i).getX();
			prevY = recList.get(i).getY();
			recList.get(i + 1).setX(prevX);
			recList.get(i + 1).setY(prevY);
			;
		}

		System.out.print("yes");
	}

	public static void addLength() {
		
			Rectangle body = new Rectangle();
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
