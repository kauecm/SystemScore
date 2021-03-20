package application;

import java.io.IOException;
import java.sql.Connection;

import db.DB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Login extends Application {
	private static Stage stage;
	
	
	@Override
	public void start(Stage stage) {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/gui/Login.fxml"));
			Scene scene = new Scene(parent);
			stage.setScene(scene);
			stage.show();
			setStage(stage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Stage getStage() {
		return stage;
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public static void main(String[] args) {
		launch(args);

		
	}
}
