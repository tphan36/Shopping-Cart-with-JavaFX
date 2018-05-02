package ViewLogin;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ShoppingCartController implements Initializable {
	ObservableList<Book> data = FXCollections.observableArrayList();

	@FXML
	TableView<Book> table;

	@FXML
	private TableColumn<?, ?> priceCol;

	@FXML
	private TableColumn<?, ?> bookNameCol;

	@FXML
	private Label bookNameLabel;

	@FXML
	private Label priceLabel;

	@FXML
	private Label subtotalLabel;

	@FXML
	private Label shippingLabel;

	@FXML
	private Label totalLabel;

	@FXML
	private Label taxLabel;

	private String username;

	private float totalPrice = 0f;
	private final float tax = 0.07f;
	public ShoppingCartController() {

	}

	public void sendData(String username) {
		this.username = username;
		System.out.println("VERIFY " + this.username);
		loadDatabase();
	}

	public void initialize(URL location, ResourceBundle resources) {
		bookNameCol.setCellValueFactory(new PropertyValueFactory<>("book"));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		table.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// Book selectedBook = table.getSelectionModel().getSelectedItem();
				// showBookDetails(selectedBook);
			}
		});
	}

	public void showBookDetails(Book book) {
		bookNameLabel.setText(book.getBook());
		priceLabel.setText("$" + book.getPrice());
	}

	public void loadDatabase() {

		Connection connection = null;
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:shoppingCartDb.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 second
			String query = "select * from cartTable where username = '" + this.username + "'";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				Book existedBook = new Book(rs.getInt("isbn"), rs.getString("book"), rs.getString("author"),
						rs.getInt("edition"), rs.getInt("year"), rs.getFloat("price"), rs.getString("soldBy"));
				totalPrice += existedBook.getPrice();
				data.add(existedBook);
				table.setItems(data);
			}
			
			DecimalFormat decimalFormat = new DecimalFormat("#.00");
			String taxAmount = decimalFormat.format(totalPrice * tax);
			String totalAmount = decimalFormat.format(totalPrice + (totalPrice * tax));
			String total = decimalFormat.format(totalPrice);
			taxLabel.setText(("$" + String.valueOf(taxAmount)));
			totalLabel.setText("$" + String.valueOf(totalAmount));
			subtotalLabel.setText("$" + String.valueOf(total));
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

	public void removeButton(ActionEvent event) throws IOException {
		Book selectedBook = table.getSelectionModel().getSelectedItem();
		if (table.getSelectionModel().getSelectedItem() != null) {
			if (removeSelected(selectedBook)) {
				Alert alert = new Alert(AlertType.NONE, "Removed Successfully!", ButtonType.OK);
				alert.showAndWait();
				FXMLLoader shoppingCartPage = new FXMLLoader(getClass().getResource("ShoppingCart.fxml"));
				Parent shoppingCartParent = (Parent) shoppingCartPage.load();
				Scene shoppingCartScene = new Scene(shoppingCartParent);
				Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();
				ShoppingCartController newUserName = shoppingCartPage.getController();
				newUserName.sendData(username);
				window.setScene(shoppingCartScene);
				window.show();
			}
			else if (event == null) {
				Alert alert = new Alert(AlertType.NONE, "Removal Failed!", ButtonType.OK);
				alert.showAndWait();
				return;
			}
			else {
				Alert alert = new Alert(AlertType.NONE, "Removal Failed!", ButtonType.OK);
				alert.showAndWait();
				return;
			}
		}
		else {
			Alert alert = new Alert(AlertType.NONE, "Please select a book for removal!", ButtonType.OK);
			alert.showAndWait();
			return;
		}
	}

	public void backButton(ActionEvent event) throws IOException {

		FXMLLoader bookstorePage = new FXMLLoader(getClass().getResource("Bookstore.fxml"));
		Parent bookstoreParent = (Parent) bookstorePage.load();
		Scene bookstoreScene = new Scene(bookstoreParent);
		Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();

		BookstoreController newUserName = bookstorePage.getController();
		newUserName.sendData(username);
		window.setScene(bookstoreScene);
		window.show();

	}

	private boolean removeSelected(Book cart) {

    	Connection connection = null;
        try
        {
          // create a database connection
          connection = DriverManager.getConnection("jdbc:sqlite:shoppingCartDb.db");
          Statement statement = connection.createStatement();
          statement.setQueryTimeout(30);  // set timeout to 30 sec.
          //check on commit
          
          String removeCart = "delete from cartTable where "
          			+ "username ='" + username + "' and "
	          		+ "isbn= '" + cart.getIsbn()  + "' and "
	          		+ "book='" + cart.getBook()  + "' and "
	          		+ "author='" + cart.getAuthor()   + "' and "
	          		+ "edition='" +cart.getEdition()   + "' and "
	          		+ "year='" + cart.getYear()  + "' and "
	          		+ "price='" +cart.getPrice() + "' and " 
	          		+ "soldBy='" +cart.getSellBy() + "'";
          statement.executeUpdate(removeCart);
          statement.close();
          return true;
        }
        catch(SQLException e)
        {
          // if the error message is "out of memory",
          // it probably means no database file is found
          System.err.println(e.getMessage());
        }
        finally
        {
          try
          {
            if(connection != null)
              connection.close();
          }
          catch(SQLException e)
          {
            // connection close failed.
            System.err.println(e);
          }
        }
        return true;
    }

	@FXML
	public void CheckoutButton(ActionEvent event) throws IOException {
		FXMLLoader creditCardPage = new FXMLLoader(getClass().getResource("CreditCardTransaction.fxml"));
		Parent creditCardParent = (Parent) creditCardPage.load();
		Scene creditCardScene = new Scene(creditCardParent);
		Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();

		TransactionController newUserName = creditCardPage.getController();
		newUserName.sendData(username);
		window.setScene(creditCardScene);
		window.show();
	}

}
