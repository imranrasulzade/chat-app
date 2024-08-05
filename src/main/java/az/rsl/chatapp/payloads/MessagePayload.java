package az.rsl.chatapp.payloads;

import az.rsl.chatapp.dto.UserDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessagePayload {
    private String content;
    private UserDto sender;
    private UserDto receiver;
    private LocalDateTime timestamp;
}
