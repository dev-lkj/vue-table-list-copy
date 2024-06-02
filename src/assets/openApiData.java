package com.project.animal.sample.openApi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.animal.adoption.service.AdoptionServiceImpl;
import com.project.animal.sample.openApi.dto.OpenApiDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
@RestController
public class OpenApiData {

    private final AdoptionServiceImpl adoptionService;

    // 해당 URL을 통해 DB에서 가져올 수 있도록 코드 작성
    // 추후 삭제 예정
    @GetMapping("/migration/open")
    public void test() throws URISyntaxException, JsonProcessingException {


        try{
            String stringResult = getResult(11); // api 데이터 가져오기

            List<OpenApiDto> openApiDtoList = getOpenApiDtos(stringResult); // 데이터 사용할 수 있게 가공하기

            // DB에 담기
            adoptionService.apiSave(openApiDtoList);

            log.info("success: >>");
        } catch (Exception ex) {
            log.info("fail: >>");
            log.error(ex.getMessage());

        }



    }

    @Nullable
    private static String getResult(int id) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        String fullUrl = "http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic?serviceKey=EzxLIzlCVgYLjZw0%2F80cOsMioDse3rHqkMt05%2FHotyGORQ3xcT6%2BFyqR2o%2B8XXuNh37OnCNoCGE7twsnT2N%2Bkg%3D%3D&pageNo=" + id + "&numOfRows=10&_type=json";


        URI uri = new URI(fullUrl);

        String stringResult = restTemplate.getForObject(uri, String.class);
        return stringResult;
    }



    @NotNull
    private static List<OpenApiDto> getOpenApiDtos(String stringResult) throws JsonProcessingException {
        // JSON 문자열을 DTO 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> test = objectMapper.readValue(stringResult, Map.class);
        Map<String,Object> response = (Map<String, Object>) test.get("response");
        Map<String,Object> body = (Map<String, Object>) response.get("body");
        Map<String,Object> itemss = (Map<String, Object>) body.get("items");
        List<Object> items = (List<Object>) itemss.get("item");

        List<OpenApiDto> openApiDtoList = new ArrayList<>();

        for(int i=0; i<items.size(); i++){
                Map<String, String> obj = (Map<String, String>) items.get(i);
                OpenApiDto openApiDto = new OpenApiDto(
                    obj.get("desertionNo"),
                    obj.get("filename"),
                    obj.get("happenDt"),
                    obj.get("happenPlace"),
                    obj.get("kindCd"),
                    obj.get("colorCd"),
                    obj.get("age"),
                    obj.get("weight"),
                    obj.get("noticeNo"),
                    obj.get("noticeSdt"),
                    obj.get("noticeEdt"),
                    obj.get("popfile"),
                    obj.get("processState"),
                    obj.get("sexCd"),
                    obj.get("neuterYn"),
                    obj.get("specialMark"),
                    obj.get("careNm"),
                    obj.get("careTel"),
                    obj.get("careAddr"),
                    obj.get("orgNm"),
                    obj.get("chargeNm"),
                    obj.get("officetel")
            );
            openApiDtoList.add(openApiDto);

        }

//        System.out.println("openApiDtoList:>>"+openApiDtoList);
        return openApiDtoList;
    }
}


