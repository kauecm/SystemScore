package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import Util.Alertas;
import application.TelaPrincipal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.service.ClienteService;

public class TelaPrincipalController implements Initializable {

	

	@FXML
	private MenuItem cliente;
	
	@FXML
	private MenuItem editar;
	
	@FXML
	private MenuItem about;
 
	@FXML
	public void onMenuItemClienteAction() {
		loadView("/gui/Cliente.fxml", (ClienteController controller) -> {
			controller.setService(new ClienteService());
			controller.updateTableView();
			
		});
	}
	
	
	private <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox =  loader.load();
			
			Scene mainScene = TelaPrincipal.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();
			initializingAction.accept(controller);
			
		}
		catch(IOException e) {
			Alertas.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
			
		}
	

}


	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	} }
