package com.RACTEC.CHATAPI.Controller;

import com.RACTEC.CHATAPI.Model.Message;
import com.RACTEC.CHATAPI.Service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/api/message/send/{userId}/{roomId}")
    public ResponseEntity<?> create_Message(@PathVariable Long userId, @PathVariable Long roomId, @RequestBody Message message) {
        return messageService.createMessage(userId, roomId, message);
    }

    @GetMapping("/api/message/alldeletedmessages/{roomid}")
    public List<Message> getAllDeletedMessagesInRoom(@PathVariable Long roomid) {
        return messageService.getAllDeletedMessagesInRoom(roomid);
    }

    @GetMapping("/api/message/{roomid}/search")
    public ResponseEntity<?> searchMessageByKeyword(@PathVariable Long roomid, @RequestParam String keyword) {
        return messageService.searchMessageByKeyword(roomid, keyword);
    }

    @GetMapping("/api/message/{roomid}/count")
    public ResponseEntity<?> getMessageCount(@PathVariable Long roomid) {
        return messageService.getMessageCount(roomid);
    }

    @GetMapping("/api/message/{senderid}/sender/count")
    public ResponseEntity<?> getMessageCountBySender(@PathVariable Long senderid) {
        return messageService.getMessageCountBySender(senderid);
    }

    @PutMapping("/api/message/{id}/delete")
    public ResponseEntity<?> delete_Message(@PathVariable Long id) {
        return messageService.deleteMessage(id);
    }

    @PutMapping("/api/message/{id}/edit")
    public ResponseEntity<?> update_Message(@PathVariable Long id, @RequestBody Message new_message) {
        return messageService.updateMessage(id, new_message);
    }

    @PutMapping("/api/message/{messageId}/pin")
    public ResponseEntity<?> pinMessage(@PathVariable Long messageId) {
        return messageService.pinMessage(messageId);
    }
}