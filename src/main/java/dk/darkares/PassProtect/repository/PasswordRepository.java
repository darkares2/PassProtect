package dk.darkares.PassProtect.repository;

import dk.darkares.PassProtect.models.Password;
import dk.darkares.PassProtect.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Long> {
    List<Password> findAllByUserId(long userId);
    Password findByIdAndUserId(long id, long userId);
    void deleteByUserId(Long userId);
    void deleteByUserIdAndId(Long userId, Long id);
}