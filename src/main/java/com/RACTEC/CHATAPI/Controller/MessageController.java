package com.RACTEC.CHATAPI.Controller;

import com.RACTEC.CHATAPI.DTO.MessageResponse;
import com.RACTEC.CHATAPI.Model.Message;
import com.RACTEC.CHATAPI.Service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;   // **** NEW import
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    //SEND MESSAGE: text only, file only, or both
    @PostMapping("/api/message/send/{roomId}")
    public ResponseEntity<?> create_Message(@PathVariable Long roomId,
                                            @RequestParam(value = "content", required = false) String content,
                                            @RequestParam(value = "file", required = false) MultipartFile file,
                                            Authentication authentication) throws IOException {
        Long userId = (Long) authentication.getPrincipal();
        return messageService.createMessage(userId, roomId, content, file);
    }

    //SEND MESSAGE IN A ROOM
    /*

    //SEARCH MESSAGES BY KEYWORD
    @GetMapping("/api/message/{roomid}/search")
    public ResponseEntity<?> searchMessageByKeyword(@PathVariable Long roomid, @RequestParam String keyword) {
        return messageService.searchMessageByKeyword(roomid, keyword);
    }
*/

    //TOTAL COUNT OF MESSAGE IN A ROOM
    @GetMapping("/api/message/{roomid}/count")
    public ResponseEntity<?> getMessageCount(@PathVariable Long roomid) {
        return messageService.getMessageCount(roomid);
    }

    //TOTAL NUMBER OF MESSAGES BY A USER
    @GetMapping("/api/message/{senderid}/sender/count")
    public ResponseEntity<?> getMessageCountBySender(@PathVariable Long senderid) {
        return messageService.getMessageCountBySender(senderid);
    }

    @PutMapping("/api/message/{id}/delete")
    public ResponseEntity<?> delete_Message(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return messageService.deleteMessage(id, userId);
    }

    @PutMapping("/api/message/{id}/edit")
    public ResponseEntity<?> update_Message(@PathVariable Long id, @RequestBody Message new_message, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return messageService.updateMessage(id, new_message, userId);
    }

    @PutMapping("/api/message/{messageId}/pin")
    public ResponseEntity<?> pinMessage(@PathVariable Long messageId, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return messageService.pinMessage(messageId, userId);
    }

    //GET ALL MESSAGES IN A ROOM
    @GetMapping("/api/message/room/{roomId}")
    public ResponseEntity<?> getAllMessagesInRoom(@PathVariable Long roomId) {
        return messageService.getAllMessagesInRoom(roomId);
    }
}