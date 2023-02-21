package com.example.android.january2022.utils

class FuzzySearch {

  companion object {
    fun levenshtein(lhs: CharSequence, rhs: CharSequence): Int {
      val lhsLength = lhs.length + 1
      val rhsLength = rhs.length + 1

      var cost = Array(lhsLength) { it }
      var newCost = Array(lhsLength) { 0 }

      for (i in 1 until rhsLength) {
        newCost[0] = i

        for (j in 1 until lhsLength) {
          val match = if (lhs[j - 1] == rhs[i - 1]) 0 else 10

          val costReplace = cost[j - 1] + match
          val costInsert = cost[j] + 1
          val costDelete = newCost[j - 1] + 1

          newCost[j] = costInsert.coerceAtMost(costDelete).coerceAtMost(costReplace)
        }

        val swap = cost
        cost = newCost
        newCost = swap
      }

      return cost[lhsLength - 1]
    }


    fun regexMatch(query: String, text: String): Boolean {
      val q = query.lowercase().filter { it.isLetterOrDigit() || it.isWhitespace() }
      val t = text.lowercase().filter { it.isLetterOrDigit() || it.isWhitespace() }
      val regexp =
        q.split(" ").joinToString(separator = "", postfix = ".*") { "(?=.*$it)" }.toRegex()
      return regexp.matches(t)
    }
  }
}
