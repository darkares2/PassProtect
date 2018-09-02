package dk.darkares.PassProtect.repository;

import dk.darkares.PassProtect.models.EventLog;
import dk.darkares.PassProtect.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventLogRepository extends CrudRepository<EventLog, Long> {
    List<EventLog> findTop100ByOrderByTimestampDesc();
}