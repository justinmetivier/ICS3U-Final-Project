package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Main extends Application {

	public static int numRowCol = 35;
	public static final int CUBESIZE = 20;
	public static final int HEIGHT = numRowCol * CUBESIZE;
	public static final int WIDTH = numRowCol * CUBESIZE;
	
	//normal game scene
 	public static Scene mainScene;
 	public static BorderPane border;
 	public static Pane root;
 	public static Label lscore, ltime;
 	public static int score;
 	public static long time;
 	public static long start;
 	
 	//main menu scene
 	public static Scene homeScene;
 	public static GridPane homePane;
 	public static Button normal,speed;
 	public static Label olabel,title,nExpl,sExpl; 
 	public static int menuRows = 4, menuColumns =2;
 	
 	//game over scene
 	public static Scene overScene;
 	public static GridPane overPane;
 	public static boolean gameOver = false;
 	public static Label overt;
 	public static TextField namein;
 	public static String name;
 	public static Button submit;
 	public static boolean annoying = false;
 
 	//Snake and movement
 	enum Direction{
 		UP, DOWN, RIGHT, LEFT
 	}
 	public static Direction direction;
 	public static ArrayList<Rectangle> recList = new ArrayList<Rectangle>();
 	public static ArrayList<Double> PrevX = new ArrayList<Double>();
 	public static ArrayList<Double> PrevY = new ArrayList<Double>();
	public static Rectangle[][] background = new Rectangle [numRowCol][numRowCol];
 	public static Rectangle rect, rect2, rect3, rect4, rect5;
 	public static int xVelocity = 0, yVelocity = 0;
 	public static double prevX = 0, prevY = 0;
 	public static int xTotal = 0, yTotal = 0;
 	public static int leftOrRight = 0, upOrDown = 0;
 	public static int velocity = CUBESIZE;
 	public static int step;
 	public static Rectangle food;
 	public static boolean commence, needFood, validLocal;
 	public static boolean h, v;
	public static Text scoreBox;
	

	public static File highscores;
	public static int counter = 0;
	public static int randx, randy;
	public static Random rx = new Random(), ry = new Random();
	


	@Override
	public void start(Stage primaryStage) {
		try {
			mainMenuSetup();
			
			normalGameModeSetup();
			
			gameOverSetup();
			
			primaryStage.setScene(homeScene);
			
			normal.setOnAction(new EventHandler<ActionEvent>() {
 	            @Override
 	            public void handle(ActionEvent event) {
 	            	primaryStage.setScene(mainScene); 
 	            	}
 	        });
 			
 			speed.setOnAction(new EventHandler<ActionEvent>() {
 	            @Override
 	            public void handle(ActionEvent event) {
 	            	//primaryStage.setScene(sGameScene); 
 	            	System.out.println("Game Mode not availble");
 	            	}
 	        });
			
			bindPlayerControls();

			start = System.nanoTime();
		

			primaryStage.show();

			new AnimationTimer() {
				@Override
				public void handle(long now) {

					if (commence == true) {
						if(gameOver) {
							 rect.setFill(Color.RED);
							  for(int i = 0; i < recList.size(); i++) {
								  recList.get(i).setFill(Color.RED);
							  }
							  this.stop();
							  PauseTransition pause = new PauseTransition(Duration.seconds(3));
							  pause.setOnFinished(e -> {
							     primaryStage.setScene(overScene);
							  });
							  pause.play();
						}
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
			//System.out.println("GameOver");
		}

		if (isEaten()) {
			addLength();
			changeColour();
			needFood = true;
			//score++;
		}

		if (needFood == true) {
			addFood();
		}

	}

	
	public static void addFood() {
		//do {
			randx = rx.nextInt((WIDTH-CUBESIZE)-CUBESIZE)+CUBESIZE;
			randy = ry.nextInt((HEIGHT-CUBESIZE)-CUBESIZE)+CUBESIZE;
			
			randx = (int)(Math.round(randx/CUBESIZE)*CUBESIZE);
			randy = (int)(Math.round(randy/CUBESIZE)*CUBESIZE);
			/*for(int i = 0; i<recList.size(); i++) {
				if(randx != recList.get(i).getX() && randy != recList.get(i).getY() ) {
					validLocal = true;
				}
				else {
					validLocal = false;
				}
			}*/
		//}while(validLocal);
		
		//validLocal = false;
		
		food.setX(randx);
		food.setY(randy);
		randx = 0;
		randy = 0;
		
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
				
				body.setFill(Color.OLIVEDRAB);
		 		recList.add(recList.size(), body);
		 		root.getChildren().add(body);
				body.toBack();
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

	public static void gameOverSetup() {
 		//Game Over
 		overPane = new GridPane();
 		overScene = new Scene(overPane,HEIGHT,WIDTH);
 		overPane.setStyle("-fx-background-color: #6B6B6B;");
 		for(int i =0; i< menuColumns;i++) {
 			ColumnConstraints column = new ColumnConstraints(WIDTH/2);
             overPane.getColumnConstraints().add(column);
 		}
 		for(int i =0;i<menuRows;i++) {
 			RowConstraints row = new RowConstraints(HEIGHT/2);
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
 	}
	
	public static void normalGameModeSetup() {

		root = new Pane();
	
		
		mainScene = new Scene(root, WIDTH, HEIGHT);
		
		highscores = new File("C:\\\\Users\\\\justi\\\\eclipse-workspace\\\\ICS3U-Final-Project");
		// HomeScene = new Scene(grid,400,400);

		rect = new Rectangle(4 * CUBESIZE, 0, CUBESIZE, CUBESIZE);
		recList.add(0, rect);
		root.getChildren().addAll(rect);

		food = new Rectangle(CUBESIZE, CUBESIZE);
		root.getChildren().add(food);
		food.setFill(Color.WHITE);
		scoreBox = new Text();
		root.getChildren().add(scoreBox);
		scoreBox.setX(0);
		scoreBox.setY(0);

		commence = false;
		needFood = true;
		h = false;
		v = false;
		score = 0;
		/*for(int i = 0;i< numRowCol; i++) {
			for(int j = 0;j< numRowCol; j++) {
				Rectangle back = new Rectangle(CUBESIZE,CUBESIZE);
				back.setX(j*CUBESIZE);
				back.setY(i*CUBESIZE);
				back.setFill(Color.TRANSPARENT);
			    back.setStroke(Color.BLACK);
			    background[i][j] = back;
			    root.getChildren().add(background[i][j]);
			}
		}*/
		food.setFill(Color.CORNFLOWERBLUE);
	}

	public static void mainMenuSetup() {
 		//main Menu
 				homePane = new GridPane();
 				homeScene = new Scene(homePane,HEIGHT,WIDTH);
 				homePane.setStyle("-fx-background-color: #6B6B6B;");
 			
 				for(int i =0; i<menuColumns;i++) {
 					ColumnConstraints column = new ColumnConstraints(WIDTH/2);
 		            homePane.getColumnConstraints().add(column);
 				}
 				for(int i =0;i<menuRows;i++) {
 					RowConstraints row = new RowConstraints(HEIGHT/4);
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
 				nExpl.setText("The normal game mode has no time limit. \n\n"
 						+ "Try to get as many points as possible without dying");
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
 				sExpl.setText("The speed game mode has a 1 minute time limit. \n\n"
 						+ "Try to get get as many points in a minute as \npossible without dying");
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
 				
 				
 	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
