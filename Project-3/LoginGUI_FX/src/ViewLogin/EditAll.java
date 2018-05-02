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

public class EditAll {
	
	private String username;
	
	@FXML
	private TextField newPassField;
	
	@FXML
	private TextField oldPassField;
	
	@FXML
	private TextField confirmNewPassField;
	
	@FXML
	private TextField oldEmailField;
	
	@FXML
	private TextField newEmailField;

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
		String password = Security.hashPassword(oldPassField.getText());
		User existedUser = getUserInDB(username, password, oldEmailField.getText());
		
		if (newPassField.getText().equals(confirmNewPassField.getText())) {
			if (existedUser != null) {
				String pass = Security.hashPassword(newPassField.getText());
				changeAction(existedUser.userName, pass, newEmailField.getText());
				Alert alert = new Alert(AlertType.NONE, "Password Changed Successfully!", ButtonType.OK);
				alert.showAndWait();
				if (alert.getResult() == ButtonType.OK) {
					FXMLLoader editPage = new FXMLLoader(getClass().getResource("AccountEdit.fxml"));
					Parent editParent = (Parent) editPage.load();
					Scene editScene = new Scene(editParent);
					Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();

					AccountDetails newUserName = editPage.getController();
					newUserName.sendData(username);
					window.setScene(editScene);
					window.show();
				}
			}
			else {
				Alert alert = new Alert(AlertType.NONE, "Change Failed! Please Check your previous password or email", ButtonType.YES);
				alert.show();
			}
		}
		else {
			Alert alert = new Alert(AlertType.NONE, "Change Failed! New Password and Confirm New Password does not match!", ButtonType.YES);
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
	
	private void changeAction(String userName, String pass, String email) {
		Connection connection = null;
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:shoppingCartDb.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			
			String changePass = "update personTable set password = '" + pass 
					+ "', email = '" + email + "' where userName = '" + userName + "'";

			statement.executeUpdate(changePass);
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
	
	public void sendData(String text) {
		this.username = text;
		System.out.println(username); // verify username passed
	}
	
}