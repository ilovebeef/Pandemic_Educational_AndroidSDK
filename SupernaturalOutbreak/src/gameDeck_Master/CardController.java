// Written by Matt Roycroft
package gameDeck_Master;

public class CardController {
	private Card model;
	private CardView view;
	
	public CardController(Card model, CardView view) {
		this.model = model;
		this.view = view;
	}

	public void setCardName(String name) {
		model.setFileName(name);
	}
	
	public String getCardName() {
		return model.getFileName();
	}
	
	public void setCardType(String type) {
		model.setType(type);
	}
	
	public String getCardType() {
		return model.getType();
	}
	
	public void viewCard() {
		view.printCardDetails(model.getFileName(), model.getType());
	}
}
