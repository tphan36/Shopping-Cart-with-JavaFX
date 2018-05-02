package ViewLogin;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class BookstoreController implements Initializable {

	ObservableList<Book> data = FXCollections.observableArrayList();

	@FXML
	TableView<Book> table;

	@FXML
	private TableColumn<?, ?> authorCol;

	@FXML
	private TableColumn<?, ?> bookCol;

	@FXML
	private Label isbnLabel;

	@FXML
	private Label bookNameLabel;

	@FXML
	private Label authorLabel;

	@FXML
	private Label editionLabel;

	@FXML
	private Label yearLabel;

	@FXML
	private Label priceLabel;
	
	@FXML
	private Button newButton;
	
	@FXML
	private Button deleteButton;
	
	@FXML
	private Button addButton;
	
	@FXML
	private Button viewButton;
	@FXML
	private Label soldByLabel;
	
	@FXML
	private Button mapButton;
	
	@FXML
	private Label sellByLabel;

	private String username;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
		bookCol.setCellValueFactory(new PropertyValueFactory<>("book"));
		table.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				Book selectedBook = table.getSelectionModel().getSelectedItem();
				showBookDetails(selectedBook);
			}
		});
	}

	public void showBookDetails(Book book) {
		isbnLabel.setText("" + book.getIsbn());
		bookNameLabel.setText(book.getBook());
		authorLabel.setText(book.getAuthor());
		editionLabel.setText("" + book.getEdition());
		yearLabel.setText("" + book.getYear());
		priceLabel.setText("$" + book.getPrice());
		soldByLabel.setText(book.getSellBy());;
	}

	public void addToCartButton() {
		// table.getItems().removeAll(table.getSelectionModel().getSelectedItem());
		if (table.getSelectionModel().getSelectedItem() != null) {
			Book selectedBook = table.getSelectionModel().getSelectedItem();
			if (saveToCart(selectedBook)) {
				Alert alert = new Alert(AlertType.NONE, selectedBook.getBook() +" Added Successfully!", ButtonType.OK);
				alert.showAndWait();
				return;
			} else {
				Alert alert = new Alert(AlertType.NONE, "Book Adding Failed!", ButtonType.OK);
				alert.showAndWait();
				return;
			}
		}
		else {
			Alert alert = new Alert(AlertType.NONE, "Please Select Book To Add!", ButtonType.OK);
			alert.showAndWait();
			return;
		}
		
	}

	private boolean saveToCart(Book cart) {
		Connection connection = null;
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:shoppingCartDb.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 second
			// check on commit

			String addToCart = "insert into cartTable values('" + username + "', '" + cart.getIsbn() + "', '"
					+ cart.getBook() + "', '" + cart.getAuthor() + "', '" + cart.getEdition() + "', '" + cart.getYear()
					+ "', '" + cart.getPrice() + "', '" + cart.getSellBy() + "')";
			statement.executeUpdate(addToCart);
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// connection close failed.
				System.err.println(e);
			}
		}
		return true;
	}

	public void loadDatabase() {

		Connection connection = null;
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:shoppingCartDb.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			String query = "select * from bookTable";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				Book existedBook = new Book(rs.getInt("isbn"), rs.getString("book"), rs.getString("author"),
						rs.getInt("edition"), rs.getInt("year"), rs.getFloat("price"), rs.getString("soldBy"));

				data.add(existedBook);
				table.setItems(data);
			}
			statement.close();
			rs.close();
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// connection close failed.
				System.err.println(e);
			}
		}
	}

	public BookstoreController() {

	}

	public void mapButtonAction(ActionEvent event) throws IOException {
		FXMLLoader mapPage = new FXMLLoader(getClass().getResource("BookstoreMap.fxml"));
		Parent mapParent = (Parent) mapPage.load();
		Scene mapScene = new Scene(mapParent);
		Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();

		BookstoreMapController newUserName = mapPage.getController();
		newUserName.sendData(username);
		window.setScene(mapScene);
		window.show();

	}
	
	public void logoutButtonAction(ActionEvent event) throws IOException {
		FXMLLoader logoutPage = new FXMLLoader(getClass().getResource("Login.fxml"));
		Parent logoutParent = (Parent) logoutPage.load();
		Scene logoutScene = new Scene(logoutParent);
		Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		window.setScene(logoutScene);
		window.show();

	}

	public void ShoppingCartButton(ActionEvent event) throws IOException {
		FXMLLoader shoppingCartPage = new FXMLLoader(getClass().getResource("ShoppingCart.fxml"));
		Parent shoppingCartParent = (Parent) shoppingCartPage.load();
		Scene shoppingCartScene = new Scene(shoppingCartParent);
		Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();

		ShoppingCartController newUserName = shoppingCartPage.getController();
		newUserName.sendData(username);
		window.setScene(shoppingCartScene);
		window.show();
	}

	// sell button
	public void sellButton(ActionEvent event) throws IOException {
		FXMLLoader sellingPage = new FXMLLoader(getClass().getResource("SellingBooks.fxml"));
		Parent sellingParent = (Parent) sellingPage.load();
		Scene sellingScene = new Scene(sellingParent);
		Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();

		SellingController newUserName = sellingPage.getController();
		newUserName.sendData(username);
		window.setScene(sellingScene);
		window.show();
		
	}

	// sell button
	public void editButton(ActionEvent event) throws IOException {
		FXMLLoader editPage = new FXMLLoader(getClass().getResource("AccountEdit.fxml"));
		Parent editParent = (Parent) editPage.load();
		Scene editScene = new Scene(editParent);
		Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();

		AccountDetails newUserName = editPage.getController();
		newUserName.sendData(username);
		window.setScene(editScene);
		window.show();
	}

	@FXML
	public void deleteBookFromTable(ActionEvent event) throws IOException {
		table.getItems().removeAll(table.getSelectionModel().getSelectedItem());
	}

	public void sendData(String text) {
		this.username = text;
		System.out.println(username); // verify username passed
		if (!this.username.equals("admin")){
			deleteButton.setVisible(false);
		}
		else
		{
			addButton.setVisible(false);
			viewButton.setVisible(false);
		}
		loadDatabase();
	}

}