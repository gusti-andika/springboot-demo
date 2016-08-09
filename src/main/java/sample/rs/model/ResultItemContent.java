package sample.rs.model;

/**
 * Holder for {@link RepositoryItem}.itemContent to return from REST API
 * 
 * @author Andika
 *
 */
public class ResultItemContent {
	private String itemContent;

	public ResultItemContent(RepositoryItem item) {
		this.itemContent = item.getItemContent();
	}
	
	public String getItemContent() {
		return itemContent;
	}
}
