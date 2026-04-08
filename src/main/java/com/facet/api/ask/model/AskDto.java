package com.facet.api.ask.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AskDto {

    // 유저가 문의를 등록할때의 요청받는 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegReq{
        private String category;
        private String email;
        private String title;
        private String contents;

        public Ask toEntity(){
            return Ask.builder()
                    .category(this.category)
                    .email(this.email)
                    .title(this.title)
                    .contents(this.contents)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class RegRes{
        private Long idx;
        private String category;
        private String title;
        private String contents;

        public static AskDto.RegRes from(Ask entity){
            return RegRes.builder()
                    .idx(entity.getIdx())
                    .category(entity.getCategory())
                    .title(entity.getTitle())
                    .contents(entity.getContents())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class listRes{
        private String category;
        private String title;
        private String contents;
        private String reply;
        private boolean reply_status;

        public static AskDto.listRes from(Ask entity){
            return AskDto.listRes.builder()
                    .category(entity.getCategory())
                    .title(entity.getTitle())
                    .contents(entity.getContents())
                    .reply(entity.getReply())
                    // 답변이 null이 아니면 true, 아니면 false
                    .reply_status(entity.getReply() != null && !entity.getReply().isBlank())
                    .build();
        }
    }
}