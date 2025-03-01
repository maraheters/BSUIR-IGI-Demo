package com.sparkplug.sparkplugbackend.messaging.mapper;

import com.sparkplug.sparkplugbackend.messaging.model.Message;
import com.sparkplug.sparkplugbackend.messaging.dto.request.MessageRequestDto;
import com.sparkplug.sparkplugbackend.messaging.dto.response.MessageResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "senderUsername", source = "sender.username")
    MessageResponseDto entityToResponseDto(Message m);

    Message requestDtoToEntity(MessageRequestDto m);
}
