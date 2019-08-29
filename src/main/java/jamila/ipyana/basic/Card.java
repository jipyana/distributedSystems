package jamila.ipyana.basic;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;


@Entity
@Table(name = "cards")
public class Card {
	
	@Id
	@Min(0)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Size(max=255)
	private String description;
	
	@Size(max=255)
	private String nameOfCard;
	
	@ManyToOne
	private User user;
	
	private int cardToUserId;
	
	public int getCardToUserId() {
		return cardToUserId;
	}

	public void setCardToUserId(int cardToUserId) {
		this.cardToUserId = cardToUserId;
	}

	private byte [] image;
	
	public int getId() {
		return id;
	}
	
	

	public byte[] getImage() {
		return image;
	}



	public void setImage(byte[] image) {
		this.image = image;
	}


	
	public Card() {
		
	}

	public Card(String nameOfCard, String description, User user, byte [] image) {
		this.setNameOfCard(nameOfCard);
		this.setDescription(description);
		this.setUser(user);
		this.setImage(image);

	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public void copy(Card src) {
		this.setNameOfCard(src.getNameOfCard());
		this.setDescription(src.getDescription());
		this.setUser(src.getUser());
		this.setImage(src.getImage());
	}

	@Override
	public String toString() {
		return "Card [id=" + id + ", description=" + description + ", nameOfCard=" + nameOfCard + "]";
	}

	public String getNameOfCard() {
		return nameOfCard;
	}

	public void setNameOfCard(String nameOfCard) {
		this.nameOfCard = nameOfCard;
	}

	
}
