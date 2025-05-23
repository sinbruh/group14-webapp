package no.ntnu.project.group4.webapp.repositories;

import no.ntnu.project.group4.webapp.models.Receipt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The ReceiptRepository class represents the repository class for the receipt entity.
 */
@Repository
public interface ReceiptRepository extends CrudRepository<Receipt, Long> {
}
