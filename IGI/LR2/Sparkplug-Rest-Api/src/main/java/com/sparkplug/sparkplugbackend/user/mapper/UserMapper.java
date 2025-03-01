package com.sparkplug.sparkplugbackend.user.mapper;

import com.sparkplug.sparkplugbackend.posting.mapper.PostingMapper;
import com.sparkplug.sparkplugbackend.posting.model.Posting;
import com.sparkplug.sparkplugbackend.user.model.entity.SparkplugUser;
import com.sparkplug.sparkplugbackend.user.model.responseDto.UserResponseDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = PostingMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    @Mapping(target = "postingIds", expression = "java(mapPostingIds(u.getPostings()))")
    @Mapping(target = "profilePicture", source = "profilePictureUrl")
    UserResponseDto entityToResponseDto(SparkplugUser u);

    default List<UUID> mapPostingIds(List<Posting> postings) {
        return postings.stream()
                .map(Posting::getId)
                .collect(Collectors.toList());
    }
}
