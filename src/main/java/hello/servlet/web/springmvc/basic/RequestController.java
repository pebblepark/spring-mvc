package hello.servlet.web.springmvc.basic;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Slf4j
@Controller
@RequestMapping("/request")
public class RequestController {

    @RequestMapping("/headers")
    public String headers(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpMethod httpMethod,
                          Locale locale,
                          @RequestHeader MultiValueMap<String, String> headerMap,
                          @RequestHeader("host") String host,
                          @CookieValue(value = "myCookie", required = false) String cookie) {
        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale);
        log.info("headerMap={}", headerMap);
        log.info("host={}", host);
        log.info("cookie={}", cookie);

        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param")
    public String requestParam(
            @RequestParam(name = "username") String memberName,
            @RequestParam String username,
            @RequestParam(name = "age", defaultValue = "-1") int memberAge,
            Integer age,
            @RequestParam(required = true) boolean required,
            @RequestParam MultiValueMap<String, Object> paramMap) {

        log.info("memberName={}", memberName);
        log.info("username={}", username);
        log.info("memberAge={}", memberAge);
        log.info("age={}", age);
        log.info("required={}", required);
        log.info("paramMap={}", paramMap);

        return "ok";
    }

    @ResponseBody
    @RequestMapping("/model-attribute")
    public String modeAttribute(@ModelAttribute HelloData helloData) {
        log.info("helloData={}", helloData);
        return "ok";
    }

    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);

        response.getWriter().write("ok");
    }

    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);

        responseWriter.write("ok");
    }

    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) {
        /*
         * HttpEntity: HTTP header, body ????????? ???????????? ??????, ?????? ??????????????? ???????????? ????????? ?????? x
         * HttpEntity ??? ???????????? RequestEntity, ResponseEntity ??????
         * */
        String messageBody = httpEntity.getBody();
        log.info("messageBody={}", messageBody);

        return new HttpEntity<>("ok");
    }

    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) {
        /*
         * ?????? ???????????? ??????: @RequestParam, @ModelAttribute -> ?????? ??????
         * HTTP ????????? ?????? ?????? ?????? : @RequestBody -> ?????? ?????????
         * */
        log.info("messageBody={}", messageBody);

        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json")
    public HelloData requestBodyJson(@RequestBody HelloData data) {
        /*
         *  @RequestBody ?????? : JSON ?????? -> HTTP ????????? ????????? -> ??????
         *  @ResponseBody ?????? : ?????? -> HTTP ????????? ????????? -> JSON
         * */
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return data;
    }
}

@Data
class HelloData {
    private String username;
    private int age;
}
