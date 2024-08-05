package az.rsl.chatapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatUsersDto {
    @NotBlank(message = "senderId can not be null or empty")
    private Long senderId;
    @NotBlank(message = "receiverId can not be null or empty")
    private Long receiverId;

}
