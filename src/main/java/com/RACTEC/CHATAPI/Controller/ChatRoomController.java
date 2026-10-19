package com.RACTEC.CHATAPI.Controller;

import com.RACTEC.CHATAPI.Model.ChatRoom;
import com.RACTEC.CHATAPI.Service.ChatRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
public class ChatRoomController {


    private final  ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    // CREATE A NEW CHATROOM
    @PostMapping("/api/chatroom")
   public ResponseEntity<?> createChatRoom(@RequestBody ChatRoom chatRoom) {
        return chatRoomService.createRoom(chatRoom);}


    // SEE CHATROOMS
    @GetMapping("/api/chatroom") //
    public List<ChatRoom> allrooms() {return chatRoomService.allRooms() ;}


    //USER JOINED WHCH ROOMS
    @GetMapping("/api/chatroom/user/{userId}")
    public List<ChatRoom> getRoomsForUser(@PathVariable Long userId){return chatRoomService.getRoomsForUser(userId);}


    //normal delete chatroom
    @PutMapping("/api/chatroom/{id}/delete")
    public ResponseEntity<?> delete_chatroom(@PathVariable Long id) {
     return  chatRoomService.deleteChatroom(id) ;}


    // update the room name
    @PutMapping("/api/chatroom/{id}")
    public ResponseEntity<?> update_chatroom(@PathVariable Long id, @RequestBody ChatRoom new_chatRoom){
        return chatRoomService.updateChatroom(id,new_chatRoom);}





}
