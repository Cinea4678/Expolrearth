package cc.cinea.huanyou.repository;

import cc.cinea.huanyou.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select case when count(u)>0 then true else false end from Comment c join c.likedUser u where c.id = :commentId and u.id = :userId")
    boolean isLikedByUser(@Param("commentId") Long commentId, @Param("userId") Long userId);
}
