package de.thbingen.epro.project.okrservice.controller.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.project.okrservice.dtos.CompanyDto;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.services.CompanyService;
import de.thbingen.epro.project.okrservice.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CompanyControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private CompanyController companyController;
    @MockBean
    private CompanyService companyService;
    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "testuser", password = "test", roles = "CO_OKR_ADMIN")
    public void CompanyController_PostCompany_ReturnCompanyDto() throws Exception {
        Company company = Company.builder()
                .name("CompanyA").build();
        CompanyDto companyDto = new CompanyDto(company);

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("test")
                .email("test@test.de")
                .firstname("my first name")
                .surname("sur")
                .companies(new HashSet<Company>())
                .build();

        user.addCompany(company);

        given(companyService.createCompany(user.getId(), companyDto)).willAnswer((invocation -> invocation.getArgument(1)));
        when(userService.findUserByEmail(user.getUsername())).thenReturn(user);

        ResultActions response = mockMvc.perform(post("/company")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companyDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(companyDto)));
    }

    @Test
    public void CompanyController_GetAllCompanies_ReturnCompanyDto() throws Exception {
        List<Company> companies = new ArrayList<Company>();
        for(long i = 0L; i < 10L; i++) {
            companies.add(new Company(i, "Company" + i, new HashSet<CompanyObjective>()));
        }

        ArrayList<CompanyDto> companyDtos = companies.stream()
                .map(CompanyDto::new).collect(Collectors.toCollection(ArrayList::new));

        when(companyService.findAllCompanies()).thenReturn(companyDtos);

        ResultActions response = mockMvc.perform(get("/company"));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(companyDtos)));
    }

    @Test
    public void CompanyController_GetCompany_ReturnCompanyDto() throws Exception {
        Company company = Company.builder()
                .name("CompanyA").build();
        CompanyDto companyDto = new CompanyDto(company);

        when(companyService.findCompany(1L)).thenReturn(company);

        ResultActions response = mockMvc.perform(get("/company/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(companyDto)));
    }

    @Test
    public void CompanyController_PatchCompany_ReturnCompanyDto() throws Exception {
        Company company = Company.builder()
                .id(1L)
                .name("CompanyA").build();
        CompanyDto companyDto = new CompanyDto(company);

        when(companyService.patchCompany(company.getId(), companyDto)).thenReturn(companyDto);

        ResultActions response = mockMvc.perform(patch("/company/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companyDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(companyDto)));
    }

    @Test
    public void CompanyController_DeleteCompany_ReturnHttpStatus() throws Exception {
        ResultActions response = mockMvc.perform(delete("/company/1"));

        response.andExpect(status().isOk());
    }
}
