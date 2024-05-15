package cc.cinea.huanyou.controller;

import cc.cinea.huanyou.dto.ApiResp;
import cc.cinea.huanyou.entity.Comment;
import cc.cinea.huanyou.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author LevisT
 */
@RestController
@RequestMapping("/comment")
@Tag(name = "Comment", description = "评论")
public class CommentController {

    CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @Secured("ROLE_USER")
    @PostMapping
    @Operation(summary = "添加评论")
    ApiResp addComment(@RequestBody Comment comment, @RequestParam Long to, @RequestParam boolean toGuide, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = commentService.addComment(comment, userId, to, toGuide);
        return ApiResp.from(result);
    }

    @Secured("ROLE_USER")
    @PutMapping
    @Operation(summary = "编辑评论")
    ApiResp editComment(@RequestBody Comment comment, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = commentService.editComment(comment, userId);
        return ApiResp.from(result);
    }

    @Secured("ROLE_USER")
    @DeleteMapping
    @Operation(summary = "移除评论")
    ApiResp removeComment(@RequestParam Long commentId, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = commentService.removeComment(commentId, userId);
        return ApiResp.from(result);
    }

    @Secured("ROLE_USER")
    @PostMapping("/reply")
    @Operation(summary = "添加回复")
    ApiResp addReply(@RequestBody Comment comment, @RequestParam Long to, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = commentService.addReply(comment, userId, to);
        return ApiResp.from(result);
    }

    @Secured("ROLE_USER")
    @PostMapping("/like")
    @Operation(summary = "点赞")
    ApiResp like(@RequestParam Long id, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = commentService.like(id, userId);
        return ApiResp.from(result);
    }

    @Secured("ROLE_USER")
    @PostMapping("/cancel-like")
    @Operation(summary = "取消点赞")
    ApiResp cancelLike(@RequestParam Long id, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        var result = commentService.cancelLike(id, userId);
        return ApiResp.from(result);
    }
}
