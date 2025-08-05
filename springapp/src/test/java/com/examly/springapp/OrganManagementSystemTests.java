package com.examly.springapp;

import com.examly.springapp.model.*;
import com.examly.springapp.repository.*;
import com.examly.springapp.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrganManagementSystemTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private OrganRequestRepository organRequestRepository;

    @Autowired
    private DonorService donorService;

    @Autowired
    private OrganRequestService organRequestService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        donorRepository.deleteAll();
        organRequestRepository.deleteAll();
    }

    // Donor Tests

    @Test
    void createDonorSuccessfully() throws Exception {
        Donor donor = Donor.builder()
                .name("John Doe")
                .age(25)
                .gender("Male")
                .organType("A+")
                .contactNumber("1234567890")
                .email("john@example.com")
                .address("123 Main St")
                .build();

        mockMvc.perform(post("/api/donors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(donor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.organType").value("A+"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void createDonorWithInvalidAge() throws Exception {
        Donor donor = Donor.builder()
                .name("Young Donor")
                .age(17)
                .gender("Male")
                .organType("A+")
                .contactNumber("1234567890")
                .email("young@example.com")
                .address("123 Main St")
                .build();

        mockMvc.perform(post("/api/donors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(donor)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Donor must be at least 18 years old"));
    }

    @Test
    void createDonorWithInvalidOrganType() throws Exception {
        Donor donor = Donor.builder()
                .name("John Doe")
                .age(25)
                .gender("Male")
                .organType("InvalidType")
                .contactNumber("1234567890")
                .email("john@example.com")
                .address("123 Main St")
                .build();

        mockMvc.perform(post("/api/donors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(donor)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Organ type"));
    }

    @Test
    void createDonorWithMissingRequiredFields() throws Exception {
        Donor donor = Donor.builder()
                .name("")
                .age(25)
                .gender("Male")
                .organType("A+")
                .contactNumber("1234567890")
                .email("invalid-email")
                .address("123 Main St")
                .build();

        mockMvc.perform(post("/api/donors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(donor)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getAllDonors() throws Exception {
        Donor donor1 = createValidDonor("John Doe", "A+");
        Donor donor2 = createValidDonor("Jane Smith", "B+");
        
        donorService.createDonor(donor1);
        donorService.createDonor(donor2);

        mockMvc.perform(get("/api/donors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[1].name").exists());
    }

    @Test
    void getDonorById() throws Exception {
        Donor donor = createValidDonor("John Doe", "A+");
        Donor saved = donorService.createDonor(donor);

        mockMvc.perform(get("/api/donors/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.organType").value("A+"));
    }

    @Test
    void getDonorByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/donors/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Donor not found"));
    }

    // Organ Request Tests

    @Test
    void createOrganRequestSuccessfully() throws Exception {
        OrganRequest request = OrganRequest.builder()
                .hospitalName("City Hospital")
                .organType("A+")
                .unitsRequired(2)
                .urgency(UrgencyLevel.HIGH)
                .patientName("Patient One")
                .contactPerson("Dr. Smith")
                .contactNumber("1234567890")
                .requestDate(LocalDate.now())
                .build();

        mockMvc.perform(post("/api/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.hospitalName").value("City Hospital"))
                .andExpect(jsonPath("$.organType").value("A+"))
                .andExpect(jsonPath("$.unitsRequired").value(2))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void createOrganRequestWithInvalidOrganType() throws Exception {
        OrganRequest request = OrganRequest.builder()
                .hospitalName("City Hospital")
                .organType("InvalidType")
                .unitsRequired(2)
                .urgency(UrgencyLevel.HIGH)
                .patientName("Patient One")
                .contactPerson("Dr. Smith")
                .contactNumber("1234567890")
                .requestDate(LocalDate.now())
                .build();

        mockMvc.perform(post("/api/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Organ type"));
    }

    @Test
    void createOrganRequestWithInvalidUnitsRequired() throws Exception {
        OrganRequest request = OrganRequest.builder()
                .hospitalName("City Hospital")
                .organType("A+")
                .unitsRequired(0)
                .urgency(UrgencyLevel.HIGH)
                .patientName("Patient One")
                .contactPerson("Dr. Smith")
                .contactNumber("1234567890")
                .requestDate(LocalDate.now())
                .build();

        mockMvc.perform(post("/api/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Units required must be a positive number"));
    }

    @Test
    void getAllOrganRequests() throws Exception {
        OrganRequest request1 = createValidOrganRequest("Hospital A", "A+", 2);
        OrganRequest request2 = createValidOrganRequest("Hospital B", "B+", 3);
        
        organRequestService.createRequest(request1);
        organRequestService.createRequest(request2);

        mockMvc.perform(get("/api/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].hospitalName").exists())
                .andExpect(jsonPath("$[1].hospitalName").exists());
    }

    @Test
    void updateOrganRequestStatus() throws Exception {
        OrganRequest request = createValidOrganRequest("Test Hospital", "A+", 1);
        OrganRequest saved = organRequestService.createRequest(request);

        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", "FULFILLED");

        mockMvc.perform(put("/api/requests/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FULFILLED"));
    }

    @Test
    void updateOrganRequestStatusNotFound() throws Exception {
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", "FULFILLED");

        mockMvc.perform(put("/api/requests/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Request not found"));
    }

    @Test
    void getOrganAvailability() throws Exception {
        mockMvc.perform(get("/api/Organ-availability"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.unitsPerType").exists())
                .andExpect(jsonPath("$.lastUpdated").exists())
                .andExpect(jsonPath("$.unitsPerType.A+").exists())
                .andExpect(jsonPath("$.unitsPerType.B+").exists());
    }

    // Helper methods
    private Donor createValidDonor(String name, String organType) {
        return Donor.builder()
                .name(name)
                .age(25)
                .gender("Male")
                .organType(organType)
                .contactNumber("1234567890")
                .email(name.toLowerCase().replace(" ", "") + "@example.com")
                .address("123 Main St")
                .lastDonationDate(LocalDate.now().minusMonths(6))
                .build();
    }

    private OrganRequest createValidOrganRequest(String hospitalName, String organType, int units) {
        return OrganRequest.builder()
                .hospitalName(hospitalName)
                .organType(organType)
                .unitsRequired(units)
                .urgency(UrgencyLevel.MEDIUM)
                .patientName("Test Patient")
                .contactPerson("Dr. Test")
                .contactNumber("1234567890")
                .requestDate(LocalDate.now())
                .build();
    }
}