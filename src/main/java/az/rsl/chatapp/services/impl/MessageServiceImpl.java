package az.rsl.chatapp.services.impl;

import az.rsl.chatapp.dto.MessageDto;
import az.rsl.chatapp.entities.Message;
import az.rsl.chatapp.payloads.MessagePayload;
import az.rsl.chatapp.repositories.MessageRepository;
import az.rsl.chatapp.services.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final ModelMapper modelMapper;
    private final MessageRepository messageRepository;

    @Override
    public MessageDto send(MessagePayload payload) {
        log.info("message send method started by: {}", payload.getSender());
        Message message = modelMapper.map(payload, Message.class);
        Message savedMessage = messageRepository.save(message);
        MessageDto messageDto = modelMapper.map(savedMessage, MessageDto.class);
        log.info("message sent by: {}", messageDto.getSender());
        return messageDto;
    }

    @Override
    public List<MessageDto> getByReceiver(String receiver) {
        log.info("message getByReceiver started: {}", receiver);
        List<Message> messageList = messageRepository.findByReceiver(receiver);
        List<MessageDto> messageDtoList = Arrays.asList(modelMapper.map(messageList, MessageDto[].class));
        log.info("message getByReceiver done: {}", receiver);
        return messageDtoList;
    }
}
