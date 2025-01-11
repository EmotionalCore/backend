package com.example.project.emotionCore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Schema(description = "실패 응답(4xx, 5xx)에 대한 Response 형식. 아마 모든 실패 응답은 이 형식으로 처리될듯")
public class ErrorResponse {
    @Schema(description = "항상 false", example = "false")
    private boolean status = false;

    @Schema(description = "에러 코드", example = "400, 401 등...")
    private int code;

    @Schema(description = "설명", example = "잘못된 id 입니다. 등...")
    private String message;

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
