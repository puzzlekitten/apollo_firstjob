
package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("NoteBook.fxml")); // 与Fxml连接的方式：注：此时是在同一个包里的

		Scene scene = new Scene(root, 785, 500);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setTitle("轮胎接地压力分布计算软件v2.0");
		stage.getIcons().add(new Image("icon.png"));
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}
}