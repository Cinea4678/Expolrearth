package cc.cinea.huanyou.controller;

import cc.cinea.huanyou.dto.ApiResp;
import cc.cinea.huanyou.entity.Resort;
import cc.cinea.huanyou.service.ResortService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author cinea
 */
@RestController
@RequestMapping("/resort")
@Tag(name = "Resort", description = "景区")
public class ResortController {
    ResortService resortService;

    public ResortController(ResortService resortService) {
        this.resortService = resortService;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    @Operation(summary = "添加景区")
    ApiResp add(@RequestBody Resort resort) {
        var result = resortService.add(resort);
        return ApiResp.from(result);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping
    @Operation(summary = "删除景区")
    ApiResp delete(@RequestParam Long id) {
        var result = resortService.delete(id);
        return ApiResp.from(result);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping
    @Operation(summary = "更新景区信息")
    ApiResp updateInfo(@RequestBody Resort resort) {
        var result = resortService.updateInfo(resort);
        return ApiResp.from(result);
    }


}
