package com.bank.service;

import com.bank.dto.EmployerFacade;
import com.bank.dto.EmployerRequest;
import com.bank.dto.EmployerResponse;
import com.bank.entity.Employer;
import com.bank.exception.NotFoundException;
import com.bank.repository.EmployerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployerServiceTest {
    @Mock
    private EmployerRepository employerRepository;

    @Mock
    private EmployerFacade employerFacade;

    @InjectMocks
    private EmployerService employerService;

    private Employer employer;
    private EmployerRequest employerRequest;
    private EmployerResponse employerResponse;

    private final String name = "Tech Corp";
    private final String address = "123 Tech Lane";

    @BeforeEach
    void setUp() {
        employer = new Employer(name, address, new HashSet<>());
        employerRequest = new EmployerRequest(1L, name, address);
        employerResponse = new EmployerResponse(1L, name, address);
    }

    @Test
    void testSave() {
        when(employerFacade.toEntity(employerRequest)).thenReturn(employer);
        when(employerRepository.save(employer)).thenReturn(employer);
        when(employerFacade.toResponse(employer)).thenReturn(employerResponse);

        EmployerResponse savedEmployer = employerService.save(employerRequest);

        assertNotNull(savedEmployer);
        assertEquals(name, savedEmployer.getName());
        assertEquals(address, savedEmployer.getAddress());
    }

    @Test
    void testGetAll() {
        when(employerRepository.findAll()).thenReturn(List.of(employer));
        when(employerFacade.toResponse(employer)).thenReturn(employerResponse);

        List<EmployerResponse> employers = employerService.getAll();

        assertNotNull(employers);
        assertEquals(1, employers.size());
        assertEquals(name, employers.getFirst().getName());
    }

    @Test
    void testGetByIdSuccess() {
        when(employerRepository.findById(1L)).thenReturn(Optional.of(employer));
        when(employerFacade.toResponse(employer)).thenReturn(employerResponse);

        EmployerResponse foundEmployer = employerService.getById(1L);

        assertNotNull(foundEmployer);
        assertEquals(1L, foundEmployer.getId());
        assertEquals(name, foundEmployer.getName());
    }

    @Test
    void testGetByIdNotFound() {
        when(employerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> employerService.getById(1L));
    }

    @Test
    void testGetEmployerByNameSuccess() {
        when(employerRepository.findByName(name)).thenReturn(employer);
        when(employerFacade.toResponse(employer)).thenReturn(employerResponse);

        EmployerResponse foundEmployer = employerService.getEmployerByName(name);

        assertNotNull(foundEmployer);
        assertEquals(name, foundEmployer.getName());
    }

    @Test
    void testGetEmployerByNameNotFound() {
        when(employerRepository.findByName(name)).thenReturn(null);

        EmployerResponse foundEmployer = employerService.getEmployerByName(name);

        assertNull(foundEmployer);
    }

    @Test
    void testDelete() {
        long employerId = 1L;

        willDoNothing().given(employerRepository).deleteById(employerId );

        employerService.delete(employerId);

        verify(employerRepository, times(1)).deleteById(1L);
    }
}
