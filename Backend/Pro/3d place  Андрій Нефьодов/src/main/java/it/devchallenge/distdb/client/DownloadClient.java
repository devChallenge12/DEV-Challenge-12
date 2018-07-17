package it.devchallenge.distdb.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DownloadClient {
    private final Client client;

    public InputStream downloadInternal(String url, String userName, String fileName, int version) {
        log.info("downloadInternal start. url: {}, userName: {}, fileName: {}, version: {}",
                url, userName, fileName, version);
        final String uri =
                url + "/internal/download?filename=" + fileName +
                        "&username=" + userName +
                        "&version=" + version;
        final InputStream inputStream = client
                .register(MultiPartFeature.class)
                .target(uri)
                .request()
                .get()
                .readEntity(InputStream.class);
        log.info("downloadInternal end. url: {}, userName: {}, fileName: {}, version: {}",
                url, userName, fileName, version);
        return inputStream;
    }
}
