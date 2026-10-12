package com.RACTEC.CHATAPI.Repository;

import com.RACTEC.CHATAPI.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE m.room.id = :roomid AND m.isDeleted = false ORDER BY m.isPinned DESC , m.createdAt ASC")       // for finding all message in a specific room
            // normal sql commands works here...........
    List<Message> findAllMessagesInRoom(@Param("roomid") Long roomid) ; // return is message list type  so  also input is room id


    @Query("SELECT m FROM Message m WHERE m.room.id = :roomid AND m.isDeleted = true")
    List<Message> findAllDeletedMessagesInRoom(@Param("roomid") Long roomid);

    @Query("SELECT m FROM Message m WHERE m.room.id = :roomid AND LOWER(m.content) LIKE LOWER(CONCAT('%', :keyword, '%')) AND m.isDeleted = false")
    List<Message> searchMessagesByKeyword(@Param("roomid") Long roomid, @Param("keyword") String keyword);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.room.id = :roomid AND m.isDeleted = false")
    Long countMessagesInRoom(@Param("roomid") Long roomid) ;

    @Query("SELECT COUNT(m) FROM Message m WHERE m.sender.id  = :userId AND m.isDeleted = false")
    Long countMessagesBySender(@Param("userId") Long userId);

}
