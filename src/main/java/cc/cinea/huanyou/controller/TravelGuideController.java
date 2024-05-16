package cc.cinea.huanyou.controller;

import cc.cinea.huanyou.dto.ApiResp;
import cc.cinea.huanyou.entity.TravelGuide;
import cc.cinea.huanyou.service.TravelGuideService;
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
@RequestMapping("/guide")
@Tag(name = "TravelGuide", description = "旅行攻略")
public class TravelGuideController {

    TravelGuideService travelGuideService;

    public TravelGuideController(TravelGuideService travelGuideService) {
        this.travelGuideService = travelGuideService;
    }

    @Secured("ROLE_USER")
    @PostMapping
    @Operation(summary = "发布")
    @ApiResponse(content = @Content(schema = @Schema(implementation = TravelGuide.class)))
    ApiResp publish(@RequestBody TravelGuide guide, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        return ApiResp.from(travelGuideService.publish(guide, userId));
    }

    @Secured("ROLE_USER")
    @DeleteMapping
    @Operation(summary = "删除")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Boolean.class)))
    ApiResp publish(@RequestParam Long id, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        return ApiResp.success(travelGuideService.delete(id, userId));
    }

    @GetMapping
    @Operation(summary = "查询")
    @ApiResponse(content = @Content(schema = @Schema(implementation = TravelGuide.class)))
    ApiResp getInfoById(@RequestParam Long id, Principal principal) {
        if(principal == null){
            return ApiResp.success(travelGuideService.getInfoById(id));
        }else{
            return ApiResp.success(travelGuideService.getInfoWithInteractionById(id, Long.valueOf(principal.getName())));
        }
    }

    @Secured("ROLE_USER")
    @PutMapping
    @Operation(summary = "更新")
    ApiResp updateInfo(@RequestBody TravelGuide guide, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        return ApiResp.success(travelGuideService.update(guide, userId));
    }

    @Secured("ROLE_USER")
    @PostMapping("/like")
    @Operation(summary = "点赞")
    ApiResp like(@RequestParam Long id, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = travelGuideService.like(id, userId);
        return ApiResp.from(result);
    }

    @Secured("ROLE_USER")
    @PostMapping("/cancel-like")
    @Operation(summary = "取消点赞")
    ApiResp cancelLike(@RequestParam Long id, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = travelGuideService.cancelLike(id, userId);
        return ApiResp.from(result);
    }

    @Secured("ROLE_USER")
    @PostMapping("/favorite")
    @Operation(summary = "收藏")
    ApiResp addFavorites(@RequestParam Long id, @RequestParam Long favoritesId, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = travelGuideService.editFavorites(id, favoritesId, userId, true);
        return ApiResp.from(result);
    }

    @Secured("ROLE_USER")
    @PostMapping("/cancel-favorite")
    @Operation(summary = "取消收藏")
    ApiResp removeFavorites(@RequestParam Long id, @RequestParam Long favoritesId, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = travelGuideService.editFavorites(id, favoritesId, userId, false);
        return ApiResp.from(result);
    }

    @GetMapping("/recommend")
    @Operation(summary = "获取推荐")
    ApiResp getRecommends(Pageable pageable) {
        var result = travelGuideService.getTravelGuides(pageable, true);
        return ApiResp.success(result);
    }

    @GetMapping("/by-resort")
    @Operation(summary = "获取基于景点的推荐")
    ApiResp getRecommendsByResort(@RequestParam Long id, Pageable pageable) {
        var result = travelGuideService.getTravelGuidesByResortId(id, pageable);
        return ApiResp.success(result);
    }

    @GetMapping("/by-user")
    @Operation(summary = "根据作者检索")
    ApiResp getRecommendsByUser(@RequestParam Long id) {
        var result = travelGuideService.getTravelGuidesByAuthorId(id);
        return ApiResp.success(result);
    }

}
