package spotify.songs;


import se.michaelthelin.spotify.model_objects.specification.*;
import com.neovisionaries.i18n.CountryCode;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsRelatedArtistsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;
import spotify.authorization.ClientCredentialsExample;

import java.io.IOException;

public class Spotify {

    private static final String accessToken = ClientCredentialsExample.getAccessToken();

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setAccessToken(accessToken)
            .build();


    public static Track getTrack(String id) {

        GetTrackRequest getTrackRequest = spotifyApi.getTrack(id)
//          .market(CountryCode.SE)
                .build();
        try {
            Track track = getTrackRequest.execute();

            return track;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static Playlist getPlaylist(String id) {
        try {

            GetPlaylistRequest getPlaylistRequest = spotifyApi.getPlaylist(id)
          .market(CountryCode.FR)
          .additionalTypes("track,episode")
                    .build();

            final Playlist playlist = getPlaylistRequest.execute();
//            System.out.println("Name: " + playlist.getName());

            return playlist;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args){
    }

}