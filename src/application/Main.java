package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.geometry.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
	// general variables
	public static int numRowCol = 25;
	public static final int CUBESIZE = 25, border = CUBESIZE;
	public static final int HEIGHT = numRowCol * CUBESIZE, WIDTH = numRowCol * CUBESIZE;
	public static final int appH = HEIGHT + (2 * border), appW = WIDTH + (2 * border) + 200;

	// game scene
	public static Scene mainScene;
	public static Pane root;
	public static Label lscore, ltime;
	public static int score, sec = 0, min = 0;
	public static long time;
	public static long start;
	public static Text Title, timedisp, scoreBox, hSDisp, controls;

	// pause
	public static Rectangle pause = new Rectangle(appW, appH);
	public static Label pausealrt, prompt;

	// main menu scene
	public static Scene homeScene;
	public static GridPane homePane;
	public static Button normal, speed;
	public static Label olabel, title, nExpl, sExpl;
	public static int menuRows = 4, menuColumns = 2;

	// game over scene
	public static Scene overScene;
	public static GridPane overPane;
	public static boolean gameOver = false;
	public static Label overt, scoredisp, alrt;
	public static TextField namein;
	public static Button submit = new Button("Submit"), quit = new Button("Quit");

	// Snake and movement
	enum Direction {
		UP, DOWN, RIGHT, LEFT
	}

	public static Direction direction, current;
	public static ArrayList<Rectangle> recList = new ArrayList<Rectangle>();
	public static ArrayList<Double> PrevX = new ArrayList<Double>();
	public static ArrayList<Double> PrevY = new ArrayList<Double>();
	public static Rectangle[][] background = new Rectangle[numRowCol][numRowCol];
	public static Rectangle rect, rect2, rect3, rect4, rect5;
	public static double xVelocity = 0, yVelocity = 0;
	public static double prevX = 0, prevY = 0;
	public static double velocity = CUBESIZE;
	public static boolean commence, needFood, validLocal;
	public static boolean h, v;
	public static Rectangle t, b, r, l;
	public static int count;

	// food
	public static Rectangle food;
	public static int randx, randy;
	public static Random rx = new Random(), ry = new Random();

	// highscores
	public static File highscores;
	public static int HS1, HS2, HS3;
	public static String namescore, name;
	public static ArrayList<String> scores = new ArrayList<>();
	public static String[] finalTable = new String[10];
	public static Text menuTable = new Text(), tableTitle = new Text(), subTitle = new Text();

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
					//sets up main scene
					primaryStage.setScene(mainScene);
				}
			});


			bindPlayerControls();


			primaryStage.show();

			new AnimationTimer() {
				@Override
				public void handle(long now) {

					// if the game isnt paused
					if (commence == true) {
						// transition to the gameover screen
						if (gameOver) {
							this.stop();
							rect.setFill(Color.RED);
							// sets body to red
							for (int i = 0; i < recList.size(); i++) {
								recList.get(i).setFill(Color.RED);
							}

							PauseTransition pause = new PauseTransition(Duration.millis(250));
							pause.setOnFinished(e -> {
								// removes all values from arraylists in order to reset game
								for (int i = recList.size() - 1; i >= 0; i--) {
									recList.remove(i);
								}
								for (int i = PrevX.size() - 1; i >= 0; i--) {
									PrevX.remove(i);
									PrevY.remove(i);
								}
								// resets values
								current = null;
								count = 0;
								min = 0;
								sec = 0;

								primaryStage.setScene(overScene);
								scoredisp.setText("Score: " + score);
							});
							pause.play();
						}

						else {
							gameLive();
							// initial move
							if (count == 0) {
								current = direction;
							}

							// movement code taking input from the enum to adjust
							switch (current) {
							case UP:
								yVelocity = -velocity;
								xVelocity = 0;
								break;
							case DOWN:
								yVelocity = +velocity;
								xVelocity = 0;
								break;

							case LEFT:
								xVelocity = -velocity;
								yVelocity = 0;
								break;

							case RIGHT:
								xVelocity = +velocity;
								yVelocity = 0;
								break;
							}
							// slows the movement down to every 10 cycles
							if (count % 10 == 0) {
								current = direction;
								move();
								xVelocity = 0;
								yVelocity = 0;

							}

						}
						count++;

					}
				}

			}.start();

			submit.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// checks to make sure input is valid of exactly 3 characters
					if (namein.getText().length() > 3) {
						alrt.setText("Only initials with 3 characters are valid. \n- too many");
					} else if (namein.getText().length() < 3) {
						alrt.setText("Only initials with 3 characters are valid. \n- too few");
					} else {
						PauseTransition pause = new PauseTransition(Duration.millis(100));
						pause.setOnFinished(e -> {
							namescore = namein.getText() + " : " + score;
							writeScore(namein.getText(), score);
							namein.clear();
							alrt.setText("");
							start(primaryStage);
						});
						pause.play();
					}
				}
			});

			quit.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

					primaryStage.close();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void bindPlayerControls() {
		mainScene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {

			// binds keys to movement patterns
			switch (event.getCode()) {
			case W:
			case UP:
				if (current != Direction.DOWN) {
					direction = Direction.UP;
					commence = true;
					houdini();
				}
				break;

			case S:
			case DOWN:
				if (current != Direction.UP) {
					direction = Direction.DOWN;
					commence = true;
					houdini();
				}
				break;

			case A:
			case LEFT:
				if (current != Direction.RIGHT) {
					direction = Direction.LEFT;
					commence = true;
					houdini();
				}
				break;

			case D:
			case RIGHT:
				if (current != Direction.LEFT) {
					direction = Direction.RIGHT;
					commence = true;
					houdini();
				}
				break;

			case P:
				commence = false;
				pause.setOpacity(.75);
				prompt.setVisible(true);
				pausealrt.setVisible(true);
			}

		});

	}

	// makes the pause menu disappear
	public static void houdini() {
		pause.setOpacity(0);
		prompt.setVisible(false);
		pausealrt.setVisible(false);
	}

	// method which houses all methods that run during the game
	public static void gameLive() {
		scoreBox.setText("Score: " + score + "");
		timedisp.setText("Time: " + min + ":" + String.format("%02d", sec));

		// checks if you ran into yourself
		if (died()) {

			gameOver = true;

		}
		// checks if you ate the food and adds length

		if (isEaten()) {
			addLength();
			needFood = true;
			score++;
		}

		// spawns the food to a new location
		if (needFood == true) {
			addFood();
		}

		// timer
		if (count % 60 == 0) {
			sec++;
		}
		if (sec == 60) {
			min++;
			sec = 0;
		}

	}

	// method that randomly sets the coords of the food
	public static void addFood() {
		do {
			randx = rx.nextInt(((WIDTH + border) - 2 * CUBESIZE)) + CUBESIZE;
			randy = ry.nextInt(((WIDTH + border) - 2 * CUBESIZE)) + CUBESIZE;

			randx = (int) (Math.round(randx / CUBESIZE) * CUBESIZE);
			randy = (int) (Math.round(randy / CUBESIZE) * CUBESIZE);
			for (int i = 0; i < recList.size(); i++) {
				if (randx != recList.get(i).getX() && randy != recList.get(i).getY()) {
					validLocal = false;
				} else {
					validLocal = true;
				}
			}
		} while (validLocal);

		validLocal = false;

		food.setX(randx);
		food.setY(randy);
		randx = 0;
		randy = 0;

		needFood = false;

	}

	// method that moves all the body parts to the previous position of the one
	// infront of it
	public void move() {
		// adds the x and y coordinates to two arraylists
		for (int i = 0; i < recList.size() - 1; i++) {
			PrevX.add(i, recList.get(i).getX());
			PrevY.add(i, recList.get(i).getY());
		}
		// sets the x and y to the previous coordinates of the rectangle infront
		for (int i = 0; i < recList.size() - 1; i++) {
			recList.get(i + 1).setX(PrevX.get(i));
			recList.get(i + 1).setY(PrevY.get(i));
		}

		// checks if you exit the boundaries
		if (rect.getX() + xVelocity < border || rect.getX() + xVelocity >= WIDTH + border) {
			gameOver = true;
		} else if (rect.getY() + yVelocity < border || rect.getY() + yVelocity >= HEIGHT + border) {
			gameOver = true;

		} else {
			// sets the x and y to their current position plus the velocity set by platyer
			// controls
			rect.setX(rect.getX() + xVelocity);
			rect.setY(rect.getY() + yVelocity);

			// checks if you died
			if (died()) {

				gameOver = true;

			}
		}

	}

	// adds a rectangle to the body
	public static void addLength() {
		Rectangle body = new Rectangle(CUBESIZE, CUBESIZE);

		body.setFill(Color.OLIVEDRAB);
		recList.add(recList.size(), body);
		root.getChildren().add(body);
		body.toBack();
		body.setFill(Color.OLIVEDRAB);
		// score++;
	}

	// boolean method which checks if you have successfully eaten one of the food
	// rectangles

	public static boolean isEaten() {
		// checks if the coords of the head are equal to the coords of the food
		if (rect.getX() == food.getX() && rect.getY() == food.getY()) {
			return true;

		} else
			return false;

	}

	// boolean method that checks if you have run into yourself
	public static boolean died() {
		boolean yes = false;

		// loops through all body rectangles and checks if head has same coordinates
		for (int i = 0; i < recList.size(); i++) {
			if (rect.getX() == recList.get(i).getX() && rect.getY() == recList.get(i).getY() && i > 1) {
				yes = true;
				break;
				//if snake exceeds boundaries of the board//adds columns and rows to the gridpane

			} else if (rect.getX() < border || rect.getX() > (WIDTH + border) || +rect.getY() < border
					|| rect.getY() > (HEIGHT + border)) {
				yes = true;
				break;
			} else {
				yes = false;
			}
		}
		return yes;
	}

	public static void gameOverSetup() {
		// Game Over
		overPane = new GridPane();
		overScene = new Scene(overPane, appW, appH);
		overPane.setStyle("-fx-background-color: #6B6B6B;");
		// adds columns and rows to the gridpane
		for (int i = 0; i < menuColumns; i++) {
			ColumnConstraints column = new ColumnConstraints(appW / 2);
			overPane.getColumnConstraints().add(column);
		}
		for (int i = 0; i < menuRows; i++) {
			RowConstraints row = new RowConstraints(appH / 3);
			overPane.getRowConstraints().add(row);
		}
		// Over Title
		overt = new Label();
		overt.setText("Game Over");
		overt.setFont(Font.font("Bangla MN", 50));
		overt.setTextFill(Color.WHITE);
		overPane.add(overt, 0, 0);
		GridPane.setColumnSpan(overt, 2);
		GridPane.setHalignment(overt, HPos.CENTER);

		// score Title
		scoredisp = new Label();
		scoredisp.setText("Score: " + score);
		scoredisp.setFont(Font.font("Bangla MN", 30));
		scoredisp.setTextFill(Color.WHITE);
		overPane.add(scoredisp, 0, 1);
		GridPane.setColumnSpan(scoredisp, 2);
		GridPane.setHalignment(scoredisp, HPos.CENTER);
		GridPane.setValignment(scoredisp, VPos.TOP);

		overPane.add(quit, 0, 1);
		GridPane.setColumnSpan(quit, 2);
		GridPane.setHalignment(quit, HPos.CENTER);
		quit.setTranslateY(25);
		GridPane.setValignment(quit, VPos.BOTTOM);

		// TextField
		namein = new TextField();
		overPane.add(namein, 0, 2);
		GridPane.setHalignment(namein, HPos.RIGHT);
		namein.setTranslateX(-25);
		namein.setPromptText("Enter your initials.");
		namein.setMaxWidth(200);

		alrt = new Label();
		overPane.add(alrt, 0, 2);
		GridPane.setHalignment(alrt, HPos.RIGHT);
		alrt.setTranslateX(-25);
		alrt.setTranslateY(40);
		alrt.setText("on the input of a valid input value a \nrederect to the main menu will occur");

		submit = new Button();
		overPane.add(submit, 1, 2);
		submit.setText("Submit");
		GridPane.setHalignment(submit, HPos.LEFT);
		submit.setTranslateX(25);

	}

	public static void normalGameModeSetup() {

		root = new Pane();

		mainScene = new Scene(root, appW, appH);

		rect = new Rectangle(4 * CUBESIZE, border, CUBESIZE, CUBESIZE);
		rect.setFill(Color.DARKOLIVEGREEN);
		recList.add(0, rect);
		root.getChildren().addAll(rect);

		// makes initial snake of length 3
		for (int i = 0; i < 2; i++) {
			Rectangle body = new Rectangle(5 * CUBESIZE + (CUBESIZE * i), border, CUBESIZE, CUBESIZE);
			root.getChildren().add(body);
			body.setFill(Color.OLIVEDRAB);
			recList.add(body);
		}

		food = new Rectangle(CUBESIZE, CUBESIZE);
		root.getChildren().add(food);
		food.setFill(Color.WHITE);
		food.setFill(Color.CORNFLOWERBLUE);

		// right display
		Title = new Text();
		root.getChildren().add(Title);
		Title.setX(appW - 180);
		Title.setY(50);
		Title.setText("Hungry\nSlug");
		Title.setFont(Font.font("Bangla MN", 32));

		timedisp = new Text();
		root.getChildren().add(timedisp);
		timedisp.setX(appW - 175);
		timedisp.setY(appH / 4 - 50);
		timedisp.setText("Time: " + min + ":" + String.format("%02d", sec));

		scoreBox = new Text();
		root.getChildren().add(scoreBox);
		scoreBox.setX(appW - 175);
		scoreBox.setY(appH / 4);
		scoreBox.setText("Score: " + score + "");

		// highscore display
		hSDisp = new Text();
		root.getChildren().add(hSDisp);
		hSDisp.setX(appW - 175);
		hSDisp.setY(appH / 4 + 25);
		hSDisp.setText("Highscores: \n1. " + finalTable[0] + "\n2. " + finalTable[1] + "\n3. " + finalTable[2]);

		controls = new Text();
		root.getChildren().add(controls);
		controls.setX(appW - 175);
		controls.setY(appH / 2);
		controls.setText("Controls: \n\nUp: up arrow or w \n\nDown: down arrow or s "
				+ "\n\nLeft: left arrow or a \n\nRight: right arrow or d \n\nPause: P ");

		commence = false;
		needFood = true;
		h = false;
		v = false;
		score = 0;
		// makes grid on screen
		for (int i = 0; i < numRowCol; i++) {
			for (int j = 0; j < numRowCol; j++) {
				Rectangle back = new Rectangle(CUBESIZE, CUBESIZE);
				back.setX((j * CUBESIZE) + border);
				back.setY((i * CUBESIZE) + border);
				back.setFill(Color.TRANSPARENT);
				back.setStroke(Color.GREY);
				background[i][j] = back;
				root.getChildren().add(background[i][j]);
			}
		}

		// borders
		t = new Rectangle(border, 0, WIDTH, border);
		t.setFill(Color.WHITE);
		b = new Rectangle(border, appH - border, WIDTH, border);
		b.setFill(Color.WHITE);
		r = new Rectangle(WIDTH + border, 0, border, appH);
		r.setFill(Color.WHITE);
		l = new Rectangle(0, 0, border, appH);
		l.setFill(Color.WHITE);
		root.getChildren().addAll(t, b, r, l);

		// pause
		root.getChildren().add(pause);
		pause.setFill(Color.GREY);
		pause.setOpacity(0);

		pausealrt = new Label();
		pausealrt.setTextAlignment(TextAlignment.CENTER);
		root.getChildren().add(pausealrt);
		pausealrt.setText("Game Paused");
		pausealrt.setTextFill(Color.BLACK);
		pausealrt.setFont(Font.font("Bangla MN", 50));
		pausealrt.setVisible(false);
		pausealrt.layoutXProperty().bind(root.widthProperty().subtract(pausealrt.widthProperty()).divide(2));
		pausealrt.layoutYProperty()
				.bind(root.heightProperty().subtract(pausealrt.heightProperty()).divide(2).subtract(35));

		prompt = new Label();
		prompt.setTextAlignment(TextAlignment.CENTER);
		root.getChildren().add(prompt);
		prompt.setText("Press any movement key to resume");
		prompt.setTextFill(Color.BLACK);
		prompt.setFont(Font.font("Bangla MN", 20));
		prompt.setVisible(false);
		prompt.layoutXProperty().bind(root.widthProperty().subtract(pausealrt.widthProperty()).divide(2));
		prompt.layoutYProperty().bind(root.heightProperty().subtract(pausealrt.heightProperty()).divide(2).add(35));

	}

	public static void mainMenuSetup() {
		// main Menu
		convertToFinal();
		homePane = new GridPane();
		homeScene = new Scene(homePane, appW, appH);
		homePane.setStyle("-fx-background-color: #6B6B6B;");
		// adds columns and rows to the gridpane
		for (int i = 0; i < menuColumns; i++) {
			ColumnConstraints column = new ColumnConstraints(appW / 2);
			homePane.getColumnConstraints().add(column);
		}
		for (int i = 0; i < menuRows; i++) {
			RowConstraints row = new RowConstraints(appH / 4);
			homePane.getRowConstraints().add(row);
		}

		// Normal mode
		normal = new Button();
		normal.setText("Normal");
		homePane.add(normal, 0, 1);
		GridPane.setHalignment(normal, HPos.CENTER);
		nExpl = new Label();
		nExpl.setTextFill(Color.WHITE);
		homePane.add(nExpl, 0, 2);
		nExpl.setText(
				"The normal game mode has no time limit. \n\n" + "Try to get as many points as possible without dying");
		nExpl.setTextAlignment(TextAlignment.CENTER);
		GridPane.setHalignment(nExpl, HPos.CENTER);
		GridPane.setValignment(nExpl, VPos.TOP);

		// shows highscores table
		displayHighScores();

		// Label
		olabel = new Label();
		olabel.setText("Choose a Game Mode");
		olabel.setFont(Font.font("Bangla MN", 20));
		olabel.setTextFill(Color.WHITE);

		title = new Label();
		title.setText("Hungry Slug");
		title.setFont(Font.font("Bangla MN", 50));
		title.setTextFill(Color.WHITE);

		homePane.getChildren().addAll(olabel, title);
		GridPane.setColumnSpan(olabel, 2);
		GridPane.setHalignment(olabel, HPos.CENTER);
		GridPane.setValignment(olabel, VPos.BOTTOM);

		GridPane.setColumnSpan(title, 2);
		GridPane.setHalignment(title, HPos.CENTER);
		GridPane.setValignment(title, VPos.TOP);

	}

	// method that writes the score of the game to the text file
	// sourced from https://www.youtube.com/watch?v=WEZRc0GoP3E
	public static void writeScore(String a, int b) {
		try {
			FileWriter fw = new FileWriter("Highscores.txt", true);
			fw.write(a + "" + b + "\n");
			fw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// fills the arraylist with a blank value (TST0)
	public static void fillScores() {
		for (int i = 0; i < 20; i++) {
			scores.add("TST0");
		}
	}

	// removes elements all from arraylist
	public static void removeAll() {
		for (int i = 0; i < scores.size(); i++) {
			scores.remove(0);
		}
	}

	// adds all the scores to the arraylist
	// sourced from
	// https://www.caveofprogramming.com/java/java-file-reading-and-writing-files-in-java.html
	public static void addScores() {
		removeAll();
		fillScores();
		int HighScore = 0;
		String fileName = "Highscores.txt";

		String line = null;

		try {
			FileReader fileReader = new FileReader(fileName);

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {

				scores.add(line);

			}

			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
	}

	// sorts the scores into the correct order of greatest to least score
	public static void orderScores() {
		addScores();
		// position of static score
		for (int i = 0; i < scores.size(); i++) {

			inner:
			// position of all scores that are before the static one
			for (int j = 0; j < i; j++) {
				int current = 0;
				int scrolling = 0;
				// determines the value of the static value

				for (int a = 0; a < scores.get(i).length() - 3; a++) {
					current += (scores.get(i).charAt(a + 3) - 48) * Math.pow(10, scores.get(i).length() - 3 - (a + 1));
				}
				// determines the value of the score which keeps moving through
				for (int a = 0; a < scores.get(j).length() - 3; a++) {
					scrolling += (scores.get(j).charAt(a + 3) - 48)
							* Math.pow(10, scores.get(j).length() - 3 - (a + 1));
				}
				// if a score before is less than the current, the current is inserted into this
				// index
				if (scrolling < current) {
					scores.add(j, scores.get(i));
					scores.remove(i + 1);
					break inner;
				}

			}
		}

	}

	// adds top 10 scores to the final score array
	public static void addToTable() {
		orderScores();
		for (int i = 0; i < 10; i++) {
			finalTable[i] = scores.get(i);
		}

	}

	// prevents null pointer exception
	public static void fillTable() {
		// fills finalTable so is not null
		for (int i = 0; i < 10; i++) {
			finalTable[i] = "No Score Yet";
		}
	}

	// converts the score from data form into displayable form
	public static void convertToFinal() {
		fillTable();
		addToTable();

		for (int a = 0; a < 10; a++) {
			// if score is not a placeholder
			if (finalTable[a].equals("TST0") == false) {
				int finalScore = 0;
				// calculates value of the score part of the string
				for (int i = 0; i < finalTable[a].length() - 3; i++) {
					finalScore += (finalTable[a].charAt(i + 3) - 48)
							* Math.pow(10, finalTable[a].length() - 3 - (i + 1));
				}

				finalTable[a] = ("" + finalTable[a].charAt(0) + finalTable[a].charAt(1) + finalTable[a].charAt(2)
						+ " - " + finalScore);
			} else {
				finalTable[a] = "No Score Yet";
			}
		}

	}

	// displays the highscores
	public static void displayHighScores() {
		convertToFinal();
		tableTitle.setText("HIGH SCORES");
		tableTitle.setFont(Font.font("Bangla MN", 20));
		tableTitle.setFill(Color.WHITE);
		homePane.add(tableTitle, 1, 1);
		GridPane.setRowSpan(tableTitle, 2);
		GridPane.setHalignment(tableTitle, HPos.CENTER);
		GridPane.setValignment(tableTitle, VPos.CENTER);
		tableTitle.setTranslateY(-60);
		tableTitle.setTextAlignment(TextAlignment.CENTER);

		subTitle.setText("Initials - Score");
		subTitle.setFont(Font.font("Bangla MN", 15));
		subTitle.setFill(Color.WHITE);
		homePane.add(subTitle, 1, 1);
		GridPane.setRowSpan(subTitle, 2);
		GridPane.setHalignment(subTitle, HPos.CENTER);
		GridPane.setValignment(subTitle, VPos.CENTER);
		subTitle.setTranslateY(-35);
		subTitle.setTextAlignment(TextAlignment.CENTER);

		menuTable.setText(finalTable[0] + "\n" + finalTable[1] + "\n" + finalTable[2] + "\n" + finalTable[3] + "\n"
				+ finalTable[4] + "\n" + finalTable[5] + "\n" + finalTable[6] + "\n" + finalTable[7] + "\n"
				+ finalTable[8] + "\n" + finalTable[9] + "\n");

		menuTable.setFill(Color.WHITE);
		homePane.add(menuTable, 1, 1);
		GridPane.setRowSpan(menuTable, 2);
		GridPane.setHalignment(menuTable, HPos.CENTER);
		GridPane.setValignment(menuTable, VPos.CENTER);
		menuTable.setTranslateY(+60);
		menuTable.setTextAlignment(TextAlignment.CENTER);
	}

	public static void main(String[] args) {
		launch(args);
	}
}