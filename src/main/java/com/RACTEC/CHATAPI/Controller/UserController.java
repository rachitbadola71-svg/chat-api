package com.RACTEC.CHATAPI.Controller;
import com.RACTEC.CHATAPI.DTO.RegisterRequest;
import com.RACTEC.CHATAPI.DTO.UpdateRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import com.RACTEC.CHATAPI.Model.ChatRoom;
import com.RACTEC.CHATAPI.Model.Users;
import com.RACTEC.CHATAPI.Repository.ChatRoomRepository;
import com.RACTEC.CHATAPI.Repository.MessageRepository;
import com.RACTEC.CHATAPI.Repository.UsersRepository;
import com.RACTEC.CHATAPI.DTO.UserResponse;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<?> create_User(@Valid@RequestBody RegisterRequest request) {
        return userService.createUser(request);
    }


    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody Users login_user) {
        return  userService.login(login_user);
    }


    @GetMapping("/api/users/getuser")
    public List<UserResponse> get_Users() {
        return userService.getAllUsersinserver() ;
    }

    @GetMapping("/api/chatroom/{roomId}/users")
    public List<UserResponse> getUsersInRoom(@PathVariable Long roomId) {                     //getg all users in the roomid
        return userService.getAllUsers(roomId);
    }

    @PutMapping("/api/users/delete")
    public void delete_User(@PathVariable Long id , Authentication authentication) {           //delete the user
        Long userId =   (Long) authentication.getPrincipal();
        userService.deleteUser(id);
    }

    @PutMapping("/api/users/update")
    public  ResponseEntity<?> update_User(@Valid @RequestBody UpdateRequest request, Authentication authentication ) {
            Long userId =   (Long) authentication.getPrincipal();
      return   userService.updateUser(userId, request);
    }

    @PutMapping("/api/users/join/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable Long roomId, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return userService.joinroom(roomId, userId);
    }

    @PutMapping("/api/users/leave/{roomId}")
    public ResponseEntity<?> leaveRoom(@PathVariable Long roomId,Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
    return userService.leaveRoom(userId, roomId);
    }

    @PutMapping("/api/users/addfriend/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable Long friendId , Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return userService.addfriend(userId, friendId);
    }

    @GetMapping("/api/users/friends")
    public ResponseEntity<?> getFriends(Authentication authentication) {
        Long userId =   (Long) authentication.getPrincipal();
        return userService.getUser(userId);
    }

    @PutMapping("/api/users/removefriend/{friendId}")
    public ResponseEntity<?> removeFriend( @PathVariable Long friendId , Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return userService.removeFriend(userId, friendId);
    }







}


