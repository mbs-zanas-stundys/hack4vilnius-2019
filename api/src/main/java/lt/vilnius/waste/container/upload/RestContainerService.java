package lt.vilnius.waste.container.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class RestContainerService {

    private final RestTemplate template;

    private final String baseUrl;

    public RestContainerService(RestTemplateBuilder builder,
                                @Value("${vilnius.api.url}") String baseUrl) {
        this.template = builder.build();
        this.baseUrl = baseUrl;
    }


    public Page<ContainerDto> getContainers(PageRequest request) {
        URI requestUri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/waste-managment/containers")
                .queryParam("limit", request.getSize())
                .queryParam("page", request.getPageNo())
                .build()
                .toUri();

        return fetchFromUrl(requestUri.toString(), request, new ParameterizedTypeReference<>() {
        });
    }

    public Page<ScheduleDto> getSchedulesPageForDate(PageRequest request, LocalDate dateFrom, LocalDate dateTo) {
        URI requestUri = buildUrl("/waste-managment/container-schedule", dateFrom, dateTo, request);

        return fetchFromUrl(requestUri.toString(), request, new ParameterizedTypeReference<>() {
        });
    }

    public Page<PickupDto> getPickupsForDateRange(PageRequest request, LocalDate dateFrom, LocalDate dateTo) {
        URI requestUri = buildUrl("/waste-managment/container-schedule", dateFrom, dateTo, request);

        return fetchFromUrl(requestUri.toString(), request, new ParameterizedTypeReference<>() {
        });
    }

    private <T> Page<T> fetchFromUrl(String uri, PageRequest request, ParameterizedTypeReference<Page<T>> responseType) {
        return
                Optional.ofNullable(template.exchange(
                        uri,
                        HttpMethod.GET,
                        null,
                        responseType))
                        .map(HttpEntity::getBody)
                        .map(i -> i.withRequest(request))
                        .orElse(Page.empty());
    }

    private URI buildUrl(String path, LocalDate dateFrom, LocalDate dateTo, PageRequest request) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(path)
                .queryParam("dateFrom", dateFrom)
                .queryParam("dateTo", dateTo)
                .queryParam("limit", request.getSize())
                .queryParam("page", request.getPageNo())
                .build()
                .toUri();
    }
}
