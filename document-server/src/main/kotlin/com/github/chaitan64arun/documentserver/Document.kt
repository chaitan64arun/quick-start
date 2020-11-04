package com.github.chaitan64arun.documentserver

import org.springframework.boot.context.properties.ConfigurationProperties
import javax.persistence.*

@ConfigurationProperties(prefix = "file")
@Entity
data class Document(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "document_id")
    val documentId: Int,

    @Column(name = "user_id")
    val userId: Int,

    @Column(name = "file_name")
    var fileName: String,

    @Column(name = "document_type")
    val documentType: String,

    @Column(name = "document_format")
    var documentFormat: String,

    @Column(name = "upload_dir")
    var uploadDir: String
)