package com.minicluster.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.UUID;

/**
 * Get chuck norris random quotes from an external HTTP API.
 * Yes, this is totally useless
 * Sometimes, instead of a quote, you will get this wonderfull ascci art pic
 */
public class ChuckNorris {

    public final static String ASCII_CHUCK = "... oh my god!\n" +
            "                       `:hmNNMMMMMNNNNNNNNNNNMMMMMMMMMMMMMMMMNmddmNmmddy-`                 `````````\n" +
            "                      `-hmNNNMMMNNNMMMMNmmmNNNNNNNNmNmmNNMMNNNNNNmmNmmddo`                 `````````\n" +
            "                      .yNNmNMMMNNMMMMMNNNNmmmmmdmdmhsyyhdmNNNmdmmNmdmmmdd:`                `````````\n" +
            "                      +NNmNMMMMMMMMMMMNNNmmNmdhdhyo++ooosydddmmddmmNmmmmdy``                ````````\n" +
            "                     :NNmNMMMMMMMMMMMNNmmNNmhyhho+///:///+syyhddmmmmNNmmmm-`                 ```````\n" +
            "                     +dmmNMMMMMNMMMNNmddmddhyds+/::::::/::+ossyhdddmmNNNNNs`                 ```````\n" +
            "                     .ymNNMNNNNNMNNmmddhhyyoyo/::::::::/::/o++++sshdmNNNNmh`                 ```````\n" +
            "                     `smNNNNNNNmmmmddhhhys++o++///:::::::::///::/oshmNNNNNo`                ````````\n" +
            "                      :dNNMNmmdddddddhyssso+o+///::://:::::::::::/+hmNNNNm.`                ````````\n" +
            "                      `omMMMmhhhdddddhhyso++o+/////++++++++//:::::/ymmNNm/.                  ```````\n" +
            "                       `sNNMmhyhdmmmmmdhysooo+///+osyyyssoooo/::::/oddh+/+`                  ```````\n" +
            "                       `smNNmyyhmmdddmdhhdddho::::/+shdhyo++//::::/os+///o`                    `````\n" +
            "                       `hmmmmyyhdhmmmmdyhdmmmy/:::/oosoo/::::-::::/oo+/:+:                     `````\n" +
            "                        /ddmmsyddddhhysooydmmh+::::///::------::::/oso/::`                     `````\n" +
            "                         /hdmsshdhhyyyssyddmdh+/::::::/::----:::::/+ss/-`                     ` ````\n" +
            "                          -ddysyhhhyyyyyhddddho/::::::::::---:::::/os+:.                      ``````\n" +
            "                           oddyyhhhhyyyyhmmdhyo///:::/:::----::::/os+:-                        `````\n" +
            "                           .hddyhhhhhhhhdmmdys++/:::/++/:---::::/+ss/-`                        `````\n" +
            "                            .hddhhhdhhhhdmhhhs++/:+o//oo/:--:::/osyo:.                       ```````\n" +
            "                             .ymdhhhhhhhdmmmNmyyo++o+/:/+/:-::/+syy+-`                       ```````\n" +
            "                              -mmdhdhyyhdmmNmdyhhsssoo+++o+::/+osyy-`                       ````````\n" +
            "                               sNmdddhdmmmmmmddddyyyysssssso++ossys``````                    ```````\n" +
            "                              `-mNmmmmNNNNmNmdhhyyyysosyhyyhsosyyhmNmdhh+```              ``````````\n" +
            "                            .dNMNmNNmNNNNNmmmmdhyssssoooyhhhsyyhhyMMMMMMMNs`` `           ``````````\n" +
            "                            yMMMMNNNNNNMMNmmdhhyso+//+oosyddhhhhy+dMMMMMMMM+` ```         ``````````\n" +
            "                           .NMMMMMNNNNMMMMNNNmdddddhyssssyhdhhhy+/+NMMMMMMMN:```````   `````````````\n" +
            "                           sMMMMMMMNNMMMMNNNNNmmmddddhhyyyyhhhs+///+mMMMMMMMm.``````````````````````\n" +
            "                          /MMMMMMMMNNNMMMNNNNmmmdhdhddhhhyhdho/////sNMMMMMMMMd.`````````````````````\n" +
            "                         -NMMMMMMMMMmNNNMNNNNmmmmmdddddhhyso+/////oNMMMMMMMMMMd-````````````````````\n" +
            "                  `-/oydmMMMMMMMMMMMNNNNNNNNNNmNmNmmmmhys++/////+sNMMMMMMMMMMMMMNdy+-```````````````\n" +
            "             `-+ymNMMMMMMMMMMMMMMMMMMNNNNNNNNNNNNNmdhso+++//+/+ohNMMMMMMMMMMMMMMMMMMMNh+-```````````\n";

    private final static String URL_STRING = "http://api.icndb.com/jokes/random?limitTo=[nerdy]";
    private final static String DEFAULT_QUOTE = "Chuck Norris' programs never exit, they terminate.";

    /**
     * get a random quote from Chuck Norris, or a picture if you are lucky ^^
     */
    public String getRandomQuote() {

        try {

            if (iAmLucky(20)) {
                return getAsciiArtPicture();
            }
            return getQuote();

        } catch (Exception e) {
            return DEFAULT_QUOTE;
        }
    }

    /**
     * get the ascii art string of Chuck Norris
     */
    public String getAsciiArtPicture() {
        return ASCII_CHUCK;
    }

    /**
     * Random generator: return true 1 out of n times
     */
    private boolean iAmLucky(Integer n) {
        int seed = UUID.randomUUID().hashCode();
        return new Random(seed).nextInt(n) == 0;
    }

    /**
     * Get the quote from the website
     */
    private String getQuote() throws IOException {

        URL url = new URL(URL_STRING);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String decodedResponse = readResponse(connection.getInputStream());
            return readQuoteFromJson(decodedResponse);
        }
        return DEFAULT_QUOTE;
    }

    /**
     * look for a specific field in a json string
     * return the value of field 'joke'
     */
    private String readQuoteFromJson(String json) {
        return json.split("\"joke\":")[1].split("\"")[1];
    }

    /**
     * read the http response from a stream
     */
    private String readResponse(InputStream response) throws IOException {
        StringBuilder out = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF8"));
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        reader.close();
        return out.toString();
    }
}
