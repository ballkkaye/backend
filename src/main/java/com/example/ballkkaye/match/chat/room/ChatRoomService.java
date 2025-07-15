package com.example.ballkkaye.match.chat.room;

import com.example.ballkkaye._core.error.ex.ExceptionApi403;
import com.example.ballkkaye._core.error.ex.ExceptionApi404;
import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.match.Match;
import com.example.ballkkaye.match.MatchRepository;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUser;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUserRepository;
import com.example.ballkkaye.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final MatchRepository matchRepository;

    @Transactional
    public Object delete(Integer chatRoomId, User sessionUser) {
        ChatRoom chatRoomPS = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));
        ChatRoomUser chatRoomUserPS = chatRoomUserRepository.findByUserIdAndChatRoomId(sessionUser.getId(), chatRoomId).orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        if (!chatRoomUserPS.getIsOwner()) throw new ExceptionApi403("해당 자원에 대한 권한이 없습니다.");

        Match matchPS = matchRepository.findByChatRoomId(chatRoomId)
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));

        // 채팅방 유저 목록 조회
        List<ChatRoomUser> chatRoomUsersPS = chatRoomUserRepository.findByChatRoomIdAndDeleteStatus(matchPS.getChatRoom().getId());


        matchPS.delete();
        chatRoomPS.delete();
        for (ChatRoomUser chatRoomUser : chatRoomUsersPS) {
            chatRoomUser.delete();
        }

        return new ChatRoomResponse.DeleteDTO(DeleteStatus.DELETED);
    }
}
