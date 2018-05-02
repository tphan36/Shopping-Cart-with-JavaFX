package ViewLogin;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AccountDetails {
	
	private String username;
	
	public void EditEmail(ActionEvent event) throws IOException{
		FXMLLoader editEmailPage = new FXMLLoader(getClass().getResource("EditEmail.fxml"));
		Parent editEmailParent = (Parent) editEmailPage.load();
		Scene editEmailScene = new Scene(editEmailParent);
		Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		
		EditEmail email = editEmailPage.getController();
		email.sendData(username);
		window.setScene(editEmailScene);
		window.show();
		
    }
	
	public void EditPassword(ActionEvent event) throws IOException{
		FXMLLoader editPassPage = new FXMLLoader(getClass().getResource("EditPassword.fxml"));
		Parent editPassParent = (Parent) editPassPage.load();
		Scene editPassScene = new Scene(editPassParent);
		Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		
		EditPassword email = editPassPage.getController();
		email.sendData(username);
		window.setScene(editPassScene);
		window.show();
		
    }
	
	public void EditAll(ActionEvent event) throws IOException{
		FXMLLoader editAllPage = new FXMLLoader(getClass().getResource("EditAll.fxml"));
		Parent editAllarent = (Parent) editAllPage.load();
		Scene editAllScene = new Scene(editAllarent);
		Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();

		EditAll newUserName = editAllPage.getController();
		newUserName.sendData(username);
		window.setScene(editAllScene);
		window.show();
    }
	
	public void CheckoutButton(ActionEvent event) throws IOException {
		FXMLLoader creditPage = new FXMLLoader(getClass().getResource("CreditCardTransaction.fxml"));
		Parent creditParent = (Parent) creditPage.load();
		Scene creditScene = new Scene(creditParent);
		Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();

		TransactionController newUserName = creditPage.getController();
		newUserName.sendData(username);
		window.setScene(creditScene);
		window.show();
    }
	
	public void sellButton(ActionEvent event) throws IOException{
		Parent Login = FXMLLoader.load(getClass().getResource("Selling.fxml"));
		Scene LoginScene = new Scene(Login);
		Stage window2 = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		window2.setScene(LoginScene);
		window2.show();
    }
	
	public void homeButton(ActionEvent event) throws IOException{
		FXMLLoader homePage = new FXMLLoader(getClass().getResource("Bookstore.fxml"));
		Parent homeParent = (Parent) homePage.load();
		Scene homeScene = new Scene(homeParent);
		Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();

		BookstoreController newUserName = homePage.getController();
		newUserName.sendData(username);
		window.setScene(homeScene);
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
	public void sendData(String text) {
		this.username = text;
		System.out.println(username); // verify username passed
	}
}