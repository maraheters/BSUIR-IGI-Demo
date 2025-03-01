package com.sparkplug.sparkplugbackend.posting.mapper;

import com.sparkplug.sparkplugbackend.posting.model.ImageInfo;
import com.sparkplug.sparkplugbackend.posting.dto.request.ImageInfoRequestDto;
import com.sparkplug.sparkplugbackend.posting.dto.response.ImageInfoResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageInfoMapper {
    ImageInfoResponseDto entityToResponseDto(ImageInfo i);
    ImageInfo requestDtoToEntity(ImageInfoRequestDto i);
}
