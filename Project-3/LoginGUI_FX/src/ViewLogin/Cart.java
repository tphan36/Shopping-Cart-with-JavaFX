package ViewLogin;

public class Cart {
	private int isbn;
	private String book;
	private String author;
	private int edition;
	private int year;
	private float price;
	private String username;
	
	public Cart () {}

	public Cart (String username,int isbn, String bookName, String author, int edition, int year, float price) {
		super();
		this.isbn = isbn;
		this.author = author;
		this.book = bookName;
		this.edition = edition;
		this.year = year;
		this.price = price;
		this.username = username;
	}

	public int getIsbn() {
		return isbn;
	}

	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}

	public String getBook() {
		return book;
	}

	public void setBook(String book) {
		this.book = book;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getEdition() {
		return edition;
	}

	public void setEdition(int edition) {
		this.edition = edition;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
