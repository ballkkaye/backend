package com.example.ballkkaye.visitRecord;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class VisitRecordController {
    private final VisitRecordService visitRecordService;
    private final UserRepository userRepository;


    @PostMapping("/s/api/visitRecords")
    public ResponseEntity<?> save(@Valid @RequestBody VisitRecordRequest.SaveDTO reqDTO, Errors errors) {
        //User sessionUser = (User) session.getAttribute("sessionUser");

        User sessionUser = userRepository.findByEmail("ssar@nate.com")
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다"));

        VisitRecordResponse.DTO respDTO = visitRecordService.save(reqDTO, sessionUser);

        return Resp.ok(respDTO);
    }
}