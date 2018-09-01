package dk.darkares.PassProtect.repository;

import dk.darkares.PassProtect.models.KeyStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeyStoreRepository extends JpaRepository<KeyStore, Long> {
    List<KeyStore> findAllByUserId(Long userId);
    void deleteByUserIdAndId(Long userId, Long id);
}