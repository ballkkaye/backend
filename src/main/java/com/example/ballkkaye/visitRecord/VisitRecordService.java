package com.example.ballkkaye.visitRecord;

import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.game.GameRepository;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.team.TeamRepository;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import com.example.ballkkaye.visitRecord.Image.VisitRecordImage;
import com.example.ballkkaye.visitRecord.Image.VisitRecordImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VisitRecordService {
    private final VisitRecordRepository visitRecordRepository;
    private final VisitRecordImageService visitRecordImageService;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;


    @Transactional
    public VisitRecordResponse.DTO save(VisitRecordRequest.SaveDTO reqDTO, User sessionUser) {
        // user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));


        // game 조회
        Game gamePS = gameRepository.findById(reqDTO.getGameId())
                .orElseThrow(() -> new RuntimeException("게임을 찾을 수 없습니다"));


        // team조회
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다"));

        // 직관기록 저장
        VisitRecord visitRecord = reqDTO.toEntity(userPS, gamePS, teamPS);
        visitRecordRepository.save(visitRecord);

        //  직관기록 이미지 저장
        VisitRecordImage savedImage = null;
        if (reqDTO.getImageString() != null && !reqDTO.getImageString().isBlank()) {
            savedImage = visitRecordImageService.save(reqDTO.getImageString(), visitRecord.getId());
        }
        return new VisitRecordResponse.DTO(visitRecord, savedImage);
    }
}
