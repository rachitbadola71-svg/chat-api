package com.RACTEC.CHATAPI.Controller;


import com.RACTEC.CHATAPI.Model.ChatRoom;
import com.RACTEC.CHATAPI.Model.Users;
import com.RACTEC.CHATAPI.Repository.ChatRoomRepository;
import com.RACTEC.CHATAPI.Repository.MessageRepository;
import com.RACTEC.CHATAPI.Repository.UsersRepository;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.el.lang.ELArithmetic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.RACTEC.CHATAPI.Service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.prefs.AbstractPreferences;

@Slf4j
@RestController
public class UserController {


    private final UserService userService;
    private final UsersRepository usersRepository;
    private final ChatRoomRepository chatRoomRepository;

    public UserController(UserService userService , MessageRepository messageRepository, UsersRepository usersRepository, ChatRoomRepository chatRoomRepository) {

        this.userService = userService;
        this.usersRepository = usersRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    @PostMapping("/api/users/new")
    public ResponseEntity<?> create_User(@RequestBody Users new_user) {
        return userService.createUser(new_user);
    }


    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody Users login_user) {
        return  userService.login(login_user);
    }


    @GetMapping("/api/users/getuser")
    public List<Users> get_Users() {
        return userService.getAllUsersinserver() ;
    }

    @GetMapping("/api/chatroom/{roomId}/users")
    public List<Users> getUsersInRoom(@PathVariable Long roomId) {                     //getg all users in the roomid
        return userService.getAllUsers(roomId);
    }

    @PutMapping("/api/users/{id}/delete")
    public void delete_User(@PathVariable Long id) {           //delete the user
        userService.deleteUser(id);
    }

    @PutMapping("/api/users/{id}/update")
    public  ResponseEntity<?> update_User(@PathVariable Long id, @RequestBody Users new_user_info) {
      return   userService.updateUser(id, new_user_info);
    }

    @PutMapping("/api/users/{userId}/join/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        return userService.joinroom(roomId, userId);
    }

    @PutMapping("/api/users/{userId}/leave/{roomId}")
    public ResponseEntity<?> leaveRoom(@PathVariable Long roomId, @PathVariable Long userId) {
    return userService.leaveRoom(userId, roomId);
    }

    @PutMapping("/api/users/{userId}/addfriend/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.addfriend(userId, friendId);
    }

    @GetMapping("/api/users/{userId}/friends")
    public ResponseEntity<?> getFriends(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/api/users/{userId}/removefriend/{friendId}")
    public ResponseEntity<?> removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.removeFriend(userId, friendId);
    }







}


