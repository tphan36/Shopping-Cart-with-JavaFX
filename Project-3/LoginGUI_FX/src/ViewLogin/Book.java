package ViewLogin;

public class Book {
	private int isbn;
	private String book;
	private String author;
	private int edition;
	private int year;
	private float price;
	private String sellBy;
	
	public Book () {}
	
	public Book (int isbn, String bookName, String author, int edition, int year, float price, String sellBy) {
		super();
		this.isbn = isbn;
		this.author = author;
		this.book = bookName;
		this.edition = edition;
		this.year = year;
		this.price = price;
		this.sellBy = sellBy;
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

	public void setBook(String bookName) {
		this.book = bookName;
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

	public String getSellBy() {
		return sellBy;
	}

	public void setSellBy(String sellBy) {
		this.sellBy = sellBy;
	}

}
