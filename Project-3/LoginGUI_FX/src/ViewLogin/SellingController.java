package ViewLogin;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SellingController {
	@FXML
	private TextField isbnField;

	@FXML
	private TextField bookName;

	@FXML
	private TextField authorFirst;

	@FXML
	private TextField authorLast;

	@FXML
	private TextField editionField;

	@FXML
	private TextField yearField;

	@FXML
	private TextField priceField;

	@FXML
	private Button Sell;

	@FXML
	private Button Cancel;
	private String username;


	public void showAlert(String alertString) {
		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		errorAlert.setTitle("Form Error!");
		errorAlert.setHeaderText(alertString);
		errorAlert.show();
	}

	public void succesfulAlert(String alertString) {
		Alert success = new Alert(Alert.AlertType.CONFIRMATION);
		success.setTitle("Book Posted");
		success.setHeaderText(alertString);
		success.show();
	}

	@FXML
	public void sellAction(ActionEvent event) throws IOException {

		if (isbnField.getText().isEmpty()) {
			showAlert("Please enter the Book name");
			return;
		}

		if (bookName.getText().isEmpty()) {
			showAlert("Please enter the Book name");
			return;
		}

		if (authorFirst.getText().isEmpty()) {
			showAlert("Please enter the Book name");
			return;
		}

		if (authorLast.getText().isEmpty()) {
			showAlert("Please enter the Book name");
			return;
		}

		if (editionField.getText().isEmpty()) {
			showAlert("Please enter the Book type");
			return;
		}

		if (priceField.getText().isEmpty()) {
			showAlert("Please enter the price name");
			return;
		}

		Book newBook = new Book();
		newBook.setAuthor(authorLast.getText() + ", " + authorFirst.getText());
		newBook.setBook(bookName.getText());
		newBook.setIsbn(Integer.parseInt(isbnField.getText()));
		newBook.setEdition(Integer.parseInt(editionField.getText()));
		newBook.setYear(Integer.parseInt(yearField.getText()));
		newBook.setPrice(Float.parseFloat(priceField.getText()));
		newBook.setSellBy(username);
		boolean isSavedSuccessfully = saveBookToDB(newBook);
		if (isSavedSuccessfully) {
			Alert alert = new Alert(AlertType.NONE, "Item succesfully listed for sale", ButtonType.OK);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {

				FXMLLoader bookstorePage = new FXMLLoader(getClass().getResource("Bookstore.fxml"));
				Parent bookstoreParent = (Parent) bookstorePage.load();
				Scene bookstoreScene = new Scene(bookstoreParent);
				Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();

				BookstoreController newUserName = bookstorePage.getController();
				newUserName.sendData(username);
				window.setScene(bookstoreScene);
				window.show();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR, "Item Sale Failed!", ButtonType.YES);
			alert.show();
		}
	}

	private boolean saveBookToDB(Book Book) {
		Connection connection = null;
		System.out.println(Book.getAuthor());
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:shoppingCartDb.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			// String getAllPersonString1 = "insert into personTable values"
			// + "(1, 'leo','le','leo123','leo123@gmail.com','leole')\"";

			String getAllBookString = "insert into bookTable values('" 
					+ Book.getIsbn() + "', '" 
					+ Book.getBook()+ "', '" 
					+ Book.getAuthor() + "', '" 
					+ Book.getEdition() + "', '" 
					+ Book.getYear() + "', '"
					+ Book.getPrice() + "', '" 
					+ Book.getSellBy() + "')";
			statement.executeUpdate(getAllBookString);
			return true;
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
		return true;
	}

	@FXML
	public void Cancel(ActionEvent event) throws IOException {
		FXMLLoader bookstorePage = new FXMLLoader(getClass().getResource("Bookstore.fxml"));
		Parent bookstoreParent = (Parent) bookstorePage.load();
		Scene bookstoreScene = new Scene(bookstoreParent);
		Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();

		BookstoreController newUserName = bookstorePage.getController();
		newUserName.sendData(username);
		window.setScene(bookstoreScene);
		window.show();
	}

	public void sendData(String username) {
		this.username = username;
		System.out.println("Selling: " + username);

	}
}