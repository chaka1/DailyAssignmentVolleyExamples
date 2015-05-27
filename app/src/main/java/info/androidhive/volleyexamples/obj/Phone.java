package info.androidhive.volleyexamples.obj;


    import java.util.HashMap;
    import java.util.Map;

    public class Phone {

        private String mobile;
        private String home;
        private String office;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        /**
         *
         * @return
         * The mobile
         */
        public String getMobile() {
            return mobile;
        }

        /**
         *
         * @param mobile
         * The mobile
         */
        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public Phone withMobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        /**
         *
         * @return
         * The home
         */
        public String getHome() {
            return home;
        }

        /**
         *
         * @param home
         * The home
         */
        public void setHome(String home) {
            this.home = home;
        }

        public Phone withHome(String home) {
            this.home = home;
            return this;
        }

        /**
         *
         * @return
         * The office
         */
        public String getOffice() {
            return office;
        }

        /**
         *
         * @param office
         * The office
         */
        public void setOffice(String office) {
            this.office = office;
        }

        public Phone withOffice(String office) {
            this.office = office;
            return this;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

        public Phone withAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
            return this;
        }

    }

