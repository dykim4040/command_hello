package com.exam.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.exam.domain.AttachVO;
import com.exam.domain.BoardVO;
import com.exam.domain.UploadDTO;
import com.exam.service.AttachService;
import com.exam.service.BoardService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@RequestMapping("/board/*")
@Log4j
public class BoardController {

    @Setter(onMethod_ = @Autowired)
    private BoardService service;
    
    @Setter(onMethod_ = @Autowired)
    private AttachService attachService;

    @GetMapping("/write")
    public String write() {
        log.info("write() 호출됨...");
        return "center/write";
    }

    @PostMapping("/write")
    public String write(BoardVO boardVO, HttpServletRequest request) {
        
        // 시퀀스로부터 글번호 구하기
        int num = service.getSeqBoardNum();
        boardVO.setNum(num);  // 주글번호
        boardVO.setReRef(num); // re_ref == num 주글일때는 글그룹번호와 글번호가 같음.
        boardVO.setReLev(0);  // re_lev 들여쓰기 레벨
        boardVO.setReSeq(0);  // re_seq 글그룹 내에서는 오름차순 정렬. 최상단에 주글이 옴.
        boardVO.setReadcount(0); // readcount 조회수 0
        
        // 글작성자 IP주소 값 저장
        boardVO.setIp(request.getRemoteAddr());
        
        service.insert(boardVO);
        return "redirect:/board/list";
    }

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(required = false) String search, 
            Model model) {

        // =========================================
        // 한 페이지에 해당하는 글목록 구하기 작업
        // =========================================
        int amount = 10; // 한 페이지 당 보여줄 글(레코드) 갯수
        
        List<BoardVO> list = service.getBoards(pageNum, amount, search);
        
        // =========================================
        //  페이지 블록 구하기 작업
        // =========================================
        int allRowCount = 0; // 전체 행 갯수
        
        allRowCount = service.getBoardCount(search);
        
        int maxPage = allRowCount / amount + (allRowCount % amount == 0 ? 0 : 1);
        // 1페이지 ~ maxPage 페이지까지 존재함.
        // -> 페이지 블록단위로 끊어줌

        // 시작페이지번호(1)             끝페이지번호(10)
        // 1 2 3 4 5 6 7 8 9 10             -- [블록구성 10개]
        
        // 시작페이지번호(11)             끝페이지번호(20)
        // 11 12 13 14 15 16 17 18 19 20   -- [블록구성 10개]
        
        // 시작페이지번호(21)             끝페이지번호(23)
        // 21 22 23                         -- [블록구성 3개]
        
        // 한 페이지블록을 구성하는 페이지갯수
        int pageBlockSize = 5;
        
        // 시작페이지번호 구하기
        int startPage = ((pageNum/pageBlockSize) - (pageNum%pageBlockSize==0 ? 1 : 0)) * pageBlockSize + 1;
        // 끝페이지번호 구하기
        int endPage = startPage + pageBlockSize - 1;
        if (endPage > maxPage) { // 마지막 블록에서 끝페이지번호 구하기
            endPage = maxPage;
        }
        
        
        Map<String, Integer> pageInfoMap = new HashMap<>();
        pageInfoMap.put("startPage", startPage); // 시작페이지번호
        pageInfoMap.put("endPage", endPage);     // 끝페이지번호
        pageInfoMap.put("pageBlockSize", pageBlockSize);//페이지블록크기
        pageInfoMap.put("maxPage", maxPage);     // 전체 페이지수
        pageInfoMap.put("allRowCount", allRowCount); // 전체 행갯수
        pageInfoMap.put("pageNum", pageNum); // 사용자가 요청한 페이지번호
        
        // model에 저장하면 스프링이 request 영역객체에 옮겨담아줌
        model.addAttribute("list", list); // 글목록 list 
        model.addAttribute("pageInfoMap", pageInfoMap); // 페이지블록 출력관련 데이터
        
        
        model.addAttribute("search", search); // 검색어
        
        return "center/notice";
    } // list()
    
    
    @GetMapping("/detail")
    public String detail(int num, Model model) {
        // 조회수 1증가
        service.updateReadcount(num);
        
        // 글번호에 해당하는 글 전체(상세)내용 가져오기
        BoardVO board = service.getBoard(num);
        log.info("detail board : " + board);
        
        // *글내용 줄바꿈 처리방법
        // (1) <pre>태그처리
        // (2) \r\n -> <br> 바꾸기
        String content = "";
        if (board.getContent() != null) {
            content = board.getContent().replace("\r\n", "<br>");
            board.setContent(content);
        }
        
        model.addAttribute("board", board); // 글번호 해당되는 글내용
        
        return "center/content";
    }
    
    @GetMapping("/modify")
    public String modify(int num, Model model) {
        
        BoardVO board = service.getBoard(num);
        
        model.addAttribute("board", board);
        
        return "center/update";
    }
    
    @PostMapping("/modify")
    public ResponseEntity<String> modify(BoardVO boardVO, String pageNum) {
        log.info("modify() - boardVO: " + boardVO);
        
        // 글 패스워드 일치하면 글수정 후 글목록으로 이동
        // 글 패스워드 불일치하면 이전화면으로 돌아가기
        // boolean isSuccess == true 수정성공
        //         isSuccess == false 수정실패
        boolean isSuccess = service.updateBoard(boardVO);
        
        if (!isSuccess) { // // 글 수정 실패
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/html; charset=UTF-8");

            StringBuilder sb = new StringBuilder();
            sb.append("<script>");
            sb.append("alert('글비밀번호가 틀립니다.');");
            sb.append("history.back();");
            sb.append("</script>");

            return new ResponseEntity<String>(sb.toString(), headers, HttpStatus.OK);
        }

        
        // 글 수정 성공 이후 글목록으로 리다이렉트
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/board/list?pageNum=" + pageNum); // redirect 경로 위치
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    }
    
    
    @GetMapping("/delete")
    public String delete() {
        return "center/delete";
    }
    
    @PostMapping("/delete")
    public ResponseEntity<String> delete(int num, String pass, String pageNum) {
        
        boolean isSuccess = service.deleteBoard(num, pass);
        
        if (!isSuccess) { // // 글 삭제 실패
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/html; charset=UTF-8");

            StringBuilder sb = new StringBuilder();
            sb.append("<script>");
            sb.append("alert('글비밀번호가 틀립니다.');");
            sb.append("history.back();");
            sb.append("</script>");

            return new ResponseEntity<String>(sb.toString(), headers, HttpStatus.OK);
        }
        
        // 글 삭제 성공 이후 글목록으로 리다이렉트
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/board/list?pageNum=" + pageNum); // redirect 경로 위치
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    }
    
    @GetMapping("/reply")
    public String reply() {
        return "center/replyWrite";
    }
    
    @PostMapping("/reply")
    public String reply(BoardVO board, HttpServletRequest request, String pageNum, RedirectAttributes rttr) {
        // 사용자 직접 입력값(답글내용) 파라미터 가져와서 저장
        // [답글을 다는 대상글]의 답글관련 정보 파라미터 가져와서 저장
        
        // 답글 작성자의 IP주소 가져와서 저장
        board.setIp(request.getRemoteAddr());
        
        service.replyInsert(board); // 답글등록
        
        rttr.addAttribute("pageNum", pageNum);
        
        return "redirect:/board/list";
    }
    
    
    @GetMapping("/filelist")
    public String filelist(@RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(required = false) String search, 
            Model model) {

        // =========================================
        // 한 페이지에 해당하는 글목록 구하기 작업
        // =========================================
        int amount = 6; // 한 페이지 당 보여줄 글(레코드) 갯수
        
        List<BoardVO> list = service.getBoards(pageNum, amount, search);
        
        // =========================================
        //  페이지 블록 구하기 작업
        // =========================================
        int allRowCount = 0; // 전체 행 갯수
        
        allRowCount = service.getBoardCount(search);
        
        int maxPage = allRowCount / amount + (allRowCount % amount == 0 ? 0 : 1);
        // 1페이지 ~ maxPage 페이지까지 존재함.
        // -> 페이지 블록단위로 끊어줌

        // 시작페이지번호(1)             끝페이지번호(10)
        // 1 2 3 4 5 6 7 8 9 10             -- [블록구성 10개]
        
        // 시작페이지번호(11)             끝페이지번호(20)
        // 11 12 13 14 15 16 17 18 19 20   -- [블록구성 10개]
        
        // 시작페이지번호(21)             끝페이지번호(23)
        // 21 22 23                         -- [블록구성 3개]
        
        // 한 페이지블록을 구성하는 페이지갯수
        int pageBlockSize = 5;
        
        // 시작페이지번호 구하기
        int startPage = ((pageNum/pageBlockSize) - (pageNum%pageBlockSize==0 ? 1 : 0)) * pageBlockSize + 1;
        // 끝페이지번호 구하기
        int endPage = startPage + pageBlockSize - 1;
        if (endPage > maxPage) { // 마지막 블록에서 끝페이지번호 구하기
            endPage = maxPage;
        }
        
        
        Map<String, Integer> pageInfoMap = new HashMap<>();
        pageInfoMap.put("startPage", startPage); // 시작페이지번호
        pageInfoMap.put("endPage", endPage);     // 끝페이지번호
        pageInfoMap.put("pageBlockSize", pageBlockSize);//페이지블록크기
        pageInfoMap.put("maxPage", maxPage);     // 전체 페이지수
        pageInfoMap.put("allRowCount", allRowCount); // 전체 행갯수
        pageInfoMap.put("pageNum", pageNum); // 사용자가 요청한 페이지번호
        
        // model에 저장하면 스프링이 request 영역객체에 옮겨담아줌
        model.addAttribute("list", list); // 글목록 list 
        model.addAttribute("pageInfoMap", pageInfoMap); // 페이지블록 출력관련 데이터
        
        
        model.addAttribute("search", search); // 검색어
        
        return "center/fileNotice";
    }
    
    
    
    @GetMapping("/fileWrite")
    public String fileWrite() {
        return "center/fileWrite";
    }
    
    @PostMapping("/fileWrite")
    public String fileWrite(BoardVO boardVO, HttpServletRequest request, MultipartFile[] files) {
        log.info("files.length : " + files.length);
        
        
        // 시퀀스로부터 글번호 구하기
        int num = service.getSeqBoardNum();
        boardVO.setNum(num);  // 주글번호
        boardVO.setReRef(num); // re_ref == num 주글일때는 글그룹번호와 글번호가 같음.
        boardVO.setReLev(0);  // re_lev 들여쓰기 레벨
        boardVO.setReSeq(0);  // re_seq 글그룹 내에서는 오름차순 정렬. 최상단에 주글이 옴.
        boardVO.setReadcount(0); // readcount 조회수 0
        // 글작성자 IP주소 값 저장
        boardVO.setIp(request.getRemoteAddr());
        
        
        //  upload 폴더의 물리적 경로
        ServletContext application = request.getServletContext();
        String realPath = application.getRealPath("/upload");
        //String uploadPath = "C:\\upload";
        log.info("realPath : " + realPath);
        
        // 폴더 동적 생성하기   /upload/2019/05/17
        File uploadPath = new File(realPath, getFolder());
        log.info("uploadPath : " + uploadPath);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs(); // 업로드할 폴더 생성
        }
        
        
        List<AttachVO> attachList = new ArrayList<>();
        
        for (MultipartFile multipartFile : files) {
            log.info("업로드 파일명: " + multipartFile.getOriginalFilename());
            log.info("업로드 파일 크기: " + multipartFile.getSize());
            
            if (multipartFile.isEmpty()) {
                continue;
            }
            
            String uploadFileName = multipartFile.getOriginalFilename();
            UUID uuid = UUID.randomUUID();
            uploadFileName = uuid.toString() + "_" + uploadFileName;
            log.info("업로드할 최종 파일명: " + uploadFileName);
            
            File saveFile = new File(uploadPath, uploadFileName);
            
            try {
                multipartFile.transferTo(saveFile); // 업로드 수행
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            
            AttachVO attachVO = new AttachVO();
            attachVO.setBnum(boardVO.getNum()); // 게시글번호 저장
            attachVO.setUuid(uuid.toString());  // 첨부파일 UUID
            attachVO.setUploadpath(getFolder());
            attachVO.setFilename(multipartFile.getOriginalFilename());
            
            if (checkImageType(saveFile)) {
                // 섬네일 이미지 생성하기
                File thumbnailFile = new File(uploadPath, "s_" + uploadFileName);
                try {
                    FileOutputStream fos = new FileOutputStream(thumbnailFile);
                    
                    Thumbnailator.createThumbnail(multipartFile.getInputStream(), fos, 100, 100);
                    
                    fos.close();
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                attachVO.setFiletype("I"); // Image file type
            } else {
                attachVO.setFiletype("O"); // Other file type
            }
            
            attachList.add(attachVO);
        } // for
        
        
        // 테이블 insert
        service.insertBoardAndAttach(boardVO, attachList);
        
        
        return "redirect:/board/filelist";
    } // fileWrite()
    

    private String getFolder() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String str = sdf.format(date);
//        log.info("File.separator : " + File.separator);
//        String folderName = str.replace("-", File.separator);
//        log.info("folderName : " + folderName);
        return str;
    }
    
    
    private boolean checkImageType(File file) {
        log.info("checkImageType file : " + file);
        
        boolean isImageType = false;
        try {
            String contentType = Files.probeContentType(file.toPath());
            log.info("contentType : " + contentType);
            
            if (contentType != null) {
                isImageType = contentType.startsWith("image");
            } else { // contentType == null
                isImageType = false;
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        return isImageType;
    }
    
    @GetMapping("/fileDetail")
    public String fileDetail(int num, Model model) {
        // 조회수 1증가
        service.updateReadcount(num);
        // 글번호에 해당하는 글 전체(상세)내용 가져오기
        BoardVO board = service.getBoard(num);
        // 글번호에 해당하는 첨부파일 리스트 가져오기
        List<AttachVO> attachList = service.findByBnum(num);
        for (AttachVO attach : attachList) {
            log.info("attach : " + attach);
        }
        
        
        // *글내용 줄바꿈 처리방법
        // (1) <pre>태그처리
        // (2) \r\n -> <br> 바꾸기
        String content = "";
        if (board.getContent() != null) {
            content = board.getContent().replace("\r\n", "<br>");
            board.setContent(content);
        }
        
        model.addAttribute("board", board);
        model.addAttribute("attachList", attachList);
        
        return "center/fileContent";
    }
    
    
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(String fileName, HttpServletRequest request, @RequestHeader("User-Agent") String userAgent) {
        log.info("download file : " + fileName);
        
        //  upload 폴더의 물리적 경로
        ServletContext application = request.getServletContext();
        String realPath = application.getRealPath("/upload");
        
        Resource resource = new FileSystemResource(realPath + "/" + fileName);
        
        if (!resource.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        
        String resourceName = resource.getFilename();
        String resourceOriginalName = resourceName.substring(resourceName.indexOf("_")+1);
        
        HttpHeaders headers = new HttpHeaders();
        try {
            String downloadName = null;
            
            if (userAgent.contains("Trident")) {
                log.info("IE browser");
                downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+", " ");
            } else if (userAgent.contains("Edge")) {
                log.info("Edge browser");
                downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
            } else {
                log.info("chrome browser");
                downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
            }
            
            log.info("downloadName : " + downloadName);
            
            headers.add("Content-Disposition", "attachment; filename=" + downloadName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    } // downloadFile()
    
    
    @GetMapping("/fileDelete")
    public String fileDelete() {
        return "center/fileDelete";
    }
    
    @PostMapping("/fileDelete")
    public ResponseEntity<String> fileDelete(int num, String pass, String pageNum, HttpServletRequest request) {
        BoardVO board = service.getBoard(num);
        
        if (!pass.equals(board.getPass())) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/html; charset=UTF-8");

            StringBuilder sb = new StringBuilder();
            sb.append("<script>");
            sb.append("alert('글비밀번호가 틀립니다.');");
            sb.append("history.back();");
            sb.append("</script>");

            return new ResponseEntity<String>(sb.toString(), headers, HttpStatus.OK);
        }
        
        // 파일삭제
        List<AttachVO> attachList = service.findByBnum(num);
        if (attachList.size() > 0) {
            ServletContext application = request.getServletContext();
            String realPath = application.getRealPath("/upload");
            
            for (AttachVO attach : attachList) {
                String path = realPath + "/" + attach.getUploadpath();
                String filename = attach.getUuid() + "_" + attach.getFilename();
                
                if (attach.getFiletype().equals("I")) {
                    // 섬네일 삭제하기
                    File thumbnailFile = new File(path, "s_"+filename);
                    if (thumbnailFile.exists()) thumbnailFile.delete();
                }
                // 원본파일 삭제하기
                File deleteFile = new File(path, filename);
                if (deleteFile.exists()) deleteFile.delete();
            } // for
        }
        
        // 테이블 레코드 삭제(게시판, 첨부파일 테이블 행 삭제)
        service.deleteBoardAndAttach(num);
        
        // 글 삭제 성공 이후 글목록으로 리다이렉트
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/board/filelist?pageNum=" + pageNum); // redirect 경로 위치
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    } // fileDelete()
    
    @GetMapping("/fileModify")
    public String fileModify(int num, Model model) {
        BoardVO board = service.getBoard(num);
        List<AttachVO> attachList = service.findByBnum(num);
        
        model.addAttribute("board", board);
        model.addAttribute("attachList", attachList);
        
        return "center/fileUpdate";
    } // fileModify() get
    
    
    @PostMapping("/fileModify")
    public ResponseEntity<String> fileModify(BoardVO board, String[] delFiles, MultipartFile[] newFiles, String pageNum, HttpServletRequest request) {
        // 글 패스워드 일치하면 글수정 후 글목록으로 이동
        // 글 패스워드 불일치하면 이전화면으로 돌아가기
        // boolean isSuccess == true 수정성공
        //         isSuccess == false 수정실패
        boolean isSuccess = service.updateBoard(board);
        
        if (!isSuccess) { // // 글 수정 실패
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/html; charset=UTF-8");

            StringBuilder sb = new StringBuilder();
            sb.append("<script>");
            sb.append("alert('글비밀번호가 틀립니다.');");
            sb.append("history.back();");
            sb.append("</script>");

            return new ResponseEntity<String>(sb.toString(), headers, HttpStatus.OK);
        }
        
        
        ServletContext application = request.getServletContext();
        String realPath = application.getRealPath("/upload");
        
        
        // 2019/05/21/64b74a73-d669-42af-8d6d-fb295742863d_이미지파일.png
        if (delFiles != null) {
            for (int i=0; i<delFiles.length; i++) {
                String folder = delFiles[i].substring(0, 10); // 2019/05/21
                String uuidAndFilename = delFiles[i].substring(11); // 2019/05/21/ 제외
                
                int index = uuidAndFilename.indexOf("_");
                String uuid = uuidAndFilename.substring(0, index);
                String filename = uuidAndFilename.substring(index + 1);
                log.info("uuid : " + uuid);
                log.info("filename : " + filename);
                
                
                // 첨부파일 삭제항목 삭제하기
                File deleteFile = new File(realPath + "/" + delFiles[i]);
                if (deleteFile.exists()) deleteFile.delete();
                
                // 섬네일 삭제하기
                File thumbnailFile = new File(realPath + "/" + folder, "s_" + uuid + "_" + filename);
                if (thumbnailFile.exists()) thumbnailFile.delete();
                
                
                // attach 테이블에서  행 삭제
                attachService.delete(uuid);
            } // for
        } // if
        
        
        
        // 폴더 동적 생성하기   /upload/2019/05/17
        File uploadPath = new File(realPath, getFolder());
        log.info("uploadPath : " + uploadPath);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs(); // 업로드할 폴더 생성
        }

        // 새로 추가된 첨부파일 정보를 담을 리스트 준비
        List<AttachVO> newAttachList = new ArrayList<>();
        // 추가된 첨부파일 업로드하기
        for (MultipartFile multipartFile : newFiles) {
            if (multipartFile.isEmpty()) {
                continue;
            }
            
            String uploadFileName = multipartFile.getOriginalFilename();
            UUID uuid = UUID.randomUUID();
            uploadFileName = uuid.toString() + "_" + uploadFileName;
            log.info("업로드할 최종 파일명: " + uploadFileName);
            
            File saveFile = new File(uploadPath, uploadFileName);
            try {
                multipartFile.transferTo(saveFile); // 업로드 수행
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            AttachVO attachVO = new AttachVO();
            attachVO.setBnum(board.getNum()); // 게시글번호 저장
            attachVO.setUuid(uuid.toString());  // 첨부파일 UUID
            attachVO.setUploadpath(getFolder());
            attachVO.setFilename(multipartFile.getOriginalFilename());
            
            
            if (checkImageType(saveFile)) { // 이미지 파일이면
                // 섬네일 이미지 생성하기
                File thumbnailFile = new File(uploadPath, "s_" + uploadFileName);
                try {
                    FileOutputStream fos = new FileOutputStream(thumbnailFile);
                    
                    Thumbnailator.createThumbnail(multipartFile.getInputStream(), fos, 100, 100);
                    
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                attachVO.setFiletype("I"); // Image file type
            } else {
                attachVO.setFiletype("O"); // Other file type
            }
            
            newAttachList.add(attachVO);
        } // for
        
        for (AttachVO attachVO : newAttachList) {
            log.info("attachVO : " + attachVO);
        }
        
        
        // 추가된 첨부파일 테이블 insert하기
        if (newAttachList.size() > 0) {
            attachService.insert(newAttachList);
        }
        
        
        // 글 수정 성공 이후 글목록으로 리다이렉트
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/board/filelist?pageNum=" + pageNum); // redirect 경로 위치
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    } // fileModify() post
    
}





