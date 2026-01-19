package com.edf.demo.component;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edf.demo.DemoApplication;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = DemoApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ComponentTests {

  @Autowired private MockMvc mvc;

  @Test
  void testLoadFiles() throws Exception {
    var response =
        mvc.perform(get("/edf/files?fileName=ZE-970-007-593.edf"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

    JSONAssert.assertEquals(loadJson("json/expected.json"), response, true);
  }

  @Test
  void testFileNames() throws Exception {
    mvc.perform(get("/edf/file-names"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0]", equalTo("ZE-970-007-593.edf")));
  }

  @Test
  void testLoadData() throws Exception {
    mvc.perform(
            get("/edf/data")
                .queryParam("fileName", "ZE-970-007-593.edf")
                .queryParam("fromRecording", "0")
                .queryParam("toRecording", "1")
                .queryParam("signal", "EEG C3")
                .queryParam("precision", "0.5"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(250)));
  }

  private String loadJson(String path) throws IOException {
    return new String(
        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path))
            .readAllBytes(),
        StandardCharsets.UTF_8);
  }
}
