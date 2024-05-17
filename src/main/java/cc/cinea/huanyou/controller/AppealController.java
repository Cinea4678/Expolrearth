package cc.cinea.huanyou.controller;

import cc.cinea.huanyou.dto.ApiResp;
import cc.cinea.huanyou.entity.Appeal;
import cc.cinea.huanyou.enums.AppealState;
import cc.cinea.huanyou.service.AppealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author cinea
 */
@RestController
@RequestMapping("/appeal")
@Tag(name = "Appeal", description = "申诉")
public class AppealController {
    AppealService appealService;

    public AppealController(AppealService appealService) {
        this.appealService = appealService;
    }

    @Secured("ROLE_USER")
    @PostMapping
    @Operation(summary = "发起申诉")
    ApiResp submitAppeal(@RequestBody Appeal appeal, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = appealService.submitAppeal(appeal, userId);
        return ApiResp.from(result);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping
    @Operation(summary = "修改申诉状态")
    ApiResp setAppealState(@RequestParam Long appealId, @RequestParam AppealState state) {
        var result = appealService.setAppealState(appealId, state);
        return ApiResp.from(result);
    }

    @Secured("ROLE_USER")
    @DeleteMapping
    @Operation(summary = "撤回申诉")
    ApiResp withdrawAppeal(@RequestParam Long appealId, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = appealService.withdrawAppeal(appealId, userId);
        return ApiResp.from(result);
    }

}
