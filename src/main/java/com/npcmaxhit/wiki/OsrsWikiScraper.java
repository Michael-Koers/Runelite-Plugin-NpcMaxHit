package com.npcmaxhit.wiki;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class OsrsWikiScraper {

    private static final String OSRS_WIKI_BASE_URL = "https://oldschool.runescape.wiki/w/";
    private static final String OSRS_WIKI_SPECIAL_LOOKUP = "Special:Lookup";
    private static final String OSRS_WIKI_LOOKUP_TYPE_NPC = "npc";

    private static final OkHttpClient httpClient = new OkHttpClient();

    public static CompletableFuture<NpcCombatStats> getNpcCombatStats(String npcName, int npcId) {
        CompletableFuture<NpcCombatStats> wikiResponse = new CompletableFuture<>();

        HttpUrl url = HttpUrl.get(OSRS_WIKI_BASE_URL + OSRS_WIKI_SPECIAL_LOOKUP)
                .newBuilder()
                .addQueryParameter("type", OSRS_WIKI_LOOKUP_TYPE_NPC)
                .addQueryParameter("id", String.valueOf(npcId))
                .addQueryParameter("name", npcName)
                .build();

        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        httpClient
                .newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        wikiResponse.completeExceptionally(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        try (response; ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful() || responseBody == null) {
                                wikiResponse.completeExceptionally(new Exception("Wiki down"));
                            } else {
                                wikiResponse.complete(parseWikiResponse(responseBody));
                            }
                        } catch (IOException e) {
                            log.error("Error parsing wiki data, request: %n{}%n, error: %n", call.request(), e);
                            wikiResponse.completeExceptionally(new Exception("Failed to read wiki response"));
                        }
                    }
                });

        return wikiResponse;
    }

    private static NpcCombatStats parseWikiResponse(ResponseBody responseBody) throws IOException {

        Document doc = Jsoup.parse(responseBody.string());
        Element infobox = doc.getElementsByClass("infobox-monster").first();

        return NpcCombatStats.builder()
                .name(infobox.getElementsByClass("infobox-header").first().text())
                .combatLevel(Integer.parseInt(extractHtmlText(infobox, "Combat level")))
                .attackType(AttackType.MELEE)
                .maxHit(Integer.parseInt(extractHtmlText(infobox, "Monster maximum hit")))
                .aggressive(parseBoolean(extractHtmlText(infobox, "Aggressiveness")))
                .poisonous(parseBoolean(extractHtmlText(infobox, "Poison")))
                .build();

    }

    private static String extractHtmlText(Element monsterInfobox, String statTitle) {
        return monsterInfobox.getElementsByAttributeValue("title", statTitle).first().parent().parent().getElementsByTag("td").text();
    }

    private static boolean parseBoolean(String booleanText) {
        return booleanText.equalsIgnoreCase("yes") || booleanText.equalsIgnoreCase("true");
    }
}
