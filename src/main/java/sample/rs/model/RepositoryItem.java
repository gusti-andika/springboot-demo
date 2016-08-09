package sample.rs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Repository Item JPA Entity
 * 
 * @author Andika
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class RepositoryItem {
	@Id
	private String itemName;
    
	@NotNull
    private String itemContent;
    
	@NotNull
    private RepositoryItemType itemType;

	public RepositoryItem() {
		
	}
	
	public RepositoryItem(String itemName, String itemContent, RepositoryItemType type) {
		this.itemName = itemName;
		this.itemContent = itemContent;
		this.itemType = type;
	}
	
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemContent() {
		return itemContent;
	}

	public void setItemContent(String itemContent) {
		this.itemContent = itemContent;
	}

	public RepositoryItemType getItemType() {
		return itemType;
	}

	public void setItemType(RepositoryItemType itemType) {
		this.itemType = itemType;
	}
}
