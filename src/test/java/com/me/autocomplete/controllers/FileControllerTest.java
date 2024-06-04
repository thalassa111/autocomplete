package com.me.autocomplete.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    //test for upload file endpoint
    @Test
    void uploadFile() throws Exception {
        //given - mock data
        String token = "test-token";
        int folderId = 1;
        MockMultipartFile file = new MockMultipartFile("file", "testfile.txt", MediaType.TEXT_PLAIN_VALUE, "some test text".getBytes());

        //when - mock upload file method
        when(fileService.uploadFile(token, 1, file)).thenReturn(ResponseEntity.ok("File uploaded successfully"));
        //mocked http request to the endpoint under test
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.multipart("/file/upload-file/{folderId}", folderId)
                .file(file)
                .header("Authorization", token);

        //then - do the http request and assertions
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("File uploaded successfully"));
    }

    //test for delete file endpoint
    @Test
    void deleteFile() throws Exception {
        //given - mock data
        String token = "test-token";
        int folderId = 1;
        String fileName = "testfile.txt";

        //when - mock delete file method
        when(fileService.deleteFile(token, folderId, fileName)).thenReturn(ResponseEntity.ok("File deleted successfully"));

        //mocked HTTP request to the endpoint under test
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/file/delete-file/{folderId}/{fileName}", folderId, fileName)
                .header("Authorization", token);

        //then - perform the HTTP request and assertions
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("File deleted successfully"));
    }
}