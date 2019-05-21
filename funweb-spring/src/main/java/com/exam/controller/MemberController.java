package com.exam.controller;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.exam.domain.MemberVO;
import com.exam.service.MemberService;

import lombok.Setter;

@Controller
@RequestMapping("/member/*")
public class MemberController {

    @Setter(onMethod_ = @Autowired)
    private MemberService service;

    /*
    @InitBinder
    public void initBinder(WebDataBinder binder) {

        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {

            @Override
            public void setAsText(String text) throws IllegalArgumentException {

                if (text == null || text.equals("")) { // "".equals(text)
                    setValue(null);
                    return;
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date parsedDate = dateFormat.parse(text);
                    Timestamp timestamp = new Timestamp(parsedDate.getTime());
                    setValue(timestamp);
                } catch (ParseException e) {
                    e.printStackTrace();
                    setValue(null);
                }
            }
        });
    } // initBinder()
    */
    
    
    
    @GetMapping("/join")
    public String join() {
        return "member/join";
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(MemberVO memberVO) {
        System.out.println(memberVO);

        service.register(memberVO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html; charset=UTF-8");

        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("alert('회원가입이 완료되었습니다.');");
        sb.append("location.href = '/member/login';");
        sb.append("</script>");

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(sb.toString(), headers, HttpStatus.OK);

        return responseEntity;
//      return "redirect:/member/loginForm";
    } // join post

    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(MemberVO memberVO, HttpSession session) {

        int check = service.loginCheck(memberVO.getId(), memberVO.getPassword());
        if (check != 1) { // 로그인 실패시
            String message = "";
            if (check == -1) { // 아이디 불일치
                message = "해당하는 아이디가 없습니다.";
            } else if (check == 0) { // 비밀번호 불일치
                message = "비밀번호가 일치하지 않습니다.";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/html; charset=UTF-8");

            StringBuilder sb = new StringBuilder();
            sb.append("<script>");
            sb.append("alert('" + message + "');");
            sb.append("location.href = '/member/login';");
            sb.append("</script>");

            return new ResponseEntity<String>(sb.toString(), headers, HttpStatus.OK);
        }

        // 아이디,비밀번호 모두 일치(로그인 성공)
        // 세션 구하기
        session.setAttribute("id", memberVO.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/"); // redirect 경로 위치
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
//        return "redirect:/";
    } // login post

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        // 로그아웃 처리
        session.invalidate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html; charset=UTF-8");

        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("alert('로그아웃 되었습니다.');");
        sb.append("location.href = '/';");
        sb.append("</script>");

        return new ResponseEntity<String>(sb.toString(), headers, HttpStatus.OK);
    } // logout()

    @GetMapping("/joinIdCheck")
    public String joinIdCheck(@RequestParam("userid") String userid, Model model) {
        System.out.println("userid : " + userid);

        boolean isDuplicated = service.isIdDuplicated(userid);
        
        // ID중복확인 결과값을 Model 객체에 저장함.
        model.addAttribute("isDuplicated", isDuplicated);
        
        return "member/joinIdCheck";
    } // joinIdCheck()
    
    @GetMapping("/joinIdCheckJson")
    public @ResponseBody boolean joinIdCheckJson(String userid) {
        boolean isDuplicated = service.isIdDuplicated(userid);
        
        return isDuplicated;
    } // joinIdCheckJson()

}



