package com.dalhousie.servicehub.service.feedback;

import com.dalhousie.servicehub.dto.FeedbackDto;
import com.dalhousie.servicehub.exceptions.FeedbackNotFoundException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.FeedbackModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.FeedbackRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.request.AddFeedbackRequest;
import com.dalhousie.servicehub.response.GetFeedbackResponse;
import com.dalhousie.servicehub.util.ResponseBody;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.SUCCESS;

@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseBody<String> addFeedback(AddFeedbackRequest addFeedbackRequest) {
        long providerId = addFeedbackRequest.getProviderId();
        long consumerId = addFeedbackRequest.getConsumerId();
        UserModel provider = userRepository.findById(providerId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + providerId));
        UserModel consumer = userRepository.findById(consumerId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + consumerId));

        FeedbackModel feedbackModel = FeedbackModel.builder()
                .consumer(consumer)
                .provider(provider)
                .rating(addFeedbackRequest.getRating())
                .description(addFeedbackRequest.getDescription())
                .build();
        feedbackRepository.save(feedbackModel);
        return new ResponseBody<>(SUCCESS, "", "Add feedback successful");
    }

    @Override
    public ResponseBody<GetFeedbackResponse> getFeedbacks(long userId) {
        if (!userRepository.existsById(userId))
            throw new UserNotFoundException("User not found for id: " + userId);

        List<FeedbackDto> feedbacks = feedbackRepository
                .findAllByConsumerId(userId)
                .orElse(List.of())
                .stream()
                .map(this::toFeedbackDto)
                .toList();
        GetFeedbackResponse getFeedbackResponse = GetFeedbackResponse.builder()
                .feedbacks(feedbacks)
                .build();
        return new ResponseBody<>(SUCCESS, getFeedbackResponse, "Get feedbacks successful");
    }

    @Override
    public Double getAverageRatingForUser(Long userId) {
        List<FeedbackModel> feedbackModels = feedbackRepository.findAllByConsumerId(userId).orElse(List.of());
        if (feedbackModels.isEmpty())
            return 0.0;
        double sumRatings = feedbackModels.stream()
                .mapToDouble(FeedbackModel::getRating)
                .sum();
        return sumRatings / feedbackModels.size();
    }

    @Override
    public FeedbackModel addFeedbackModel(FeedbackModel feedbackModel) {
        return feedbackRepository.save(feedbackModel);
    }

    @Override
    public FeedbackModel updateFeedbackModel(long id, double rating, String description) {
        FeedbackModel feedbackModel = feedbackRepository.findById(id).orElseThrow(
                () -> new FeedbackNotFoundException("Cannot find feedback with id: " + id)
        );
        feedbackModel.setRating(rating);
        feedbackModel.setDescription(description);
        return feedbackRepository.save(feedbackModel);
    }

    /**
     * Converts the FeedbackModel into FeedbackDto
     * @param feedbackModel FeedbackModel to be converted
     * @return FeedbackDto instance
     */
    private FeedbackDto toFeedbackDto(FeedbackModel feedbackModel) {
        return FeedbackDto.builder()
                .providerName(feedbackModel.getProvider().getName())
                .rating(feedbackModel.getRating())
                .description(feedbackModel.getDescription())
                .type(feedbackModel.getType())
                .build();
    }
}
