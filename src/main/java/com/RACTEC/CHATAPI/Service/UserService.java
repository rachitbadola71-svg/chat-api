package com.RACTEC.CHATAPI.Service;

import com.RACTEC.CHATAPI.Model.ChatRoom;
import com.RACTEC.CHATAPI.Model.Users;
import com.RACTEC.CHATAPI.Repository.ChatRoomRepository;
import com.RACTEC.CHATAPI.Repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final ChatRoomRepository chatRoomRepository;

    public UserService(UsersRepository usersRepository, ChatRoomRepository chatRoomRepository) {
        this.usersRepository = usersRepository;
        this.chatRoomRepository = chatRoomRepository;
    }



//REMOVE FRIEND
    public ResponseEntity<?> removeFriend(Long userId, Long friendId) {
        Optional<Users> old_user = usersRepository.findById(userId);
        if (old_user.isPresent()) {
            Users user = old_user.get();
            Optional<Users> old_friend = usersRepository.findById(friendId);
            if (old_friend.isPresent()) {
                Users friend = old_friend.get();
                if (user.getFriends().contains(friend)) {
                    user.getFriends().remove(friend);
                    friend.getFriends().remove(user);
                    usersRepository.save(user);
                    usersRepository.save(friend);
                    log.info("{} removed {} as a friend", user.getUsername(), friend.getUsername());
                    return ResponseEntity.ok().body("friend removed successfully");
                }
                log.warn("{} attempted to remove {}, but they are not friends", user.getUsername(), friend.getUsername());
                return ResponseEntity.status(403).body("they are not friends");
            }
            return ResponseEntity.status(404).body("friend dont exist");
        }
        return ResponseEntity.status(404).body("User dont exist ");
    }


//LEAVE ROOM
    public  ResponseEntity<?> leaveRoom(Long userId, Long roomId) {
    Optional<Users> old_user = usersRepository.findById(userId);
        if (old_user.isPresent()) {
        Users user = old_user.get();
        Optional<ChatRoom> old_room = chatRoomRepository.findById(roomId);
        if (old_room.isPresent()) {
            ChatRoom room = old_room.get();
            if (user.getChatRooms().contains(room)) {
                usersRepository.save(user);
                return ResponseEntity.ok().body("Room leaved successfully");
            }
            return ResponseEntity.status(403).body("User is not this  room");
        }
        return ResponseEntity.status(404).body("Room not found");
    }
        return ResponseEntity.status(404).body("User not found");
}

//NEW USER
    public  ResponseEntity<?> createUser(Users new_user) {
     if (!usersRepository.existsByUsername(new_user.getUsername())) {    // only new username can be created
        usersRepository.save(new_user);
        return ResponseEntity.ok().body("new user is created ");
    }
        return ResponseEntity.status(403).body("username is already in use");
}

//FAKE LOGIN
    public ResponseEntity<?> login(Users login_user) {
Optional<Users> user = usersRepository.findByUsername(login_user.getUsername());

        if (user.isPresent()) {
        if (user.get().getEmail().equals(login_user.getEmail())) {
            if (user.get().getPassword().equals(login_user.getPassword())) {

                return ResponseEntity.ok().body("login succesfull ");
            }
            return ResponseEntity.status(403).body("password is wrong!!!!");
        }
        return ResponseEntity.status(403).body("wrond email renter the email ");
    }
        return ResponseEntity.status(404).body("user does not exist");
}

// GET USERs
    public ResponseEntity<?> getUser(Long userId) {
    Optional<Users> old_user = usersRepository.findById(userId);
        if (old_user.isPresent()) {
        Users user = old_user.get();
        return ResponseEntity.ok().body(user.getFriends());       // bro how many friend u got ??
    }
        return ResponseEntity.status(404).body("user dont exist");

}

// GET USERS IN THE  ROOM
    public List<Users> getAllUsers(Long roomId) {
    return usersRepository.getAllUsersInRoom(roomId);
}

//DELETE THE USER
    public void  deleteUser(Long userId) {
    usersRepository.deleteById(userId);
}


// UPDATE THE USER
public  ResponseEntity<?>  updateUser(Long userId , Users new_user_info) {
Optional<Users> old_user = usersRepository.findById(userId);

        if (old_user.isPresent()) {
        Users user = old_user.get();
// next extra  4 if because  there was bug thay when we change somehting for user  like email or password or name
        // it changes but it puts nulls in the old place if we change name other 2 things become null cuz  there was no input so json thinks its null
        if (new_user_info.getUsername() != null) user.setUsername(new_user_info.getUsername());

        if (new_user_info.getEmail() != null) user.setEmail(new_user_info.getEmail());

        if (new_user_info.getPassword() != null) user.setPassword(new_user_info.getPassword());

        if (new_user_info.getChatRooms() != null) user.setChatRooms(new_user_info.getChatRooms());
        return ResponseEntity.ok(usersRepository.save(user));
    }
        return ResponseEntity.status(404).body("user no exist");
}

//ADDFRIEND
    public  ResponseEntity<?>  addfriend(Long userId, Long friendId) {
    Optional<Users> old_user = usersRepository.findById(userId);
        if (old_user.isPresent()) {
        Users user = old_user.get();
        Optional<Users> old_friend = usersRepository.findById(friendId);
        if (old_friend.isPresent()) {
            Users friend = old_friend.get();
            if (user.getFriends().contains(friend)) {
                return ResponseEntity.status(403).body("they are already friend");
            }
            user.getFriends().add(friend);
            friend.getFriends().add(user);             // they making friends bro
            usersRepository.save(user);
            usersRepository.save(friend);

            return ResponseEntity.ok().body("friend added successfully");
        }
        return ResponseEntity.status(404).body("friend not found");
    }
        return ResponseEntity.status(404).body("User not found");
}


//JOIN ROOM
public ResponseEntity<?>  joinroom(Long roomId, Long userId) {
    Optional<Users> old_user = usersRepository.findById(userId);
        if (old_user.isPresent()) {
        Users user = old_user.get();               // user jjoin the room using id

        Optional<ChatRoom> old_room = chatRoomRepository.findById(roomId);
        if (old_room.isPresent()) {
            ChatRoom room = old_room.get();
            if (user.getChatRooms().contains(room)) {
                return ResponseEntity.status(403).body("User already in this room");
            }
            user.getChatRooms().add(room);
            usersRepository.save(user);
            return ResponseEntity.ok().body("User joined room successfully");
        }
        return ResponseEntity.status(404).body("Room not found");
    }
        return ResponseEntity.status(404).body("User not found");
}
    //GET all  USER  in server
    public List <Users> getAllUsersinserver() {
        return usersRepository.findAll();
    }

























        }

