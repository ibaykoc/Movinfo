package com.koc.movinfo.TmdbApi;

/**
 * Created by mochi on 12/11/2017.
 */

/**
 * Configuration file on TMDB API
 */
public class Configuration {
    private Image mImage;
    private String[] change_keys;

    public Configuration(String base_url, String secure_base_url, String[] back_drop_sizes, String[] logo_sizes, String[] poster_sizes, String[] profile_sizes, String[] still_sizes, String[] change_keys) {
        this.mImage = new Image(base_url, secure_base_url, back_drop_sizes, logo_sizes, poster_sizes, profile_sizes, still_sizes);
        this.change_keys = change_keys;
    }

    public Image getImage() {
        return mImage;
    }

    public String[] getChange_keys() {
        return change_keys;
    }



    public class Image{
        private String base_url;
        private String secure_base_url;
        private String[] back_drop_sizes;
        private String[] logo_sizes;
        private String[] poster_sizes;
        private String[] profile_sizes;
        private String[] still_sizes;

        private Image(String base_url, String secure_base_url, String[] back_drop_sizes, String[] logo_sizes, String[] poster_sizes, String[] profile_sizes, String[] still_sizes) {
            this.base_url = base_url;
            this.secure_base_url = secure_base_url;
            this.back_drop_sizes = back_drop_sizes;
            this.logo_sizes = logo_sizes;
            this.poster_sizes = poster_sizes;
            this.profile_sizes = profile_sizes;
            this.still_sizes = still_sizes;
        }

        public String getBase_url() {
            return base_url;
        }

        public String getSecure_base_url() {
            return secure_base_url;
        }

        public String[] getBack_drop_sizes() {
            return back_drop_sizes;
        }

        public String[] getLogo_sizes() {
            return logo_sizes;
        }

        public String[] getPoster_sizes() {
            return poster_sizes;
        }

        public String[] getProfile_sizes() {
            return profile_sizes;
        }

        public String[] getStill_sizes() {
            return still_sizes;
        }
    }
}
