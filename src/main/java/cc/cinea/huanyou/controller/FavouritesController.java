package cc.cinea.huanyou.controller;


import cc.cinea.huanyou.dto.ApiResp;
import cc.cinea.huanyou.entity.RegisteredUser;
import cc.cinea.huanyou.service.FavoritesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author LevisT
 */
@RestController
@RequestMapping("/favorites")
@Tag(name = "Favorites", description = "收藏夹")
public class FavoritesController {
    FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }


    @Secured("ROLE_USER")
    @PostMapping
    @Operation(summary = "创建收藏夹")
    ApiResp create(@RequestParam String name, Principal principal) {
        var operatorId = Long.parseLong(principal.getName());
        var result = favoritesService.createFavorites(name, operatorId);
        return ApiResp.from(result);
    }

    @Secured("ROLE_USER")
    @PutMapping
    @Operation(summary = "更改收藏夹名称")
    ApiResp setName(@RequestParam Long id, @RequestParam String name, Principal principal) {
        var operatorId = Long.parseLong(principal.getName());
        var result = favoritesService.setFavoritesName(id, name, operatorId);
        return ApiResp.from(result);
    }

    @GetMapping("/user")
    @Operation(summary = "检索用户创建的收藏夹")
    @ApiResponse(content = @Content(schema = @Schema(implementation = RegisteredUser.class)))
    ApiResp getByUserId(@RequestParam Long userId) {
        var result = favoritesService.getFavoritesByUserId(userId);
        return ApiResp.from(result);
    }

    @GetMapping("/following")
    @Operation(summary = "检索用户关注的收藏夹")
    @ApiResponse(content = @Content(schema = @Schema(implementation = RegisteredUser.class)))
    ApiResp getFollowingByUserId(@RequestParam Long userId) {
        var result = favoritesService.getFollowingFavoritesByUserId(userId);
        return ApiResp.from(result);
    }

    @GetMapping
    @Operation(summary = "查看收藏夹内容")
    @ApiResponse(content = @Content(schema = @Schema(implementation = RegisteredUser.class)))
    ApiResp getById(@RequestParam Long id) {
        var result = favoritesService.getFavoritesByFavoritesId(id);
        return ApiResp.from(result);
    }

    @Secured("ROLE_USER")
    @PostMapping("/follow")
    @Operation(summary = "关注收藏夹")
    @ApiResponse(content = @Content(schema = @Schema(implementation = RegisteredUser.class)))
    ApiResp follow(@RequestParam Long id, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = favoritesService.followFavorites(id, userId);
        return ApiResp.from(result);
    }

    @Secured("ROLE_USER")
    @PostMapping("/stop-follow")
    @Operation(summary = "停止关注收藏夹")
    @ApiResponse(content = @Content(schema = @Schema(implementation = RegisteredUser.class)))
    ApiResp stopFollow(@RequestParam Long id, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = favoritesService.stopFollowFavorites(id, userId);
        return ApiResp.from(result);
    }

}
