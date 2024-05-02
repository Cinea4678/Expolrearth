package cc.cinea.huanyou.controller;

import cc.cinea.huanyou.dto.ApiResp;
import cc.cinea.huanyou.entity.Ban;
import cc.cinea.huanyou.service.BanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * @author LevisT
 */
@RestController
@RequestMapping("/ban")
@Tag(name = "Ban", description = "封禁")
public class BanController {
    BanService banService;

    public BanController(BanService banService) {
        this.banService = banService;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    @Operation(summary = "新建封禁")
    ApiResp addBan(@RequestBody Ban ban, @RequestParam Long userId) {
        var result = banService.addBan(ban, userId);
        return ApiResp.from(result);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping
    @Operation(summary = "修改封禁")
    ApiResp editBan(@RequestBody Ban ban) {
        var result = banService.editBan(ban);
        return ApiResp.from(result);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping
    @Operation(summary = "取消封禁")
    ApiResp deleteBan(@RequestParam Long banId) {
        var result = banService.deleteBan(banId);
        return ApiResp.from(result);
    }
}
