package com.settleup.backend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.settleup.backend.entity.Transaction;
import com.settleup.backend.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction createTransaction(Transaction tx) {
        tx.setCreatedAt(LocalDateTime.now());
        return transactionRepository.save(tx);
    }
}