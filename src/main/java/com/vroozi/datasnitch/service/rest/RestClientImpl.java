package com.vroozi.datasnitch.service.rest;

import static org.springframework.http.HttpMethod.POST;

import com.vroozi.datasnitch.model.FileObject;
import com.vroozi.datasnitch.service.RestServiceUrl;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class RestClientImpl implements RestClient {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private RestServiceUrl restServiceUrl;

  @Override
  public FileObject upload(Object item, String bucketName, String unitId, String folder) {
    final File uploadFile;

    if (item instanceof String) {
      uploadFile = new File((String) item);
    } else if (item instanceof File) {
      uploadFile = (File) item;
    } else {
      throw new IllegalStateException("Unknown upload item type.");
    }

    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    parts.add("file", new FileSystemResource(uploadFile));
    parts.add("bucketName", bucketName);
    parts.add("folder", folder);
    parts.add("useNameAsKey", true);
    parts.add("unitId", unitId);
    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    headers.add("Authorization", "Basic "
        + Base64Utils.encodeToString(restServiceUrl.getFileStorageServiceAuthToken().getBytes()));

    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parts, headers);
    ResponseEntity<FileObject> response = restTemplate.exchange(
        restServiceUrl.getSecureUploadFileURI(), POST, requestEntity, FileObject.class);
    return response.getBody();
  }
}
