package com.sparkplug.sparkplugbackend.posting.mapper;

import com.sparkplug.sparkplugbackend.posting.model.Posting;
import com.sparkplug.sparkplugbackend.posting.dto.response.PostingResponseDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {CarMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PostingMapper {

    @Mapping(target = "images", source = "images.urls")
    @Mapping(target = "creator", source = "creator.username")
    @Mapping(target = "creatorId", source = "creator.id")
    PostingResponseDto entityToResponseDto(Posting p);
}
