package com.RACTEC.CHATAPI.Controller;

import com.RACTEC.CHATAPI.Model.ChatRoom;
import com.RACTEC.CHATAPI.Model.Message;
import com.RACTEC.CHATAPI.Repository.ChatRoomRepository;
import com.RACTEC.CHATAPI.Repository.MessageRepository;
import com.RACTEC.CHATAPI.Repository.UsersRepository;
import jakarta.persistence.Id;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class ChatRoomController {



    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomController(MessageRepository messageRepository,UsersRepository usersRepository,ChatRoomRepository chatRoomRepository) {

        this.chatRoomRepository = chatRoomRepository;
    }

    @PostMapping("/api/chatroom")
    public ResponseEntity<?> createRoom (@RequestBody ChatRoom new_chatRoom) {
      if(chatRoomRepository.existsByChatRoomName(new_chatRoom.getChatRoomName())) {
           return ResponseEntity.status(403).body("Room already exists") ;
        }                                        // create a new chatroom
      chatRoomRepository.save(new_chatRoom);
      return ResponseEntity.ok().body("chat room created successfully");
}

    @GetMapping("/api/chatroom") //
    public List<ChatRoom> get_allrooms() {return chatRoomRepository.findAll();}

    @GetMapping("/api/chatroom/user/{userId}") //TO GET ALL ROOMS WHITH USER ID
public List<ChatRoom> getRoomsForUser(@PathVariable Long userId){return chatRoomRepository.findRoomByUserId(userId);}


    @Autowired
    private MessageRepository messageRepository;

    @PutMapping("/api/chatroom/{id}/delete")                           //normal delete
    public ResponseEntity<?> delete_chatroom(@PathVariable Long id) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(id);
        if (chatRoom.isPresent()) {
            ChatRoom room = chatRoom.get(); // but this part tells that dont remove the  room unless there are active users
            if (room.getUsers().isEmpty()) {
                List<Message> messagesInRoom = messageRepository.findAllMessagesInRoom(id);
                messageRepository.deleteAll(messagesInRoom);
                chatRoomRepository.deleteById(id);
                return ResponseEntity.ok().body("CHAT ROOM DELETED");
            }
            return ResponseEntity.status(404).body("Cannot Delete CHATROOM ----> USER STILL USING THIS CHATROOM");
        }
        return ResponseEntity.status(403).body("CHAT ROOM NOT FOUND");
    }


    @PutMapping("/api/chatroom/{id}")
    public ResponseEntity<?> update_chatroom(@PathVariable Long id, @RequestBody ChatRoom new_chatRoom){
        Optional<ChatRoom> old_chatroom = chatRoomRepository.findById(id);
        if(old_chatroom.isPresent()){                          // change the room name
            ChatRoom chatroom = old_chatroom.get();
            if(new_chatRoom.getChatRoomName()!=null){ chatroom.setChatRoomName(new_chatRoom.getChatRoomName());
                chatRoomRepository.save(chatroom);
                return ResponseEntity.ok().body("room name changed to--->"+ chatroom.getChatRoomName());
            }

        }
        return ResponseEntity.status(404).body("room not found") ;
    }





}
