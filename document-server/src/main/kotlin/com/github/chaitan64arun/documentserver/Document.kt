package com.github.chaitan64arun.documentserver

import javax.persistence.*

@Entity
data class Document(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "document_id")
    val documentId: Int,

    @Column(name = "user_id")
    val userId: Int,

    @Column(name = "file_name")
    val fileName: String,

    @Column(name = "document_type")
    val documentType: String,

    @Column(name = "document_format")
    val documentFormat: String,

    @Column(name = "upload_dir")
    val uploadDir: String
)