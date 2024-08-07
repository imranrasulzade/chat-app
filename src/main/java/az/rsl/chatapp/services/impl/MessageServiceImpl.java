package az.rsl.chatapp.services.impl;

import az.rsl.chatapp.dto.ChatUsersDto;
import az.rsl.chatapp.dto.MessageDto;
import az.rsl.chatapp.entities.Message;
import az.rsl.chatapp.payloads.MessagePayload;
import az.rsl.chatapp.repositories.MessageRepository;
import az.rsl.chatapp.repositories.UserRepository;
import az.rsl.chatapp.services.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final ModelMapper modelMapper;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public MessageDto send(MessagePayload payload) {
        log.info("message send method started by: {}", payload.getSender().getId());
        Message message = modelMapper.map(payload, Message.class);
        message.setStatus(true);
        Message savedMessage = messageRepository.save(message);
        MessageDto messageDto = modelMapper.map(savedMessage, MessageDto.class);
        log.info("message sent by: {}", messageDto.getSender());
        return messageDto;
    }

    @Override
    public List<MessageDto> getByReceiver(Long receiver) {
        log.info("message getByReceiver started: {}", receiver);
        List<Message> messageList = messageRepository.findByReceiver_Id(receiver);
        List<MessageDto> messageDtoList = Arrays.asList(modelMapper.map(messageList, MessageDto[].class));
        log.info("message getByReceiver done: {}", receiver);
        return messageDtoList;
    }

    @Override
    @Transactional
    public List<MessageDto> getBySenderAndReceiverChat(ChatUsersDto dto) {
        log.info("message getBySenderAndReceiverChat started: {}, {}", dto.getSenderId(), dto.getReceiverId());
        userRepository.findById(dto.getReceiverId()).orElseThrow(() -> new RuntimeException("Receiver not found"));
        userRepository.findById(dto.getSenderId()).orElseThrow(() -> new RuntimeException("Sender not found"));
        Long receiverId = dto.getReceiverId();
        Long senderId = dto.getSenderId();
        List<Message> messageList = messageRepository
                .findMessagesByStatusTrueAndSenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(senderId,
                        receiverId,
                        senderId,
                        receiverId);
        List<MessageDto> messageDtoList = Arrays.asList(modelMapper.map(messageList, MessageDto[].class));
        log.info("message getBySenderAndReceiverChat done: {}, {}", dto.getSenderId(), dto.getReceiverId());

        return messageDtoList;
    }
}
