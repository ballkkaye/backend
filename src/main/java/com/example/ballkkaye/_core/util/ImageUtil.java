package com.example.ballkkaye._core.util;

import com.example.ballkkaye.board.Board;
import com.example.ballkkaye.board.image.BoardImage;
import com.example.ballkkaye.board.image.BoardImageRepository;
import com.example.ballkkaye.board.image.BoardImageResponse;
import com.example.ballkkaye.common.enums.DeleteStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class ImageUtil {
    public static List<BoardImageResponse.ItemDTO> saveBase64Images(List<String> base64Images, Board board, BoardImageRepository boardImageRepository) {
        List<BoardImageResponse.ItemDTO> imageUrls = new ArrayList<>();
        if (base64Images == null || base64Images.isEmpty()) return imageUrls;

        for (String base64Image : base64Images) {
            if (base64Image == null || base64Image.isBlank()) continue;

            try {
                String mimeType = Base64Util.getMimeType(base64Image);
                String extension;

                if (mimeType.contains("jpeg") || mimeType.contains("jpg")) extension = ".jpg";
                else if (mimeType.contains("png")) extension = ".png";
                else if (mimeType.contains("gif")) extension = ".gif";
                else throw new RuntimeException("지원하지 않는 이미지 형식입니다.");

                String uploadDir = System.getProperty("user.dir") + "/upload/";
                Files.createDirectories(Paths.get(uploadDir));

                String fileName = UUID.randomUUID() + extension;
                Path imagePath = Paths.get(uploadDir + fileName);

                byte[] imageBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);
                Files.write(imagePath, imageBytes);

                BoardImage boardImage = BoardImage.builder()
                        .board(board)
                        .imgUrl("/upload/" + fileName)
                        .deleteStatus(DeleteStatus.NOT_DELETED)
                        .build();
                boardImageRepository.save(boardImage);

                imageUrls.add(new BoardImageResponse.ItemDTO(boardImage.getId(), boardImage.getImgUrl()));
            } catch (Exception e) {
                throw new RuntimeException("이미지 저장 실패", e);
            }
        }

        return imageUrls;
    }

    // 직관기록 이미지 디코딩
    public static String saveBase64VisitImage(String base64Image) {
        if (base64Image == null || base64Image.isBlank()) {
            throw new IllegalArgumentException("이미지 데이터가 비어 있습니다.");
        }

        try {
            // 1. MIME 타입 확인
            String mimeType = Base64Util.getMimeType(base64Image);
            String extension;

            if (mimeType.contains("jpeg") || mimeType.contains("jpg")) extension = ".jpg";
            else if (mimeType.contains("png")) extension = ".png";
            else if (mimeType.contains("gif")) extension = ".gif";
            else throw new RuntimeException("지원하지 않는 이미지 형식입니다.");

            // 2. 저장 경로 설정
            String uploadDir = System.getProperty("user.dir") + "/upload/visit-record/";
            Files.createDirectories(Paths.get(uploadDir));

            // 3. 파일명 생성
            String fileName = "visit_" + System.currentTimeMillis() + extension;
            Path imagePath = Paths.get(uploadDir + fileName);

            // 4. 이미지 디코딩 및 저장
            byte[] imageBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);
            Files.write(imagePath, imageBytes);

            // 5. 업로드된 파일 경로 리턴
            return "/upload/visit-record/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("직관기록 이미지 저장 실패", e);
        }
    }

}

