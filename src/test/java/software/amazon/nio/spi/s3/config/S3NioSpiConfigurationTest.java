/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package software.amazon.nio.spi.s3.config;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static software.amazon.nio.spi.s3.config.S3NioSpiConfiguration.S3_SPI_READ_MAX_FRAGMENT_SIZE_PROPERTY;

public class S3NioSpiConfigurationTest {

    S3NioSpiConfiguration config = new S3NioSpiConfiguration();
    Properties overrides = new Properties();
    Properties badOverrides = new Properties();
    S3NioSpiConfiguration overriddenConfig;
    S3NioSpiConfiguration badOverriddenConfig;

    @BeforeEach
    public void before(){
        overrides.setProperty(S3NioSpiConfiguration.S3_SPI_READ_MAX_FRAGMENT_SIZE_PROPERTY, "1111");
        overrides.setProperty(S3NioSpiConfiguration.S3_SPI_READ_MAX_FRAGMENT_NUMBER_PROPERTY, "2");
        overrides.setProperty(S3NioSpiConfiguration.S3_SPI_ENDPOINT_PROTOCOL_PROPERTY, "http");
        overriddenConfig = new S3NioSpiConfiguration(overrides);

        badOverrides.setProperty(S3NioSpiConfiguration.S3_SPI_READ_MAX_FRAGMENT_NUMBER_PROPERTY, "abcd");
        badOverrides.setProperty(S3NioSpiConfiguration.S3_SPI_READ_MAX_FRAGMENT_SIZE_PROPERTY, "abcd");
        badOverrides.setProperty(S3NioSpiConfiguration.S3_SPI_ENDPOINT_PROTOCOL_PROPERTY, "abcd");
        badOverriddenConfig = new S3NioSpiConfiguration(badOverrides);
    }

    @Test
    public void overridesAsMap() {
        assertThrows(NullPointerException.class, () -> new S3NioSpiConfiguration((Map)null));

        Map<String, String> map = new HashMap<>();
        map.put(S3_SPI_READ_MAX_FRAGMENT_SIZE_PROPERTY, "1212");
        S3NioSpiConfiguration c = new S3NioSpiConfiguration(map);

        assertEquals(1212, c.getMaxFragmentSize());
    }

    @Test
    public void getS3SpiReadMaxFragmentSize() {
        assertEquals(Integer.parseInt(S3NioSpiConfiguration.S3_SPI_READ_MAX_FRAGMENT_SIZE_DEFAULT), config.getMaxFragmentSize());

        assertEquals(1111, overriddenConfig.getMaxFragmentSize());
        assertEquals(Integer.parseInt(S3NioSpiConfiguration.S3_SPI_READ_MAX_FRAGMENT_SIZE_DEFAULT), badOverriddenConfig.getMaxFragmentSize());
    }

    @Test
    public void getS3SpiReadMaxFragmentNumber() {
        assertEquals(Integer.parseInt(S3NioSpiConfiguration.S3_SPI_READ_MAX_FRAGMENT_NUMBER_DEFAULT), config.getMaxFragmentNumber());

        assertEquals(2, overriddenConfig.getMaxFragmentNumber());
        assertEquals(Integer.parseInt(S3NioSpiConfiguration.S3_SPI_READ_MAX_FRAGMENT_NUMBER_DEFAULT), badOverriddenConfig.getMaxFragmentNumber());
    }

    @Test
    public void endpointProtocol() {
        assertEquals(S3NioSpiConfiguration.S3_SPI_ENDPOINT_PROTOCOL_DEFAULT, new S3NioSpiConfiguration().getEndpointProtocol());
        assertEquals("http", overriddenConfig.getEndpointProtocol());
        assertEquals(S3NioSpiConfiguration.S3_SPI_ENDPOINT_PROTOCOL_DEFAULT, badOverriddenConfig.getEndpointProtocol());
    }

    @Test
    public void convertPropertyNameToEnvVar() {
        String expected = "FOO_BAA_FIZZ_BUZZ";
        assertEquals(expected, config.convertPropertyNameToEnvVar("foo.baa.fizz-buzz"));

        expected = "";
        assertEquals(expected, config.convertPropertyNameToEnvVar(null));
        assertEquals(expected, config.convertPropertyNameToEnvVar("  "));
    }
}
