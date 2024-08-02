package az.rsl.chatapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDto {
    private Long id;
    private String content;
    private String sender;
    private String receiver;
    private LocalDateTime timestamp;

}
