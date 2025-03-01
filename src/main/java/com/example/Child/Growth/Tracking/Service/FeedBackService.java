package com.example.Child.Growth.Tracking.Service;

import com.example.Child.Growth.Tracking.Model.FeedBack;
import com.example.Child.Growth.Tracking.Repository.FeedBackRepository;

import java.util.List;

import org.springframework.stereotype.Service;


@Service
public class FeedBackService {
    private final FeedBackRepository feedbackRepository;

    public FeedBackService(FeedBackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<FeedBack> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public void deleteFeedbackById(Long id) {
        feedbackRepository.deleteById(id);
    }
}
