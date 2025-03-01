package com.sparkplug.sparkplugbackend.posting.mapper;

import com.sparkplug.sparkplugbackend.posting.model.Manufacturer;
import com.sparkplug.sparkplugbackend.posting.dto.request.ManufacturerRequestDto;
import com.sparkplug.sparkplugbackend.posting.dto.response.ManufacturerResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ManufacturerMapper {
    ManufacturerResponseDto entityToResponseDto(Manufacturer m);
    Manufacturer requestDtoToEntity(ManufacturerRequestDto m);
}
