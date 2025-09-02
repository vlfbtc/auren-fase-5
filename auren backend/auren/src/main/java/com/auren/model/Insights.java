package com.auren.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "insights")
public class Insights {

    public enum Priority { HIGH, MEDIUM, LOW }
    public enum ContentType { TIP, RECOMMENDATION, ARTICLE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    private User user;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false, length=2000)
    private String description;

    @Column(nullable=false)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private ContentType contentType;

    private String articleId;

    @Column(nullable=false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public Priority getPriority() { return priority; }
    public ContentType getContentType() { return contentType; }
    public String getArticleId() { return articleId; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setContentType(ContentType contentType) { this.contentType = contentType; }
    public void setArticleId(String articleId) { this.articleId = articleId; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
