package com.facet.api.auction.controller;

import com.facet.api.auction.model.AucDto;
import com.facet.api.auction.service.AuctionBidService;
import com.facet.api.common.model.BaseEntity;
import com.facet.api.common.model.BaseResponse;
import com.facet.api.user.model.AuthUserDetails;
import com.facet.api.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auction")
@RestController
@RequiredArgsConstructor
@Tag(name = "경매 기능")
public class AuctionBidController {
    private final AuctionBidService auctionBidService;


    @PostMapping("/bid")
    @Operation(summary = "입찰 기능", description = "현재 입찰가보다 높은 금액을 입력하면 입찰할 수 있는 기능 \n"
            +"--- \n" + "**※ 주의:** JWT 인증 쿠키가 반드시 포함되어야 합니다.")
    public ResponseEntity bid(@RequestBody AucDto.BidReq dto, @AuthenticationPrincipal AuthUserDetails authUserDetails){

        AucDto.BidRes result = auctionBidService.bid(dto, authUserDetails.getIdx());
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    private final SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/bid/{productIdx}")
    public void sendBidMessage(@DestinationVariable Long productIdx, String message) {
        System.out.println("productIdx : " + productIdx);
        messagingTemplate.convertAndSend("/topic/"+productIdx, message);
    }

    @PostMapping("/success")
    public ResponseEntity success(){
        return ResponseEntity.ok("성공1");
    }
}
