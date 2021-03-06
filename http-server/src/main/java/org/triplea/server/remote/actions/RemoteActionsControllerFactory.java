package org.triplea.server.remote.actions;

import lombok.experimental.UtilityClass;
import org.jdbi.v3.core.Jdbi;
import org.triplea.lobby.server.db.dao.ModeratorAuditHistoryDao;
import org.triplea.lobby.server.db.dao.user.ban.UserBanDao;

@UtilityClass
public class RemoteActionsControllerFactory {

  public RemoteActionsController buildController(
      final Jdbi jdbi, final RemoteActionsEventQueue remoteActionsEventQueue) {
    return RemoteActionsController.builder()
        .remoteActionsModule(
            RemoteActionsModule.builder()
                .auditHistoryDao(jdbi.onDemand(ModeratorAuditHistoryDao.class))
                .userBanDao(jdbi.onDemand(UserBanDao.class))
                .remoteActionsEventQueue(remoteActionsEventQueue)
                .build())
        .build();
  }
}
