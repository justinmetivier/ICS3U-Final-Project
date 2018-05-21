package application;
	
import java.io.File;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;

import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.TextField;


public class Main extends Application {
	//Snake and movement
	public static Rectangle [] rect = new Rectangle [10];
	
	public static int xVelocity = 0;
	public static int yVelocity = 0;
	public static double prevX = 0;
	public static double prevY = 0;
		
	
	//general sizes
	public static int blocksize = 10;
	public static int appW = 70 *10, appH = 70*10;
	
	
	
	//game scene
	public static Scene nGameScene;
	public static Pane root;
	public static Label lscore, ltime;
	public static int score;
	public static long time;


	
	//game over scene
	public static Scene overScene;
	public static GridPane overPane;
	public static boolean gameOver;
	public static Label overt;
	public static TextField namein;
	public static String name;
	public static Button submit;
	
	
	//main menu scene
	public static Scene homeScene;
	public static GridPane homePane;
	public static Button normal,speed;
	public static Label olabel,title,nExpl,sExpl; 
	public static int numRows = 4, numColumns =2;
	
	
	//high scores
	public static int nHighScore =0,sHighScore = 0;
	public static File highscores;
	
	
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setResizable(false);
			
			
			
			
			//main Menu
			homePane = new GridPane();
			homeScene = new Scene(homePane,appH,appW);
			homePane.setStyle("-fx-background-color: #242424;");
		
			for(int i =0; i<numColumns;i++) {
				ColumnConstraints column = new ColumnConstraints(appW/2);
	            homePane.getColumnConstraints().add(column);
			}
			for(int i =0;i<numRows;i++) {
				RowConstraints row = new RowConstraints(appH/4);
				homePane.getRowConstraints().add(row);
			}
			
			
			
			//Normal mode
			normal = new Button();
			normal.setText("Normal");
			homePane.add(normal, 0, 1); 
			GridPane.setHalignment(normal, HPos.CENTER);
			nExpl = new Label();
			nExpl.setTextFill(Color.WHITE);
			homePane.add(nExpl,0,2);
			nExpl.setText("The normal game mode has no time limit. \n\nTry to get as many points as possible without dying");
			nExpl.setTextAlignment(TextAlignment.CENTER);
			GridPane.setHalignment(nExpl, HPos.CENTER);
			GridPane.setValignment(nExpl, VPos.TOP);
			
			//Speed Mode
			speed = new Button();
			speed.setText("Speed");
			homePane.add(speed, 1, 1);
			GridPane.setHalignment(speed, HPos.CENTER);
			sExpl = new Label();
			sExpl.setTextFill(Color.WHITE);
			homePane.add(sExpl,1,2);
			sExpl.setText("The speed game mode has a 1 minute time limit. \n\nTry to get get as many points in a minute as \npossible without dying");
			sExpl.setTextAlignment(TextAlignment.CENTER);
			GridPane.setHalignment(sExpl, HPos.CENTER);
			GridPane.setValignment(sExpl, VPos.TOP);
			
			
			//Label
			olabel = new Label();
			olabel.setText("Chose a game Mode");
			olabel.setFont(Font.font ("Verdana", 20));
			olabel.setTextFill(Color.WHITE);
			
			title = new Label();
			title.setText("SNAKE GAME");
			title.setFont(Font.font ("Verdana", 50));
			title.setTextFill(Color.WHITE);
			
			homePane.getChildren().addAll(olabel,title);
			GridPane.setColumnSpan(olabel, 2);
			GridPane.setHalignment(olabel, HPos.CENTER);
			GridPane.setValignment(olabel, VPos.BOTTOM);
			
			GridPane.setColumnSpan(title, 2);
			GridPane.setHalignment(title, HPos.CENTER);
			GridPane.setValignment(title, VPos.TOP);
			
			normal.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	primaryStage.setScene(nGameScene); 
	            	}
	        });
			normal.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	primaryStage.setScene(nGameScene); 
	            	}
	        });
			speed.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	//primaryStage.setScene(sGameScene); 
	            	System.out.println("Game Mode not availble");
	            	}
	        });
			
			
			
			
			
			
			//game scene
			root = new Pane();
			nGameScene = new Scene(root,appH,appW);
			root.setStyle("-fx-background-color: #242424;");
			highscores = new File("C:\\\\Users\\\\justi\\\\eclipse-workspace\\\\ICS3U-Final-Project");
		
			
			for(int i = 0 ;i<rect.length;i++) {
				rect[i] = new Rectangle(blocksize,blocksize,blocksize,blocksize);
				root.getChildren().add(rect[i]);
			}
			for(int i = 0 ;i<rect.length;i++) {
				rect[i].setX(rect[0].getX()+10*i);
				rect[i].setFill(Color.WHITE);
			}
			rect[0].setFill(Color.RED);
			
			
			
			
			
			
			
			
			//Game Over
			overPane = new GridPane();
			overScene = new Scene(overPane,appH,appW);
			overPane.setStyle("-fx-background-color: #242424;");
			for(int i =0; i<numColumns;i++) {
				ColumnConstraints column = new ColumnConstraints(appW/2);
	            overPane.getColumnConstraints().add(column);
			}
			for(int i =0;i<numRows;i++) {
				RowConstraints row = new RowConstraints(appH/2);
				overPane.getRowConstraints().add(row);
			}
			//Over Title
			overt = new Label();
			overt.setText("Game Over");
			overt.setFont(Font.font ("Comic Sans", 50));
			overt.setTextFill(Color.WHITE);
			overPane.add(overt, 0, 0); 
			GridPane.setColumnSpan(overt, 2);
			GridPane.setHalignment(overt, HPos.CENTER);	
			
			//TextFeild
			namein = new TextField();
			overPane.add(namein, 0, 1); 
			GridPane.setHalignment(namein, HPos.RIGHT);	
			namein.setPromptText("Enter your initials.");
			submit = new Button();
			overPane.add(submit, 1, 1); 
			submit.setText("Submit");
			GridPane.setHalignment(submit, HPos.CENTER);	
			
			submit.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	name = namein.getText();
	            }
	        });
			
			
			

			
			
			
			primaryStage.setTitle("SNAKE");
			
			
			primaryStage.setScene(homeScene);
			bindPlayerControls();
			
			if(gameOver==false) {
				//primaryStage.setScene();
			} else {
				//play animation
				//System.out.println(HighScore+getHighScore());
				primaryStage.setScene(overScene);
			}
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void bindPlayerControls() {
		nGameScene.addEventHandler(KeyEvent.KEY_PRESSED, event ->{
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
		for(int i = 1;i < rect.length ; i++) {
			prevX = rect[i-1].getX();
			prevY = rect[i-1].getY();
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
