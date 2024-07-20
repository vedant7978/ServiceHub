package com.dalhousie.servicehub.mapper;

import com.dalhousie.servicehub.dto.FeedbackDto;
import com.dalhousie.servicehub.model.FeedbackModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedbackMapper implements Mapper<FeedbackModel, FeedbackDto> {

    private final ModelMapper modelMapper;

    @Override
    public FeedbackDto toDto(FeedbackModel feedbackModel) {
        return modelMapper.map(feedbackModel, FeedbackDto.class);
    }

    @Override
    public FeedbackModel toEntity(FeedbackDto feedbackDto) {
        return modelMapper.map(feedbackDto, FeedbackModel.class);
    }
}
