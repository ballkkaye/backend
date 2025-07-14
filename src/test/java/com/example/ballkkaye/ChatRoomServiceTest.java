package com.example.ballkkaye;

import com.example.ballkkaye._core.eventlistener.WebSocketEventListener;
import com.example.ballkkaye.common.enums.Age;
import com.example.ballkkaye.common.enums.ChatConnectedType;
import com.example.ballkkaye.common.enums.Gender;
import com.example.ballkkaye.match.chat.message.ChatMessageRepository;
import com.example.ballkkaye.match.chat.message.ChatMessageResponse;
import com.example.ballkkaye.match.chat.room.ChatRoom;
import com.example.ballkkaye.match.chat.room.ChatRoomRepository;
import com.example.ballkkaye.match.chat.room.ChatRoomService;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUserRepository;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUserService;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private ChatRoomUserRepository chatRoomUserRepository;
    @Mock
    private SimpMessagingTemplate messagingTemplate;
    @Mock
    private ChatMessageRepository chatMessageRepository;

    @InjectMocks
    private ChatRoomUserService chatRoomUserService;

    @Test
    void 채팅방_입장시_입장메시지가_전송된다() {
        // given
        Integer chatRoomId = 1;
        Integer userId = 2;

        User user = User.builder()
                .id(userId)
                .nickname("테스트유저")
                .birthDate(LocalDate.of(1999, 1, 1))
                .gender(Gender.MALE)
                .team(Team.builder().id(1).build())
                .build();

        ChatRoom chatRoom = ChatRoom.builder()
                .id(chatRoomId)
                .isSameTeam(false)
                .maxParticipants(5)
                .preferredGender(Gender.NONE)
                .preferredAge(Age.NONE)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));
        when(chatRoomUserRepository.findByUserIdAndChatRoomId(userId, chatRoomId)).thenReturn(Optional.empty());
        when(chatRoomUserRepository.countByChatRoomId(chatRoomId)).thenReturn(1);

        // when
        chatRoomUserService.save(chatRoomId, user);

        // then
        ArgumentCaptor<ChatMessageResponse.DTO> captor = ArgumentCaptor.forClass(ChatMessageResponse.DTO.class);
        verify(messagingTemplate).convertAndSend(eq("/sub/chat/" + chatRoomId), captor.capture());

        ChatMessageResponse.DTO dto = captor.getValue();
        assertEquals("테스트유저님이 입장하셨습니다.", dto.getMessage());
        assertEquals(userId, dto.getSenderId());
        assertEquals(ChatConnectedType.ENTER, dto.getMessageType());
    }


    @Test
    void 웹소켓_연결종료시_서비스가_호출된다() {
        // given
        ChatRoomService mockService = mock(ChatRoomService.class);
        WebSocketEventListener listener = new WebSocketEventListener(mockService);

        // 세션 속성 설정
        Map<String, Object> sessionAttributes = new HashMap<>();
        User mockUser = User.builder().id(1).build();
        sessionAttributes.put("sessionUser", mockUser);
        sessionAttributes.put("roomId", 123);

        // WebSocket 헤더 생성
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
        headerAccessor.setSessionAttributes(sessionAttributes);
        Message<byte[]> message = mock(Message.class);
        when(message.getHeaders()).thenReturn(headerAccessor.getMessageHeaders());

        // Disconnect 이벤트 생성
        String sessionId = "test-session-id";
        CloseStatus closeStatus = CloseStatus.NORMAL; // 보통 정상 종료

        SessionDisconnectEvent event = new SessionDisconnectEvent(this, message, sessionId, closeStatus);

        // when
        listener.handleDisconnectEvent(event);

        // then
        verify(mockService, times(1)).handleUserLeft(mockUser, 123);
    }
}



