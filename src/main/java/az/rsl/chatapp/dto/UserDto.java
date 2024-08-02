package az.rsl.chatapp.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserDto {
    private Long id;
    private String userName;
    private Date createdAt;
    private Date updatedAt;
    private String role;
}
