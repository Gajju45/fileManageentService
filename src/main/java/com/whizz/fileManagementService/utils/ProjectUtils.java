package com.whizz.fileManagementService.utils;

public class ProjectUtils {

    public static String getFileName(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }

        int indexOfDot = filename.lastIndexOf('.');
        if (indexOfDot != -1) {
            return filename.substring(0, indexOfDot);
        } else {
            return filename; // Handle filename without extension
        }
    }
 public static String getFileExtension(String filename) {

  if (filename == null || filename.isEmpty()) {
   return ""; // Handle empty or null filename
  }

  int indexOfDot = filename.lastIndexOf('.');
  if (indexOfDot != -1) {
   return filename.substring(indexOfDot + 1);
  } else {
   return ""; // Handle filename without extension
  }
 }

    public static long getCurrentTimeStamp() {
        return System.currentTimeMillis();
    }

}
