package com.facet.api.ask;

import com.facet.api.ask.model.Ask;
import com.facet.api.ask.model.AskDto;
import com.facet.api.common.model.BaseResponse;
import com.facet.api.user.model.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;
@RequestMapping("/ask")
@RequiredArgsConstructor
@RestController
public class AskController {
    private final AskService askService;

    // 문의 작성 기능
    @PostMapping("/reg")
    public ResponseEntity reg(@RequestBody AskDto.RegReq dto){
        AskDto.RegRes result = askService.reg(dto);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    // 사용자 이메일로 문의 내역 list 출력하기
    @GetMapping("/list")
    public ResponseEntity list(@AuthenticationPrincipal AuthUserDetails user){
        String userEmail = user.getUsername();
        List<AskDto.listRes> result = askService.list(userEmail);
        return ResponseEntity.ok(BaseResponse.success(result));
    }
}
