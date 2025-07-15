package com.example.ballkkaye.match.like;

import com.example.ballkkaye._core.error.ex.ExceptionApi400;
import com.example.ballkkaye._core.error.ex.ExceptionApi404;
import com.example.ballkkaye.match.Match;
import com.example.ballkkaye.match.MatchRepository;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
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
            throw new ExceptionApi400("이미 좋아요한 매치입니다");
        }

        // User, Match 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ExceptionApi404("유저를 찾을 수 없습니다"));

        Match matchPS = matchRepository.findById(matchId)
                .orElseThrow(() -> new ExceptionApi404("동행글을 찾을 수 없습니다"));

        // MatchLike 엔티티 생성
        MatchLike matchLike = MatchLike.builder()
                .user(userPS)
                .match(matchPS)
                .build();

        // 저장
        matchLikeRepository.save(matchLike);

        Integer likeCount = matchLikeRepository.totalCount(matchPS.getId());

        log.info("userId={} 동행글 좋아요 등록 - matchId={}, 총 좋아요 수={}",
                sessionUser.getId(), matchId, likeCount);

        MatchLikeResponse.SaveDTO respDTO = new MatchLikeResponse.SaveDTO(matchLike.getId(), likeCount);
        return respDTO;
    }

    // 좋아요 삭제
    @Transactional
    public MatchLikeResponse.DeleteDTO delete(Integer likeId, User sessionUser) {
        log.info("좋아요 삭제 요청 - likeId: {}, 요청자: {}", likeId, sessionUser.getUsername());
        // 1. user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 유저: id={}", sessionUser.getId());
                    return new ExceptionApi404("유저를 찾을 수 없습니다");
                });

        // 2. match/like 조회
        MatchLike matchLikePS = matchLikeRepository.findById(likeId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 좋아요: likeId={}", likeId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 3. 주인인지 확인
        if (matchLikePS.getUser().getId() != userPS.getId()) {
            log.warn("권한 없는 좋아요 삭제 시도 - 요청자: {}, 좋아요 주인: {}",
                    userPS.getId(), matchLikePS.getUser().getId());
            throw new ExceptionApi404("해당 기능에 대한 권한이 없습니다.");
        }

        // 4. 좋아요 삭제
        matchLikeRepository.deleteById(matchLikePS.getId());
        log.info("좋아요 삭제 완료 - likeId: {}", likeId);

        // 5. 좋아요 갯수 반환
        Integer matchLikeCount = matchLikeRepository.totalCount(matchLikePS.getId());

        // 6. DTO에 옮기기
        MatchLikeResponse.DeleteDTO respDTO = new MatchLikeResponse.DeleteDTO(matchLikeCount);

        return respDTO;
    }
}
