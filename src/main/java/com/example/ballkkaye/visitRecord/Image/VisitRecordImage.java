package com.example.ballkkaye.visitRecord.Image;

import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.visitRecord.VisitRecord;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private VisitRecord visitRecord;

    @Column(nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeleteStatus deleteStatus;

    @CreationTimestamp
    private Timestamp createdAt;


    @Builder
    public VisitRecordImage(Integer id, VisitRecord visitRecord, String imageUrl, DeleteStatus deleteStatus, Timestamp createdAt) {
        this.id = id;
        this.visitRecord = visitRecord;
        this.imageUrl = imageUrl;
        this.deleteStatus = deleteStatus;
        this.createdAt = createdAt;
    }

//    public void updateVisitRecordId(Integer newId) {
//        this.visitRecordId = newId;
//    }

    public void delete() {
        this.deleteStatus = DeleteStatus.DELETED;
    }
}