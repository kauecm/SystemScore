package gui;

import application.Login;
import application.TelaPrincipal;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TelaPrincipalController {

	@FXML
	private Button btEntrar;

	@FXML
	private Label lblMensagem;

	@FXML
	public void onBtTelaCadastro() {
		lblMensagem.setText("Em contrução....");
		System.out.println("Em construção...");
	}
	
	@FXML
	public void onBtSairAction() {
		fechar();
				
	}
	
	public void fechar() {
		TelaPrincipal.getStage().close();
	}

}
