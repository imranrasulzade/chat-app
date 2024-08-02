package az.rsl.chatapp.payloads;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessagePayload {
    private String content;
    private String sender;
    private String receiver;
    private LocalDateTime timestamp;
}
