package org.example.judge.core.fraud;

import org.example.judge.core.fraud.compare.TokenComparator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FraudConfiguration {

    @Bean
    public TokenComparator tokenComparator() {
        return new TokenComparator();
    }

    @Bean
    public FraudDetector fraudDetector(TokenComparator tokenComparator){
        return new FraudDetector(tokenComparator);
    }

}
