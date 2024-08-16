package az.rsl.chatapp.controller;

import az.rsl.chatapp.dto.ChatUsersDto;
import az.rsl.chatapp.dto.MessageDto;
import az.rsl.chatapp.payloads.MessagePayload;
import az.rsl.chatapp.services.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    private final MessageService messageService;

//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    public MessageDto send(@Payload MessagePayload payload) {
//        payload.setTimestamp(LocalDateTime.now());
//        return messageService.send(payload);
//    }

    @MessageMapping("/chat.sendMessage")
    public void send(@Payload MessagePayload payload) {
        payload.setTimestamp(LocalDateTime.now());
        MessageDto messageDto = messageService.send(payload);

        // alana gore(xas) xususi kanala
        messagingTemplate.convertAndSendToUser(
                payload.getReceiver().getId().toString(),
                "/queue/messages",
                messageDto
        );
    }


    @GetMapping("/{receiver}")
    public List<MessageDto> get(@PathVariable Long receiver) {
        return messageService.getByReceiver(receiver);
    }

    @PostMapping
    public List<MessageDto> get(@RequestBody @Valid ChatUsersDto dto) {
        return messageService.getBySenderAndReceiverChat(dto);
    }
}
