package com.services;

import java.util.HashMap;
import java.util.Map;

public class EducationalService {

    private final Map<String, String> knowledgeBase;

    public EducationalService() {
        this.knowledgeBase = new HashMap<>();
        initializeKnowledgeBase();
    }

    private void initializeKnowledgeBase() {
        knowledgeBase.put("prophet",
                "Prophet Muhammad (PBUH) is the last messenger of Allah, sent to confirm the essential teachings of monotheism preached by previous prophets like Adam, Abraham, Moses, and Jesus.");
        knowledgeBase.put("quran",
                "The Quran is the central religious text of Islam, believed by Muslims to be a revelation from God (Allah). It is widely regarded as the finest work in classical Arabic literature.");
        knowledgeBase.put("history",
                "Islamic history began in the 7th century in Arabia. It saw the rapid expansion of the Islamic state under the Rashidun Caliphs and subsequent Umayyad and Abbasid caliphates, fostering a Golden Age of science, philosophy, and culture.");
        knowledgeBase.put("five pillars",
                "The Five Pillars of Islam are: Shahada (Faith), Salah (Prayer), Zakat (Charity), Sawm (Fasting), and Hajj (Pilgrimage).");
        knowledgeBase.put("ramadan",
                "Ramadan is the ninth month of the Islamic calendar, observed by Muslims worldwide as a month of fasting, prayer, reflection, and community.");
    }

    public String processCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            return "Please ask a question or say a keyword.";
        }

        String lowerCommand = command.toLowerCase();

        for (Map.Entry<String, String> entry : knowledgeBase.entrySet()) {
            if (lowerCommand.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "I'm sorry, I don't have information about that yet. Try asking about 'prophet', 'quran', 'history', or 'ramadan'.";
    }
}
