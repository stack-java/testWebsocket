package com.example.testwebsocket.repository;

import com.example.testwebsocket.entity.CurrencyExchangeRateEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyExchangeRateRepository extends CrudRepository<CurrencyExchangeRateEntity, Integer> {

}


