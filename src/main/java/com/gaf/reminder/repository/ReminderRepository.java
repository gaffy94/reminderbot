package com.gaf.reminder.repository;

import com.gaf.reminder.entities.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, UUID> {
    List<Reminder> findByIsProcessedAndReminderTimeIsLessThanEqual(boolean isProcessed, LocalDateTime currentTime);
}
