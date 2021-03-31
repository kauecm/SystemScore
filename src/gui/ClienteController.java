package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Util.Alertas;
import Util.Utils;
import application.TelaPrincipal;
import db.DbIntegrityException;
import gui.listener.DataChangeListener;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Cliente;
import model.service.ClienteService;

public class ClienteController implements Initializable, DataChangeListener {

	private ClienteService service;

	@FXML
	private TableView<Cliente> tableViewCliente;

	@FXML
	private TableColumn<Cliente, Integer> id;
	@FXML
	private TableColumn<Cliente, String> nome;

	@FXML
	private TableColumn<Cliente, Cliente> tableColumnEDIT;
	
	@FXML
	private  TableColumn<Cliente, Cliente> tableColumnREMOVE;

	@FXML
	private TableColumn<Cliente, String> email;
	@FXML
	private TableColumn<Cliente, String> cpf;
	@FXML
	private TableColumn<Cliente, Integer> score;
	@FXML
	private TableColumn<Cliente, String> teleone;
	@FXML
	private Button btNew;
	@FXML
	private Button btEdit;
	@FXML
	private Button delet;

	private ObservableList<Cliente> obsList;

	public ClienteService getService() {
		return service;
	}

	public void setService(ClienteService service) {
		this.service = service;
	}

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Cliente obj = new Cliente();
		createDialogForm(obj, "/gui/ClienteForm.fxml", parentStage);

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		email.setCellValueFactory(new PropertyValueFactory<>("email"));
		cpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		score.setCellValueFactory(new PropertyValueFactory<>("score"));
		teleone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

		Stage stage = (Stage) TelaPrincipal.getMainScene().getWindow();
		tableViewCliente.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("O service estava nulo");
		}
		List<Cliente> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewCliente.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Cliente obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ClienteFormController controller = loader.getController();
			controller.setCliente(obj);
			controller.setClienteService(new ClienteService());
			controller.subscribeDataCHangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();

			dialogStage.setTitle("System Score");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alertas.showAlert("IO Exception", "Erro ao carregar a pagina", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();

	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Cliente, Cliente>() {
			private final Button button = new Button("EDITAR");

			@Override
			protected void updateItem(Cliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/ClienteForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() { 
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Cliente, Cliente>() { 
		 private final Button button = new Button("DELETAR"); 
		 @Override
		 protected void updateItem(Cliente obj, boolean empty) { 
		 super.updateItem(obj, empty); 
		 if (obj == null) { 
		 setGraphic(null); 
		 return; 
		 } 
		 setGraphic(button); 
		 button.setOnAction(event -> removeEntity(obj)); 
		 } 
		 }); 
		}

	private void removeEntity(Cliente obj) {
		Optional<ButtonType> result = Alertas.showConfirmation("Confirmação", "TEM CERTEZA QUE DESEJA APAGAR?");
		
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Serviço nulo");
			}
			try {
				service.remove(obj);
				updateTableView();
			}catch(DbIntegrityException e) {
				Alertas.showAlert("Erro ao remover", "Erro ao tentar remover", e.getMessage(), AlertType.ERROR);
			}
	
				
			}
		}
	} 


