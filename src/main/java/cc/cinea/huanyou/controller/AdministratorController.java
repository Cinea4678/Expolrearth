package cc.cinea.huanyou.controller;

import cc.cinea.huanyou.dto.ApiResp;
import cc.cinea.huanyou.entity.Administrator;
import cc.cinea.huanyou.service.AdministratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author cinea
 */
@RestController
@RequestMapping("/admin")
@Tag(name = "Administrator", description = "管理员")
public class AdministratorController {

    AdministratorService administratorService;

    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping
    @Operation(summary = "获取当前用户信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Administrator.class)))
    ApiResp getSelfInfo(Principal principal) {
        var id = principal.getName();
        return ApiResp.success(administratorService.getAdministratorServiceInfoById(id).orElse(null));
    }

}
