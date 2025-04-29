package projectPack;

public class PersonalityMatcher {
    /**
     *
     * @param score
     * @return
     */
    public static String getPersonalityName(int score) {
        if (score >= 160) return "Albert Einstein";
        if (score >= 155) return "Stephen Hawking";
        if (score >= 150) return "Alan Turing";
        if (score >= 145) return "Bill Gates";
        if (score >= 140) return "Steve Jobs";
        if (score >= 135) return "Mark Zuckerberg";
        if (score >= 130) return "Tim Cook";
        if (score >= 125) return "Hillary Clinton";
        if (score >= 120) return "Barack Obama";
        if (score >= 115) return "Margaret Thatcher";
        if (score >= 110) return "Winston Churchill";
        if (score >= 105) return "Leonardo DiCaprio";
        if (score >= 100) return "Donald Trump";
        if (score >= 95) return "Cristiano Ronaldo";
        if (score >= 90) return "Justin Bieber";
        return "Justin Bieber"; //Default case (score can't be <85)
    }

    /**
     *
     * @param score
     * @return
     */
    public static String getPersonalityImagePath(int score) {
        if (score >= 160) return "images/personalities/albert-einstein.png";
        if (score >= 155) return "images/personalities/stephen-hawking.png";
        if (score >= 150) return "images/personalities/alan-turing.png";
        if (score >= 145) return "images/personalities/bill-gates.png";
        if (score >= 140) return "images/personalities/steve-jobs.png";
        if (score >= 135) return "images/personalities/mark-zuck.png";
        if (score >= 130) return "images/personalities/tim-cook.png";
        if (score >= 125) return "images/personalities/hillary-clinton.png";
        if (score >= 120) return "images/personalities/obama.png";
        if (score >= 115) return "images/personalities/margareth.png";
        if (score >= 110) return "images/personalities/winston-churchill.png";
        if (score >= 105) return "images/personalities/diCaprio.png";
        if (score >= 100) return "images/personalities/donald-trump.png";
        if (score >= 95) return "images/personalities/cristiano-ronaldo.png";
        if (score >= 90) return "images/personalities/justin-bieber.png";

        return "images/personalities/justin-bieber.png"; //default fallback
    }
}