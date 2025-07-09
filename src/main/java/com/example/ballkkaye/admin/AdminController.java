package com.example.ballkkaye.admin;

import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.common.enums.UserRole;
import com.example.ballkkaye.user.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final HttpSession session;

    @GetMapping("/admin/login-form")
    public String loginForm() {
        return "login-form";
    }

    @PostMapping("/admin/login")
    public Object login(@Valid AdminRequest.LoginDTO loginDTO, HttpServletResponse response, Errors errors) {
        if (!loginDTO.getUserRole().equals(UserRole.ADMIN)) {
            return "redirect:/admin/login-form";
        }

        User sessionUser = adminService.login(loginDTO);
        session.setAttribute("sessionUser", sessionUser);

        String accessToken = JwtUtil.create(sessionUser);

        Cookie jwtCookie = new Cookie("access_token", accessToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60); // 1시간
        response.addCookie(jwtCookie);

        return "redirect:/admin";
    }

    // @PostMapping("/s/company/user/update")
    // public String update(@Valid UserRequest.CompanyUpdateDTO reqDTO, Errors
    // errors) {
    // User sessionUser = (User) session.getAttribute("sessionUser");
    // if (sessionUser == null) throw new Exception401("로그인 후 이용");
    // if (!reqDTO.getNewPassword().equals(reqDTO.getConfirmPassword())) throw new
    // Exception400("입력한 비밀번호가 다릅니다.");
    // User userPS = userService.update(reqDTO, sessionUser.getId());
    // session.setAttribute("sessionUser", userPS);
    // return "redirect:/login-form";
    // }

    // @GetMapping("/login-form")
    // public String loginForm() {
    // return "login-form";
    // }

    // @PostMapping("/login")
    // public String login(@Valid UserRequest.LoginDTO loginDTO, HttpServletResponse
    // response, Errors errors, @RequestParam(required = false) String redirect) {
    //
    // User sessionUser = userService.login(loginDTO);
    // session.setAttribute("sessionUser", sessionUser);
    //
    // if (loginDTO.getRememberMe() == null) {
    // Cookie cookie = new Cookie("username", null);
    // cookie.setMaxAge(0); // 즉시 만료
    // response.addCookie(cookie);
    // } else {
    // Cookie cookie = new Cookie("username", loginDTO.getUsername());
    // cookie.setMaxAge(60 * 60 * 24 * 7);
    // response.addCookie(cookie);
    // }
    //
    // // 1. redirect 파라미터가 있으면 우선 처리
    // if (redirect != null && !redirect.isEmpty()) {
    // return "redirect:" + redirect;
    // }
    //
    // // 사용자 역할에 따라 리다이렉트 분기
    // if (loginDTO.getRole() == Role.personal) {
    // return "redirect:/";
    // } else {
    // // 회사 사용자일 경우 companyInfoId 확인
    // Integer companyInfoId =
    // companyInfoService.findCompanyInfoIdByUserId(sessionUser.getId());
    //
    // if (companyInfoId != null) {
    // session.setAttribute("companyInfoId", companyInfoId);
    // return "redirect:/s/company/info/" + companyInfoId;
    // } else {
    // session.removeAttribute("companyInfoId");
    // return "redirect:/s/company/info/save-form";
    // }
    // }
    // }

}
