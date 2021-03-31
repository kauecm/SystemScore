package gui;

import Util.Alertas;
import application.Login;
import application.TelaPrincipal;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private Button btTest;

	@FXML
	private TextField txtUser;

	@FXML
	private PasswordField txtSenha;

	@FXML
	private Button btSair;

	@FXML
	public void onBttestAction() {
		if (txtUser.getText().equals("admin") && txtSenha.getText().equals("admin")) {
			TelaPrincipal tp = new TelaPrincipal();
			tp.start(new Stage());
			fechar();
			System.out.println("Voce entrou");

		} else {
			Alertas.showAlert("System Erro", "Erro de Login", "Usuário ou senha inválidos tente novamente", AlertType.ERROR);
			System.out.println("Login ou Senha invalidos!!");
		}

	}
	
	

	@FXML
	public void onBtSairAction() {
		fechar();

	}
	
	public void fechar() {
		Login.getStage().close();
	}

}
