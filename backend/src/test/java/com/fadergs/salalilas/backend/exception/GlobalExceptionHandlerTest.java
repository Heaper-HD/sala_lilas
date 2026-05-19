package com.fadergs.salalilas.backend.exception;

import com.fadergs.salalilas.backend.exception.types.AuthException;
import com.fadergs.salalilas.backend.exception.types.BusinessException;
import com.fadergs.salalilas.backend.exception.types.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        TestController controller = new TestController();
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(exceptionHandler)
                .build();
    }

    @RestController
    @RequestMapping("/test")
    static class TestController {

        @GetMapping("/not-found")
        public void notFound() {
            throw new ResourceNotFoundException(ErrorCode.AGD_NOT_FOUND);
        }

        @GetMapping("/business")
        public void business() {
            throw new BusinessException(ErrorCode.AGD_ALREADY_FINALIZED);
        }

        @GetMapping("/auth")
        public void auth() {
            throw new AuthException(ErrorCode.AUTH_TOKEN_INVALID);
        }

        @PostMapping("/validation")
        public void validation(@Valid @RequestBody ValidationRequest body) {}

        record ValidationRequest(
                @NotBlank(message = "Nome é obrigatório") String nome,
                @Email(message = "E-mail é inválido") String email
        ) {}
    }

    @Test
    @DisplayName("ResourceNotFoundException should return 404 with correct error code")
    void shouldReturn404ForNotFound() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("AGD_001"))
                .andExpect(jsonPath("$.httpStatus").value(404))
                .andExpect(jsonPath("$.path").value("/test/not-found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("BusinessException should return 422 with correct error code")
    void shouldReturn422ForBusiness() throws Exception {
        mockMvc.perform(get("/test/business"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value("AGD_005"))
                .andExpect(jsonPath("$.httpStatus").value(422));
    }

    @Test
    @DisplayName("AuthException should return 401 with correct error code")
    void shouldReturn401ForAuth() throws Exception {
        mockMvc.perform(get("/test/auth"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("AUTH_003"));
    }

    @Test
    @DisplayName("Validation errors should return 400 with field details")
    void shouldReturn400WithFieldDetailsForValidation() throws Exception {
        String body = """
            {
                "nome": "",
                "email": "not-an-email"
            }
        """;

        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VAL_001"))
                .andExpect(jsonPath("$.fields").isArray())
                .andExpect(jsonPath("$.fields.length()").value(2));
    }

    @Test
    @DisplayName("Error resonse should never expose stack trace")
    void shouldNeverExposeStackTrace() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(jsonPath("$.trace").doesNotExist())
                .andExpect(jsonPath("$.stackTrace").doesNotExist());
    }
}
