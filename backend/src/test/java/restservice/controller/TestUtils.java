package restservice.controller;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;

import java.io.IOException;

public class TestUtils {

  public static String json(Object o, HttpMessageConverter mappingJackson2HttpMessageConverter) throws IOException {
    MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
    mappingJackson2HttpMessageConverter.write(
        o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
    return mockHttpOutputMessage.getBodyAsString();
  }

  public static String toJson(String o, Class clasz, HttpMessageConverter<Object> mappingJackson2HttpMessageConverter) throws IOException {
    MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
    mappingJackson2HttpMessageConverter.read(clasz, new MockHttpInputMessage(o.getBytes()));
    return mockHttpOutputMessage.getBodyAsString();
  }
}
