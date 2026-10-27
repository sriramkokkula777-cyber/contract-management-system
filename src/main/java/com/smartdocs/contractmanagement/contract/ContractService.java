package com.smartdocs.contractmanagement.contract;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContractService {

    private final ContractRepository contractRepository;

    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Transactional
    public ContractResponse create(ContractRequest request) {
        validateDateRange(request);
        Contract contract = new Contract(
                request.title().trim(),
                request.vendor().trim(),
                request.status(),
                request.startDate(),
                request.endDate(),
                request.value()
        );
        return ContractResponse.from(contractRepository.save(contract));
    }

    @Transactional(readOnly = true)
    public List<ContractResponse> getAll(String search, ContractStatus status) {
        boolean hasSearch = search != null && !search.isBlank();
        String normalizedSearch = hasSearch ? search.trim() : null;

        List<Contract> contracts;
        if (normalizedSearch != null && status != null) {
            contracts = contractRepository.searchByTermAndStatus(normalizedSearch, status);
        } else if (normalizedSearch != null) {
            contracts = contractRepository.findByTitleContainingIgnoreCaseOrVendorContainingIgnoreCase(
                    normalizedSearch, normalizedSearch);
        } else if (status != null) {
            contracts = contractRepository.findByStatus(status);
        } else {
            contracts = contractRepository.findAll();
        }

        return contracts.stream().map(ContractResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ContractResponse getById(Long id) {
        return ContractResponse.from(findContract(id));
    }

    @Transactional
    public ContractResponse update(Long id, ContractRequest request) {
        validateDateRange(request);
        Contract contract = findContract(id);
        contract.setTitle(request.title().trim());
        contract.setVendor(request.vendor().trim());
        contract.setStatus(request.status());
        contract.setStartDate(request.startDate());
        contract.setEndDate(request.endDate());
        contract.setValue(request.value());
        return ContractResponse.from(contractRepository.save(contract));
    }

    @Transactional
    public void delete(Long id) {
        contractRepository.delete(findContract(id));
    }

    private Contract findContract(Long id) {
        return contractRepository.findById(id).orElseThrow(() -> new ContractNotFoundException(id));
    }

    private void validateDateRange(ContractRequest request) {
        if (request.startDate() != null && request.endDate() != null
                && request.endDate().isBefore(request.startDate())) {
            throw new InvalidContractException("endDate must be on or after startDate");
        }
    }
}
