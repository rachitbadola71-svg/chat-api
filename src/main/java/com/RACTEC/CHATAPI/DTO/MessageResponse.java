package com.RACTEC.CHATAPI.DTO;
import com.RACTEC.CHATAPI.Model.MessageStatus;
import java.time.LocalDateTime;
public record MessageResponse(Long id,
                              String content,
                              MessageStatus status,
                              LocalDateTime createdAt,
                              int editCount,
                              Long senderId,
                              String senderUsername) {
}
