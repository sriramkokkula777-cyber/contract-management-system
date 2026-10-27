package com.smartdocs.contractmanagement.contract;

public class ContractNotFoundException extends RuntimeException {

    public ContractNotFoundException(Long id) {
        super("Contract with ID " + id + " was not found");
    }
}
