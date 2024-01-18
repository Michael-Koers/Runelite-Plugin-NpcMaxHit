package com.npcmaxhit.wiki;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

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
                    public void onResponse(Call call, Response response) throws IOException {
                        try (response; ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful() || responseBody == null) {
                                wikiResponse.complete(new NpcCombatStats());
                            } else {
                                wikiResponse.complete(parseWikiResponse(responseBody));
                            }
                        }
                    }
                });

        return wikiResponse;
    }

    private static NpcCombatStats parseWikiResponse(ResponseBody responseBody) throws IOException {
        System.out.println(responseBody.string());
        return null;
    }


}
