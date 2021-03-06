package org.triplea.http.client.lobby.game;

import com.google.common.base.Preconditions;
import java.net.URI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.triplea.domain.data.ApiKey;
import org.triplea.http.client.AuthenticationHeaders;
import org.triplea.http.client.HttpClient;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectivityCheckClient {
  public static final String CONNECTIVITY_CHECK_PATH = "/lobby/check-connectivity";

  private final AuthenticationHeaders authenticationHeaders;
  private final ConnectivityCheckFeignClient connectivityCheckFeignClient;

  public static ConnectivityCheckClient newClient(final URI serverUri, final ApiKey apiKey) {
    return new ConnectivityCheckClient(
        new AuthenticationHeaders(apiKey),
        new HttpClient<>(ConnectivityCheckFeignClient.class, serverUri).get());
  }

  /**
   * Sends a request to the server to attempt a 'reverse' connection back to the local host. This
   * simulates other clients connecting to the local server. If the connection is successful, true
   * is returned. This can return false when a host's IP address is not public, in those cases the
   * reverse connection would fail.
   */
  public boolean checkConnectivity(final int port) {
    Preconditions.checkArgument(port > 0, "Port must be a positive number, was: " + port);
    Preconditions.checkArgument(
        port < Math.pow(2, 16), "Port must be less than max value (2^16), was: " + port);
    return connectivityCheckFeignClient.checkConnectivity(
        authenticationHeaders.createHeaders(), port);
  }
}
