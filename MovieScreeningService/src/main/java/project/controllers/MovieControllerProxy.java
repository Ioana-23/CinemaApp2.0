package project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;
import project.controllers.response.Response;
import project.dtos.MovieDTO;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class MovieControllerProxy {
    private static String ip_address = "";
    public MovieDTO getMovieByUuid(int uuid) throws IOException, InterruptedException {
        if (ip_address.isBlank()) {
            if (isRunningInsideDocker()) {
                ip_address = "host.docker.internal";
            } else {
                getIpAddress();
            }
        }
        HttpResponse<String> response = make_call("http://" + ip_address + ":8083/project/movies/movie/" + uuid);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        if (response.body().isBlank()) {
            return null;
        }
        return mapper.readValue(mapper.writeValueAsString(mapper.readValue(response.body(), Response.class).getResponseObject()), MovieDTO.class);

    }

    private static void getIpAddress() throws UnknownHostException, SocketException {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip_address = socket.getLocalAddress().getHostAddress();
        }
    }

    private static Boolean isRunningInsideDocker() {

        try (Stream<String> stream =
                     Files.lines(Paths.get("/proc/1/cgroup"))) {
            return stream.anyMatch(line -> line.contains("/docker"));
        } catch (IOException e) {
            return false;
        }
    }

    private HttpResponse<String> make_call(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}
