package com.github.chaitan64arun.documentserver

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface DocumentRepository : JpaRepository<Document, Int> {

  @Query("select a from Document a where user_id = ?1 and document_type = ?2")
  fun checkDocumentByUserId(userId: Int, docType: String): Document?

  @Query("select fileName from Document a where user_id = ?1 and document_type = ?2")
  fun getUploadDocumentPath(userId: Int, docType: String): String?
}