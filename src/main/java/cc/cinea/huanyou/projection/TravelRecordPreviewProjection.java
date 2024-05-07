package cc.cinea.huanyou.projection;

import cc.cinea.huanyou.entity.RegisteredUser;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.util.List;

public interface TravelRecordPreviewProjection {
    Long getId();

    RegisteredUser getAuthor();

    String getTitle();

    List<String> getImages();

    LocalDateTime getPublishTime();

    Long getLikes();

    Long getFavorites();
}
