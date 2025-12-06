package com.smarthome.model;

import java.util.Map;
import java.util.Random;

public class EducationalContentService {

    private final Random random = new Random();

    // A simple database of facts. You can easily add more topics and facts here.
    private final Map<String, String[]> factDatabase = Map.of(
        "prophet", new String[]{
            "Fact of the day. Did you know that the Prophet Muhammad (peace be upon him) was born in the Year of the Elephant?",
            "Fact of the day. Did you know that the Prophet Muhammad's (peace be upon him) father's name was Abdullah, and his mother's name was Amina?",
            "Fact of the day. Did you know that before his prophethood, Muhammad (peace be upon him) was known as Al-Amin (the trustworthy) for his honesty?"
        },
        "quran", new String[]{
            "Fact of the day. Did you know the word 'Quran' means 'recitation' and is mentioned over 70 times in the book itself?",
            "Fact of the day. Did you know the longest chapter in the Quran is Al-Baqarah (The Cow) with 286 verses?",
            "Fact of the day. Did you know the Quran was revealed over a period of approximately 23 years?"
        },
        "ramadan", new String[]{
            "Fact of the day. Did you know that the gates of Heaven are believed to be open during the entire month of Ramadan?",
            "Fact of the day. Did you know that Laylat al-Qadr, the Night of Decree, is within the last ten nights of Ramadan and is better than a thousand months?"
        }
    );

    /**
     * Retrieves a random fact about a given topic.
     * @param topic The topic to learn about (e.g., "prophet", "quran").
     * @return A formatted string with a fact, or a message if the topic is not found.
     */
    public String getFactAbout(String topic) {
        // Convert the input topic to lowercase to make the search case-insensitive
        String searchKey = topic.toLowerCase();

        if (factDatabase.containsKey(searchKey)) {
            String[] facts = factDatabase.get(searchKey);
            // Pick a random fact from the list for the given topic
            int randomIndex = random.nextInt(facts.length);
            return facts[randomIndex];
        } else {
            return "Sorry, I don't have any information on the topic '" + topic + "'. Try asking about 'prophet' or 'quran'.";
        }
    }
}
