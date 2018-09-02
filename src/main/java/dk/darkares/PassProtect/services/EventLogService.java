package dk.darkares.PassProtect.services;

import dk.darkares.PassProtect.models.EventLog;
import dk.darkares.PassProtect.repository.EventLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class EventLogService {
    @Autowired
    private EventLogRepository eventLogRepository;

    public EventLog createEvent(String event, String message) {
        EventLog log = new EventLog();
        log.setEvent(event);
        log.setMessage(message);
        log.setTimestamp(new Timestamp(new Date().getTime()));
        return eventLogRepository.save(log);
    }

    public List<EventLog> getLatest() {
        return eventLogRepository.findTop100ByOrderByTimestampDesc();
    }

}
