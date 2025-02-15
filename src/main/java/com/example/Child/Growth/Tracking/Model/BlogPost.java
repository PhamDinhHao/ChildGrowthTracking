package com.example.Child.Growth.Tracking.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "blog_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }

    @ManyToOne
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private User author;
}

