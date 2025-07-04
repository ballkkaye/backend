package com.example.ballkkaye.match.chat.room.user;

import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.match.chat.room.ChatRoom;
import com.example.ballkkaye.match.chat.room.ChatRoomRepository;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatRoomUserService {
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public void save(Integer chatRoomId, User sessionUser) {
        // 유저 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        // 채팅방 조회
        ChatRoom chatRoomPS = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        // 채팅방 참여 여부 조회
        chatRoomUserRepository.findByUserIdAndChatRoomId(sessionUser.getId(), chatRoomId)
                .ifPresent(cru -> {
                    throw new RuntimeException("이미 참여 중인 채팅방입니다.");
                });

        // 조건 필터링

        // 1. 같은 팀이면 좋겠어요 조건 파싱
        if (chatRoomPS.getIsSameTeam() && !sessionUser.getTeam().getId().equals(chatRoomPS.getTeam().getId())) {
            throw new RuntimeException("해당 채팅방에 참여할 수 없습니다.");
        }

        // 선호 지역으로 파싱
        chatRoomPS.getPreferredGender();
        chatRoomPS.getPreferredAge();
        chatRoomPS.getMaxParticipants();


        ChatRoomUser chatRoomUser = new ChatRoomUser()
                .builder()
                .chatRoom(chatRoomPS)
                .user(userPS)
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();

        chatRoomUserRepository.save(chatRoomUser);
    }
}
