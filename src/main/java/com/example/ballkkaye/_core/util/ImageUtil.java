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

    public static List<BoardImageResponse.ItemDTO> saveBase64Image(String base64Image, Board board, BoardImageRepository boardImageRepository) {

        return null;
    }

}

