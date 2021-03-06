package com.pitchedapps.frost.parsers

import ca.allanwang.kau.utils.withMaxLength
import com.pitchedapps.frost.facebook.FbItem
import com.pitchedapps.frost.facebook.formattedFbUrlCss
import com.pitchedapps.frost.utils.L
import com.pitchedapps.frost.utils.frostJsoup
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * Created by Allan Wang on 2017-10-09.
 */
object SearchParser : FrostParser<List<FrostSearch>> by SearchParserImpl() {
    fun query(input: String): List<FrostSearch>? {
        val url = "${FbItem.SEARCH.url}?q=$input"
        L.i(null, "Search Query $url")
        return parse(frostJsoup(url))
    }
}

enum class SearchKeys(val key: String) {
    USERS("keywords_users"),
    EVENTS("keywords_events")
}

/**
 * As far as I'm aware, all links are independent, so the queries don't matter
 * A lot of it is tracking information, which I'll strip away
 * Other text items are formatted for safety
 */
class FrostSearch(href: String, title: String, description: String?) {
    val href = with(href.indexOf("?")) { if (this == -1) href else href.substring(0, this) }
    val title = title.format()
    val description = description?.format()

    private fun String.format() = replace("\n", " ").withMaxLength(50)

    override fun toString(): String
            = "FrostSearch(href=$href, title=$title, description=$description)"

}

private class SearchParserImpl : FrostParserBase<List<FrostSearch>>() {
    override fun parse(doc: Document): List<FrostSearch>? {
        val container: Element = doc.getElementById("BrowseResultsContainer")
                ?: doc.getElementById("root")
                ?: return null
        val hrefSet = mutableSetOf<String>()
        /**
         * When mapping items, some links are duplicated because they are nested below a main one
         * We will filter out search items whose links are already in the list
         *
         * Removed [data-store*=result_id]
         */
        return container.select("a.touchable.primary[href]").filter(Element::hasText).mapNotNull {
            val item = FrostSearch(it.attr("href").formattedFbUrlCss,
                    it.select("._uok").first()?.text() ?: it.text(),
                    it.select("._1tcc").first()?.text())
            if (hrefSet.contains(item.href)) return@mapNotNull null
            hrefSet.add(item.href)
            item
        }
    }

    override fun textToDoc(text: String): Document? = Jsoup.parse(text)

    override fun debugImpl(data: List<FrostSearch>, result: MutableList<String>) {
        result.addAll(data.map(FrostSearch::toString))
    }

}