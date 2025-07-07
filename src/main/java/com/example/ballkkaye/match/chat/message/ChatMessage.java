package com.example.ballkkaye.match.chat.message;

import com.example.ballkkaye.common.enums.ChatConnectedType;
import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.match.chat.room.ChatRoom;
import com.example.ballkkaye.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Table(name = "chat_message_tb")
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @Column
    @Enumerated(EnumType.STRING)
    private ChatConnectedType messageType;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeleteStatus deleteStatus;

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public ChatMessage(ChatRoom chatRoom, User user, String content, ChatConnectedType messageType) {
        this.chatRoom = chatRoom;
        this.user = user;
        this.content = content;
        this.messageType = messageType;
        this.deleteStatus = DeleteStatus.NOT_DELETED;
    }
}