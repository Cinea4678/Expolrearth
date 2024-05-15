package cc.cinea.huanyou.service.impl;

import cc.cinea.huanyou.entity.Comment;
import cc.cinea.huanyou.entity.RegisteredUser;
import cc.cinea.huanyou.repository.CommentRepository;
import cc.cinea.huanyou.repository.RegistedUserRepository;
import cc.cinea.huanyou.repository.TravelGuideRepository;
import cc.cinea.huanyou.repository.TravelRecordRepository;
import cc.cinea.huanyou.service.CommentService;
import com.spencerwi.either.Either;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author LevisT
 */
@Service
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    TravelGuideRepository travelGuideRepository;
    TravelRecordRepository travelRecordRepository;
    RegistedUserRepository registedUserRepository;

    public CommentServiceImpl(CommentRepository commentRepository, TravelGuideRepository travelGuideRepository, TravelRecordRepository travelRecordRepository, RegistedUserRepository registedUserRepository) {
        this.commentRepository = commentRepository;
        this.travelGuideRepository = travelGuideRepository;
        this.travelRecordRepository = travelRecordRepository;
        this.registedUserRepository = registedUserRepository;
    }

    @Override
    @Transactional
    public Either<Void, String> addComment(Comment comment, Long authorId, Long writtenTo, boolean writtenToGuide) {
        var authorOptional = registedUserRepository.findById(authorId);
        if (authorOptional.isEmpty()) {
            return Either.right("用户不存在");
        }
        var author = authorOptional.get();

        if (author.isBanned()) {
            return Either.right("用户被封禁，不可评论");
        }

        comment.setId(null);
        comment.setAuthor(author);
        comment.setTime(LocalDateTime.now());
        comment.setLikes(0L);

        if (writtenToGuide) {
            var guideOptional = travelGuideRepository.findById(writtenTo);
            if (guideOptional.isEmpty()) {
                return Either.right("没有找到对应的旅行攻略");
            }

            var newComment = commentRepository.save(comment);
            guideOptional.get().getComments().add(newComment);
        } else {
            var recordOptional = travelRecordRepository.findById(writtenTo);
            if (recordOptional.isEmpty()) {
                return Either.right("没有找到对应的旅行记录");
            }

            var newComment = commentRepository.save(comment);
            recordOptional.get().getComments().add(newComment);
        }

        return Either.left(null);
    }

    @Override
    public Either<Void, String> editComment(Comment comment, Long operatorId) {
        var oldCommentOptional = commentRepository.findById(comment.getId());
        if (oldCommentOptional.isEmpty()) {
            return Either.right("评论不存在");
        }

        var oldComment = oldCommentOptional.get();
        if (!oldComment.getAuthor().getId().equals(operatorId)) {
            return Either.right("您不能编辑这条评论");
        }

        var authorOptional = registedUserRepository.findById(operatorId);
        if (authorOptional.isEmpty()) {
            return Either.right("用户不存在");
        }
        var author = authorOptional.get();

        if (author.isBanned()) {
            return Either.right("用户被封禁");
        }

        oldComment.setContent(comment.getContent());
        commentRepository.save(oldComment);

        return Either.left(null);
    }

    @Override
    public Either<Void, String> removeComment(Long commentId, Long operatorId) {
        var oldCommentOptional = commentRepository.findById(commentId);
        if (oldCommentOptional.isEmpty()) {
            return Either.right("评论不存在");
        }

        var oldComment = oldCommentOptional.get();
        if (!oldComment.getAuthor().getId().equals(operatorId)) {
            return Either.right("您不能删除这条评论");
        }

        commentRepository.deleteById(commentId);
        return Either.left(null);
    }

    @Override
    @Transactional
    public Either<Void, String> addReply(Comment comment, Long authorId, Long replyTo) {
        var repliedCommentOptional = commentRepository.findById(replyTo);
        if (repliedCommentOptional.isEmpty()) {
            return Either.right("被回复的评论不存在");
        }
        var repliedComment = repliedCommentOptional.get();

        var authorOptional = registedUserRepository.findById(authorId);
        if (authorOptional.isEmpty()) {
            return Either.right("用户不存在");
        }
        var author = authorOptional.get();

        if (author.isBanned()) {
            return Either.right("用户被封禁，不可回复");
        }

        comment.setId(null);
        comment.setAuthor(author);
        comment.setTime(LocalDateTime.now());
        comment.setLikes(0L);

        var newComment = commentRepository.save(comment);
        repliedComment.getReply().add(newComment);
        commentRepository.save(repliedComment);

        return Either.left(null);
    }

    @Override
    public Either<Void, String> like(Long commentId, Long operatorId) {
        var commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isEmpty()) {
            return Either.right("评论不存在");
        }

        var comment = commentOptional.get();
        if (commentRepository.isLikedByUser(commentId, operatorId)) {
            return Either.right("您已经为评论点过赞了");
        }

        var user = new RegisteredUser();
        user.setId(operatorId);
        comment.getLikedUser().add(user);
        comment.setLikes(comment.getLikes() + 1);

        commentRepository.save(comment);
        return Either.left(null);
    }

    @Override
    public Either<Void, String> cancelLike(Long commentId, Long operatorId) {
        var commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isEmpty()) {
            return Either.right("评论不存在");
        }

        var comment = commentOptional.get();
        if (!commentRepository.isLikedByUser(commentId, operatorId)) {
            return Either.right("您还没有为评论点过赞");
        }

        var user = registedUserRepository.findById(operatorId).orElse(null);
        comment.getLikedUser().remove(user);
        comment.setLikes(comment.getLikes() + 1);

        commentRepository.save(comment);
        return Either.left(null);
    }
}
