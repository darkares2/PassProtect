package dk.darkares.PassProtect.repository;

import dk.darkares.PassProtect.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE LOWER(u.name) = LOWER(:username)")
    User findByNameCaseInsensitive(@Param("username") String username);

    User findByName(String userName);

}