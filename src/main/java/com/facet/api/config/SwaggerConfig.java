package com.facet.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Facet 백엔드 프로젝트 API")
                        .description("")
                        .version("1.0.0")
                );
    }
    /*
        Swagger 사용법

        - 구현한 기능에 @Tag 를 붙여서 제목을 넣을 수 있다. (주로 controller에 사용한다.)
        ex)
            @Tag(name="경매 기능")
            public class AuctionController{...}

        - 메소드에 @Operation 를 붙여서 실제 구현된 기능들에 이름과 상세 설명을을 붙일 수 있다.
        ex)
            @Operation(summary = "입찰 기능", description = "현재 입찰가보다 높은 금액을 입력하면 입찰할 수 있는 기능")
            public ResponseEntity bid(AuctionDto.BidReq dto){...}

        - DTO에 선언된 변수에 @Schema를 붙여서 입력할 데이터 형식의 설명이나 제약 조건, 예시 등을 넣을 수 있다.
          (required = true로 설정하면 필수로 입력할 수 있도록 설정할 수 있다.)
        ex)
            public static class BidReq{
                private Long productIdx;

                @Schema(description = "입찰가는 현재 입찰가보다 높은 금액을 넣어야 합니다.", required = true, example = "50000")
                private Long bid;
            }

        >> Swagger API 명세서는 백엔드 실행 후 웹 브라우저에 아래와 같이 입력하면 확인할 수 있습니다.
            http://localhost:8080/swagger-ui/index.html
    */
}
