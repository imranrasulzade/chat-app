package az.rsl.chatapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long id;
    private String content;
    private UserDto sender;
    private UserDto receiver;
    private LocalDateTime timestamp;

}
