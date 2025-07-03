package com.example.ballkkaye.match.like;

import com.example.ballkkaye.match.Match;
import com.example.ballkkaye.match.MatchRepository;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MatchLikeService {
    private final MatchLikeRepository matchLikeRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;

    @Transactional
    public Object save(Integer matchId, User sessionUser) {
        // 이미 좋아요 했는지 확인
        Optional<MatchLike> matchLikePS = matchLikeRepository.findByMatchIdAndUserId(matchId, sessionUser.getId());
        if (matchLikePS.isPresent()) {
            throw new RuntimeException("이미 좋아요한 매치입니다");
        }

        // User, Match 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

        Match matchPS = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("동행글을 찾을 수 없습니다"));

        // MatchLike 엔티티 생성
        MatchLike matchLike = MatchLike.builder()
                .user(userPS)
                .match(matchPS)
                .build();

        // 저장
        matchLikeRepository.save(matchLike);
        Integer likeCount = matchLikeRepository.totalCount(matchPS.getId());

        MatchLikeResponse.SaveDTO respDTO = new MatchLikeResponse.SaveDTO(matchLike.getId(), likeCount);
        return respDTO;
    }

    // 좋아요 삭제
    @Transactional
    public MatchLikeResponse.DeleteDTO delete(Integer likeId, User sessionUser) {
        // 1. user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

        // 2. match/like 조회
        MatchLike matchLikePS = matchLikeRepository.findById(likeId)
                .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        // 3. 주인인지 확인
        if (matchLikePS.getUser().getId() != userPS.getId()) {
            throw new RuntimeException("해당 기능에 대한 권한이 없습니다.");
        }

        // 4. 좋아요 삭제
        matchLikeRepository.deleteById(matchLikePS.getId());

        // 5. 좋아요 갯수 반환
        Integer matchLikeCount = matchLikeRepository.totalCount(matchLikePS.getId());

        // 6. DTO에 옮기기
        MatchLikeResponse.DeleteDTO respDTO = new MatchLikeResponse.DeleteDTO(matchLikeCount);

        return respDTO;
    }
}
