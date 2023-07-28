package com.example.android.january2022.utils

class FuzzySearch {

  companion object {
    fun regexMatch(query: String, text: String): Boolean {
      val q = query.lowercase().filter { it.isLetterOrDigit() || it.isWhitespace() }
      val t = text.lowercase().filter { it.isLetterOrDigit() || it.isWhitespace() }
      val regexp =
        q.split(" ").joinToString(separator = "", postfix = ".*") { "(?=.*$it)" }.toRegex()
      return regexp.matches(t)
    }
  }
}
