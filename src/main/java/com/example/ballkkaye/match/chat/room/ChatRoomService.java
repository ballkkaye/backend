package com.example.ballkkaye.match.chat.room;

import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.match.Match;
import com.example.ballkkaye.match.MatchRepository;
import com.example.ballkkaye.match.chat.message.ChatMessage;
import com.example.ballkkaye.match.chat.message.ChatMessageRepository;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUser;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUserRepository;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUserResponse;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public Object delete(Integer chatRoomId, User sessionUser) {
        ChatRoom chatRoomPS = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));
        ChatRoomUser chatRoomUserPS = chatRoomUserRepository.findByUserIdAndChatRoomId(sessionUser.getId(), chatRoomId).orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        if (!chatRoomUserPS.getIsOwner()) throw new RuntimeException("해당 자원에 대한 권한이 없습니다.");

        Match matchPS = matchRepository.findByChatRoomId(chatRoomId)
                .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        // 채팅방 유저 목록 조회
        List<ChatRoomUser> chatRoomUsersPS = chatRoomUserRepository.findByChatRoomIdAndDeleteStatus(matchPS.getChatRoom().getId());


        matchPS.delete();
        chatRoomPS.delete();
        for (ChatRoomUser chatRoomUser : chatRoomUsersPS) {
            chatRoomUser.delete();
        }

        return new ChatRoomResponse.DeleteDTO(DeleteStatus.DELETED);
    }

    @Transactional
    public void handleUserLeft(User sessionUser, Integer chatRoomId) {
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저 없음"));
        ChatRoom chatRoomPS = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));
        ChatRoomUser chatRoomUserPS = chatRoomUserRepository.findByUserIdAndChatRoomId(userPS.getId(), chatRoomId).orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        chatRoomUserPS.updateLastDisconnectedAt();
    }


    public Object chatrooms(User sessionUser) {
        PrettyTime p = new PrettyTime(Locale.KOREAN);
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        List<ChatRoomResponse.ItemDTO> respDTO = new ArrayList<>();
        List<ChatRoomUser> userChatRooms = chatRoomUserRepository.findByUserId(userPS.getId());

        for (ChatRoomUser chatRoomUser : userChatRooms) {
            List<ChatRoomUserResponse.ProfileImgDTO> profileImages = new ArrayList<>();
            Boolean isOwner = chatRoomUser.getIsOwner();

            // 채팅 참여중인 유저들 조회
            List<ChatRoomUser> chatRoomParticipants = chatRoomUserRepository.findByChatRoomIdAndDeleteStatus(chatRoomUser.getChatRoom().getId());

            // 채팅 참여 중인 유저들 프로플 주워담기
            for (ChatRoomUser participant : chatRoomParticipants) {
                ChatRoomUserResponse.ProfileImgDTO profileImgDTO = new ChatRoomUserResponse.ProfileImgDTO(participant.getUser());
                profileImages.add(profileImgDTO);
            }


            String latestMessageTime = "";
            String latestMessageContent = "";

            ChatMessage chatMessagePS = chatMessageRepository.findLatestByChatRoomIdAndDeleteStatus(chatRoomUser.getChatRoom().getId());
            ChatRoom chatRoom = chatRoomUser.getChatRoom();
            Match matchPS = matchRepository.findByChatRoomId(chatRoom.getId())
                    .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));
            if (chatMessagePS != null) {
                latestMessageTime = p.format(new Date(chatMessagePS.getCreatedAt().getTime()));
                latestMessageContent = chatMessagePS.getContent();
            }
            ChatRoomResponse.ItemDTO dto = new ChatRoomResponse.ItemDTO(chatRoom.getId(), matchPS.getTitle(), latestMessageTime, latestMessageContent, profileImages, isOwner);
            respDTO.add(dto);
        }
        return respDTO;
    }
}
