package com.github.chaitan64arun.documentserver

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.IOException
import javax.servlet.http.HttpServletRequest

@RestController
class DocumentController {

  @Autowired
  lateinit var documentService: DocumentService

  @PostMapping("/uploadFile")
  fun uploadFile(@RequestParam("file") file: MultipartFile,
                 @RequestParam("userId") UserId: Int,
                 @RequestParam("docType") docType: String): UploadFileResponse {
    val fileName = documentService.storeFile(file, UserId, docType)
    val fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/downloadFile/")
        .path(fileName)
        .toUriString()
    return UploadFileResponse(fileName, fileDownloadUri, file.contentType, file.size)
  }

  @GetMapping("/downloadFile")
  fun downloadFile(@RequestParam("userId") userId: Int,
                   @RequestParam("docType") docType: String,
                   request: HttpServletRequest): ResponseEntity<Resource> {
    val fileName = documentService.getDocumentName(userId, docType)
    var resource: Resource? = null
    if (fileName != null && !fileName.isEmpty()) {
      try {
        resource = documentService.loadFileAsResource(fileName)
      } catch (e: Exception) {
        e.printStackTrace()
      }
      // Try to determine file's content type

      var contentType: String = ""
      try {
        if (resource != null) {
          contentType = request.servletContext.getMimeType(resource.file.absolutePath)
        }
      } catch (ex: IOException) {
        //logger.info("Could not determine file type.")
      }
      // Fallback to the default content type if type could not be determined
      if (contentType == null) {
        contentType = "application/octet-stream"
      }
      if (resource != null) {
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment filename=\"" + resource.filename + "\"")
            .body(resource)
      }
    }
    return ResponseEntity.notFound().build()
  }
}