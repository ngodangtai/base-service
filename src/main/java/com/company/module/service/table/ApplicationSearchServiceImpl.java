package com.company.module.service.table;

import com.company.module.dto.CountryDto;
import com.company.module.base.dto.PageView;
import com.company.module.dto.ApplicationDto;
import com.company.module.entity.ApplicationEntity;
import com.company.module.repository.ApplicationRepository;
import com.company.module.repository.custom.ApplicationCustomRepository;
import com.company.module.request.ApplicationSearchReq;
import com.company.module.utils.ConvertUtils;
import com.company.module.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationSearchServiceImpl implements ApplicationSearchService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationCustomRepository applicationCustomRepository;
    private final RestTemplate restTemplate;

    @Value("${service.profile.url}")
    private String profileUrl;

    @Override
    @Cacheable(cacheNames = "ApplicationSearchService#getApplications")
    public List<ApplicationDto> getApplications() {
        return applicationRepository.findAll().stream().map(e -> ConvertUtils.convertDto((ApplicationEntity) e)).collect(Collectors.toList());
    }

    @Override
    public PageView<ApplicationDto> searchApplication(ApplicationSearchReq request) {
        PageView<ApplicationEntity> pageView = applicationCustomRepository.search(request);
        List<ApplicationDto> applicationDtos = pageView.getContent().stream()
                .map(a -> MapperUtils.convertValue(a, ApplicationDto.class)).collect(Collectors.toList());
        return new PageView<ApplicationDto>().convert(applicationDtos, pageView.getTotalPage(), pageView.getTotalRow());
    }

    @Override
    public List<CountryDto> getPhoneCodes() {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(profileUrl + "/list-phone-code");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<List<CountryDto>> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {});
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception ex) {
            log.error("getPhoneCodes exception cause: {}", ex.getCause(), ex);
            throw ex;
        }
        return null;
    }
}
