package com.ce.hakanarayici.recipes.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SQLUtil {

    public static String clobToString(Clob data) {
        StringBuilder sb = new StringBuilder();
        try {
            Reader reader = data.getCharacterStream();
            BufferedReader br = new BufferedReader(reader);

            String line;
            while (null != (line = br.readLine())) {
                sb.append(line);
            }
            br.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return sb.toString();
    }
}
