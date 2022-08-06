package com.waigel.tolgee.models


class ExportParams(
    var languages: List<String>? = null,
    var splitByScope: Boolean? = null,
    var splitByScopeDelimiter: String? = null,
    var splitByScopeDepth: Int? = null,
    var filterKeyId: List<Long>? = null,
    var filterKeyIdNot: List<Long>? = null,
    var filterTag: String? = null,
    var filterKeyPrefix: String? = null,
    var filterState: FilterState? = null,
)