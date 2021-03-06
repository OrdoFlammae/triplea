package org.triplea.server.user.account.login.authorizer.anonymous;

import java.util.Optional;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import org.jdbi.v3.core.Jdbi;
import org.triplea.domain.data.UserName;
import org.triplea.lobby.server.db.dao.UserJdbiDao;
import org.triplea.server.lobby.chat.event.processing.Chatters;

@UtilityClass
public class AnonymousLoginFactory {

  public static Function<UserName, Optional<String>> build(
      final Jdbi jdbi, final Chatters chatters) {
    return AnonymousLogin.builder()
        .chatters(chatters)
        .userJdbiDao(jdbi.onDemand(UserJdbiDao.class))
        .build();
  }
}
