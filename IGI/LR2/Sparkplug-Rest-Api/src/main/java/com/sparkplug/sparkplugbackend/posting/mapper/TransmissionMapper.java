package com.sparkplug.sparkplugbackend.posting.mapper;

import com.sparkplug.sparkplugbackend.posting.model.Transmission;
import com.sparkplug.sparkplugbackend.posting.dto.request.TransmissionRequestDto;
import com.sparkplug.sparkplugbackend.posting.dto.response.TransmissionResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransmissionMapper {
    TransmissionResponseDto entityToResponseDto(Transmission t);
    Transmission requestDtoToEntity(TransmissionRequestDto t);
}
