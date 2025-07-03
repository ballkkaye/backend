package com.example.ballkkaye._core.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

public class Base64Util {
    public static String getMimeType(String base64) {
//        System.out.println("[디버그] 받은 base64 문자열: " + base64);

        if (base64 == null || base64.isBlank()) {
            throw new IllegalArgumentException("Base64 문자열이 null이거나 비어 있습니다.");
        }

        if (!base64.startsWith("data:") || !base64.contains(",")) {
            throw new IllegalArgumentException("유효하지 않은 Base64 문자열 형식입니다. (data:... 확인 필요)");
        }

        try {
            String[] parts = base64.split(",");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Base64 문자열이 ',' 구분자로 나뉘지 않습니다.");
            }

            String mimePart = parts[0];
            if (!mimePart.contains(":") || !mimePart.contains(";")) {
                throw new IllegalArgumentException("Base64 문자열의 MIME 타입 정보가 누락되었거나 잘못되었습니다.");
            }

            String mime = mimePart.split(":")[1].split(";")[0];
            return mime;
        } catch (Exception e) {
            throw new IllegalArgumentException("MIME 타입 추출 실패", e);
        }
    }

    public static String encodeAsString(byte[] imgBytes, String mimeType) {
        String imgBase64 = Base64.getEncoder().encodeToString(imgBytes);
        imgBase64 = "data:$mimeType;base64,$imgBase64".replace("$mimeType", mimeType).replace("$imgBase64", imgBase64);
        return imgBase64;
    }

    public static byte[] decodeAsBytes(String imgBase64) {
        // 1. mimetype parsing
        String mimeType = getMimeType(imgBase64);
        //System.out.println(mimeType);

        // 2. img parsing
        int prefixEndIndex = imgBase64.indexOf(",");
        String img = imgBase64.substring(prefixEndIndex + 1);
        //System.out.println(img);

        // 3. base64 decode to byte[]
        byte[] imgBytes = Base64.getDecoder().decode(img);
        return imgBytes;
    }

    public static byte[] readImageAsByteArray(String filenameInUpload) {
        try {
            // 현재 프로젝트 루트 경로 + /upload/ + 파일명
            String uploadDir = System.getProperty("user.dir") + "/upload/";
            Path filePath = Paths.get(uploadDir + filenameInUpload);

            // 파일이 실제로 존재하는지 확인
            if (!Files.exists(filePath)) {
                throw new IllegalArgumentException("upload 폴더에서 파일을 찾을 수 없습니다: " + filePath);
            }

            // 파일을 byte[] 로 읽어서 반환
            return Files.readAllBytes(filePath);

        } catch (IOException e) {
            throw new RuntimeException("upload 폴더에서 이미지 읽기 실패: " + filenameInUpload, e);
        }
    }

    public static String saveBase64Image(String base64) {
        byte[] imgBytes = decodeAsBytes(base64);
        String mimeType = getMimeType(base64);

        String extension = switch (mimeType) {
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            default -> throw new IllegalArgumentException("지원하지 않는 이미지 타입: " + mimeType);
        };

        String filename = UUID.randomUUID().toString() + extension;
        String uploadDir = System.getProperty("user.dir") + "/upload/";
        Path path = Paths.get(uploadDir + filename);

        try {
            Files.write(path, imgBytes);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }

        return "/upload/" + filename;
    }
}
