package spotify.authorization;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class ClientCredentialsExample {
    private static final String clientId = "bd39cceea89947b7bed5d11d14a3c97a";
    private static final String clientSecret = "d8b3c162864a417c94ed259116a9e929";
    public static String accessToken;

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();

    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
            .build();

    public static String getAccessToken(){

        ClientCredentials clientCredentials = null;

        try {
            clientCredentials = clientCredentialsRequest.execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String accessToken = clientCredentials.getAccessToken();
        spotifyApi.setAccessToken(accessToken);

        return accessToken;
    }
}