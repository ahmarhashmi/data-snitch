package com.vroozi.datasnitch.service.rest;

import com.vroozi.datasnitch.model.FileObject;

public interface RestClient {

  FileObject upload(Object item, String bucketName, String unitId, String folder);
}
