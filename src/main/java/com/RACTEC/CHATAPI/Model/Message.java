package com.RACTEC.CHATAPI.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
public class Message {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    private Long id;


    private String content ;

    private int  editCount = 0 ;

    @Enumerated(EnumType.STRING)
    private MessageStatus status = MessageStatus.ACTIVE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users sender;

    @ManyToOne
    @JoinColumn(name="room_id")
    private ChatRoom room;





}


