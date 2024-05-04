package cc.cinea.huanyou.controller;

import cc.cinea.huanyou.dto.ApiResp;
import cc.cinea.huanyou.entity.RegisteredUser;
import cc.cinea.huanyou.service.RegisteredUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
@Tag(name = "RegisteredUser", description = "注册用户")
public class RegisteredUserController {

    RegisteredUserService registeredUserService;

    public RegisteredUserController(RegisteredUserService registeredUserService) {
        this.registeredUserService = registeredUserService;
    }

    @GetMapping
    @Operation(summary = "获取当前用户信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = RegisteredUser.class)))
    ApiResp getSelfInfo(Principal principal) {
        if (principal == null) {
            return ApiResp.failure(4001, "用户未登录");
        }
        var id = Long.parseLong(principal.getName());
        return ApiResp.from(registeredUserService.getUserInfoById(id));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取指定用户信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = RegisteredUser.class)))
    ApiResp getUserInfo(@PathVariable Long id) {
        return ApiResp.from(registeredUserService.getUserInfoById(id));
    }

    @PostMapping("/register")
    @Operation(summary = "新用户注册")
    @ApiResponse(content = @Content(schema = @Schema(implementation = RegisteredUser.class)))
    ApiResp register(@RequestBody RegisteredUser user) {
        return ApiResp.from(registeredUserService.register(user));
    }

    @PostMapping("/register/check-phone")
    @Operation(summary = "检查手机号")
    @ApiResponse(content = @Content(schema = @Schema(implementation = RegisteredUser.class)))
    ApiResp checkPhoneNumber(@RequestParam String phoneNumber) {
        return ApiResp.from(registeredUserService.checkPhoneNumberValidate(phoneNumber));
    }


}
