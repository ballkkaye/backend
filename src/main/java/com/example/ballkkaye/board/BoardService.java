package com.example.ballkkaye.board;

import com.example.ballkkaye._core.util.Base64Util;
import com.example.ballkkaye.board.image.BoardImage;
import com.example.ballkkaye.board.image.BoardImageRepository;
import com.example.ballkkaye.board.image.BoardImageResponse;
import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.team.TeamRepository;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final BoardImageRepository boardImageRepository;

    // 커뮤니티 게시글 등록
    @Transactional
    public BoardResponse.SaveDTO save(BoardRequest.SaveDTO reqDTO, User sessionUser) {
        // 1. user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

        // 2. 팀 조회
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다"));

        // 3. 이미지 최대 수 넘으면 throw
        if (reqDTO.getImages() != null && reqDTO.getImages().size() > 10) {
            throw new RuntimeException("최대 이미지 저장 한도를 넘었습니다.");
        }


        // 4. board 게시글 저장
        Board board = reqDTO.toEntity(userPS, teamPS);
        boardRepository.save(board);

        // 5. board 게시글의 이미지 저장 << image 테이블에
        List<BoardImageResponse.ItemDTO> imageUrls = new java.util.ArrayList<>();
        List<String> base64Images = reqDTO.getImages();
        if (base64Images != null && !base64Images.isEmpty()) {
            for (String base64Image : base64Images) {
                if (base64Image == null || base64Image.isBlank()) continue;

                try {
                    // 전체 base64Image에서 MIME 타입 추출
                    String mimeType = Base64Util.getMimeType(base64Image); // ← 이게 핵심
                    String extension;
                    if (mimeType.contains("jpeg") || mimeType.contains("jpg")) {
                        extension = ".jpg";
                    } else if (mimeType.contains("png")) {
                        extension = ".png";
                    } else if (mimeType.contains("gif")) {
                        extension = ".gif";
                    } else {
                        throw new RuntimeException("지원하지 않는 이미지 형식입니다. jpeg, png, gif만 허용됩니다.");
                    }

                    String uploadDir = System.getProperty("user.dir") + "/upload/";
                    Files.createDirectories(Paths.get(uploadDir));

                    String fileName = UUID.randomUUID() + extension;
                    Path imagePath = Paths.get(uploadDir + fileName);

                    // 전체 base64Image에서 ',' 이후 base64 데이터만 추출
                    String base64Data = base64Image.split(",")[1];
                    byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                    Files.write(imagePath, imageBytes);

                    // DB 저장
                    BoardImage boardImage = BoardImage.builder()
                            .board(board)
                            .imgUrl("/upload/" + fileName)
                            .deleteStatus(DeleteStatus.NOT_DELETED)
                            .build();
                    boardImageRepository.save(boardImage);

                    // response에 옮겨담는다.
                    imageUrls.add(new BoardImageResponse.ItemDTO(boardImage.getId(), boardImage.getImgUrl()));

                } catch (Exception e) {
                    throw new RuntimeException("이미지 저장 실패", e);
                }
            }
        }

        // 6. 응답 DTO 생성 및 반환
        return new BoardResponse.SaveDTO(board, imageUrls);
    }
}
