package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;


public class Main extends Application {
	
	public static Scene mainScene;
	public static Pane root;
	public static Line l;
	public static Rectangle rect;
	public static int xVelocity = 0;
	public static int yVelocity = 0;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			root = new BorderPane();
			mainScene = new Scene(root,400,400);
			rect = new Rectangle(10,10,10,10);
			root.getChildren().add(rect);

			
			bindPlayerControls();
			
			if(xVelocity>0) {
				System.out.println("Right");
			}
			
			primaryStage.setScene(mainScene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void bindPlayerControls() {
		mainScene.addEventHandler(KeyEvent.KEY_PRESSED, event ->{
			rect.setX(rect.getX()+xVelocity);
			rect.setY(rect.getY()+yVelocity);
			switch(event.getCode()){
				case UP:
					yVelocity = -3;
					xVelocity = 0;
				break;
				
				case DOWN:
					yVelocity = 3;
					xVelocity = 0;				
				break;
				
				case LEFT:
					yVelocity = 0;
					xVelocity = -3;				
					break;
				
				case RIGHT:
					yVelocity = 0;
					xVelocity = 3;	
					System.out.println("RIGHT");
				break;
			}
			
			
		
		});
	
		bindPlayerControls();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
