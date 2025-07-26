package com.carlosarroyoam.service.kafka.messages.dto;

import com.carlosarroyoam.service.kafka.messages.entity.Message;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Data
@Builder
public class MessageDto {
  private Long id;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
  public interface MessageDtoMapper {
    MessageDtoMapper INSTANCE = Mappers.getMapper(MessageDtoMapper.class);

    MessageDto toDto(Message entity);

    List<MessageDto> toDtos(List<Message> entities);

    Message createRequestToEntity(CreateMessageRequestDto requestDto);
  }
}
