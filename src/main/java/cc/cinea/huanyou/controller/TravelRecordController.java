package cc.cinea.huanyou.controller;

import cc.cinea.huanyou.dto.ApiResp;
import cc.cinea.huanyou.entity.TravelRecord;
import cc.cinea.huanyou.service.TravelRecordService;
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
@RequestMapping("/record")
@Tag(name = "TravelRecord", description = "旅行记录")
public class TravelRecordController {

    TravelRecordService travelRecordService;

    public TravelRecordController(TravelRecordService travelRecordService) {
        this.travelRecordService = travelRecordService;
    }

    @Secured("ROLE_USER")
    @PostMapping
    @Operation(summary = "发布")
    @ApiResponse(content = @Content(schema = @Schema(implementation = TravelRecord.class)))
    ApiResp publish(@RequestBody TravelRecord record, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        return ApiResp.from(travelRecordService.publish(record, userId));
    }

    @Secured("ROLE_USER")
    @DeleteMapping
    @Operation(summary = "删除")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Boolean.class)))
    ApiResp publish(@RequestParam Long id, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        return ApiResp.success(travelRecordService.delete(id, userId));
    }

    @GetMapping
    @Operation(summary = "查询")
    @ApiResponse(content = @Content(schema = @Schema(implementation = TravelRecord.class)))
    ApiResp getInfoById(@RequestParam Long id, Principal principal) {
        if(principal == null){
            return ApiResp.success(travelRecordService.getInfoById(id));
        }else{
            return ApiResp.success(travelRecordService.getInfoWithInteractionById(id, Long.valueOf(principal.getName())));
        }
    }

    @Secured("ROLE_USER")
    @PutMapping
    @Operation(summary = "更新")
    ApiResp updateInfo(@RequestBody TravelRecord record, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        return ApiResp.success(travelRecordService.update(record, userId));
    }

    @Secured("ROLE_USER")
    @PostMapping("/like")
    @Operation(summary = "点赞")
    ApiResp like(@RequestParam Long id, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = travelRecordService.like(id, userId);
        return ApiResp.from(result);
    }

    @Secured("ROLE_USER")
    @PostMapping("/cancel-like")
    @Operation(summary = "取消点赞")
    ApiResp cancelLike(@RequestParam Long id, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = travelRecordService.cancelLike(id, userId);
        return ApiResp.from(result);
    }

    @Secured("ROLE_USER")
    @PostMapping("/favorite")
    @Operation(summary = "收藏")
    ApiResp addFavorites(@RequestParam Long id, @RequestParam Long favoritesId, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = travelRecordService.editFavorites(id, favoritesId, userId, true);
        return ApiResp.from(result);
    }

    @Secured("ROLE_USER")
    @PostMapping("/cancel-favorite")
    @Operation(summary = "取消收藏")
    ApiResp removeFavorites(@RequestParam Long id, @RequestParam Long favoritesId, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = travelRecordService.editFavorites(id, favoritesId, userId, false);
        return ApiResp.from(result);
    }

    @GetMapping("/recommend")
    @Operation(summary = "获取推荐")
    ApiResp getRecommends(Pageable pageable) {
        var result = travelRecordService.getTravelRecords(pageable, true);
        return ApiResp.success(result);
    }

    @GetMapping("/by-user")
    @Operation(summary = "根据作者检索")
    ApiResp getRecommendsByUser(@RequestParam Long id) {
        var result = travelRecordService.getTravelRecordsByAuthorId(id);
        return ApiResp.success(result);
    }

}
