package com.vroozi.datasnitch.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

  private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

  public static File writeStringToFile(String data, String location, String fileName)
      throws IOException {
    LOG.info("Writing String to file {}", data);
    File file = null;
    FileWriter fileWriter = null;
    try {
      file = new File(location + "/" + fileName);
      fileWriter = new FileWriter(file);
      fileWriter.write(data);
      fileWriter.flush();
    } catch (Exception e) {
      LOG.error("Error while writing string to file ", e);
    } finally {
      if (fileWriter != null) {
        fileWriter.close();
      }
    }
    return file;
  }

  /**
   * Deletes a file from path if file exists
   *
   * @param path
   */
  public static void deleteFile(String path) {
    try {
      Files.deleteIfExists(Paths.get(path));
    } catch (IOException e) {
      LOG.error("Error while deleting file {}", path, e);
    }
  }
}
