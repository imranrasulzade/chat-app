package az.rsl.chatapp.services;

import az.rsl.chatapp.dto.MessageDto;
import az.rsl.chatapp.payloads.MessagePayload;

import java.util.List;

public interface MessageService {
    MessageDto send(MessagePayload payload);

    List<MessageDto> getByReceiver(String receiver);
}
