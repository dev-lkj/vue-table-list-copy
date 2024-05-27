<!-- 스프링 서버에서 요청 날리기 -->
@RestController
@RequiredArgsConstructor
@Slf4j
public class OpenApiController {
    private final OpenApiManager openApiManager;

    @GetMapping("open-api")
    public ResponseEntity<?> fetch() throws UnsupportedEncodingException {
        return success(openApiManager.fetch().getBody());
    }
}


<!--  -->
@Component
public class OpenApiManager {
    private final String BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService";
    private final String apiUri = "/areaBasedList";
    private final String serviceKey = "?ServiceKey=디코딩 서비스 키";
    private final String defaultQueryParam = "&MobileOS=ETC&MobileApp=AppTest&_type=json";
    private final String numOfRows = "&numOfRows=100";
    private final String areaCode = "&areaCode=1";
    private final String contentTypeId = "&contentTypeId=12";


    private String makeUrl() throws UnsupportedEncodingException {
        return BASE_URL +
                apiUri +
                serviceKey +
                defaultQueryParam +
                numOfRows +
                areaCode +
                contentTypeId;
    }

    public ResponseEntity<?> fetch() throws UnsupportedEncodingException {
        System.out.println(makeUrl());
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<Map> resultMap = restTemplate.exchange(makeUrl(), HttpMethod.GET, entity, Map.class);
        System.out.println(resultMap.getBody());
        return resultMap;

    }
}

<!--  -->

<!-- 다른코드 -->
@Component
public class sendData {
    private static RestTemplate restTemplate;

    public static ResponseEntity<SubmitData> sendEngine() {
        int SubNum = 123;
        int Pnum = 1;
        Object Pcode = "코드";
        SubmitData requestDto = SubmitData.builder()
                .SubNum(SubNum)
                .Pnum(Pnum)
                .Pcode(Pcode)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<SubmitData> entity = new HttpEntity<>(requestDto, headers);

        String url = "http://localhost:8080/send";
        
        return restTemplate.exchange(url, HttpMethod.POST, entity, SubmitData.class);
    }
}

<!-- URI decoding -->
URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/local.json")
                .queryParam("query","약국")
                .queryParam("display",3)
                .queryParam("start", 1)
                .queryParam("sort","random")
                .encode()
                .build()
                .toUri();

        RequestEntity requestEntity = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id","네이버에서 받은 id")
                .header("X-Naver-Client-Secret","네이버에서 받은 secret key")
                .build();

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<NaverApiDto> searchList = restTemplate.exchange(requestEntity, NaverApiDto.class);
        log.info("uri:{}",uri);
        log.info("searchList:{}",searchList.getBody());
        return searchList.getBody();
    }

    <!--  -->
    https://blog.naver.com/hj_kim97/222295259904
    https://velog.io/@whdrb2643/SPRING-RestTemplate
    https://akku-dev.tistory.com/5#google_vignette
    https://velog.io/@lilseongwon/%EC%8A%A4%ED%94%84%EB%A7%81-RestTemplate2-%EC%98%88%EC%A0%9C