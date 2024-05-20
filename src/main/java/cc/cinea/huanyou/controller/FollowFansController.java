package cc.cinea.huanyou.controller;

import cc.cinea.huanyou.dto.ApiResp;
import cc.cinea.huanyou.entity.RegisteredUser;
import cc.cinea.huanyou.service.FollowFansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/follow")
@Tag(name = "Follow & Fans", description = "关注和被关注")
public class FollowFansController {

    FollowFansService followFansService;

    public FollowFansController(FollowFansService followFansService) {
        this.followFansService = followFansService;
    }
    
    @Secured("ROLE_USER")
    @PostMapping("/follow")
    @Operation(summary = "关注用户")
    ApiResp follow(@RequestParam Long id, Principal principal) {
        var operatorId = Long.parseLong(principal.getName());
        var result = followFansService.follow(id, operatorId);
        return ApiResp.from(result);
    }

    @Secured("ROLE_USER")
    @PostMapping("/stop-follow")
    @Operation(summary = "停止关注用户")
    ApiResp stopFollow(@RequestParam Long id, Principal principal) {
        var operatorId = Long.parseLong(principal.getName());
        var result = followFansService.stopFollow(id, operatorId);
        return ApiResp.from(result);
    }
}
