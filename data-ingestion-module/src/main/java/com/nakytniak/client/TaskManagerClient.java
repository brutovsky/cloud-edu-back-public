package com.nakytniak.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.api.client.http.HttpRequest;
import com.nakytniak.client.dto.DataflowTaskDto;
import com.nakytniak.common.api.CoreResponse;
import com.nakytniak.common.api.client.SecuredGoogleHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskManagerClient {

    private static final String TASKS_ENDPOINT = "/tasks";

    @Value("${task.manager.client.url}")
    private String taskManagerUrl;
    private final SecuredGoogleHttpClient googleHttpClient;

    public CoreResponse<List<DataflowTaskDto>> getTasks(final String schoolId) throws IOException {
        final HttpRequest request = googleHttpClient.createRequest(
                taskManagerUrl + TASKS_ENDPOINT + "?schoolId=" + schoolId, HttpMethod.GET, null);
        log.info("request: {}", request);
        final CoreResponse<List<DataflowTaskDto>> res = googleHttpClient.performRequest(request, new TypeReference<>() {
        });
        return res;
    }

    public CoreResponse<DataflowTaskDto> getTask(final Integer taskId, final String schoolId)
            throws IOException {
        final HttpRequest request = googleHttpClient.createRequest(
                taskManagerUrl + TASKS_ENDPOINT + "/" + taskId + "?schoolId=" + schoolId, HttpMethod.GET, null);
        log.info("request: {}", request);
        final CoreResponse<DataflowTaskDto> res = googleHttpClient.performRequest(request, new TypeReference<>() {
        });
        return res;
    }

    public CoreResponse<DataflowTaskDto> createTask(final String type, final String entityType, final String schoolId,
            final String filename) throws IOException {
        final HttpRequest request = googleHttpClient.createRequest(
                taskManagerUrl + TASKS_ENDPOINT + "?schoolId=" + schoolId + "&entityType=" + entityType + "&type="
                        + type + "&filename=" + filename, HttpMethod.POST, null);
        log.info("request: {}", request);
        final CoreResponse<DataflowTaskDto> res = googleHttpClient.performRequest(request, new TypeReference<>() {
        });
        return res;
    }


}
