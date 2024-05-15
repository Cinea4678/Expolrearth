package cc.cinea.huanyou.service;

import cc.cinea.huanyou.entity.Comment;
import com.spencerwi.either.Either;

/**
 * @author LevisT
 */
public interface CommentService {

    Either<Void, String> addComment(Comment comment, Long authorId, Long writtenTo, boolean writtenToGuide);

    Either<Void, String> editComment(Comment comment, Long operatorId);

    Either<Void, String> removeComment(Long commentId, Long operatorId);

    Either<Void, String> addReply(Comment comment, Long authorId, Long replyTo);

    Either<Void, String> like(Long commentId, Long operatorId);

    Either<Void, String> cancelLike(Long commentId, Long operatorId);
}
