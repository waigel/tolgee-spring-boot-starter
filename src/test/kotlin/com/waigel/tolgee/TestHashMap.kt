package com.waigel.tolgee

class TestHashMap {
    companion object{
        fun create(): HashMap<String, HashMap<String, String>> {
            val translations: HashMap<String, HashMap<String, String>> = linkedMapOf()

            val deDe: HashMap<String, String> = linkedMapOf();
            deDe["TEST_LABEL"] = "Test Beschriftung"
            deDe["TEST_LABEL_WITH_PARAMS"] = "Test Beschriftung für {name}. Erstellt am {created}"
            translations["de-DE"] = deDe

            val en: HashMap<String, String> = linkedMapOf();
            en["TEST_LABEL"] = "Test label"
            en["TEST_LABEL_WITH_PARAMS"] = "Test label for {name}. Created at {created}"
            translations["en"] = en
            return translations;
        }
    }
}