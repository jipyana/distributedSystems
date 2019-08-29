package jamila.ipyana.basic;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Image {
	
	@Id
	private int imageId;
	
	private byte []  data;
	
	@Transient
	private Card card;
	
	@Transient
	private int cardId;
	
	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public Image(int id, byte data) {
		imageId = id;
		data = data;
	}
	
	public Image() {
		
	}

	public int getId() {
		return imageId;
	}

	public void setId(int id) {
		this.imageId = imageId;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
