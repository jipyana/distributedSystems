package jamila.ipyana.basic;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


public interface CardJpaRepository extends JpaRepository<Card, Integer>  {
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Card getOne(Integer id);
	
	List<Card> findByDescription(String description);
	
	@Query("SELECT h from Card h where h.user.username = ?1")
	List<Card> findByUsername(String username);

}
