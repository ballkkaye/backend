package com.example.ballkkaye.match;

import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.match.chat.room.ChatRoom;
import com.example.ballkkaye.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Table(name = "match_tb")
@Entity
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private String title;

    @Column
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeleteStatus deleteStatus;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public Match(Integer id, User user, ChatRoom chatRoom, String title, String content) {
        this.id = id;
        this.user = user;
        this.chatRoom = chatRoom;
        this.title = title;
        this.content = content;
        this.deleteStatus = DeleteStatus.NOT_DELETED;
    }

    public void update(ChatRoom chatRoomPS, String title, String content) {
        this.chatRoom = chatRoomPS == null ? this.chatRoom : chatRoomPS;
        this.title = title == null ? this.title : title;
        this.content = content == null ? this.content : content;
    }

    public void delete() {
        this.deleteStatus = DeleteStatus.DELETED;
    }
}