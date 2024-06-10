package com.dalhousie.servicehub.response;

import com.dalhousie.servicehub.dto.FeedbackDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetFeedbackResponse {
    private List<FeedbackDto> feedbacks;
}
