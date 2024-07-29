package az.example.eventsapp.scheduler;

import az.example.eventsapp.entity.EventEntity;
import az.example.eventsapp.enums.EventStatus;
import az.example.eventsapp.repository.EventRepository;
import jdk.dynalink.linker.LinkerServices;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventStatusScheduler {

    private final EventRepository eventRepository;

    @Scheduled(cron = "0 0  * * * ?")
    public void updateEventStatuses() {
        List<EventEntity> events = eventRepository.findAll();
        for (EventEntity event : events){
            if (event.getStatus() == EventStatus.ACTIVE && LocalDateTime.now().isAfter(event.getEndDate())){
                event.setStatus(EventStatus.INACTIVE);
                eventRepository.save(event);
            }
        }
    }
}
