package com.RACTEC.CHATAPI.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     String  chatRoomName;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonIgnore   // there was a bug which was infinitely printing
    // when jackson calls chatroom it call user which call chaatroom ---------
    @ManyToMany(mappedBy = "chatRooms")
    private List<Users> users = new ArrayList<>();




}
