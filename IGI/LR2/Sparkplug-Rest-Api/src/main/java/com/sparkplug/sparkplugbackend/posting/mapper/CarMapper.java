package com.sparkplug.sparkplugbackend.posting.mapper;

import com.sparkplug.sparkplugbackend.posting.model.Car;
import com.sparkplug.sparkplugbackend.posting.model.Category;
import com.sparkplug.sparkplugbackend.posting.dto.request.CarRequestDto;
import com.sparkplug.sparkplugbackend.posting.dto.response.CarResponseDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {
                EngineMapper.class,
                ManufacturerMapper.class,
                TransmissionMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CarMapper {

    @Mapping(target = "categoryName", source = "category.name")
    CarResponseDto entityToResponseDto(Car c);

    @Mapping(target = "category", expression = "java(createCategory(c.getCategoryName()))")
    Car requestDtoToEntity(CarRequestDto c);

    default Category createCategory(String categoryName) {
        if (categoryName == null || categoryName.isEmpty()) {
            return null;
        }
        Category category = new Category();
        category.setName(categoryName);
        return category;
    }
}
