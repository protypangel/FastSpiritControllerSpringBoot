package org.fast.spirit.security;

public class RandomStringUtils {
  public static String random(int size, String characters) {
    StringBuilder builder = new StringBuilder();
    for (int index = 0; index < size; index++) {
      int random = ((int) Math.random()) % characters.length();
      builder.append(characters.charAt(random));
    }
    return builder.toString();
  }
}
