package com.example.project.emotionCore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Schema(description = "API 호출 성공시 사용 하는 공용 Response 형식. 적용 안함. 사용 여부 미지수")
public class SuccessResponse<T> {
    @Schema(description = "성공 여부 확인 용도")
    private boolean success = true;

    @Schema(description = "결과 데이터")
    private T data;

    public SuccessResponse(T data) {
        this.data = data;
    }
}
