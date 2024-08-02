package az.rsl.chatapp.controller;

import az.rsl.chatapp.dto.MessageDto;
import az.rsl.chatapp.payloads.MessagePayload;
import az.rsl.chatapp.repositories.MessageRepository;
import az.rsl.chatapp.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public MessageDto send(@Payload MessagePayload payload) {
        payload.setTimestamp(LocalDateTime.now());
        return messageService.send(payload);
    }

    @GetMapping("/{receiver}")
    public List<MessageDto> get(@PathVariable String receiver) {
        return messageService.getByReceiver(receiver);
    }
}
