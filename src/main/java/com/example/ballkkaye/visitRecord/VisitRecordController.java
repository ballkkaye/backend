package com.example.ballkkaye.visitRecord;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class VisitRecordController {
    private final VisitRecordService visitRecordService;
    private final UserRepository userRepository;
    private final HttpSession session;


    // 직관기록 등록
    @PostMapping("/s/api/visitRecords")
    public ResponseEntity<?> save(@Valid @RequestBody VisitRecordRequest.SaveDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        VisitRecordResponse.DTO respDTO = visitRecordService.save(reqDTO, sessionUser);

        return Resp.ok(respDTO);
    }


    // 직관기록 수정
    @PutMapping("/s/api/visitRecords/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @Valid @RequestBody VisitRecordRequest.UpdateDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        VisitRecordResponse.DTO respDTO = visitRecordService.update(reqDTO, id, sessionUser.getId());

        return Resp.ok(respDTO);
    }


    /**
     * 특정 날짜: GET /s/api/visitRecords?date=2025-07-03
     * 특정 월 : GET /s/api/visitRecords?year=2025&month=7
     */
    // 직관기록 목록 (특정 월 or 특정 날짜)
    @GetMapping("/s/api/visitRecords")
    public ResponseEntity<?> getList(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                     @RequestParam(required = false) Integer year,
                                     @RequestParam(required = false) Integer month) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        List<VisitRecordResponse.ListDTO> respDTO = visitRecordService.getList(sessionUser.getId(), date, year, month);
        return Resp.ok(respDTO);
    }


    /**
     * GET /s/api/visitRecords/highlight-dates?year=2025&month=7
     */
    // 달력에 하이라이트할 직관 날짜 조회
    @GetMapping("/s/api/visitRecords/highlight-dates")
    public ResponseEntity<?> getHighlightDates(@RequestParam Integer year,
                                               @RequestParam Integer month) {
        // 테스트용 유저 세팅
        User sessionUser = userRepository.findByEmail("ssar@nate.com")
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다"));

        List<LocalDate> dates = visitRecordService.getHighlightDates(sessionUser.getId(), year, month);
        return Resp.ok(dates);
    }

    // 직관기록 상세
    @GetMapping("/s/api/visitRecords/{id}")
    public ResponseEntity<?> getDetail(@PathVariable("id") Integer id) {
        //User sessionUser = (User) session.getAttribute("sessionUser");

        User sessionUser = userRepository.findByEmail("ssar@nate.com")
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다"));

        VisitRecordResponse.DetailDTO respDTO = visitRecordService.getDetail(id, sessionUser.getId());

        return Resp.ok(respDTO);
    }

    // 직관 기록 삭제
    @PutMapping("/s/api/visitRecords/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = visitRecordService.delete(id, sessionUser.getId());
        return Resp.ok(respDTO);
    }
}