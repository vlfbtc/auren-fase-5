package com.auren.repository;

import com.auren.model.Insights;
import com.auren.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsightRepository extends JpaRepository<Insights, Long> {
    List<Insights> findTop10ByUserOrderByCreatedAtDesc(User user);
}
