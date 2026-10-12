package com.RACTEC.CHATAPI.Service;

import com.RACTEC.CHATAPI.Model.ChatRoom;
import com.RACTEC.CHATAPI.Model.Message;
import com.RACTEC.CHATAPI.Repository.ChatRoomRepository;
import com.RACTEC.CHATAPI.Repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class







ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, MessageRepository messageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.messageRepository = messageRepository;
    }

    // CREATE A NEW CHATROOM
    public ResponseEntity<?> createRoom(ChatRoom new_chatRoom) {
        String name = new_chatRoom.getChatRoomName();
        if (name == null || name.isBlank()) {
            return ResponseEntity.status(400).body("chatRoomName is required");
        }
        if (chatRoomRepository.existsByChatRoomName(name)) {
            return ResponseEntity.status(403).body("Room already exists");
        }
        chatRoomRepository.save(new_chatRoom);
        log.info("Chat room created: {}", name);
        return ResponseEntity.ok().body("chat room created successfully");
    }

    //SEE AL CHATROOM
    public List<ChatRoom> getAllRooms() {
        return chatRoomRepository.findAll();
    }

    // USER CHATOOM LIST
    public List<ChatRoom> getRoomsForUser(Long userId) {
        return chatRoomRepository.findRoomByUserId(userId);
    }


    //DELETE THE CHATROOM
    public ResponseEntity<?> deleteChatroom(Long id) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(id);
        if (chatRoom.isPresent()) {
            ChatRoom room = chatRoom.get();
            if (room.getUsers().isEmpty()) {
                List<Message> messagesInRoom = messageRepository.findAllMessagesInRoom(id);
                messageRepository.deleteAll(messagesInRoom);
                chatRoomRepository.deleteById(id);
                return ResponseEntity.ok().body("CHAT ROOM DELETED");
            }
            return ResponseEntity.status(403).body("Cannot Delete CHATROOM ----> USER STILL USING THIS CHATROOM");
        }
        return ResponseEntity.status(404).body("CHAT ROOM NOT FOUND");
    }

    public ResponseEntity<?> updateChatroom(Long id, ChatRoom new_chatRoom) {
        Optional<ChatRoom> old_chatroom = chatRoomRepository.findById(id);
        if (old_chatroom.isPresent()) {
            ChatRoom chatroom = old_chatroom.get();
            if (new_chatRoom.getChatRoomName() != null) {
                chatroom.setChatRoomName(new_chatRoom.getChatRoomName());
                chatRoomRepository.save(chatroom);
                return ResponseEntity.ok().body("room name changed to--->" + chatroom.getChatRoomName());
            }
        }
        return ResponseEntity.status(404).body("room not found");
    }
}