package application;
	
import java.io.File;
import java.util.Scanner;

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
	
	public static Scene mainScene;
	public static GridPane grid;
	public static Scene HomeScene;
	public static boolean gameOver;
	public static int HighScore =0;
	public static Pane root;
	public static Line l;
	public static Rectangle [] rect = new Rectangle [100];
	//public static Rectangle rect2;
	public static int xVelocity = 0;
	public static int yVelocity = 0;
	public static double prevX = 0;
	public static double prevY = 0;
	public static File highscores;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			root = new BorderPane();
			mainScene = new Scene(root,400,400);
			highscores = new File("C:\\\\Users\\\\justi\\\\eclipse-workspace\\\\ICS3U-Final-Project");
//			HomeScene = new Scene(grid,400,400);
			
			for(int i = 0 ;i<rect.length;i++) {
				rect[i] = new Rectangle(10,10,10,10);
				rect[i].setFill(Color.BLACK);
				root.getChildren().add(rect[i]);
			}
			for(int i = 2 ;i<rect.length;i++) {
				rect[i].setX(rect[i-1].getX());
				rect[i].setY(rect[i-1].getY());
			}
			
			bindPlayerControls();
			
			if(gameOver==false) {
				primaryStage.setScene(mainScene);
			} else {
//				System.out.println(HighScore+getHighScore());
			}
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void bindPlayerControls() {
		mainScene.addEventHandler(KeyEvent.KEY_PRESSED, event ->{
			setTrailer();

			switch(event.getCode()){
				case UP:
					if(yVelocity <= 0) {
						yVelocity = -10;
						xVelocity = 0;
					}
				break;
				
				case DOWN:
					if(yVelocity >= 0) {
						yVelocity = 10;
						xVelocity = 0;
					}			
				break;
				
				case LEFT:
					if(xVelocity <= 0) {
						yVelocity = 0;
						xVelocity = -10;
					}				
				break;
				
				case RIGHT:
					if(xVelocity >= 0) {
						yVelocity = 0;
						xVelocity = 10;
					}	
					//System.out.println("RIGHT");
				break;
			}
			
			rect[0].setX(rect[0].getX()+xVelocity);
			rect[0].setY(rect[0].getY()+yVelocity);
		
		});
	}
	
	public static void setTrailer() {
		for(int i = 1 , j = 0 ;i < rect.length ; i++ , j++) {
			prevX = rect[j].getX();
			prevY = rect[j].getY();
			rect[i].setX(prevX);
			rect[i].setY(prevY);
			//System.out.print("yes");
		}
	}
	
//	public static int getHighScore() {
//		Scanner sc = new Scanner(highscores);
//		int scoreA = 0;
//		int scoreB = 0;
//		int highScore = 0;
//		while(sc.hasNext()) {
//			scoreA = sc.nextInt();
//			highScore = Math.max(scoreA, scoreB);
//			scoreB = scoreA;
//		}
//		
//		return highScore;
//	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
