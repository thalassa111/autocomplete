package com.me.autocomplete.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    @InjectMocks
    private FileService fileService;

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    void deleteFile() {
        //given - mock data
        String token = "test-token";
        int folderId = 1;
        String fileName = "testfile.txt";

        //mocking methods
        when(jwtUtil.verifyToken(token)).thenReturn(true);
        when(jwtUtil.getSubjectFromToken(token)).thenReturn("1");
        when(folderRepository.findByIdAndUser_Id(folderId, 1)).thenReturn(Optional.of(new Folder()));
        when(fileRepository.existsByFileNameAndFolder(eq(fileName), any(Folder.class))).thenReturn(true);
        doNothing().when(fileRepository).deleteFileByFileName(fileName);

        //when - calling test method
        ResponseEntity<String> response = fileService.deleteFile(token, folderId, fileName);

        //then - assertions and verifications
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("file -> testfile.txt from folder with id -> 1 has been deleted successfully", response.getBody());
        //verify that methods were only called once
        verify(jwtUtil, times(1)).verifyToken(token);
        verify(jwtUtil, times(1)).getSubjectFromToken(token);
        verify(folderRepository, times(1)).findByIdAndUser_Id(folderId, 1);
        verify(fileRepository, times(1)).existsByFileNameAndFolder(eq(fileName), any(Folder.class));
        verify(fileRepository, times(1)).deleteFileByFileName(fileName);
    }
}