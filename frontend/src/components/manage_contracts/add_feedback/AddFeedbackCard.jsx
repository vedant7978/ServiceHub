import { HttpStatusCode } from "axios";
import React, { useEffect, useState } from "react";
import { Button, Container, Stack } from "react-bootstrap";
import "./AddFeedbackCard.css";
import Rating from "react-rating";
import EmptyStar from "../../../assets/IconStarEmpty.png"
import FilledStar from "../../../assets/IconStarFilled.png"
import { useAxios } from "../../../context/AxiosContext";
import { ENDPOINTS } from "../../../utils/Constants";
import AppToast from "../../app_toast/AppToast";

export const AddFeedbackCard = ({ contract }) => {
  const [userRating, setUserRating] = useState(0.0);
  const [feedbackDescription, setFeedbackDescription] = useState("");
  const [isSubmitButtonDisabled, setIsSubmitButtonDisabled] = useState(contract.status === "Pending")
  const [initialFeedback, setInitialFeedback] = useState({});
  const [hasFeedbackChanged, setHasFeedbackChanged] = useState(false);
  const [showToast, setShowToast] = useState(false);
  const [toastTitle, setToastTitle] = useState("");
  const [toastMessage, setToastMessage] = useState("");
  const { getRequest, postRequest } = useAxios();

  useEffect(() => {
    loadFeedback()
  }, []);

  useEffect(() => {
    setUserRating(initialFeedback.rating);
    setFeedbackDescription(initialFeedback.description);
  }, [initialFeedback]);

  useEffect(() => {
    setHasFeedbackChanged(feedbackDescription !== initialFeedback.description || userRating !== initialFeedback.rating)
  }, [userRating], [feedbackDescription]);

  const loadFeedback = async () => {
    try {
      const response = await getRequest(ENDPOINTS.GET_CONTRACT_FEEDBACK, true, { contractId: contract.id });
      if (response.status === HttpStatusCode.Ok) {
        setInitialFeedback(response.data.data);
      }
    } catch (error) {
      console.log("Failed to load contract feedback", error);
    }
  }

  const handleRatingChange = (value) => {
    setUserRating(value);
  }

  const handleDescriptionChange = (e) => {
    setFeedbackDescription(e.target.value);
    if (feedbackDescription.length > 0 && contract.status !== "Pending" && isSubmitButtonDisabled) {
      setIsSubmitButtonDisabled(false);
    }
  }

  const handleSubmitFeedback = async (e) => {
    e.preventDefault()
    if (!hasFeedbackChanged) {
      setToastTitle("Redundant")
      setToastMessage("Please update feedback before submitting")
      setShowToast(true)
      return
    }
    const feedback = { contractId: contract.id, rating: userRating, description: feedbackDescription };
    try {
      const response = await postRequest(ENDPOINTS.ADD_CONTRACT_FEEDBACK, true, feedback);
      if (response.status === HttpStatusCode.Ok) {
        setToastTitle("Success")
        setToastMessage("Successfully submitted the feedback.")
        setShowToast(true)
      }
    } catch (error) {
      console.log("Failed to load contract feedback", error);
    }
  }

  return (
    <Container fluid className="p-4">
      <Stack gap={4}>
        <div>
          <Stack gap={3}>
            <div className="add-feedback-title">Feedback</div>

            <div>
              <Stack gap={1}>
                <div className="add-feedback-username">{contract.userName}</div>
                <div className="add-feedback-rating">{userRating}/5.0</div>
                <Rating
                  start={0}
                  initialRating={userRating}
                  fractions={4}
                  emptySymbol={<EmptySymbol/>}
                  fullSymbol={<FilledSymbol/>}
                  onChange={handleRatingChange}
                />
              </Stack>
            </div>

            <textarea
              className="add-feedback-description p-3 mt-2"
              value={feedbackDescription}
              onChange={handleDescriptionChange}
              placeholder="Describe your experience (Optional)"
            />
          </Stack>
        </div>

        <Button
          disabled={isSubmitButtonDisabled}
          className="add-feedback-submit-button mt-5"
          onClick={handleSubmitFeedback}>
          Submit
        </Button>
      </Stack>
      <AppToast show={showToast} setShow={setShowToast} title={toastTitle} message={toastMessage}/>
    </Container>
  );
}

const EmptySymbol = () => {
  return (<img
    src={EmptyStar}
    alt="Feecback unfilled star"
    style={{ width: "40px", height: "40px" }}
  />);
}

const FilledSymbol = () => {
  return (<img
    src={FilledStar}
    alt="Feecback filled star"
    style={{ width: "40px", height: "40px" }}
  />);
}