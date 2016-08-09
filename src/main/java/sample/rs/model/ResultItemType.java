package sample.rs.model;

/**
 * Holder for {@link RepositoryItem}.itemType to return from REST API
 * 
 * @author Andika
 *
 */
public class ResultItemType {

	private int itemType;
	
	public ResultItemType(RepositoryItem item) {
		this.itemType = item.getItemType().ordinal() + 1;
	}
	
	public int getItemType() {
		return this.itemType;
	}
}
