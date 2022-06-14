package com.vroozi.datasnitch.model;

public class FileObject {

  public static FileObject of(String id, String name, long size) {
    FileObject file = new FileObject();
    file.setId(id);
    file.setName(name);
    file.setSize(size);
    return file;
  }

  private String id;
  private String name;
  private long size;
  private String status;
  /**
   * If this file has PDF format. Then this property will hold the number of pages.
   */
  private Integer pageCount;

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return this.id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public long getSize() {
    return this.size;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return this.status;
  }

  public Integer getPageCount() {
    return pageCount;
  }

  public void setPageCount(Integer pageCount) {
    this.pageCount = pageCount;
  }
}
