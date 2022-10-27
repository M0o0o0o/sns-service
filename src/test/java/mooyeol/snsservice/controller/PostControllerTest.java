package mooyeol.snsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mooyeol.snsservice.domain.Post;
import mooyeol.snsservice.dto.PostAddDto;
import mooyeol.snsservice.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@WithMockUser
class PostControllerTest {
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext ctx;

    @MockBean
    PostService postService;

    @PersistenceContext
    EntityManager em;

    private ObjectMapper mapper;

    @BeforeEach
    public void setUpMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @BeforeEach
    public void setMapper() {
        mapper = new ObjectMapper();
    }


    @Test
    @DisplayName("없는 게시글 조회 테스트")
    public void find_post_empty_post() throws Exception {
        //given
        when(postService.findPost(anyLong(), anyBoolean())).thenReturn(Optional.empty());

        //when, then
        mockMvc.perform(get("/post/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("성공적으로 게시글 조회")
    public void sucess_find_post() throws Exception {
        //given
        Post findPost = new Post();
        when(postService.findPost(anyLong(), anyBoolean())).thenReturn(Optional.of(findPost));

        //when, then
        mockMvc.perform(get("/post/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 생성 실패 테스트 without title")
    public void failure_write_post_empty_title() throws Exception {
        //given
        PostAddDto dto = new PostAddDto();
        dto.setContent("test content");

        //when, then
        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("게시글 생성 실패 테스트 without content")
    public void failure_write_post_empty_content() throws Exception {
        //given
        PostAddDto dto = new PostAddDto();
        dto.setTitle("test title");

        //when, then
        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("게시글 생성 실패 테스트(해시태크 형식)")
    public void failure_write_post_wrongFormat_hashTags() throws Exception {
        //given
        PostAddDto dto = new PostAddDto();
        dto.setTitle("test title");
        dto.setContent("test content");
        dto.setHashTags("#test,#hashtags,hashtag");

        //when, then
        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("게시글 등록 성공 테스트")
    public void success_write_post() throws Exception {
        //given
        PostAddDto dto = new PostAddDto();
        dto.setTitle("test title");
        dto.setContent("test content");
        dto.setHashTags("#test,#hashtags,#hashtag");

        Post createdPost = dto.toEntity();

        when(postService.addPost(any(), anyList(), any())).thenReturn(Optional.of(createdPost));

        //when, then
        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated());
    }
}