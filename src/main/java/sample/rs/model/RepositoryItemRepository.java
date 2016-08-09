package sample.rs.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link RepositoryItem} entity
 * @author Andika
 *
 */
@Repository
public interface RepositoryItemRepository extends
		JpaRepository<RepositoryItem, String> {

	RepositoryItem findByItemName(String itemName);
}
