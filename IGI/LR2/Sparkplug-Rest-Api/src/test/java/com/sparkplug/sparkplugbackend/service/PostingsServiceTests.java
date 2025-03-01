package com.sparkplug.sparkplugbackend.service;

import com.sparkplug.sparkplugbackend.minio.service.MinioFileService;
import com.sparkplug.sparkplugbackend.posting.model.Car;
import com.sparkplug.sparkplugbackend.posting.model.Posting;
import com.sparkplug.sparkplugbackend.user.model.entity.SparkplugUser;
import com.sparkplug.sparkplugbackend.posting.repository.CarsRepository;
import com.sparkplug.sparkplugbackend.posting.repository.PostingsRepository;
import com.sparkplug.sparkplugbackend.user.repository.SparkplugUsersRepository;
import com.sparkplug.sparkplugbackend.posting.service.PostingsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostingsServiceTests {

    @InjectMocks
    private PostingsService postingsService;

    @Mock
    private PostingsRepository postingsRepository;
    @Mock
    private CarsRepository carsRepository;
    @Mock
    private SparkplugUsersRepository usersRepository;
    @Mock
    private MinioFileService fileService;

    @Test
    public void PostingsService_save_shouldReturnUUID() {
        UUID userId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        List<MultipartFile> images = Collections.singletonList(new MockMultipartFile("file", new byte[]{}));
        UUID postingId = UUID.randomUUID();

        when(carsRepository.findById(carId)).thenReturn(Optional.of(new Car()));
        when(usersRepository.findById(userId)).thenReturn(Optional.of(new SparkplugUser()));
        when(postingsRepository.save(any(Posting.class))).thenAnswer(invocation -> {
            Posting posting = invocation.getArgument(0);
            posting.setId(postingId);
            return posting;
        });
        when(fileService.uploadFiles(any(), any())).thenReturn(List.of("a", "b", "c"));

        UUID id = postingsService.save(userId, carId, "desc", BigDecimal.valueOf(22000), images);

        Assertions.assertNotNull(id);
    }
}
