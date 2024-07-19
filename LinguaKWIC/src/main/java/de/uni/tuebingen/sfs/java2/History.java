package de.uni.tuebingen.sfs.java2;

public class History {
    public String searchTopic;
    public String target;
    public int match;
    public double frequency;

    public History(String searchTopic, String target, int match, double frequency) {
        this.searchTopic = searchTopic;
        this.target = target;
        this.match = match;
        this.frequency = frequency;
    }

    public String toString() {
        String html = "<html>" +
                "<head>" +
                "<style>" +
                "body {font-family: Arial, sans-serif; margin: 0; padding: 15px; background-color: #9999BD;}" +
                ".container {max-width: 600px; margin: auto; background: #9999BD; padding: 15px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); color: #9999BD;}" +
                "h1 {text-align: center; color: #fff; font-size: 20px;}" +
                "p {font-size: 14px; line-height: 1.6; color: #fff;}" +
                ".highlight {font-weight: bold; color: #000}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1>Search History</h1>" +
                "<p>You have searched for <span class='highlight'>" + searchTopic + "</span><br>" +
                "in <span class='highlight'>" + target + "</span>.</p>" +
                "<p>You got <span class='highlight'>" + match + "</span> matches <br>" +
//                "and the frequency of this search was <span class='highlight'>" + frequency + "</span>.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
        return html;
    }
}
