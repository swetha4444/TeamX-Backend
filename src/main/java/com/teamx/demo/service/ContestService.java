package com.teamx.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamx.demo.model.Contest;
import com.teamx.demo.repository.ContestRepository;

@Service
public class ContestService {

    @Autowired
    private ContestRepository contestRepository;

    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }

    public Optional<Contest> getContestById(String id) {
        return contestRepository.findById(id);
    }

    public Optional<Contest> updatePrizePoolAndSpots(String id) {
        Optional<Contest> contestOpt = contestRepository.findById(id);
        if (contestOpt.isPresent()) {
            Contest contest = contestOpt.get();
            // Assuming only one data entry per contest (adjust if multiple)
            if (contest.getData() != null && !contest.getData().isEmpty()) {
                Contest.ContestData data = contest.getData().get(0);
                data.setPrizePool(data.getPrizePool() + data.getEntryFee());
                if (data.getSpotsLeft() > 0) {
                    data.setSpotsLeft(data.getSpotsLeft() - 1);
                }
                contestRepository.save(contest);
            }
            return Optional.of(contest);
        }
        return Optional.empty();
    }

    public Optional<Contest> reduceSpotsLeft(String contestId) {
        Optional<Contest> contestOpt = contestRepository.findById(contestId);
        if (contestOpt.isPresent()) {
            Contest contest = contestOpt.get();
            if (contest.getData() != null && !contest.getData().isEmpty()) {
                Contest.ContestData data = contest.getData().get(0);
                if (data.getSpotsLeft() > 0) {
                    data.setSpotsLeft(data.getSpotsLeft() - 1);
                    contestRepository.save(contest);
                }
            }
            return Optional.of(contest);
        }
        return Optional.empty();
    }
}