package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import Util.Alertas;
import Util.Constraints;
import Util.Utils;
import db.DbException;
import gui.listener.DataChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Cliente;
import model.exceptions.ValidationException;
import model.service.ClienteService;

public class ClienteFormController implements Initializable {

	private Cliente entity;

	private ClienteService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtCPF;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtTelefone;

	@FXML
	private TextField txtScore;

	@FXML
	private Label error;

	@FXML
	private Label error1;

	@FXML
	private Button btSave;

	@FXML
	private Button BtCancel;

	public void setCliente(Cliente entity) {
		this.entity = entity;

	}

	public void setClienteService(ClienteService service) {
		this.service = service;
	}

	public void subscribeDataCHangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade está vazia");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço está vazio");
		}

		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alertas.showAlert("Erro ao salvar o objeto", "Erro ao salvar um objeto", e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	@FXML
	public void onBtCancelAtion(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	private Cliente getFormData() {
		Cliente obj = new Cliente();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("nome", "O campo não pode ser vazio");
		}
		
		obj.setNome(txtNome.getText());

		obj.setCpf(txtCPF.getText());

		obj.setEmail(txtEmail.getText());
		obj.setTelefone(txtTelefone.getText());
		obj.setScore(Utils.tryParseToInt(txtScore.getText()));
		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializaNodes();

	}

	private void initializaNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
		Constraints.setTextFieldMaxLength(txtCPF, 12);
		Constraints.setTextFieldMaxLength(txtEmail, 100);
		Constraints.setTextFieldMaxLength(txtTelefone, 25);
		Constraints.setTextFieldInteger(txtScore);
		;

	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Endidade está vazia");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtCPF.setText(entity.getCpf());
		txtEmail.setText(entity.getEmail());
		txtTelefone.setText(entity.getTelefone());
		txtScore.setText(String.valueOf(entity.getScore()));

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("nome")) {
			error.setText(errors.get("nome"));
		}
	

	}

}
