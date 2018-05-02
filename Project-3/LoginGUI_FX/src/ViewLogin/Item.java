package ViewLogin;

public class Item {
	String itemName;
	String itemType;
	String itemCondition;
	double itemPrice;
	String itemDescription;
	
	public Item() {
	}
	public Item(String n, String t, String c, Double p, String d) {
		this.itemName = n;
		this.itemType = t;
		this.itemCondition = c;
		this.itemPrice = p;
		this.itemDescription = d;
	}
}
