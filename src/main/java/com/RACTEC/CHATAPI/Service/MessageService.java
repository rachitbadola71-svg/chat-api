package com.RACTEC.CHATAPI.Service;

import com.RACTEC.CHATAPI.Model.ChatRoom;
import com.RACTEC.CHATAPI.Model.Message;
import com.RACTEC.CHATAPI.Model.MessageStatus;
import com.RACTEC.CHATAPI.Model.Users;
import com.RACTEC.CHATAPI.Repository.ChatRoomRepository;
import com.RACTEC.CHATAPI.Repository.MessageRepository;
import com.RACTEC.CHATAPI.Repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.RACTEC.CHATAPI.DTO.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final FileStorageService fileStorageService;

    public MessageService(MessageRepository messageRepository, UsersRepository usersRepository, ChatRoomRepository chatRoomRepository , FileStorageService fileStorageService )  {
        this.messageRepository = messageRepository;
        this.usersRepository = usersRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.fileStorageService = fileStorageService;
    }

    private MessageResponse toResponse(Message m) {
        Long senderId = null;
        String senderUsername = null;
        if (m.getSender() != null) {
            senderId = m.getSender().getId();
            senderUsername = m.getSender().getUsername();
        }
        return new MessageResponse(
                m.getId(),
                m.getContent(),
                m.getStatus(),
                m.getCreatedAt(),
                m.getEditCount(),
                senderId,
                senderUsername,
                m.getFileName(),
                m.getOriginalFileName());
    }

    //NEW MESSAGE
   /* public ResponseEntity<?> createMessage(Long userId, Long roomId, Message message) {
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
        Message newMessage = new Message();
        newMessage.setContent(message.getContent());
        newMessage.setSender(sender.get());
        newMessage.setRoom(room.get());
        messageRepository.save(newMessage);
        log.info("Message sent by user {} in room {}", userId, roomId);
        return ResponseEntity.ok().body("message sent successfully");
    }*/

    // CREATE A FILE MESSAGE
    public ResponseEntity<?> createMessage(Long userId, Long roomId, String content, MultipartFile file) throws IOException {
        boolean hasText = content != null && !content.isBlank();
        boolean hasFile = file != null && !file.isEmpty();

        if (!hasText && !hasFile) {
            return ResponseEntity.status(400).body("send some text or a file");
        }

        if (hasFile) {
            String error = fileStorageService.validateFile(file);
            if (error != null) {return ResponseEntity.status(400).body(error);}
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

        Message newMessage = new Message();
        newMessage.setSender(sender.get());
        newMessage.setRoom(room.get());

        if (hasText) {newMessage.setContent(content);}

        if (hasFile) {
            String savedName = fileStorageService.save(file);
            newMessage.setFileName(savedName);
            newMessage.setOriginalFileName(file.getOriginalFilename());
        }

        Message saved = messageRepository.save(newMessage);
        log.info("Message sent by user {} in room {}", userId, roomId);
        return ResponseEntity.ok(toResponse(saved));
    }


///ALL DELETED MESSAGE IN THE ROOM
    public List<MessageResponse> getAllDeletedMessagesInRoom(Long roomid) {
        return messageRepository.findAllDeletedMessagesInRoom(roomid)
                .stream()
                .map(this ::toResponse)
                .toList();
    }

//SEARCH THE MESSAGE
    public ResponseEntity<?> searchMessageByKeyword(Long roomid, String keyword) {
        List<Message> results = messageRepository.searchMessagesByKeyword(roomid, keyword);
        if (results.isEmpty()) {
            return ResponseEntity.ok("No similar message found ");
        }
        return ResponseEntity.ok(results.stream().map(this::toResponse).toList());
    }

// NUMBER OF MESSAGES IN THE ROOM
    public ResponseEntity<?> getMessageCount(Long roomid) {
        Long count = messageRepository.countMessagesInRoom(roomid);
        return ResponseEntity.ok(count);
    }

//NUMBER OF MESSAGE HY THE SENDER
    public ResponseEntity<?> getMessageCountBySender(Long senderid) {
        Long count = messageRepository.countMessagesBySender(senderid);
        return ResponseEntity.ok(count);
    }



    //DELETE THE MESSAGE
    public ResponseEntity<?> deleteMessage(Long id, Long userId) {
        Optional<Message> old_message = messageRepository.findById(id);
        if (old_message.isPresent()) {
            Message message = old_message.get();

            if (message.getSender() == null || !message.getSender().getId().equals(userId)) {
                return ResponseEntity.status(403).body("Only the sender can do this");
            }

            LocalDateTime delete_Time = LocalDateTime.now();
            LocalDateTime message_time = message.getCreatedAt();
            Duration delete_duration = Duration.between(message_time, delete_Time);
            long delete_window = delete_duration.toHours();
            if (delete_window < 1) {
                message.setStatus(MessageStatus.DELETED);
                messageRepository.save(message);
                return ResponseEntity.ok().body("Deleted Successfully");
            } else {
                return ResponseEntity.status(403).body("Cannot Delete Messages After 1 HOUR");
            }
        }
        return ResponseEntity.status(404).body("no message found to deleted");
    }

    //EDIT A MESSAGE
    public ResponseEntity<?> updateMessage(Long id, Message new_message, Long userId) {
        Optional<Message> old_message = messageRepository.findById(id);
        if (old_message.isPresent()) {
            Message message = old_message.get();

            if (message.getSender() == null || !message.getSender().getId().equals(userId)) {
                return ResponseEntity.status(403).body("Only the sender can do this");
            }

            LocalDateTime message_time = message.getCreatedAt();
            LocalDateTime now = LocalDateTime.now();
            Duration message_duration = Duration.between(message_time, now);
            long edit_window = message_duration.toMinutes();
            if (10 > edit_window && message.getEditCount() == 0) {
                message.setContent(new_message.getContent());
                message.setEditCount(message.getEditCount() + 1);
                return ResponseEntity.ok(toResponse (messageRepository.save(message)));
            } else if (edit_window >= 10) {
                return ResponseEntity.status(403).body("Cannot Edit Message After 10 Minutes");
            } else if (message.getEditCount() != 0) {
                return ResponseEntity.status(403).body("Message Can Be Edited Only Once");
            }
        }
        return ResponseEntity.notFound().build();
    }

    //PINNING A MESSAGE
    public ResponseEntity<?> pinMessage(Long messageId, Long userId) {
        Optional<Message> old_message = messageRepository.findById(messageId);
        if (old_message.isPresent()) {
            Message message = old_message.get();

            if (message.getSender() == null || !message.getSender().getId().equals(userId)) {
                return ResponseEntity.status(403).body("Only the sender can do this");
            }

            if (message.getStatus() == MessageStatus.DELETED) {
                return ResponseEntity.status(403).body("Cannot pin a deleted Message");
            }

            if (message.getStatus() == MessageStatus.PINNED) {
                message.setStatus(MessageStatus.ACTIVE);
                messageRepository.save(message);
                return ResponseEntity.ok().body("message is now unpinned");
            }

            if (message.getStatus() == MessageStatus.ACTIVE) {
                message.setStatus(MessageStatus.PINNED);
                messageRepository.save(message);
                return ResponseEntity.ok().body("message is now pinned");
            }
        }
        return ResponseEntity.status(404).body("no message found to active ");
    }

//ALL MESSAGES IN THE ROOM
    public ResponseEntity<?> getAllMessagesInRoom(Long roomId) {
        if (!chatRoomRepository.existsById(roomId)) {
            return ResponseEntity.status(404).body("Room not found");
        }
        List<MessageResponse> messages = messageRepository.findAllMessagesInRoom(roomId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(messages);
    }





}