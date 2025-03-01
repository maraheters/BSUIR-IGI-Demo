package com.sparkplug.sparkplugbackend.posting.mapper;

import com.sparkplug.sparkplugbackend.posting.model.Engine;
import com.sparkplug.sparkplugbackend.posting.dto.request.EngineRequestDto;
import com.sparkplug.sparkplugbackend.posting.dto.response.EngineResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EngineMapper {
    EngineResponseDto entityToResponseDto(Engine e);
    Engine requestDtoToEntity(EngineRequestDto e);
}
