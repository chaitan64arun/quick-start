package com.github.chaitan64arun.documentserver

data class UploadFileResponse(
    var fileName: String,
    var fileDownloadUri: String,
    var fileType: String,
    var size: Long
)