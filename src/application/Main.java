package application;
	
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.util.Duration;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.TextField;


public class Main extends Application {
	//General Variables
		public static final int blockSize = 30;
		public static int numRows = 20, numColumns = numRows;
		public static final int appH = numRows*blockSize;
		public static final int appW = numRows*blockSize;
		public static long start;


		//normal game scene
		public static Scene nGameScene;
		public static GridPane root;
		public static Label lscore, ltime;
		public static int score;
		public static long time;
		


		//game over scene
		public static Scene overScene;
		public static GridPane overPane;
		public static boolean gameOver = false;
		public static Label overt;
		public static TextField namein;
		public static String name;
		public static Button submit, tempBtn;
		public static boolean annoying = false, temp;
		


		//main menu scene
		public static Scene homeScene;
		public static GridPane homePane;
		public static Button normal,speed;
		public static Label olabel,title,nExpl,sExpl; 
		public static int menuRows = 4, menuColumns =2;
		

		//high scores
		public static int nHighScore =0,sHighScore = 0;
		public static File highscores;


	//Snake and movement
		enum Direction{
			UP, DOWN, RIGHT, LEFT
		}
		public static Direction direction;
		public static ArrayList<Rectangle> recList = new ArrayList<Rectangle>();
		public static ArrayList<Integer> PrevX = new ArrayList<Integer>();
		public static ArrayList<Integer> PrevY = new ArrayList<Integer>();
		public static Rectangle rect, rect2, rect3, rect4, rect5;
		public static int xVelocity = 0, yVelocity = 0;
		public static double prevX = 0, prevY = 0;
		public static int xTotal = 0, yTotal = 0;
		public static int leftOrRight = 0, upOrDown = 0;
		public static int velocity = blockSize;
		public static int step;
		public static boolean commence, needFood;
		public static Circle food; 
	
	
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setResizable(false);
			
			MMsetup();
			
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
			
			
			
			NGMsetup();

			
			GOsetup();
			

			bindPlayerControls();
			// addLength();

			start = System.nanoTime();
			if(gameOver) {
				primaryStage.setScene(overScene);
			}
			else {
				primaryStage.setScene(homeScene);
			}
			primaryStage.show();

			new AnimationTimer() {
				@Override
				public void handle(long now) {

					if (commence == true) {
						
						/*tempBtn.setOnAction(new EventHandler<ActionEvent>() {
				            @Override
				            public void handle(ActionEvent event) {
				            	temp=true;
				            }
				        });*/
						
						if(dead()) {
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
						
						
						if(isEaten()) {
							addLength();
							needFood=true;
						}

						if(needFood == true) {
							addFood();
						}

						double t = (double) (now - start / 1000000000);
						if (t % 0.5 == 0) {
							for (int i = 0; i < recList.size() - 1; i++) {
								PrevX.add(GridPane.getColumnIndex(recList.get(i)));
								PrevY.add(GridPane.getRowIndex(recList.get(i)));
							}
							
							
							
						}
							for (int i = 0; i < recList.size() - 1; i++) {
								GridPane.setColumnIndex(recList.get(i+1), PrevX.get(i));
								GridPane.setRowIndex(recList.get(i+1), PrevY.get(i));
							}
							for (int i = 0; i < PrevX.size() - 1; i++) {
								PrevX.remove(i);
								PrevY.remove(i);
							}
							switch (direction){
							case UP:
								GridPane.setRowIndex(rect, GridPane.getRowIndex(rect)-1);
								pause();
								break;
							case DOWN:
								GridPane.setRowIndex(rect, GridPane.getRowIndex(rect)+1);
								pause();
								break;
							case RIGHT:
								GridPane.setColumnIndex(rect, GridPane.getColumnIndex(rect)+1);
								pause();
								break;
							case LEFT:
								GridPane.setColumnIndex(rect, GridPane.getColumnIndex(rect)-1);
								pause();
								break;
								/*
								if (rect.getX() > appW) {
									rect.setX(0);
								}
								if (rect.getX() < 0) {
									rect.setX(appW);
								}
								if (rect.getY() > appH) {
									rect.setY(0);
								}
								if (rect.getY() < 0) {
									rect.setY(appH);
								}

								xTotal += xVelocity;
								yTotal += yVelocity;*/
						}
						  

					}
				}

			}.start();
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void bindPlayerControls() {
		nGameScene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			// setTrailer();

			switch (event.getCode()) {
			case UP:
				if(direction != Direction.DOWN) {
				direction = Direction.UP;
				commence = true;
				}
				break;

			case DOWN:
				if(direction != Direction.UP) {
					direction = Direction.DOWN;
					commence = true;
					}
				break;

			case LEFT:
				if(direction != Direction.RIGHT) {
					direction = Direction.LEFT;
					commence = true;
					}
				break;

			case RIGHT:
				if(direction != Direction.LEFT) {
					direction = Direction.RIGHT;
					commence = true;
					}
				break;
			case P:
				commence = false;
				//addLength();
				break;
			}

		});

	}
	public static void setTrailer() {
		
	}
	public static void addFood() {
		GridPane.setRowIndex(food, (int)(Math.random()*numRows));
		GridPane.setColumnIndex(food, (int)(Math.random()*numRows));
		
		needFood = false;

	}
	public static void addLength() {

		Rectangle body = new Rectangle(blockSize, blockSize);
		body.setFill(Color.WHITE);
		recList.add(recList.size(), body);
		root.getChildren().add(body);
	}
	public boolean isEaten() {
		if(GridPane.getColumnIndex(rect)==GridPane.getColumnIndex(food)&&+
				GridPane.getRowIndex(rect)==GridPane.getRowIndex(food)) {
			
			return true;
		} else
			return false;
		
	}
	public boolean dead() {
		
		for (int i = 1; i < PrevX.size(); i++) {
			if(PrevX.get(i) == GridPane.getColumnIndex(rect) && PrevY.get(i) == GridPane.getRowIndex(rect)) {
				annoying = true;
			}
			else
				annoying = false;
		}
		if(annoying) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public static void GOsetup() {
		//Game Over
		overPane = new GridPane();
		overScene = new Scene(overPane,appH,appW);
		overPane.setStyle("-fx-background-color: #6B6B6B;");
		for(int i =0; i<menuColumns;i++) {
			ColumnConstraints column = new ColumnConstraints(appW/2);
            overPane.getColumnConstraints().add(column);
		}
		for(int i =0;i<menuRows;i++) {
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
	}
	public static void NGMsetup() {
		//normal Game Mode
		root = new GridPane();
		
		nGameScene = new Scene(root, appH, appW);
		for(int i =0; i<numColumns;i++) {
			ColumnConstraints column = new ColumnConstraints(blockSize);
            root.getColumnConstraints().add(column);
		}
		
		for(int i =0;i<numRows;i++) {
			RowConstraints row = new RowConstraints(blockSize);
			root.getRowConstraints().add(row);
		}
		root.setStyle("-fx-background-color: #6B6B6B");
		root.setGridLinesVisible(true);
		
		
		highscores = new File("C:\\\\Users\\\\justi\\\\eclipse-workspace\\\\ICS3U-Final-Project");

		
		rect = new Rectangle(blockSize, blockSize);
		rect.setFill(Color.WHITE);
		GridPane.setColumnIndex(rect, 1);
		GridPane.setRowIndex(rect, 1);
		recList.add(rect);
		
		//tempBtn = new Button();
		//root.getChildren().add(tempBtn);

		rect2 = new Rectangle(blockSize, blockSize);
		rect2.setFill(Color.WHITE);
		GridPane.setColumnIndex(rect2, 2);
		GridPane.setRowIndex(rect2, 1);
		
		recList.add(1, rect2);

		root.getChildren().addAll(recList);
/*	rect3 = new Rectangle(2*blockSize, 0, blockSize, blockSize);
		recList.add(2, rect3);

		rect4 = new Rectangle(blockSize, 0, blockSize, blockSize);
		recList.add(3, rect4);

		rect5 = new Rectangle(0, 0, blockSize, blockSize);
		recList.add(4, rect5);*/
		
		food = new Circle(blockSize/2);
		
		food.setFill(Color.BLUE);
		GridPane.setRowIndex(food, (numRows)/2);
		GridPane.setColumnIndex(food, (numRows)/2);
		root.getChildren().add(food);
		
		
		commence = false;
		
		
		needFood = true; 
	}
	public static void MMsetup() {
		//main Menu
				homePane = new GridPane();
				homeScene = new Scene(homePane,appH,appW);
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

	public static void pause() {
		try {
			TimeUnit.MILLISECONDS.sleep(250);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
