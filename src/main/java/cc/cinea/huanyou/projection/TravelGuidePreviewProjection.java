package cc.cinea.huanyou.projection;

import cc.cinea.huanyou.entity.RegisteredUser;

import java.time.LocalDateTime;
import java.util.List;

public interface TravelGuidePreviewProjection {
    Long getId();

    RegisteredUser getAuthor();

    String getTitle();

    List<String> getImages();

    LocalDateTime getPublishTime();

    Long getLikes();

    Long getFavorites();
}
