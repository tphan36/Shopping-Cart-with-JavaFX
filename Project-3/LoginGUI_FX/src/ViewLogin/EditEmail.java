package ViewLogin;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class EditEmail {

	private String username;
	
	@FXML
	private TextField newEmailField;
	
	@FXML
	private TextField previousEmailField;
	
	@FXML
	private TextField passwordField;

	public void Cancel(ActionEvent event) throws IOException{
		FXMLLoader editPage = new FXMLLoader(getClass().getResource("AccountEdit.fxml"));
		Parent editParent = (Parent) editPage.load();
		Scene editScene = new Scene(editParent);
		Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();

		AccountDetails newUserName = editPage.getController();
		newUserName.sendData(username);
		window.setScene(editScene);
		window.show();
    }
	
	public void Accept(ActionEvent event) throws IOException, NoSuchAlgorithmException{
		System.out.println("accept: " + username);
		String password = Security.hashPassword(passwordField.getText());
		User existedUser = getUserInDB(username, password, previousEmailField.getText());
		if (existedUser != null) {
			change(existedUser.userName, newEmailField.getText());
			Alert alert = new Alert(AlertType.NONE, "Email Changed Successfully!", ButtonType.YES);
			alert.show();
			FXMLLoader editPage = new FXMLLoader(getClass().getResource("AccountEdit.fxml"));
			Parent editParent = (Parent) editPage.load();
			Scene editScene = new Scene(editParent);
			Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();

			AccountDetails newUserName = editPage.getController();
			newUserName.sendData(username);
			window.setScene(editScene);
			window.show();
		}
		else {
			Alert alert = new Alert(AlertType.ERROR, "Change Failed! Please Check your previous email or password", ButtonType.YES);
			alert.show();
		}
    }
	
	private User getUserInDB(String userName, String password, String email) {
		Connection connection = null;
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:shoppingCartDb.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			String getAllPersonString = "select * from personTable where userName = '" + userName + "' and password = '"
					+ password + "' and email = '" + email + "'";

			ResultSet rs = statement.executeQuery(getAllPersonString);
			while (rs.next()) {
				User existedUser = new User();
				existedUser.firstName = rs.getString("firstName");
				existedUser.lastName = rs.getString("lastName");
				existedUser.userName = rs.getString("userName");
				existedUser.email = rs.getString("email");
				existedUser.password = rs.getString("password");

				return existedUser;
				// read the result set
				// System.out.println("name = " + rs.getString("firstName"));
				// System.out.println("personId = " + rs.getInt("personId"));
			}

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
		return null;
	}
	
	private void change(String userName, String email) {
		Connection connection = null;
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:shoppingCartDb.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			
			String changeEmail = "update personTable set email = '" + email 
					+ "' where userName = '" + userName + "'";

			statement.executeUpdate(changeEmail);
			statement.close();

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
		return;
	}

	public void sendData(String username) {
		this.username = username;
		System.out.println(username);
	}
}