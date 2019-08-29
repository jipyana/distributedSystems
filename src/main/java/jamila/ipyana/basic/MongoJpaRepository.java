package jamila.ipyana.basic;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoJpaRepository extends MongoRepository<Image, Integer> {
	
	 Image findByImageId(int id);
}
