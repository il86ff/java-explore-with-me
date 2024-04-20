package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class StatisticsClient {
    private final String serverUrl;
    private final WebClient webClient;


    public StatisticsClient(@Value("${stats.server.url}") String serverUrl) {
        this.serverUrl = serverUrl;
        this.webClient = WebClient.builder().baseUrl(serverUrl).build();
    }

    public void addRequest(RequestDTO requestDto) {
        webClient.post().uri("/hit").bodyValue(requestDto).retrieve().bodyToMono(Object.class).block();
    }

    public ResponseEntity<List<RequestOutDTO>> getStats(String start,
                                                        String end,
                                                        List<String> uris,
                                                        Boolean unique) {

        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/stats")
                            .queryParam("start", start)
                            .queryParam("end", end);
                    if (uris != null)
                        uriBuilder.queryParam("uris", String.join(",", uris));
                    if (unique != null)
                        uriBuilder.queryParam("unique", unique);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ResponseEntity<List<RequestOutDTO>>>() {})
                .block();
    }

    public ResponseEntity<List<RequestOutDTO>> getStatsByIp(String start,
                                                            String end,
                                                            List<String> uris,
                                                            Boolean unique,
                                                            String ip) {

        ResponseEntity<List<RequestOutDTO>> listResponseEntity = webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/statsByIp")
                            .queryParam("start", start)
                            .queryParam("end", end)
                            .queryParam("ip", ip);
                    if (uris != null)
                        uriBuilder.queryParam("uris", String.join(",", uris));
                    if (unique != null)
                        uriBuilder.queryParam("unique", unique);
                    return uriBuilder.build();
                })
                .retrieve()
                .toEntityList(RequestOutDTO.class)
                .block();
        return listResponseEntity;
    }
}
