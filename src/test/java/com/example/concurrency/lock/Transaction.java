package com.example.concurrency.lock;

import java.math.BigDecimal;

public record Transaction(
        BigDecimal balance,
        TransactionType transactionType
        ) {

}
