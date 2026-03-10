package com.facet.api.auction;

import com.facet.api.auction.model.AucDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/auction")
@RestController
@RequiredArgsConstructor
public class AuctionController {
    private final AuctionService auctionService;

    @GetMapping("/list")
    private ResponseEntity list(
            @RequestParam(required = true, defaultValue = "0") int page,
            @RequestParam(required = true, defaultValue = "12") int size
    ){
        AucDto.PageRes dto = auctionService.list(page, size);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/read/{search}")
    public ResponseEntity read(@PathVariable String search){
        AucDto.PageRes dto = auctionService.read(0, 12, search);
        return ResponseEntity.ok(dto);
    }

}
