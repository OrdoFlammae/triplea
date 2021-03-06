package org.triplea.server.user.account.update;

import com.google.common.base.Preconditions;
import io.dropwizard.auth.Auth;
import javax.annotation.Nonnull;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import lombok.Builder;
import org.triplea.http.client.lobby.user.account.FetchEmailResponse;
import org.triplea.http.client.lobby.user.account.UserAccountClient;
import org.triplea.java.ArgChecker;
import org.triplea.lobby.server.db.data.UserRole;
import org.triplea.server.access.AuthenticatedUser;
import org.triplea.server.http.HttpController;

/** Controller providing endpoints for user account management. */
@Builder
public class UpdateAccountController extends HttpController {
  @Nonnull private final UpdateAccountService userAccountService;

  @POST
  @Path(UserAccountClient.CHANGE_PASSWORD_PATH)
  @RolesAllowed(UserRole.PLAYER)
  public Response changePassword(
      @Auth final AuthenticatedUser authenticatedUser, final String newPassword) {
    ArgChecker.checkNotEmpty(newPassword);
    Preconditions.checkArgument(authenticatedUser.getUserIdOrThrow() > 0);

    userAccountService.changePassword(authenticatedUser.getUserIdOrThrow(), newPassword);
    return Response.ok().build();
  }

  @GET
  @Path(UserAccountClient.FETCH_EMAIL_PATH)
  @RolesAllowed(UserRole.PLAYER)
  public FetchEmailResponse fetchEmail(@Auth final AuthenticatedUser authenticatedUser) {
    Preconditions.checkArgument(authenticatedUser.getUserIdOrThrow() > 0);

    return new FetchEmailResponse(
        userAccountService.fetchEmail(authenticatedUser.getUserIdOrThrow()));
  }

  @POST
  @Path(UserAccountClient.CHANGE_EMAIL_PATH)
  @RolesAllowed(UserRole.PLAYER)
  public Response changeEmail(
      @Auth final AuthenticatedUser authenticatedUser, final String newEmail) {
    ArgChecker.checkNotEmpty(newEmail);
    Preconditions.checkArgument(authenticatedUser.getUserIdOrThrow() > 0);

    userAccountService.changeEmail(authenticatedUser.getUserIdOrThrow(), newEmail);
    return Response.ok().build();
  }
}
