package com.smartdocs.contractmanagement.contract;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private ContractService contractService;

    @Test
    void createTrimsTextFieldsAndSavesContract() {
        ContractRequest request = request("  Cloud Services  ", "  Acme Inc  ",
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));
        when(contractRepository.save(any(Contract.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ContractResponse response = contractService.create(request);

        assertEquals("Cloud Services", response.title());
        assertEquals("Acme Inc", response.vendor());
        verify(contractRepository).save(any(Contract.class));
    }

    @Test
    void getAllUsesCombinedSearchAndStatusWhenBothFiltersArePresent() {
        when(contractRepository.searchByTermAndStatus("Acme", ContractStatus.ACTIVE))
                .thenReturn(List.of());

        List<ContractResponse> results = contractService.getAll(" Acme ", ContractStatus.ACTIVE);

        assertEquals(List.of(), results);
        verify(contractRepository).searchByTermAndStatus("Acme", ContractStatus.ACTIVE);
        verify(contractRepository, never()).findAll();
    }

    @Test
    void getByIdThrowsWhenContractDoesNotExist() {
        when(contractRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(ContractNotFoundException.class, () -> contractService.getById(42L));
    }

    @Test
    void createRejectsEndDateBeforeStartDate() {
        ContractRequest request = request("Cloud Services", "Acme Inc",
                LocalDate.of(2026, 12, 31), LocalDate.of(2026, 1, 1));

        assertThrows(InvalidContractException.class, () -> contractService.create(request));
        verify(contractRepository, never()).save(any(Contract.class));
    }

    private ContractRequest request(String title, String vendor, LocalDate startDate, LocalDate endDate) {
        return new ContractRequest(
                title,
                vendor,
                ContractStatus.ACTIVE,
                startDate,
                endDate,
                new BigDecimal("1250.00")
        );
    }
}
