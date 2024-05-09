package com.miguelangel.supermarketDataCollector.configuration.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class SecurityConstants {
    public static final long TOKEN_EXPIRATION = 2 * 24 * 60 * 60 * 1000; //2 days in milliseconds

    public static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
}
