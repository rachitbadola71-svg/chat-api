package com.RACTEC.CHATAPI.Service;

import com.RACTEC.CHATAPI.Model.ChatRoom;
import com.RACTEC.CHATAPI.Model.Message;
import com.RACTEC.CHATAPI.Model.Users;
import com.RACTEC.CHATAPI.Repository.ChatRoomRepository;
import com.RACTEC.CHATAPI.Repository.MessageRepository;
import com.RACTEC.CHATAPI.Repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UsersRepository usersRepository;
    private final ChatRoomRepository chatRoomRepository;

    public MessageService(MessageRepository messageRepository, UsersRepository usersRepository, ChatRoomRepository chatRoomRepository) {
        this.messageRepository = messageRepository;
        this.usersRepository = usersRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public ResponseEntity<?> createMessage(Long userId, Long roomId, Message message) {
        if (message.getContent() == null || message.getContent().isEmpty()) {
            return ResponseEntity.status(400).body("please type some message");
        }
        Optional<Users> sender = usersRepository.findById(userId);
        if (sender.isEmpty()) {
            log.warn("Message creation failed: no such user {}", userId);
            return ResponseEntity.status(404).body("no such user");
        }
        Optional<ChatRoom> room = chatRoomRepository.findById(roomId);
        if (room.isEmpty()) {
            log.warn("Message creation failed: no such room {}", roomId);
            return ResponseEntity.status(404).body("no such room exists");
        }
        message.setSender(sender.get());
        message.setRoom(room.get());
        messageRepository.save(message);
        log.info("Message sent by user {} in room {}", userId, roomId);
        return ResponseEntity.ok().body("message sent successfully");
    }




    public List<Message> getAllDeletedMessagesInRoom(Long roomid) {
        return messageRepository.findAllDeletedMessagesInRoom(roomid);
    }




    public ResponseEntity<?> searchMessageByKeyword(Long roomid, String keyword) {
        List<Message> results = messageRepository.searchMessagesByKeyword(roomid, keyword);
        if (results.isEmpty()) {
            return ResponseEntity.ok("No similar message found ");
        }
        return ResponseEntity.ok(results);
    }




    public ResponseEntity<?> getMessageCount(Long roomid) {
        Long count = messageRepository.countMessagesInRoom(roomid);
        return ResponseEntity.ok(count);
    }



    public ResponseEntity<?> getMessageCountBySender(Long senderid) {
        Long count = messageRepository.countMessagesBySender(senderid);
        return ResponseEntity.ok(count);
    }




    public ResponseEntity<?> deleteMessage(Long id) {
        Optional<Message> old_message = messageRepository.findById(id);
        if (old_message.isPresent()) {
            Message message = old_message.get();
            LocalDateTime delete_Time = LocalDateTime.now();
            LocalDateTime message_time = message.getCreatedAt();
            Duration delete_duration = Duration.between(message_time, delete_Time);
            long delete_window = delete_duration.toHours();
            if (delete_window < 1) {
                message.setDeleted(true);
                messageRepository.save(message);
                return ResponseEntity.ok().body("Deleted Successfully");
            } else {
                return ResponseEntity.status(403).body("Cannot Delete Messages After 1 HOUR");
            }
        }
        return ResponseEntity.status(404).body("no message found to deleted");
    }




    public ResponseEntity<?> updateMessage(Long id, Message new_message) {
        Optional<Message> old_message = messageRepository.findById(id);
        if (old_message.isPresent()) {
            Message message = old_message.get();
            LocalDateTime message_time = message.getCreatedAt();
            LocalDateTime now = LocalDateTime.now();
            Duration message_duration = Duration.between(message_time, now);
            long edit_window = message_duration.toMinutes();

            if (10 > edit_window && message.getEditCount() == 0) {
                message.setContent(new_message.getContent());
                message.setEditCount(message.getEditCount() + 1);
                return ResponseEntity.ok(messageRepository.save(message));
            } else if (edit_window >= 10) {
                return ResponseEntity.status(403).body("Cannot Edit Message After 10 Minutes");
            } else if (message.getEditCount() != 0) {
                return ResponseEntity.status(403).body("Message Can Be Edited Only Once");
            }
        }
        return ResponseEntity.notFound().build();
    }






    public ResponseEntity<?> pinMessage(Long messageId) {
        Optional<Message> old_message = messageRepository.findById(messageId);
        if (old_message.isPresent()) {
            Message message = old_message.get();
            message.setPinned(!message.isPinned());
            messageRepository.save(message);
            String status = message.isPinned() ? "Pinned" : "Unpinned";
            return ResponseEntity.ok().body("Message" + status + " Successfully");
        }
        return ResponseEntity.status(404).body("no message found to pinned");
    }


    public ResponseEntity<?> getAllMessagesInRoom(Long roomId) {
        if (!chatRoomRepository.existsById(roomId)) {
            return ResponseEntity.status(404).body("Room not found");
        }
        return ResponseEntity.ok(messageRepository.findAllMessagesInRoom(roomId));
    }


}