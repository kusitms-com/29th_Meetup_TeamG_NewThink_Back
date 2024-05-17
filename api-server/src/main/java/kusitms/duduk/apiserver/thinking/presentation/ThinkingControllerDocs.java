package kusitms.duduk.apiserver.thinking.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kusitms.duduk.apiserver.security.infrastructure.CustomUserDetails;
import kusitms.duduk.core.comment.dto.request.OpenAISummaryRequest;
import kusitms.duduk.core.comment.dto.response.OpenAISummaryResponse;
import kusitms.duduk.domain.thinking.Thinking;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Thinking", description = "생각 API")
public interface ThinkingControllerDocs {

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(summary = "생각 더하기 홈 조회", description = "생각 더하기 홈 데이터를 조회합니다.")
    ResponseEntity<List<Thinking>> retrieveThinkingHome(
        @AuthenticationPrincipal final CustomUserDetails customUserDetails
    );

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(summary = "생각 구름 상세 조회", description = "생각 구름 상세 데이터를 조회합니다.")
    ResponseEntity<Void> retrieveThinkingDetail();

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(summary = "생각 요약 생성", description = "생각 요약을 생성합니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "생각 요약 생성을 위한 필수 정보를 담고 있는 DTO 클래스",
            required = true,
            content = @Content(
	schema = @Schema(implementation = OpenAISummaryRequest.class)
            )
        )
    )
    ResponseEntity<OpenAISummaryResponse> summaryThinking(
        @RequestBody final OpenAISummaryRequest request
    );

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "CREATED"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(summary = "생각 구름 생성", description = "새로운 생각 구름을 생성합니다.")
    ResponseEntity<Void> createThinkingCloud();
}