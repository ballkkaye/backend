package com.example.ballkkaye.visitRecord.Image;

import com.example.ballkkaye.common.enums.DeleteStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Table(name = "visit_record_image_tb")
@Entity
public class VisitRecordImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer visitRecordId;

    @Column(nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeleteStatus deleteStatus;

    @CreationTimestamp
    private Timestamp createdAt;


    @Builder

    public VisitRecordImage(Integer id, Integer visitRecordId, String imageUrl, DeleteStatus deleteStatus, Timestamp createdAt) {
        this.id = id;
        this.visitRecordId = visitRecordId;
        this.imageUrl = imageUrl;
        this.deleteStatus = deleteStatus;
        this.createdAt = createdAt;
    }
}