package edu.url.salle.eric.macia.bubblefy.utils;

public class Constants {
    public static final String URL = "url";

    public interface NETWORK {
        public static  String BASE_URL = "http://" + "sallefy.eu-west-3.elasticbeanstalk.com/api/";
        public static int LOGIN_OK = 1;
        public static int LOGIN_KO = 2;
    }

    public interface PERMISSIONS {
        public static int MICROPHONE = 7;
    }

    public interface STORAGE {
        public static int SONG_SELECTED = 4;
    }
}
