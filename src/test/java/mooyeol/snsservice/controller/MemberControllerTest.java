package mooyeol.snsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mooyeol.snsservice.domain.Member;
import mooyeol.snsservice.service.MemberService;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
//@WebMvcTest(value = MemberController.class, excludeFilters = {
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AjaxSecurityConfig.class)
//})
@SpringBootTest
@WithMockUser
class MemberControllerTest {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext ctx;

    @MockBean
    MemberService memberService;

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
    @DisplayName("이메일 입력을 하지 않아 회원가입에 실패한 경우")
    public void failure_join_empty_email() throws Exception {
        //given
        String passwrod = "@pwd1234123";

        //when, then
        mockMvc.perform(post("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new MemberDto(null, "!qasd12345")))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("잘못된 이메일 형식으로 회원가입에 실패한 경우")
    public void failure_join_invaild_email() throws Exception {
        //given
        String passwrod = "@pwd1234123";

        //when, then
        mockMvc.perform(post("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new MemberDto("emailtest@", "!qasd12345")))
                )
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("비밀번호 입력을 하지 않아 회원가입에 실패한 경우")
    public void failure_join_empty_pwd() throws Exception {
        //given
        String email = UUID.randomUUID().toString();

        //when, then
        mockMvc.perform(post("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new MemberDto(email, null)))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("잘못된 비밀번호 형식으로 회원가입에 실패한 경우")
    public void failure_join_invaild_pwd() throws Exception {
        //given
        String email = UUID.randomUUID().toString() + "@test.com";

        //when, then
        mockMvc.perform(post("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new MemberDto(email, "123123!!")))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 중복으로 회원가입 실패한 경우")
    public void failure_join_duplicate_email() throws Exception {
        //given
        doThrow(new IllegalArgumentException("이미 존재하는 이메일입니다.")).when(memberService).join(any());
        String email = UUID.randomUUID().toString() + "@test.com";
        String password = "!qasd12345";
        //when, then
        mockMvc.perform(post("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new MemberDto(email, password)))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 성공")
    public void success_join() throws Exception {
        //given
        String email = UUID.randomUUID().toString() + "@test.com";
        String  pwd = "!@qwer123";

        //when, then
        mockMvc.perform(post("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new MemberDto(email, pwd)))
                )
                .andExpect(status().isNoContent());
    }
}