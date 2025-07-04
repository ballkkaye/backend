package com.example.ballkkaye.visitRecord;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class VisitRecordController {
    private final VisitRecordService visitRecordService;
    private final UserRepository userRepository;


    // 직관기록 등록
    @PostMapping("/s/api/visitRecords")
    public ResponseEntity<?> save(@Valid @RequestBody VisitRecordRequest.SaveDTO reqDTO, Errors errors) {
        //User sessionUser = (User) session.getAttribute("sessionUser");

        User sessionUser = userRepository.findByEmail("ssar@nate.com")
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다"));

        VisitRecordResponse.DTO respDTO = visitRecordService.save(reqDTO, sessionUser);

        return Resp.ok(respDTO);
    }


    // 직관기록 수정
    @PutMapping("/s/api/visitRecords/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @Valid @RequestBody VisitRecordRequest.UpdateDTO reqDTO, Errors errors) {
        //User sessionUser = (User) session.getAttribute("sessionUser");

        User sessionUser = userRepository.findByEmail("ssar@nate.com")
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다"));

        VisitRecordResponse.DTO respDTO = visitRecordService.update(reqDTO, id, sessionUser.getId());

        return Resp.ok(respDTO);
    }


    // 직관기록 수정 확인
    @GetMapping("/s/api/visitRecords/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Integer id) {
        //User sessionUser = (User) session.getAttribute("sessionUser");

        User sessionUser = userRepository.findByEmail("ssar@nate.com")
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다"));

        VisitRecordResponse.DTO respDTO = visitRecordService.getOne(id, sessionUser.getId());
        return Resp.ok(respDTO);
    }

    // 직관 기록 삭제
    @DeleteMapping("/s/api/visitRecords/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        //User sessionUser = (User) session.getAttribute("sessionUser");

        User sessionUser = userRepository.findByEmail("ssar@nate.com")
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다"));

        visitRecordService.delete(id, sessionUser.getId());

        return Resp.ok(null);
    }

}