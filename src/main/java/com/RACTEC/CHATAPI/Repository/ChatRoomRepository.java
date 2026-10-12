package com.RACTEC.CHATAPI.Repository;

import com.RACTEC.CHATAPI.Model.ChatRoom;
import com.RACTEC.CHATAPI.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c FROM ChatRoom c JOIN c.users u WHERE u.id = :userId")
    List<ChatRoom> findRoomByUserId(@Param("userId") Long userId);

    Optional<ChatRoom> findByChatRoomName(String chatRoomName);

    boolean existsByChatRoomName(String chatRoomName);
}