package com.webfontaine.ghclient.helper;

import java.util.UUID;

/**
 * The helper class
 */
public class Helper {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
