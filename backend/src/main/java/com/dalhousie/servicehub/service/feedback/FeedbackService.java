package com.dalhousie.servicehub.service.feedback;

import com.dalhousie.servicehub.model.FeedbackModel;
import com.dalhousie.servicehub.request.AddFeedbackRequest;
import com.dalhousie.servicehub.response.GetFeedbackResponse;
import com.dalhousie.servicehub.util.ResponseBody;

public interface FeedbackService {
    /**
     * Add feedback into the database
     * @param addFeedbackRequest Request body of feedback to add
     * @return Response body with data of type Object
     */
    ResponseBody<String> addFeedback(AddFeedbackRequest addFeedbackRequest);

    /**
     * Get all feedbacks for the requesting user id
     * @param userId ID of the user to get all feedbacks
     * @return List of feedbacks of requesting user id
     */
    ResponseBody<GetFeedbackResponse> getFeedbacks(long userId);

    /**
     * Provide average rating of the requesting user
     * @param userId ID of the user to get average rating of
     * @return Double representing average rating of the user
     */
    Double getAverageRatingForUser(Long userId);

    /**
     * Adds the feedback and provides added feedback in return
     * @param feedbackModel Model to add to database
     * @return FeedbackModel instance stored in database
     */
    FeedbackModel addFeedbackModel(FeedbackModel feedbackModel);

    /**
     * Update already added feedback
     * @param id ID of the feedback to update
     * @param rating New rating value of the feedback
     * @param description New description of the feedback
     * @return FeedbackModel instance stored in database
     */
    FeedbackModel updateFeedbackModel(long id, double rating, String description);
}
