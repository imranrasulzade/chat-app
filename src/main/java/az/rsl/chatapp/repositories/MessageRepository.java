package az.rsl.chatapp.repositories;

import az.rsl.chatapp.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiver_Id(Long receiver);


    @Query("SELECT m FROM Message m WHERE (m.status = true AND m.sender.id = :sender1 AND m.receiver.id = :receiver1)" +
            " OR (m.status = true AND m.sender.id = :receiver2 AND m.receiver.id = :sender2) " +
            "ORDER BY m.timestamp ASC")
    List<Message> findMessagesByStatusTrueAndSenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(
            @Param("sender1") Long sender1,
            @Param("receiver1") Long receiver1,
            @Param("sender2") Long sender2,
            @Param("receiver2") Long receiver2);

}