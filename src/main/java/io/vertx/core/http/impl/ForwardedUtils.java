package io.vertx.core.http.impl;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.net.SocketAddress;

/**
 * Class for extracting remote client data from HTTP headers.
 *
 * @see <a href="https://en.wikipedia.org/wiki/X-Forwarded-For">X-Forwarded-For on Wikipedia</a>
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Forwarded-For">X-Forwarded @ MDN Web Docs</a>
 * @see <a href="https://tools.ietf.org/html/rfc7239">RFC 7239: Forwarded HTTP Extension</a>
 * @see <a href="https://docs.aws.amazon.com/elasticloadbalancing/latest/classic/x-forwarded-headers.html">AWS ELB forwarded headers</a>
 * @see <a href="https://cloud.google.com/compute/docs/load-balancing/http/">GCP forwarded headers (search for x-forwarded-for)</a>
 */
class ForwardedUtils {
  private static final String HTTPS = "https";

  private final boolean supportForwarded;
  private final boolean supportXFF;
  private final boolean preferForwarded;

  /**
   * Creates new instance supporting all headers and <b>not</b> preferring {@code Forwarded} headers.
   */
  public ForwardedUtils() {
    this(true, true, false);
  }

  /**
   * Creates new instance.
   *
   * @param supportForwarded support {@code Forwarded} header?
   * @param supportXFF       support {@code X-Forwarded-*} headers?
   * @param preferForwarded  prefer {@code Forwarded} header to {@code X-Forwarded-*} headers?
   */
  public ForwardedUtils(boolean supportForwarded, boolean supportXFF, boolean preferForwarded) {
    if (!supportForwarded && !supportXFF) {
      throw new IllegalArgumentException("At least one header set needs to be supported.");
    }
    this.supportForwarded = supportForwarded;
    this.supportXFF = supportXFF;
    this.preferForwarded = preferForwarded;
  }

  /**
   * Returns remote address from request headers
   *
   * @param headers           HTTP request headers
   * @param connectionAddress original connection address
   * @return remote socket address if it can be determined from HTTP headers, otherwise original connection address.
   * @see HttpHeaders#X_FORWARDED_FOR
   * @see HttpHeaders#X_FORWARDED_PORT
   * @see HttpHeaders#FORWARDED
   * @see <a href="https://en.wikipedia.org/wiki/X-Forwarded-For">X-Forwarded-For on Wikipedia</a>
   * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Forwarded-For">X-Forwarded @ MDN Web Docs</a>
   * @see <a href="https://tools.ietf.org/html/rfc7239">RFC 7239: Forwarded HTTP Extension</a>
   */
  public SocketAddress getRemoteAddress(MultiMap headers, SocketAddress connectionAddress) {
    if (headers == null || headers.isEmpty()) {
      return connectionAddress;
    }

    final String xfFor = headers.get(HttpHeaders.X_FORWARDED_FOR);
    final String xfPort = headers.get(HttpHeaders.X_FORWARDED_PORT);
    final String xForwarded = headers.get(HttpHeaders.FORWARDED);

    return null;
  }

  /**
   * Tells whether remote client uses TLS/SSL connection.
   *
   * @param headers         HTTP request headers
   * @param connectionValue default value from connection.
   * @return value from headers if exists, otherwise {@code connectionValue}
   */
  public boolean isSsl(MultiMap headers, boolean connectionValue) {
    if (headers == null || headers.isEmpty()) {
      return connectionValue;
    }

    final String proto = headers.get(HttpHeaders.X_FORWARDED_PROTO);
    if (proto != null && !proto.isEmpty()) {
      return HTTPS.equalsIgnoreCase(proto);
    }

    return connectionValue;
  }
}
