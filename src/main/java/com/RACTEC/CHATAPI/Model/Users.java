package com.RACTEC.CHATAPI.Model;


import com.RACTEC.CHATAPI.Repository.UsersRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)   // this part is for json so it never return password to the client
    private String password;

    @CreationTimestamp                               // for a new column with creation date and time
    private LocalDateTime creationDate;

    @ManyToMany
    @JoinTable( name = "user_chatroom",     // new table name
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="room_id")

    )
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_friends" ,
            joinColumns = @JoinColumn(name = "user_id_1"),
            inverseJoinColumns = @JoinColumn(name = "user_id_2")
    )
    @JsonIgnoreProperties("friends")
    private List<Users> friends = new ArrayList<>();






















}

