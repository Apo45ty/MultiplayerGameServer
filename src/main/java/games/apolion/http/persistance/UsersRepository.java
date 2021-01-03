package games.apolion.http.persistance;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface UsersRepository extends PagingAndSortingRepository<Users, Long> {
	//@Query(value ="select username,email,'*****' as passwordhash from users ", nativeQuery = true)
	List<Users> findAll();
	Optional<Users> findByUsernameAndPasswordhash(String username, String passwordhash);
	Users  findByEmailAndPasswordhash(String email,String passwordhash);
	Users findByEmail(String email);
	//@Query(value ="select username,email,'*****' as passwordhash from users where username = ?1", nativeQuery = true)
	Users findByUsername(String username);

}
