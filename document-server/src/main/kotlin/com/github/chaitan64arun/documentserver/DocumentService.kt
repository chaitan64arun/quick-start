package com.github.chaitan64arun.documentserver

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.FileNotFoundException
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class DocumentService {

  private lateinit var fileStorageLocation: Path

  @Autowired
  lateinit var docStorageRepo: DocumentRepository

  @Autowired
  constructor(fileStorageProperties: Document) {
    this.fileStorageLocation = Paths.get(fileStorageProperties.uploadDir).toAbsolutePath().normalize()
    try {
      Files.createDirectories(this.fileStorageLocation)
    } catch (ex: Exception) {
      //throw DocumentStorageException("Could not create the directory where the uploaded files will be stored.", ex);
    }
  }

  fun storeFile(file: MultipartFile, userId: Int, docType: String): String {

    // Normalize file name
    val originalFileName = StringUtils.cleanPath(file.originalFilename)

    var fileName: String = ""
    try {
      // Check if the file's name contains invalid characters
      if (originalFileName.contains("..")) {
        //throw new DocumentStorageException("Sorry! Filename contains invalid path sequence " + originalFileName);
      }

      var fileExtension: String
      try {
        fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."))
      } catch (e: Exception) {
        fileExtension = ""
      }

      fileName = "".plus(userId).plus("_").plus(docType).plus(fileExtension)

      // Copy file to the target location (Replacing existing file with the same name)
      val targetLocation = this.fileStorageLocation.resolve(fileName)

      Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)

      var doc = docStorageRepo.checkDocumentByUserId(userId, docType)

      if (doc != null) {
        doc.documentFormat = file.contentType
        doc.fileName = fileName
        docStorageRepo.save(doc)

      } else {
        val newDoc = Document(0, userId, fileName, docType, file.contentType, "D:/document-dir")
        docStorageRepo.save(newDoc)
      }

    } catch (ex: IOException) {
//      throw new DocumentStorageException("Could not store file " + fileName + ". Please try again!", ex);
    }
    return fileName
  }

  fun loadFileAsResource(fileName: String): Resource {
    try {
      val filePath = fileStorageLocation.resolve(fileName).normalize()

      val resource = UrlResource(filePath.toUri())

      if (resource.exists()) {
        return resource
      } else {
        throw FileNotFoundException("File not found ".plus(fileName))
      }
    } catch (ex: MalformedURLException) {
      throw FileNotFoundException("File not found ".plus(fileName))
    }
  }

  fun getDocumentName(userId: Int, docType: String): String? {
    return docStorageRepo.getUploadDocumentPath(userId, docType)
  }
}