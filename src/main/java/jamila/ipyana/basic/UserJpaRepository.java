package jamila.ipyana.basic;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserJpaRepository extends JpaRepository<User, Integer> {
	
	@Query("SELECT h from User h where h.username = ?1")
	User findByUsername(String username);
	
	User findById(int userId);
	
	List<User> findByUsernameLike(String username);

}
