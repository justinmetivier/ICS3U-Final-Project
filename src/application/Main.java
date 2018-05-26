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

	public static int numRowCol = 25;
	public static final int CUBESIZE = 25, border = CUBESIZE;
	public static final int HEIGHT = numRowCol * CUBESIZE, WIDTH = numRowCol * CUBESIZE;
	public static final int appH = HEIGHT+(2*border), appW = WIDTH+(2*border)+200;
	
	//normal game scene
 	public static Scene mainScene;
 	public static Pane root;
 	public static Label lscore, ltime;
 	public static int score;
 	public static long time;
 	public static long start;
	public static Text scoreBox;
 	
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
 	public static Label overt,scoredisp;
 	public static TextField namein;
 	public static String name;
 	public static Button submit, back = new Button("Back to Menu");
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
 	public static double xVelocity = 0, yVelocity = 0;
 	public static double prevX = 0, prevY = 0;
 	public static double velocity = CUBESIZE;
 	public static Rectangle food;
 	public static boolean commence, needFood, validLocal;
 	public static boolean h, v;
 	public static Rectangle t,b,r,l;

	

	public static File highscores;

	public static int randx, randy;
	public static Random rx = new Random(), ry = new Random();
	


	@Override
	public void start(Stage primaryStage) {
		try {
			mainMenuSetup();
			
			normalGameModeSetup();
			
			gameOverSetup();
			gameOver = false;
			primaryStage.setScene(homeScene);
			primaryStage.setResizable(false);
			
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
							this.stop();
							 rect.setFill(Color.RED);
							  for(int i = 0; i < recList.size(); i++) {
								  recList.get(i).setFill(Color.RED);
							  }
							  
							  PauseTransition pause = new PauseTransition(Duration.seconds(2));
							  pause.setOnFinished(e -> {
								  for (int i = recList.size()-1; i >= 0 ; i--) {
									  recList.remove(i);
								  }
								  for (int i = PrevX.size()-1; i >= 0 ; i--) {
									  PrevX.remove(i);
									  PrevY.remove(i);
								  }
							     primaryStage.setScene(overScene);
							  });
							  pause.play();
						}
						else {
							gameLive();
							//double t = (double) (now - start / 1000000000);
							//if (t % 10 == 0)
								move();
								//pause();
						}

					}
				}

			}.start();
			
			back.setOnAction(new EventHandler<ActionEvent>() {
 	            @Override
 	            public void handle(ActionEvent event) {
 	            	
 	            	start(primaryStage);
 	            	}
 	        });

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
					commence = true;
					h = false;
					v = true;
				}
				break;

			case DOWN:
				if (v == false) {
					yVelocity = velocity;
					xVelocity = 0;
					commence = true;
					h = false;
					v = true;
				}
				break;

			case LEFT:
				if (h == false) {
					yVelocity = 0;
					xVelocity = -velocity;
					commence = true;
					h = true;
					v = false;
				}
				break;

			case RIGHT:
				if (h == false) {
					yVelocity = 0;
					xVelocity = velocity;
					commence = true;
					h = true;
					v = false;
				}
				break;
			case P:
				commence = false;
				
				PauseTransition pause = new PauseTransition(Duration.seconds(3));
				  pause.setOnFinished(e -> {
				     
				  });
				  pause.play();
				break;
			
			}

		});

	}

	public static void gameLive() {
		scoreBox.setText("Score: "+ score + "");

		if (killedYourself()) {
			
			gameOver = true;
			//System.out.println("GameOver");
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
		//do {
			randx = rx.nextInt(((WIDTH+border)-CUBESIZE)-(CUBESIZE+border))+CUBESIZE;
			randy = ry.nextInt(((WIDTH+border)-CUBESIZE)-(CUBESIZE+border))+CUBESIZE;
			
			randx = (int)(Math.round(randx/CUBESIZE)*CUBESIZE);
			randy = (int)(Math.round(randy/CUBESIZE)*CUBESIZE);
			for(int i = 0; i<recList.size(); i++) {
				if(randx != recList.get(i).getX() && randy != recList.get(i).getY() ) {
					validLocal = true;
				}
				else {
					validLocal = false;
				}
			}
		//}while(validLocal);
		
		validLocal = false;
		
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
/*
		if (rect.getX() > WIDTH+border) {
			//rect.setX(0);
			gameOver=true;
		}
		if (rect.getX() < border) {
			//rect.setX(WIDTH - CUBESIZE);
			gameOver=true;
		}
		if (rect.getY() > HEIGHT+border) {
			//rect.setY(0);
			gameOver=true;
		}
		if (rect.getY() < border) {
			//rect.setY(HEIGHT - CUBESIZE);
			gameOver=true;
		}*/
		
		if(rect.getX() + xVelocity<border|| rect.getX() + xVelocity>(WIDTH+border)){
			gameOver = true;
		}
		else if(rect.getY() + yVelocity<border|| rect.getY() + yVelocity>(HEIGHT)+border){
			gameOver = true;
		}
		else {
			rect.setX(rect.getX() + xVelocity);
			rect.setY(rect.getY() + yVelocity);
		}
		if (killedYourself()) {
			
			gameOver = true;
			//System.out.println("GameOver");
		}
		
	}

	public static void pause(){
		PauseTransition pause = new PauseTransition(Duration.seconds(5));
		  pause.setOnFinished(e -> {
			  
		  });
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

		}else
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
				break;
			} 
			else if(rect.getX() < border || rect.getX() > (WIDTH+border)||+
					rect.getY() < border || rect.getY() > (HEIGHT+border)) {
				yes = true;
				break;
			}
			else {
				yes = false;
			}
		}
		return yes;
	}

	public static void gameOverSetup() {
 		//Game Over
 		overPane = new GridPane();
 		overScene = new Scene(overPane,appW,appH);
 		overPane.setStyle("-fx-background-color: #6B6B6B;");
 		for(int i =0; i< menuColumns;i++) {
 			ColumnConstraints column = new ColumnConstraints(appW/2);
             overPane.getColumnConstraints().add(column);
 		}
 		for(int i =0;i<menuRows;i++) {
 			RowConstraints row = new RowConstraints(appH/3);
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
 		
 		//score Title
 		scoredisp = new Label();
 		scoredisp.setText("Score: " + score);
 		scoredisp.setFont(Font.font ("Comic Sans", 50));
 		scoredisp.setTextFill(Color.WHITE);
 		overPane.add(scoredisp, 0, 1); 
 		GridPane.setColumnSpan(scoredisp, 2);
 		GridPane.setHalignment(scoredisp, HPos.CENTER);	
 		GridPane.setValignment(scoredisp, VPos.TOP);
 		
 		overPane.add(back, 0, 1); 
 		GridPane.setColumnSpan(back, 2);
 		GridPane.setHalignment(back, HPos.CENTER);	
 		GridPane.setValignment(back, VPos.BOTTOM);
 		
 		
 		//TextFeild
 		namein = new TextField();
 		overPane.add(namein, 0, 2); 
 		GridPane.setHalignment(namein, HPos.RIGHT);	
 		namein.setPromptText("Enter your initials.");
 		namein.setPrefWidth(100); 
 		submit = new Button();
 		overPane.add(submit, 1, 2); 
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
	
		mainScene = new Scene(root,appW, appH);
		
		highscores = new File("C:\\\\Users\\\\justi\\\\eclipse-workspace\\\\ICS3U-Final-Project");
		

		rect = new Rectangle(4 * CUBESIZE, border, CUBESIZE, CUBESIZE);
		recList.add(0, rect);
		root.getChildren().addAll(rect);

		food = new Rectangle(CUBESIZE, CUBESIZE);
		root.getChildren().add(food);
		food.setFill(Color.WHITE);
		scoreBox = new Text();
		root.getChildren().add(scoreBox);
		scoreBox.setX(appW-75);
		scoreBox.setY(appH/4);

		commence = false;
		needFood = true;
		h = false;
		v = false;
		score = 0;
		for(int i = 0;i< numRowCol; i++) {
			for(int j = 0;j< numRowCol; j++) {
				Rectangle back = new Rectangle(CUBESIZE,CUBESIZE);
				back.setX((j*CUBESIZE)+border);
				back.setY((i*CUBESIZE)+border);
				back.setFill(Color.TRANSPARENT);
			    back.setStroke(Color.GREY);
			    background[i][j] = back;
			    root.getChildren().add(background[i][j]);
			}
		}
		
		t = new Rectangle(border,0,WIDTH,border);
		t.setFill(Color.WHITE);
		b = new Rectangle(border,appH-border,WIDTH,border);
		b.setFill(Color.WHITE);
		r = new Rectangle(WIDTH+border,0,border,appH);
		r.setFill(Color.WHITE);
		l = new Rectangle(0,0,border,appH);
		l.setFill(Color.WHITE);
		root.getChildren().addAll(t,b,r,l);
		
		food.setFill(Color.CORNFLOWERBLUE);
		
		
	}

	public static void mainMenuSetup() {
 		//main Menu
 				homePane = new GridPane();
 				homeScene = new Scene(homePane,appW,appH);
 				homePane.setStyle("-fx-background-color: #6B6B6B;");
 			
 				for(int i =0; i<menuColumns;i++) {
 					ColumnConstraints column = new ColumnConstraints(appW/2);
 		            homePane.getColumnConstraints().add(column);
 				}
 				for(int i =0;i<menuRows;i++) {
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
